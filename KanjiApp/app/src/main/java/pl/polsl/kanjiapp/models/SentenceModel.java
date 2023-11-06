package pl.polsl.kanjiapp.models;

public class SentenceModel extends KanjiDbObject{
    private int id;
    private String japanese;
    private String english;
    private String particle;
    private String word;
    private String kanji;
    private String furigana; //todo: display words and sentences with furigana
    private String obi2;

    public SentenceModel(String id, String japanese, String english, String particle, String word, String kanji, String furigana, String obi2) {
        this.id = Integer.parseInt((id));
        this.japanese = japanese;
        this.english = english;
        this.particle = particle;
        this.word = word;
        this.kanji = kanji;
        this.furigana = furigana;
        this.obi2 = obi2;
    }

    @Override
    public String toString() {
        return "SentenceModel{" +
                "id=" + id +
                ", japanese='" + japanese + '\'' +
                ", english='" + english + '\'' +
                ", particle='" + particle + '\'' +
                ", word='" + word + '\'' +
                ", kanji='" + kanji + '\'' +
                ", furigana='" + furigana + '\'' +
                ", obi2='" + obi2 + '\'' +
                '}';
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJapanese() {
        return japanese;
    }

    public void setJapanese(String japanese) {
        this.japanese = japanese;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getParticle() {
        return particle;
    }

    public void setParticle(String particle) {
        this.particle = particle;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public String getFurigana() {
        return furigana;
    }

    public void setFurigana(String furigana) {
        this.furigana = furigana;
    }

    public String getObi2() {
        return obi2;
    }

    public void setObi2(String obi2) {
        this.obi2 = obi2;
    }

}
