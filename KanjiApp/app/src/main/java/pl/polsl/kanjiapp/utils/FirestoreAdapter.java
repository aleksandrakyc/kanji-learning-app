package pl.polsl.kanjiapp.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.models.GroupModel;
import pl.polsl.kanjiapp.models.SetModel;
import pl.polsl.kanjiapp.models.UserModel;

public class FirestoreAdapter {

    protected static final String TAG = "FirestoreAdapter";
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFstore;
    private FirebaseUser user;

    public FirestoreAdapter() {
        mAuth = FirebaseAuth.getInstance();
        mFstore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
    }
    public void refreshCurrentUser() {
        user = mAuth.getCurrentUser();
    }

    public void createUser(String login, String password, boolean teacherRequest) {
        mAuth.createUserWithEmailAndPassword(login, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //add user to db
                        user = mAuth.getCurrentUser();
                        UserModel userModel = new UserModel(user.getUid(), login, new ArrayList<String>(), false, false, teacherRequest);
                        addUser(userModel);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e);
                    }
                });
    }

    public void addUser(UserModel userModel){
        DocumentReference df = mFstore.collection("Users").document(userModel.getId());
        df.set(userModel);
    }

    public UserModel getUserInfo(String email){
        //todo
        return new UserModel();
    }

    public Task<DocumentSnapshot> getCurrentUserInfo(){
        DocumentReference df = mFstore.collection("Users").document(user.getUid());
        return df.get();
    }

    public CollectionReference getSetsForCurrentUser(){
        return mFstore.collection("Users").document(user.getUid()).collection("Sets");
    }

    public FirebaseUser getUser() {
        return user;
    }

    public Task<Void> addMemberToGroup(String email, String groupId){
        DocumentReference df = mFstore.collection("Groups").document(groupId);
        return df.update("members", FieldValue.arrayUnion(email));
    }

    public void addGroupToMember(String email, String groupId){
        CollectionReference cf = mFstore.collection("Users");
        cf.whereEqualTo("email", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //update "groups" array with this group id
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                documents.forEach(document -> {
                    String userId = document.getId();
                    DocumentReference df = mFstore.collection("Users").document(userId);
                    df.update("groups", FieldValue.arrayUnion(groupId));
                });
            }
        });
    }

    public void addHomework(SetModel set, String groupId){
        DocumentReference df = mFstore.collection("Groups").document(groupId).collection("Sets").document(groupId+set.getName());
        df.set(set);
    }

    public Task<DocumentSnapshot> getUserSet(String owner, String setId){
        Log.d(TAG, "getUserSet: "+ owner+ setId);
        DocumentReference df = mFstore.collection("Users").document(owner).collection("Sets").document(setId);
        return df.get();
    }

    public Task<DocumentSnapshot> getPredefinedUserSet(String setId){
        DocumentReference df = mFstore.collection("Users").document(user.getUid()).collection("Predefined").document(setId);
        return df.get();
    }

    public Task<QuerySnapshot> getUserSets() {
        CollectionReference cf = mFstore.collection("Users").document(user.getUid()).collection("Sets");
        return cf.get();
    }

    public Task<DocumentSnapshot> getMembers(String groupId){
        DocumentReference df = mFstore.collection("Groups").document(groupId);
        return df.get();
    }

    public Task<QuerySnapshot> getHomework(String groupId){
        CollectionReference cf = mFstore.collection("Groups").document(groupId).collection("Sets");
        return cf.get();
    }

    public Task<Void> addCharacterToSet(String setId, CharacterModel character) {
        DocumentReference df = mFstore.collection("Users").document(user.getUid()).collection("Sets").document(setId);
        return df.update(
                "kanjiInfo."+character.getKanji(), 2.5
        );
    }

    public Task<DocumentSnapshot> getSetFromGroup(SetModel set){
        Log.d(TAG, "getSetFromGroup: " + set);
        DocumentReference df = mFstore.collection("Groups")
                .document(set.getGroupId())
                .collection("Sets")
                .document(set.getGroupId()+set.getName());
        return df.get();
    }

    public Task<Void> addSetToUser(SetModel set){
        Log.d(TAG, "addSetToUser: " + set);
        DocumentReference df = mFstore.collection("Users").document(user.getUid()).collection("Sets").document(set.getId());
        Log.d(TAG, "addSetToUser: " + set.toString() + user.getEmail());
        return df.set(set);
    }

    public Task<Void> addUserPredefinedSet(SetModel set){
        DocumentReference df = mFstore.collection("Users").document(user.getUid()).collection("Predefined").document(set.getId());
        return df.set(set);
    }

    public Task<Void> createGroup(GroupModel groupModel){
        DocumentReference df = mFstore.collection("Groups")
                .document(user.getUid()+groupModel.getName());
        return df.set(groupModel);
    }

    public Task<QuerySnapshot> getGroupsTeacher(){
        CollectionReference cf = mFstore.collection("Groups");
        return cf.whereEqualTo("owner", user.getUid()).get();
    }

    public void updateEasinessFactors(boolean isCustom, HashMap<String, Double> kanjiEFMap, String setId){
        String collectionType = isCustom ? "Sets" : "Predefined";
        DocumentReference df = mFstore.collection("Users").document(user.getUid()).collection(collectionType).document(setId);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                kanjiEFMap.forEach((kanji, EF) -> {
                    double oldEF = (double) documentSnapshot.get("kanjiInfo."+kanji); //ugly
                    Log.d(TAG, "onSuccess: "+oldEF);
                    double newEF = oldEF + EF;
                    if(newEF<1.3)
                        newEF = 1.3;
                    df.update(
                            "kanjiInfo."+kanji, newEF
                    );
                });
            }
        });
    }
}
