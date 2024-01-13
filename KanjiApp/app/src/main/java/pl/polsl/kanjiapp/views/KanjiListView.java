package pl.polsl.kanjiapp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pl.polsl.kanjiapp.R;
import pl.polsl.kanjiapp.adapters.CharacterAdapter;
import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.models.SetModel;
import pl.polsl.kanjiapp.types.CategoryType;
import pl.polsl.kanjiapp.types.Grade;
import pl.polsl.kanjiapp.types.Jlpt;
import pl.polsl.kanjiapp.utils.DataBaseAdapter;
import pl.polsl.kanjiapp.utils.FirestoreAdapter;

public class KanjiListView extends Fragment {
    protected static final String TAG = "KanjiListView";
    int mLevel;
    String setId;
    CategoryType type;
    Set<String> characterSet;
    private FirestoreAdapter firestore;
    private ArrayList<CharacterModel> characterModelArrayList;
    CharacterAdapter adapter;
    GridView kanjiGV;
    DataBaseAdapter dataBaseAdapter;
    Button btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = new FirestoreAdapter();
        if (getArguments() != null) {
            Log.d(TAG, "onCreate: " + getArguments().getInt("categoryType"));
            type = CategoryType.intToCategoryType(getArguments().getInt("categoryType"));
            if (type == CategoryType.Custom || type == CategoryType.Search){
                setId = getArguments().getString("level");
            }
            else
                mLevel = getArguments().getInt("level");
        }
        Log.d(TAG, "onCreate: " + type.name());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kanji_list_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn = getView().findViewById(R.id.button);

        if (type == CategoryType.Search){
            btn.setVisibility(View.GONE);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            Bundle bundle = new Bundle();;
            @Override
            public void onClick(View v) {
                bundle.putInt("categoryType", type.getValue());
                if (type == CategoryType.Custom)
                    bundle.putString("level", setId);
                else
                    bundle.putInt("level", mLevel);
                Navigation.findNavController(view).navigate(R.id.action_kanjiListView_to_learnOptions, bundle);
            }
        });

        dataBaseAdapter = new DataBaseAdapter(getContext());
        dataBaseAdapter.createDatabase();
        dataBaseAdapter.open();

        kanjiGV = getView().findViewById(R.id.gridKanjis);
        //todo trycatch

        if (type!=CategoryType.Custom){
            switch (type){
                case Jlpt:
                    characterModelArrayList = dataBaseAdapter.getKanjiByJlpt(Jlpt.stringToJlpt("N"+mLevel));
                    break;
                case Grade:
                    characterModelArrayList = dataBaseAdapter.getKanjiByGrade(Grade.intToGrade(mLevel));
                    break;
                case Search:
                    characterModelArrayList = dataBaseAdapter.searchByMeaning(setId);
                    Log.d(TAG, "onViewCreated: " + characterModelArrayList);
                    break;
                case invalid:
                default:
                    Log.d(TAG, "onViewCreated: unsupported type");
            }
            adapter = new CharacterAdapter(getContext(), characterModelArrayList);
            kanjiGV.setAdapter(adapter);
        }
        else {
            //get kanji details from list of characters
            characterSet = new TreeSet<>();
            firestore.getUserSet(firestore.getUser().getUid(), setId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    //TODO check this cast
                    SetModel setModel = documentSnapshot.toObject(SetModel.class);
                    setModel.getKanjiInfo().forEach((key, value) -> characterSet.add(key));
                    Log.d(TAG, "onSuccess: " + characterSet);
                    characterModelArrayList = dataBaseAdapter.getKanjiDetailsFromSet(characterSet);
                    adapter = new CharacterAdapter(getContext(), characterModelArrayList);
                    kanjiGV.setAdapter(adapter);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "No such set", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}