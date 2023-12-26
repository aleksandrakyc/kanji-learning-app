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

    public SetModel getCustomSet(int setId){
        return new SetModel();
    }

    public SetModel getPredefinedSet(int setId){
        return new SetModel();
    }

    private void createPredefinedSet(ArrayList<CharacterModel> characters){

    }

    public void updateEasinessFactors(boolean isPredefined, HashMap<String, Double> kanjiEFMap, String setId){

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
        return new UserModel();
    }

    public CollectionReference getSetsForCurrentUser(){
        return mFstore.collection("Users").document(user.getUid()).collection("Sets");
    }

    public FirebaseUser getUser() {
        return user;
    }

    public Task<Void> addMemberToGroup(String email, String groupId){
        DocumentReference df = mFstore.collection("Groups").document(groupId);
        return df.update("Members", FieldValue.arrayUnion(email));
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
                    df.update("Groups", FieldValue.arrayUnion(groupId));
                });
            }
        });
    }

    public void addHomework(SetModel set, String groupId){
        DocumentReference df = mFstore.collection("Groups").document(groupId).collection("Sets").document(groupId+set.getId().substring(28));
        df.set(set);
    }

    public Task<DocumentSnapshot> getUserSet(String owner, String setId){
        DocumentReference df = mFstore.collection("Users").document(owner).collection("Sets").document(setId);
        return df.get();
    }

    public Task<DocumentSnapshot> getMembers(String groupId){
        DocumentReference df = mFstore.collection("Groups").document(groupId);
        return df.get();
    }

    public Task<QuerySnapshot> getHomework(String groupId){
        CollectionReference cf = mFstore.collection("Groups").document(groupId).collection("Sets");
        return cf.get();
    }

    public boolean checkTeacherPermissions(){
        return true;
    }
}
