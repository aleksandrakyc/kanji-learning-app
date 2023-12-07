package pl.polsl.kanjiapp.utils;

import static java.lang.Math.min;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.types.QuestionType;

public class QuestionGenerator {

    protected static final String TAG = "QuestionGenerator";
    ArrayList<Question> questions;
    HashMap<String, Double> kanjiEFmap;
    ArrayList<CharacterModel> characters;
    int turns, maxUniqueQs;
    boolean wordEnabled, sentenceEnabled;
    public QuestionGenerator(ArrayList<CharacterModel> characters, Map<String, Double> kanjiEFmap, int turns, boolean wordEnabled, boolean sentenceEnabled) {

        Log.d(TAG, "QuestionGenerator: Constructor");
        this.turns = turns;
        this.wordEnabled = wordEnabled;
        this.sentenceEnabled = sentenceEnabled;
        int n = (turns>characters.size())? characters.size():turns;
        this.characters = new ArrayList<>();

        maxUniqueQs = characters.size() * QuestionType.numOfAvailQuestions(wordEnabled, sentenceEnabled);
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
        TreeSet<Question> questionsSet = new TreeSet<>();

        int index = 0, size;
        while (questionsSet.size()<maxUniqueQs&&questionsSet.size()<turns){
            size = questionsSet.size();
            questionsSet.add(new Question(characters.get(index%characters.size()), QuestionType.randomQuestion(wordEnabled, sentenceEnabled)));
            if (questionsSet.size() > size)
                index++;
        }

        questions = new ArrayList<>();
        questions.addAll(questionsSet);
        return questions;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public int getTurns() {
        return min(maxUniqueQs, turns);
    }
}
