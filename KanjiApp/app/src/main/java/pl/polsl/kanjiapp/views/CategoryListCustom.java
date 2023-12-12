package pl.polsl.kanjiapp.views;

import android.app.AlertDialog;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import pl.polsl.kanjiapp.R;
import pl.polsl.kanjiapp.adapters.CategoryListAdapter;
import pl.polsl.kanjiapp.types.CategoryType;
import pl.polsl.kanjiapp.types.Grade;
import pl.polsl.kanjiapp.types.Jlpt;

public class CategoryListCustom extends Fragment implements CategoryListAdapter.ItemClickListener{
    protected static final String TAG = "CategoryListCustom";
    RecyclerView recyclerView;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFstore;
    FirebaseUser user;
    private ArrayList<String> setChoices;
    CategoryListAdapter categoryListAdapter;
    public CategoryListCustom() {
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
        return inflater.inflate(R.layout.fragment_category_list_custom, container, false);
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

            CollectionReference cf = mFstore.collection("Users").document(user.getUid()).collection("Sets");
            setChoices = new ArrayList<>();
            categoryListAdapter = new CategoryListAdapter(getContext(), setChoices, 1);
            categoryListAdapter.setClickListener(this);
            recyclerView.setAdapter(categoryListAdapter);
            cf.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                    documents.forEach(documentSnapshot -> setChoices.add(documentSnapshot.getId()));
                    progressBar.setVisibility(View.GONE);
                    categoryListAdapter.notifyDataSetChanged();

                }
            });
    ;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: " + position);
        Bundle bundle = new Bundle();

        bundle.putString("level", setChoices.get(position));
        Log.d(TAG, "onItemClick: " + setChoices.get(position));
        bundle.putInt("categoryType", CategoryType.Custom.getValue());

        Toast.makeText(view.getContext(), "Recycle Click" + position, Toast.LENGTH_SHORT).show();
        Navigation.findNavController(view).navigate(R.id.action_startMenu_to_kanjiListView, bundle);

    }
}