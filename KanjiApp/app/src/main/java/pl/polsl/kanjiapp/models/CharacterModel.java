package pl.polsl.kanjiapp.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.polsl.kanjiapp.types.Jlpt;

public class CharacterModel extends KanjiDbObject{
    private String kanji;
    private List<String> onyomi;
    private List<String> kunyomi;
    private List<String> meaning;
    private String compact_meaning;
    private String grade; //nullable
    private Jlpt jlpt; //nullable
    private int frequency; //nullable

    public CharacterModel(String kanji, String onyomi, String kunyomi, String meaning, String compact_meaning, String grade, String jlpt, String frequency) {
        this.kanji = kanji;
        this.onyomi = new ArrayList<String>(Arrays.asList(onyomi.split("、")));
        this.kunyomi = new ArrayList<String>(Arrays.asList(kunyomi.split("、")));
        this.meaning = new ArrayList<String>(Arrays.asList(meaning.split(";")));
        if(compact_meaning == null || compact_meaning.trim().isEmpty())
            this.compact_meaning = this.meaning.get(0);
        else
            this.compact_meaning = compact_meaning.replace(";", ", ");
        this.grade = grade;
        this.jlpt = Jlpt.stringToJlpt(jlpt);
        try {
            this.frequency = Integer.parseInt(frequency);
        } catch (Exception e)
        {
            this.frequency = 0;
        }
        this.cleanUpOnyomi();
    }
    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public List<String> getOnyomi() {
        return onyomi;
    }

    public void setOnyomi(List<String> onyomi) {
        this.onyomi = onyomi;
    }

    public List<String> getKunyomi() {
        return kunyomi;
    }

    public void setKunyomi(List<String> kunyomi) {
        this.kunyomi = kunyomi;
    }

    public List<String> getMeaning() {
        return meaning;
    }

    public String getMeaningString() {
        return String.join(", ", meaning);
    }

    public void setMeaning(List<String> meaning) {
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

    private void cleanUpOnyomi(){
        List<String> newList = new ArrayList<String>();
        int index;
        for (String reading : this.onyomi){
            index = reading.indexOf('(');
            if (index != -1)
                newList.add(reading.substring(0,index));
            else
                newList.add(reading);
        }
        this.onyomi = newList;
    }

    public String getCompact_meaning() {
        return compact_meaning;
    }

    public void setCompact_meaning(String compact_meaning) {
        this.compact_meaning = compact_meaning;
    }
}
