package pl.polsl.kanjiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.models.SentenceModel;
import pl.polsl.kanjiapp.utils.DataBaseAdapter;

public class MainActivity extends AppCompatActivity {
    DataBaseAdapter dataBaseAdapter;

    TextView kanjiView, kunyomiView, onyomiView, meaningView, wordView, sentenceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBaseAdapter = new DataBaseAdapter(MainActivity.this);
        dataBaseAdapter.createDatabase();
        dataBaseAdapter.open();

        CharacterModel character = dataBaseAdapter.getKanjiByCharacter('来');

        kanjiView = findViewById(R.id.kanjiView);
        kanjiView.setText(character.getKanji());

        onyomiView = findViewById((R.id.onyomiView));
        onyomiView.setText(String.join(", ", character.getOnyomi()));

        kunyomiView = findViewById((R.id.kunyomiView));
        kunyomiView.setText(String.join(", ", character.getKunyomi()));

        meaningView = findViewById((R.id.meaningView));
        meaningView.setText(String.join(", ", character.getMeaning()));

        SentenceModel sentence = dataBaseAdapter.getSentencesByKanji(character.getKanji()).get(0);
        sentenceView = findViewById((R.id.exampleSentenceView));
        sentenceView.setText(sentence.getJapanese());

    }
}