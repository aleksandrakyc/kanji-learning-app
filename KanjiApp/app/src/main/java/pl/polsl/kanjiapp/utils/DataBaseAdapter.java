package pl.polsl.kanjiapp.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.models.SentenceModel;
import pl.polsl.kanjiapp.models.WordModel;
import pl.polsl.kanjiapp.types.Jlpt;

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
            Log.e(TAG, mIOException + "  UnableToCreateDatabase");
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
            Log.e(TAG, "open >>"+ mSQLException);
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
            Log.e(TAG, "readDatabase >>" + mSQLException);
            throw mSQLException;
        }
    }

    public List<CharacterModel> getKanjiByLevel(Jlpt level) {
        List<CharacterModel> returnList = new ArrayList<>();
        try {
            String sql = "SELECT kanji, onyomi, kunyomi, meaning, grade, jlpt, frequency FROM kanjidict WHERE jlpt LIKE '%" + level.name() + "%';";
            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    String kanji = cursor.getString(0);
                    ArrayList<String> onyomi = new ArrayList<String>(Arrays.asList(cursor.getString(1).split("、")));
                    ArrayList<String> kunyomi = new ArrayList<String>(Arrays.asList(cursor.getString(2).split("、")));
                    ArrayList<String> meaning = new ArrayList<String>(Arrays.asList(cursor.getString(3).split("、")));
                    String grade = cursor.getString(4);
                    String jlpt = cursor.getString(5);
                    int frequency = cursor.getInt(6);
                    CharacterModel newCharacter = new CharacterModel(kanji, onyomi, kunyomi, meaning, grade, jlpt, frequency);
                    returnList.add(newCharacter);
                } while (cursor.moveToNext());
            }
            return returnList;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "readDatabase >>" + mSQLException);
            throw mSQLException;
        }
    }
    public CharacterModel getKanjiByCharacter(char character) {
        try {
            String sql = "SELECT kanji, onyomi, kunyomi, meaning, grade, jlpt, frequency FROM kanjidict WHERE kanji='" + character + "';";
            Cursor cursor = mDb.rawQuery(sql, null);
            CharacterModel newCharacter = null;
            if (cursor.moveToFirst()) {
                String kanji = cursor.getString(0);
                ArrayList<String> onyomi = new ArrayList<String>(Arrays.asList(cursor.getString(1).split("、")));
                ArrayList<String> kunyomi = new ArrayList<String>(Arrays.asList(cursor.getString(2).split("、")));
                ArrayList<String> meaning = new ArrayList<String>(Arrays.asList(cursor.getString(3).split(";")));
                String grade = cursor.getString(4);
                String jlpt = cursor.getString(5);
                int frequency = cursor.getInt(6);
                newCharacter = new CharacterModel(kanji, onyomi, kunyomi, meaning, grade, jlpt, frequency);
            }
            return newCharacter;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "readDatabase >>" + mSQLException);
            throw mSQLException;
        }
    }
    public ArrayList<WordModel> getWordsByKanjiAndLevel(char character, Jlpt level){
        ArrayList<WordModel> returnList = new ArrayList<>();
        Map<String, String> tableNames =  new HashMap<String, String>() {{
            put("edict", "okurigana");
            put("jukugo", "jukugo");
            put("compverbs", "compverb");
        }};
        try {
            for(String tableName : tableNames.keySet()) {
                String sql = "SELECT id, kanji, "+tableNames.get(tableName)+", reading, meaning, jlpt FROM " + tableName + " WHERE kanji='"+character+"' AND jlpt LIKE '%" + level.name() + "%';";
                Cursor cursor = mDb.rawQuery(sql, null);
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(0);
                        String kanji = cursor.getString(1);
                        String word = cursor.getString(2);
                        String reading = cursor.getString(3);
                        String meaning = cursor.getString(4);
                        String jlpt = cursor.getString(5);
                        WordModel newWord = new WordModel(id, kanji, word, reading, meaning, jlpt);
                        returnList.add(newWord);
                    } while (cursor.moveToNext());
                }
            }
            return returnList;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "readDatabase >>" + mSQLException);
            throw mSQLException;
        }
    }
    public void close() {
        mDbHelper.close();
    }

}