package pl.polsl.kanjiapp.models;

import java.util.ArrayList;

import pl.polsl.kanjiapp.types.Jlpt;

public class CharacterModel {
    private String kanji;
    private ArrayList<String> onyomi;
    private ArrayList<String> kunyomi;
    private ArrayList<String> meaning;
    private String grade; //nullable
    private Jlpt jlpt; //nullable
    private int frequency; //nullable

    public CharacterModel(String kanji, ArrayList<String> onyomi, ArrayList<String> kunyomi, ArrayList<String> meaning, String grade, String jlpt, int frequency) {
        this.kanji = kanji;
        this.onyomi = onyomi;
        this.kunyomi = kunyomi;
        this.meaning = meaning;
        this.grade = grade;
        this.jlpt = Jlpt.stringToJlpt(jlpt);
        this.frequency = frequency;
    }
    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public ArrayList<String> getOnyomi() {
        return onyomi;
    }

    public void setOnyomi(ArrayList<String> onyomi) {
        this.onyomi = onyomi;
    }

    public ArrayList<String> getKunyomi() {
        return kunyomi;
    }

    public void setKunyomi(ArrayList<String> kunyomi) {
        this.kunyomi = kunyomi;
    }

    public ArrayList<String> getMeaning() {
        return meaning;
    }

    public void setMeaning(ArrayList<String> meaning) {
        this.meaning = meaning;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Jlpt getJlpt() {
        return jlpt;
    }

    public void setJlpt(Jlpt jlpt) {
        this.jlpt = jlpt;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "CharacterModel{" +
                ", kanji='" + kanji + '\'' +
                ", onyomi=" + onyomi +
                ", kunyomi=" + kunyomi +
                ", meaning=" + meaning +
                ", grade='" + grade + '\'' +
                ", jlpt='" + jlpt + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}
