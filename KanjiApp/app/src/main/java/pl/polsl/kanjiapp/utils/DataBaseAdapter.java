package pl.polsl.kanjiapp.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Telephony;
import android.util.Log;

import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.models.SentenceModel;

public class DataBaseAdapter {

    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public DataBaseAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public DataBaseAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DataBaseAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public List<SentenceModel> getSentencesByKanji(String character) {
        List<SentenceModel> returnList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM sentences WHERE kanji LIKE '%" + character + "%';";
            Cursor cursor = mDb.rawQuery(sql,null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String japanese = cursor.getString(1);
                    String english = cursor.getString(2);
                    String particle = cursor.getString(3);
                    String word = cursor.getString(4);
                    String kanji = cursor.getString(5);
                    String furigana = cursor.getString(6);
                    String obi2 = cursor.getString(7);
                    SentenceModel newSentence = new SentenceModel(id, japanese, english, particle, word, kanji, furigana, obi2);
                    returnList.add(newSentence);
                } while (cursor.moveToNext());
            }
            return returnList;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "readDatabase >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public List<CharacterModel> getKanjiByLevel(String level) {
        List<CharacterModel> returnList = new ArrayList<>();
        try {
            String sql = "SELECT id, kanji, reading, okurigana, annotated_acc, meaning, grade, jlpt, frequency FROM edict WHERE jlpt LIKE '%" + level + "%';";
            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String kanji = cursor.getString(1);
                    String reading = cursor.getString(2);
                    String okurigana = cursor.getString(3);
                    String annotated_acc = cursor.getString(4);
                    String meaning = cursor.getString(5);
                    String grade = cursor.getString(6);
                    String jlpt = cursor.getString(7);
                    String frequency = cursor.getString(8);
                    CharacterModel newCharacter = new CharacterModel(id, kanji, reading, okurigana, annotated_acc, meaning, grade, jlpt, frequency);
                    returnList.add(newCharacter);
                } while (cursor.moveToNext());
            }
            return returnList;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "readDatabase >>" + mSQLException.toString());
            throw mSQLException;
        }
    }
    public void close() {
        mDbHelper.close();
    }

}