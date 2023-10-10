package pl.polsl.kanjiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

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

                Cursor testdata = mDbHelper.getTestData();
                TextView tv = new TextView(MainActivity.this);
                tv.setText(testdata.getColumnName(2));
                scrl1.addView(tv);

                mDbHelper.close();

            }
        });

    }
}