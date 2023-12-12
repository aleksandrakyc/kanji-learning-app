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

import pl.polsl.kanjiapp.R;
import pl.polsl.kanjiapp.adapters.CategoryListAdapter;
import pl.polsl.kanjiapp.types.CategoryType;

public class CategoryListHomework extends Fragment implements CategoryListAdapter.ItemClickListener{
    protected static final String TAG = "CategoryListHomework";
    RecyclerView recyclerView;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFstore;
    FirebaseUser user;
    private ArrayList<String> setChoices;
    CategoryListAdapter categoryListAdapter;
    ArrayList<String> groups;
    public CategoryListHomework() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mFstore = FirebaseFirestore.getInstance();
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

        if (user == null) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "log in to see custom sets!", Toast.LENGTH_SHORT).show();
        }
        else {
            //get sets from all our groups
            setChoices = new ArrayList<>();
            categoryListAdapter = new CategoryListAdapter(getContext(), setChoices, 1);
            categoryListAdapter.setClickListener(this);
            recyclerView.setAdapter(categoryListAdapter);

            DocumentReference df = mFstore.collection("Users").document(user.getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    groups = (ArrayList<String>) documentSnapshot.get("Groups");
                    if (groups!=null && groups.size()>0){
                        groups.forEach(group -> {
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

        CollectionReference cf = mFstore.collection("Groups")
                .document(groupId)
                .collection("Sets");
        cf.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        if (documents != null && !documents.isEmpty()){
                            documents.forEach(document -> {
                                String setName = document.getId();
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

        loadSet(setChoices.get(position));

        bundle.putString("level", setChoices.get(position));
        Log.d(TAG, "onItemClick: " + setChoices.get(position));
        bundle.putInt("categoryType", CategoryType.Custom.getValue());

        Toast.makeText(view.getContext(), "Recycle Click" + position, Toast.LENGTH_SHORT).show();
        Navigation.findNavController(view).navigate(R.id.action_startMenu_to_kanjiListView, bundle);
    }

    private void loadSet(String setName){

        Log.d(TAG, "loadSet: "+ setName);
        //check if set has already been copied from the group
        DocumentReference df = mFstore.collection("Users")
                .document(user.getUid())
                .collection("Sets")
                .document(setName);
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()){
                        //if not, copy it
                        copySet(setName, df);
                    }
                }
            }
        });
    }
    private void copySet(String setName, DocumentReference destination){
        DocumentReference df = mFstore.collection("Groups")
                .document(setName.substring(0,28))
                .collection("Sets")
                .document(setName);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                Map<String, Object> setInfo = new HashMap<>();
                HashMap<String, Double> kanjiList = (HashMap<String, Double>) document.get("Kanji_list");
                String owner = document.getString("Owner");
                setInfo.put("Owner", owner);
                setInfo.put("Kanji_list", kanjiList);
                setInfo.put("Set_name", setName.substring(28));
                destination.set(setInfo);
            }
        });
    }
}