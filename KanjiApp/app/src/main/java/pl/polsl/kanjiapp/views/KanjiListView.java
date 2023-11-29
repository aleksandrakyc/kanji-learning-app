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

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import pl.polsl.kanjiapp.R;
import pl.polsl.kanjiapp.adapters.CharacterAdapter;
import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.types.CategoryType;
import pl.polsl.kanjiapp.types.Grade;
import pl.polsl.kanjiapp.types.Jlpt;
import pl.polsl.kanjiapp.utils.DataBaseAdapter;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class KanjiListView extends Fragment {
    protected static final String TAG = "KanjiListView";
    int mLevel;
    CategoryType type;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLevel = getArguments().getInt("level");
            type = CategoryType.intToCategoryType(getArguments().getInt("categoryType"));
            Log.d("hewwo", "onCreate: "+mLevel);
        }
        //Toolbar myToolbar = (Toolbar) getView().findViewById(R.id.my_toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kanji_list_view, container, false);
    }
    GridView kanjiGV;
    DataBaseAdapter dataBaseAdapter;
    Button btn;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn = getView().findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            Bundle bundle = new Bundle();;
            @Override
            public void onClick(View v) {
                bundle.putInt("level", mLevel);
                bundle.putInt("categoryType", type.getValue());
                Navigation.findNavController(view).navigate(R.id.action_kanjiListView_to_learnOptions, bundle);
            }
        });

        dataBaseAdapter = new DataBaseAdapter(getContext());
        dataBaseAdapter.createDatabase();
        dataBaseAdapter.open();

        kanjiGV = getView().findViewById(R.id.gridKanjis);
        //todo trycatch
        ArrayList<CharacterModel> characterModelArrayList = new ArrayList<>();
        switch (type){
            case Jlpt:
                characterModelArrayList = dataBaseAdapter.getKanjiByJlpt(Jlpt.stringToJlpt("N"+mLevel));
                break;
            case Grade:
                characterModelArrayList = dataBaseAdapter.getKanjiByGrade(Grade.intToGrade(mLevel));
                break;
            case Custom:
                //get kanji details from list of characters
                Set<String> characterSet = new TreeSet<>();
                characterModelArrayList = dataBaseAdapter.getKanjiDetailsFromSet(characterSet);
            case invalid:
            default:
                Log.d(TAG, "onViewCreated: unsupported type");
        }

        CharacterAdapter adapter = new CharacterAdapter(getContext(), characterModelArrayList);
        kanjiGV.setAdapter(adapter);

    }

}