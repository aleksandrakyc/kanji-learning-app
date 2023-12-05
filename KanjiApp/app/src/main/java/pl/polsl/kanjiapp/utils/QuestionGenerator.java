package pl.polsl.kanjiapp.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.types.QuestionType;

public class QuestionGenerator {

    protected static final String TAG = "QuestionGenerator";
    ArrayList<Question> questions;
    HashMap<String, Double> kanjiEFmap;
    ArrayList<CharacterModel> characters;
    int turns;
    boolean wordEnabled, sentenceEnabled;
    public QuestionGenerator(ArrayList<CharacterModel> characters, Map<String, Double> kanjiEFmap, int turns, boolean wordEnabled, boolean sentenceEnabled) {

        Log.d(TAG, "QuestionGenerator: Constructor");
        this.turns = turns;
        this.wordEnabled = wordEnabled;
        this.sentenceEnabled = sentenceEnabled;
        int n = (turns>characters.size())? characters.size():turns;
        this.characters = new ArrayList<>();

        //find lowest EF scores and choose these characters
        this.kanjiEFmap = kanjiEFmap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(n)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, HashMap::new));

        this.kanjiEFmap.forEach((key, value) -> { this.characters.add(characters.stream().filter(character -> character.getKanji().equals(key)).findFirst().orElse(null));});
        this.characters.forEach(character -> Log.d(TAG, "QuestionGenerator: " + character));
    }
    public ArrayList<Question> generateQuestions() {
        questions = new ArrayList<>();
        int turnsPerChar = turns/characters.size();
        //change if word/sentence enabled
        QuestionType[] types = QuestionType.values();
        characters.forEach(characterModel -> {
            for (int i = 0; i<turnsPerChar; i++){
                questions.add(new Question(characterModel, types[i%types.length]));
            }
        });

        return questions;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }
}
