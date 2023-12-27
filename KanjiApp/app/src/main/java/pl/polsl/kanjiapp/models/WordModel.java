package pl.polsl.kanjiapp.models;

import java.util.ArrayList;
import java.util.List;

import pl.polsl.kanjiapp.types.Jlpt;

public class WordModel {
    private int id;
    private String kanji;
    private String word; //okurigana in edict, jukugo in jukugo, compverb in compverbs
    private String reading;
    private List<String> meaning;
    private Jlpt jlpt;

    public WordModel(String id, String kanji, String word, String reading, String meaning, String jlpt) {
        this.id = Integer.parseInt(id);
        this.kanji = kanji;
        this.word = word;
        this.reading = reading;
        this.meaning = this.extractMeaning(meaning);
        this.jlpt = Jlpt.stringToJlpt(jlpt);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public String getWordAndMeaning(){
        return this.word + " - " + this.meaning.get(0);
    }

    @Override
    public String toString() {
        return "WordModel{" +
                "id=" + id +
                ", kanji='" + kanji + '\'' +
                ", word='" + word + '\'' +
                ", meaning='" + meaning + '\'' +
                ", jlpt='" + jlpt + '\'' +
                '}';
    }

    private List<String> extractMeaning(String meaning){
        List<String> result = new ArrayList<>();
        int index = meaning.indexOf('{');
        int nextIndex = meaning.indexOf('{', index+1);
        if(index == -1){
            result.add(meaning);
            return result;
        }
        String toBeAdded;
        while (nextIndex != -1) {
            toBeAdded = meaning.substring(index+3, nextIndex-1);
            toBeAdded = toBeAdded.replace(";",", ");
            result.add(toBeAdded);
            index = nextIndex;
            nextIndex = meaning.indexOf('{', index+1);
        }
        return result;
    }

}
