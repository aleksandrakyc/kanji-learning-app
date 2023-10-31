package pl.polsl.kanjiapp.models;

import android.util.Log;

import java.util.List;

public abstract class KanjiDbObject {
    public KanjiDbObject(List<String> arguments){
        Log.d("TAG", "KanjiDbObject: parent");
    }
    public KanjiDbObject(){

    }
}
