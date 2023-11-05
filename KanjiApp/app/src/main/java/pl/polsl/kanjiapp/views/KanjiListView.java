package pl.polsl.kanjiapp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

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
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kanji_list_view, container, false);
    }
    GridView kanjiGV;
    DataBaseAdapter dataBaseAdapter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataBaseAdapter = new DataBaseAdapter(getContext());
        dataBaseAdapter.createDatabase();
        dataBaseAdapter.open();

        kanjiGV = getView().findViewById(R.id.gridKanjis);
        //todo trycatch
        ArrayList<CharacterModel> characterModelArrayList = new ArrayList<>();
        switch (type){
            case Jlpt:
                characterModelArrayList = dataBaseAdapter.getKanjiByLevel(Jlpt.stringToJlpt("N"+mLevel));
                break;
            case Grade:
                characterModelArrayList = dataBaseAdapter.getKanjiByGrade(Grade.intToGrade(mLevel));
                break;
            case Custom:
            case invalid:
            default:
                Log.d(TAG, "onViewCreated: unsupported type");
        }

        CharacterAdapter adapter = new CharacterAdapter(getContext(), characterModelArrayList);
        kanjiGV.setAdapter(adapter);

    }

}