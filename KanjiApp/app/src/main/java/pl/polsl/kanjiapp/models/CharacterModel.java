package pl.polsl.kanjiapp.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import pl.polsl.kanjiapp.types.Jlpt;

public class CharacterModel implements Comparable<CharacterModel>{
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
        this.onyomi = cleanUpReadings(this.onyomi);
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

    public List<String> getKunyomi() {
        return kunyomi;
    }

    public List<String> getMeaning() {
        return meaning;
    }

    public String getMeaningString() {
        return String.join(", ", meaning);
    }
    public String getReadingString() {
        String readings = String.join(", ", onyomi)+((onyomi.size()>0) ? ", ": "")+String.join(", ", kunyomi);
        return readings;
    }
    public Jlpt getJlpt() {
        return jlpt;
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

    private List<String> cleanUpReadings(List<String> list){
        List<String> newList = new ArrayList<>();
        int index;
        for (String reading : list){
            index = reading.indexOf('(');
            if (index != -1)
                newList.add(reading.substring(0,index));
            else
                newList.add(reading);
        }
        return newList;
    }

    public String getCompact_meaning() {
        return compact_meaning;
    }

    @Override
    public int compareTo(CharacterModel o) {
        return (Objects.equals(this.getKanji(), o.getKanji())) ? 0:1;
    }
}
