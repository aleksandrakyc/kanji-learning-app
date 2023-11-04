package pl.polsl.kanjiapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.polsl.kanjiapp.utils.DataBaseAdapter;
import pl.polsl.kanjiapp.models.*;
import pl.polsl.kanjiapp.types.Jlpt;

public class SingleKanjiView extends Fragment {

    private String mCharacter;

    public SingleKanjiView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.d("lalala", getArguments().getString("character"));
            mCharacter = getArguments().getString("character");
        }
    }
    DataBaseAdapter dataBaseAdapter;
    TextView kanjiView, kunyomiView, onyomiView, meaningView, wordView, sentenceView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_kanji_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataBaseAdapter = new DataBaseAdapter(getContext());
//        dataBaseAdapter.createDatabase();
        dataBaseAdapter.open();
        //TODO ADD CHECKS, SOMETIMES CRASHES
        CharacterModel character = dataBaseAdapter.getKanjiByCharacter(mCharacter.charAt(0));

        kanjiView = getView().findViewById(R.id.kanjiView);
        kanjiView.setText(character.getKanji());

        onyomiView = getView().findViewById((R.id.onyomiView));
        onyomiView.setText(String.join(", ", character.getOnyomi()));

        kunyomiView = getView().findViewById((R.id.kunyomiView));
        kunyomiView.setText(String.join(", ", character.getKunyomi()));

        meaningView = getView().findViewById((R.id.meaningView));
        meaningView.setText(String.join(", ", character.getMeaning()));

        wordView = getView().findViewById(R.id.wordView);
        Log.d("heyy", "onViewCreated: "+character.getKanji()+character.getJlpt().name());
        if(character.getJlpt()!=Jlpt.invalid)
            wordView.setText(dataBaseAdapter.getWordsByKanjiAndLevel(character.getKanji().charAt(0), character.getJlpt()).get(0).getWordAndMeaning());
        else
            wordView.setText(dataBaseAdapter.getWordsByKanjiAndLevel(character.getKanji().charAt(0), Jlpt.N5).get(0).getWordAndMeaning());
        SentenceModel sentence = dataBaseAdapter.getSentencesByKanji(character.getKanji()).get(0);
        sentenceView = getView().findViewById((R.id.exampleSentenceView));
        sentenceView.setText(sentence.getJapanese());
        Log.d("hello", sentence.getJapanese());
    }
}