package pl.polsl.kanjiapp.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import pl.polsl.kanjiapp.R;
import pl.polsl.kanjiapp.adapters.CategoryListAdapter;
import pl.polsl.kanjiapp.models.SetModel;
import pl.polsl.kanjiapp.utils.FirestoreAdapter;

public class SingleGroup extends Fragment implements CategoryListAdapter.ItemClickListener {
    protected static final String TAG = "SingleGroup";
    private FirestoreAdapter firestore;
    private String groupId;
    TextView groupNameTextView;
    Button addMemberBtn, addSetBtn;
    private ArrayList<String> members, sets, customSets;
    CategoryListAdapter adapterMembers, adapterHomework;
    public SingleGroup() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupId = getArguments().getString("id");
        }
        firestore = new FirestoreAdapter();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupNameTextView = getView().findViewById(R.id.textViewGroupName);
        groupNameTextView.setText(groupId.substring(28));

        RecyclerView recyclerView = getView().findViewById(R.id.membersRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RecyclerView recyclerViewSets = getView().findViewById(R.id.setsRecycler);
        recyclerViewSets.setLayoutManager(new LinearLayoutManager(getContext()));

        sets = new ArrayList<>();
        adapterHomework = new CategoryListAdapter(getContext(), sets, 0);
        recyclerViewSets.setAdapter(adapterHomework);
        getHomework();

        members = new ArrayList<>();
        adapterMembers = new CategoryListAdapter(getContext(), members, 0);
        adapterMembers.setClickListener(this);
        recyclerView.setAdapter(adapterMembers);
        getMembers();

        addMemberBtn = getView().findViewById(R.id.buttonAddMember);
        addMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final View dialogLayout = getLayoutInflater().inflate(R.layout.fragment_set_name_dialog, null);
                builder.setView(dialogLayout);
                builder.setTitle("Enter member email");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText email = dialogLayout.findViewById(R.id.editTextSetName);
                        addMember(email.getText().toString());
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        addSetBtn = getView().findViewById(R.id.buttonAddSet);
        addSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customSets = new ArrayList<>();
                CollectionReference cf = firestore.getSetsForCurrentUser();
                cf.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        documents.forEach(documentSnapshot -> customSets.add(documentSnapshot.getId()));
                        Log.d(TAG, "onSuccess: " + customSets);

                        String[] choices = new String[customSets.size()];
                        for (int i = 0; i<customSets.size(); i++){
                            choices[i] = customSets.get(i).substring(28);
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder
                                .setTitle("Choose homework")
                                .setPositiveButton("Add", (dialog, which) -> {
                                    int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                                    Log.d(TAG, "onSuccess: " + customSets.get(selectedPosition));

                                    addHomework(customSets.get(selectedPosition));
                                })
                                .setNegativeButton("Cancel", (dialog, which) -> {

                                })
                                .setSingleChoiceItems(choices, 0, (dialog, which) -> {

                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        });

    }

    private void addMember(String email){
        //todo check if member is a valid user
        firestore.addMemberToGroup(email, groupId).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                getMembers();
                firestore.addGroupToMember(email, groupId);
                Toast.makeText(getContext(), "Added user with email: "+ email, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addHomework(String setId){
        //copy set data from teacher and put it in group
        String owner = setId.substring(0,28); //owner id

        firestore.getUserSet(owner, setId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    //todo test this
                    DocumentSnapshot document = task.getResult();

                    SetModel setModel = document.toObject(SetModel.class);
                    //temp
                    setModel.setId(setId);

                    HashMap<String, Double> kanjiInfo = setModel.getKanjiInfo();
                    if (kanjiInfo != null && kanjiInfo.size()>0){
                        kanjiInfo.replaceAll((key, value)->2.5);
                        setModel.setKanjiInfo(kanjiInfo);
                    }

                    firestore.addHomework(setModel, groupId);

                    getHomework();
                }
            }
        });
    }

    private void getMembers(){
        members.clear();
        firestore.getMembers(groupId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<String> members_ = (ArrayList<String>) documentSnapshot.get("Members");
                if (members_!=null && members_.size()>0){
                    members_.forEach(member -> {
                        members.add(member);
                        adapterMembers.notifyItemChanged(members_.indexOf(member));
                    });
                }
                Log.d(TAG, "onSuccess: "+ members);
            }
        });
    }

    private void getHomework(){
        sets.clear();
        firestore.getHomework(groupId).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                documents.forEach(documentSnapshot -> {
                    String setName = documentSnapshot.getId().substring(28);
                    sets.add(setName);
                    adapterHomework.notifyItemChanged(sets.indexOf(setName));
                });
                Log.d(TAG, "onSuccess: "+sets);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}