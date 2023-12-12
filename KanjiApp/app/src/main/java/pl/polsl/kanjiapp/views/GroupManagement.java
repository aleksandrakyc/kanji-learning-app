package pl.polsl.kanjiapp.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.polsl.kanjiapp.R;
import pl.polsl.kanjiapp.adapters.CategoryListAdapter;
import pl.polsl.kanjiapp.types.CategoryType;

public class GroupManagement extends Fragment implements CategoryListAdapter.ItemClickListener{

    protected static final String TAG = "GroupManagement";
    Button button;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFstore;
    private FirebaseUser user;
    private ArrayList<String> groupChoices;
    CategoryListAdapter adapter;
    private boolean isTeacher;

    public GroupManagement() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mFstore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = getView().findViewById(R.id.groupRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        groupChoices = new ArrayList<>();
        adapter = new CategoryListAdapter(getContext(), groupChoices, 1);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        button = getView().findViewById(R.id.buttonAddGroup);
        button.setVisibility(View.GONE);
        checkTeacherPermissions();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //no new fragment, just dialog
                //choose name in dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final View dialogLayout = getLayoutInflater().inflate(R.layout.fragment_set_name_dialog, null);
                builder.setView(dialogLayout);
                builder.setTitle("Choose new group name");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText name = dialogLayout.findViewById(R.id.editTextSetName);
                        //create empty group with this name
                        DocumentReference df = mFstore.collection("Groups")
                                .document(user.getUid()+name.getText().toString());
                        Map<String, Object> setInfo = new HashMap<>();
                        setInfo.put("Owner", user.getUid());
                        //todo name cant be empty
                        setInfo.put("Name", name.getText().toString());
                        df.set(setInfo);

                        getGroups(isTeacher);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void checkTeacherPermissions(){
        DocumentReference df = mFstore.collection("Users").document(user.getUid());
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        button.setVisibility(View.VISIBLE);
                        Log.d(TAG, "onComplete: teacher");
                        getGroups(true);
                        isTeacher = true;
                    }
                    else{
                        Log.d(TAG, "onComplete: student");
                        getGroups(false);
                        isTeacher = false;
                    }
                }
            }
        });
    }

    private void getGroups(boolean isTeacher){
        //get all groups where owner = userid
        //
        if (isTeacher){
            CollectionReference cf = mFstore.collection("Groups");
            cf.whereEqualTo("Owner", user.getUid()).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    groupChoices.clear();
                    queryDocumentSnapshots.getDocuments().forEach(document -> {
                        String docuName = (String) document.getId();
                        groupChoices.add(docuName);
                        adapter.notifyItemChanged(groupChoices.indexOf(docuName));
                    });
                }
            });
        }
        else {
            DocumentReference df = mFstore.collection("Users").document(user.getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    groupChoices = (ArrayList<String>) documentSnapshot.get("Groups");
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        //go to details of that group
        Log.d(TAG, "onItemClick: " + position);
        Bundle bundle = new Bundle();
        //change to id?
        bundle.putString("id", groupChoices.get(position));
        Navigation.findNavController(view).navigate(R.id.action_GroupManagement_to_singleGroup, bundle);
    }
}