package pl.polsl.kanjiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.models.SentenceModel;
import pl.polsl.kanjiapp.utils.DataBaseAdapter;

public class MainActivity extends AppCompatActivity {

    Button btn1;
    ScrollView scrl1;
    DataBaseAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.btn1);
        scrl1 = findViewById(R.id.scrl1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDbHelper = new DataBaseAdapter(MainActivity.this);
                mDbHelper.createDatabase();
                mDbHelper.open();

                List<SentenceModel> testdata = mDbHelper.getSentencesByKanji("字");
                TextView tv = new TextView(MainActivity.this);
                tv.setText(testdata.get(0).toString());
                scrl1.addView(tv);

                mDbHelper.close();

            }
        });

    }
}