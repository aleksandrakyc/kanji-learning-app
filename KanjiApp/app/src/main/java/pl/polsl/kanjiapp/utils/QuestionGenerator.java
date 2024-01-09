package pl.polsl.kanjiapp.utils;

import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import java.util.stream.Collectors;

import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.models.QuestionModel;
import pl.polsl.kanjiapp.types.QuestionType;

public class QuestionGenerator {

    protected static final String TAG = "QuestionGenerator";
    ArrayList<QuestionModel> questions;
    HashMap<String, Double> kanjiEFmap;
    ArrayList<CharacterModel> characters;
    int turns, maxUniqueQs;
    boolean wordEnabled, sentenceEnabled;
    public QuestionGenerator(ArrayList<CharacterModel> characters, Map<String, Double> kanjiEFmap, int turns, boolean wordEnabled, boolean sentenceEnabled) {

        this.turns = turns;
        this.wordEnabled = wordEnabled;
        this.sentenceEnabled = sentenceEnabled;
        int n = Math.min(turns, characters.size());
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

        //check for null
        while(this.characters.contains(null)){
            this.characters.remove(null);
        }
        //this.characters.forEach(character -> Log.d(TAG, "QuestionGenerator: " + character));
    }
    public QuestionGenerator(ArrayList<CharacterModel> characters, int turns, boolean wordEnabled, boolean sentenceEnabled) {

        //Log.d(TAG, "QuestionGenerator: Constructor");
        Random RANDOM = new Random();
        this.turns = turns;
        this.maxUniqueQs = turns;
        this.wordEnabled = wordEnabled;
        this.sentenceEnabled = sentenceEnabled;
        this.characters = new ArrayList<>();
        //get random
        for (int i = 0; i<turns; i++){
            this.characters.add(characters.get(RANDOM.nextInt(characters.size())));
            //Log.d(TAG, "QuestionGenerator: " + this.characters.get(i));
        }
    }
    public ArrayList<QuestionModel> generateQuestions() {
        TreeSet<QuestionModel> questionsSet = new TreeSet<>();

        int index = 0, size;
        while (questionsSet.size()<maxUniqueQs&&questionsSet.size()<turns){
            size = questionsSet.size();
            questionsSet.add(new QuestionModel(characters.get(index%characters.size()), QuestionType.randomQuestion(wordEnabled, sentenceEnabled)));
            if (questionsSet.size() > size)
                index++;
        }

        //if questiontype, add wrong answers

        questions = new ArrayList<>();
        questions.addAll(questionsSet);
        return questions;
    }

    public int getTurns() {
        return min(maxUniqueQs, turns);
    }
}
