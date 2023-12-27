package pl.polsl.kanjiapp.models;

public class SentenceModel {
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

    public String getEnglish() {
        return english;
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

}
