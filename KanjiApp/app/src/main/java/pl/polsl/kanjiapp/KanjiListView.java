package pl.polsl.kanjiapp;

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

import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.types.Jlpt;
import pl.polsl.kanjiapp.utils.DataBaseAdapter;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class KanjiListView extends Fragment {

    String mLevel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLevel = getArguments().getString("level");
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
        ArrayList<CharacterModel> characterModelArrayList = dataBaseAdapter.getKanjiByLevel(Jlpt.stringToJlpt("N"+mLevel));

        CharacterAdapter adapter = new CharacterAdapter(getContext(), characterModelArrayList);
        kanjiGV.setAdapter(adapter);

    }

}