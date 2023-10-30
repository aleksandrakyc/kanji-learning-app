package pl.polsl.kanjiapp.models;

import pl.polsl.kanjiapp.types.Jlpt;

public class WordModel {
    private int id;
    private String kanji;
    private String word; //okurigana in edict, jukugo in jukugo, compverb in compverbs
    private String reading;
    private String meaning;
    private Jlpt jlpt;

    public WordModel(int id, String kanji, String word, String reading, String meaning, String jlpt) {
        this.id = id;
        this.kanji = kanji;
        this.word = word;
        this.reading = reading;
        this.meaning = meaning;
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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }
    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public Jlpt getJlpt() {
        return jlpt;
    }

    public void setJlpt(Jlpt jlpt) {
        this.jlpt = jlpt;
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

}
