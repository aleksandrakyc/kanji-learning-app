package pl.polsl.kanjiapp.models;

public class CharacterModel {
    private int id;
    private String kanji;
    private String reading;
    private String okurigana;
    private String annotated_acc; //nullable
    private String html_acc; //nullable
    private String binary_acc; //nullable
    private String acc_pos; //nullable
    private String meaning;
    private String grade; //nullable
    private String jlpt; //nullable
    private String frequency; //nullable
    private int wanikani; //nullable
    private String particles; //nullable
    private String type; //nullable

    public CharacterModel(int id, String kanji, String reading, String okurigana, String annotated_acc, String html_acc, String binary_acc, String acc_pos, String meaning, String grade, String jlpt, String frequency, int wanikani, String particles, String type) {
        this.id = id;
        this.kanji = kanji;
        this.reading = reading;
        this.okurigana = okurigana;
        this.annotated_acc = annotated_acc;
        this.html_acc = html_acc;
        this.binary_acc = binary_acc;
        this.acc_pos = acc_pos;
        this.meaning = meaning;
        this.grade = grade;
        this.jlpt = jlpt;
        this.frequency = frequency;
        this.wanikani = wanikani;
        this.particles = particles;
        this.type = type;
    }

    @Override
    public String toString() {
        return "CharacterModel{" +
                "id=" + id +
                ", kanji='" + kanji + '\'' +
                ", reading='" + reading + '\'' +
                ", okurigana='" + okurigana + '\'' +
                ", annotated_acc='" + annotated_acc + '\'' +
                ", html_acc='" + html_acc + '\'' +
                ", binary_acc='" + binary_acc + '\'' +
                ", acc_pos='" + acc_pos + '\'' +
                ", meaning='" + meaning + '\'' +
                ", grade='" + grade + '\'' +
                ", jlpt='" + jlpt + '\'' +
                ", frequency='" + frequency + '\'' +
                ", wanikani=" + wanikani +
                ", particles='" + particles + '\'' +
                ", type='" + type + '\'' +
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

    public String getHtml_acc() {
        return html_acc;
    }

    public void setHtml_acc(String html_acc) {
        this.html_acc = html_acc;
    }

    public String getBinary_acc() {
        return binary_acc;
    }

    public void setBinary_acc(String binary_acc) {
        this.binary_acc = binary_acc;
    }

    public String getAcc_pos() {
        return acc_pos;
    }

    public void setAcc_pos(String acc_pos) {
        this.acc_pos = acc_pos;
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

    public int getWanikani() {
        return wanikani;
    }

    public void setWanikani(int wanikani) {
        this.wanikani = wanikani;
    }

    public String getParticles() {
        return particles;
    }

    public void setParticles(String particles) {
        this.particles = particles;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
