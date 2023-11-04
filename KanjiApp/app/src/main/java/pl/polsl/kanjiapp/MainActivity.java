package pl.polsl.kanjiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.models.SentenceModel;
import pl.polsl.kanjiapp.types.Jlpt;
import pl.polsl.kanjiapp.utils.DataBaseAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}