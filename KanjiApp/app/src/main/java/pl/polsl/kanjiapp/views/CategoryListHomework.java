package pl.polsl.kanjiapp.views;

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
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.polsl.kanjiapp.R;
import pl.polsl.kanjiapp.adapters.CategoryListAdapter;
import pl.polsl.kanjiapp.models.SetModel;
import pl.polsl.kanjiapp.models.UserModel;
import pl.polsl.kanjiapp.types.CategoryType;
import pl.polsl.kanjiapp.utils.FirestoreAdapter;

public class CategoryListHomework extends Fragment implements CategoryListAdapter.ItemClickListener{
    protected static final String TAG = "CategoryListHomework";
    RecyclerView recyclerView;
    ProgressBar progressBar;
    private FirestoreAdapter firestore;
    private ArrayList<String> setChoices;
    private ArrayList<SetModel> sets;
    CategoryListAdapter categoryListAdapter;
    public CategoryListHomework() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = new FirestoreAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_list_homework, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = getView().findViewById(R.id.progressBar);

        recyclerView = getView().findViewById(R.id.customRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (firestore.getUser() == null) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "log in to see custom sets!", Toast.LENGTH_SHORT).show();
        }
        else {
            //get sets from all our groups
            setChoices = new ArrayList<>();
            sets = new ArrayList<>();
            categoryListAdapter = new CategoryListAdapter(getContext(), setChoices, 1);
            categoryListAdapter.setClickListener(this);
            recyclerView.setAdapter(categoryListAdapter);

            firestore.getCurrentUserInfo().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    UserModel userModel = documentSnapshot.toObject(UserModel.class);

                    if (userModel.getGroups()!=null && userModel.getGroups().size()>0){
                        userModel.getGroups().forEach(group -> {
                            //get all sets in that group
                            getSetsForGroup(group);
                        });
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
            //display only pretty name but remember full id
        }
    }
    private void getSetsForGroup(String groupId){
        firestore.getHomework(groupId)
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        if (!documents.isEmpty()){
                            documents.forEach(document -> {
                                SetModel setModel = document.toObject(SetModel.class);
                                sets.add(setModel);
                                Log.d(TAG, "onSuccess: " + setModel.toString());
                                String setName = document.getId();
                                Log.d(TAG, "onSuccess: " + setName);
                                setChoices.add(setName);
                                categoryListAdapter.notifyItemChanged(setChoices.indexOf(setName));
                            });
                        }
                    }
                });
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: " + position);
        Bundle bundle = new Bundle();

        loadSet(sets.get(position));

        bundle.putString("level", sets.get(position).getId());
        bundle.putInt("categoryType", CategoryType.Custom.getValue());

        Toast.makeText(view.getContext(), "Recycle Click" + position, Toast.LENGTH_SHORT).show();
        Navigation.findNavController(view).navigate(R.id.action_startMenu_to_kanjiListView, bundle);
    }

    private void loadSet(SetModel set){
        Log.d(TAG, "loadSet: "+ set);
        //check if set has already been copied from the group
        firestore.getUserSet(firestore.getUser().getUid(), set.getId())
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()){
                        //if not, copy it
                        copySet(set);
                    }
                }
            }
        });
    }
    private void copySet(SetModel set){
        Log.d(TAG, "copySet: hello" + set);
        firestore.getSetFromGroup(set).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                SetModel set = document.toObject(SetModel.class);
                firestore.addSetToUser(set);
            }
        });
    }
}