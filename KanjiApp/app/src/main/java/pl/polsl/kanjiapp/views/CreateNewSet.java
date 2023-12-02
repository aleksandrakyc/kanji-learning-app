package pl.polsl.kanjiapp.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import pl.polsl.kanjiapp.R;
import pl.polsl.kanjiapp.adapters.SelectCharactersAdapter;
import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.types.CategoryType;
import pl.polsl.kanjiapp.utils.DataBaseAdapter;

public class CreateNewSet extends Fragment implements SelectCharactersAdapter.ItemClickListener {
    protected static final String TAG = "CreateNewSet";
    SelectCharactersAdapter adapter;
    DataBaseAdapter dataBaseAdapter;
    ArrayList<CharacterModel> characterModelArrayList;
    RecyclerView recyclerView;
    Set<CharacterModel> selectedCharacters;
    Button btn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFstore;
    String setName;
    public CreateNewSet() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mFstore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_new_set, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectedCharacters = new TreeSet<>();

        dataBaseAdapter = new DataBaseAdapter(getContext());
        dataBaseAdapter.open();

        characterModelArrayList = dataBaseAdapter.getAllKanji();

        recyclerView = getView().findViewById(R.id.select_kanji_grid);
        int numberOfColumns = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        adapter = new SelectCharactersAdapter(getContext(), characterModelArrayList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        btn = getView().findViewById(R.id.createSetButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choose name in dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final View dialogLayout = getLayoutInflater().inflate(R.layout.fragment_set_name_dialog, null);
                builder.setView(dialogLayout);
                builder.setTitle("title");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText name = dialogLayout.findViewById(R.id.editTextSetName);
                        setName = name.getText().toString();
                        //create new set in db
                        FirebaseUser user = mAuth.getCurrentUser();
                        String setId = user.getUid() + setName;
                        DocumentReference df = mFstore.collection("Users").document(user.getUid()).collection("Sets").document(setId);

                        Map<String, Object> setInfo = new HashMap<>();
                        Map<String, Double> kanjiInfo = new HashMap<>();
                        selectedCharacters.forEach(c->kanjiInfo.put(c.getKanji(), 2.5));

                        setInfo.put("Owner", user.getUid());
                        setInfo.put("Kanji_list", kanjiInfo);
                        setInfo.put("Set_name", setName);
                        df.set(setInfo);

                        Toast.makeText(getContext(), "Created set " + setName, Toast.LENGTH_SHORT).show();

                        //navigate to set view page
                        Bundle bundle = new Bundle();
                        bundle.putString("level", setId);
                        bundle.putInt("categoryType", CategoryType.Custom.getValue());
                        Navigation.findNavController(view).navigate(R.id.action_createNewSet_to_kanjiListView, bundle);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
}
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        CharacterModel character = characterModelArrayList.get(position);
        //if character not in set, add it
        if(!selectedCharacters.contains(character)){
            selectedCharacters.add(character);
            Log.d(TAG, "onItemClick: " + selectedCharacters.size());
            Toast.makeText(getContext(), "Added " + character.getKanji() + " to the set", Toast.LENGTH_SHORT).show();
        }
        else {
            //if character in set, remove it
            selectedCharacters.remove(character);
            Log.d(TAG, "onItemClick: " + selectedCharacters.size());
            Toast.makeText(getContext(), "Removed " + character.getKanji() + " from the set", Toast.LENGTH_SHORT).show();
        }

        for(CharacterModel character_ : selectedCharacters){
            Log.d(TAG, "onItemClick: " + character_.getKanji());
        }
        adapter.setSelectedCharacters(selectedCharacters);
    }
}