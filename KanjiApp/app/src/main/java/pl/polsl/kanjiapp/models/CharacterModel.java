package pl.polsl.kanjiapp.models;

public class CharacterModel {
    private int id;
    private String kanji;
    private String reading;
    private String okurigana;
    private String annotated_acc; //nullable
    private String meaning;
    private String grade; //nullable
    private String jlpt; //nullable
    private String frequency; //nullable

    public CharacterModel(int id, String kanji, String reading, String okurigana, String annotated_acc, String meaning, String grade, String jlpt, String frequency) {
        this.id = id;
        this.kanji = kanji;
        this.reading = reading;
        this.okurigana = okurigana;
        this.annotated_acc = annotated_acc;
        this.meaning = meaning;
        this.grade = grade;
        this.jlpt = jlpt;
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "CharacterModel{" +
                "id=" + id +
                ", kanji='" + kanji + '\'' +
                ", reading='" + reading + '\'' +
                ", okurigana='" + okurigana + '\'' +
                ", annotated_acc='" + annotated_acc + '\'' +
                ", meaning='" + meaning + '\'' +
                ", grade='" + grade + '\'' +
                ", jlpt='" + jlpt + '\'' +
                ", frequency='" + frequency + '\'' +
                '}';
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

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public String getOkurigana() {
        return okurigana;
    }

    public void setOkurigana(String okurigana) {
        this.okurigana = okurigana;
    }

    public String getAnnotated_acc() {
        return annotated_acc;
    }

    public void setAnnotated_acc(String annotated_acc) {
        this.annotated_acc = annotated_acc;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getJlpt() {
        return jlpt;
    }

    public void setJlpt(String jlpt) {
        this.jlpt = jlpt;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
