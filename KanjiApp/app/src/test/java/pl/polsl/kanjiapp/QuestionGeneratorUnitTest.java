package pl.polsl.kanjiapp;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeSet;

import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.models.QuestionModel;
import pl.polsl.kanjiapp.utils.QuestionGenerator;

public class QuestionGeneratorUnitTest {
    QuestionGenerator generator;

    private void setUp(int turns){
        ArrayList<CharacterModel> characters = new ArrayList<>();
        CharacterModel one = new CharacterModel("一", "イチ", "ひと", "one", "one", "Kyōiku-Jōyō (1st grade of primary school)", "N5", "");
        CharacterModel two = new CharacterModel("二", "ニ", "ふた", "two", "two", "Kyōiku-Jōyō (1st grade of primary school)", "N5", "");
        CharacterModel three = new CharacterModel("三", "サン", "み", "three", "three", "Kyōiku-Jōyō (1st grade of primary school)", "N5", "");

        Collections.addAll(characters, one, two, three);

        HashMap<String, Double> kanjiInfo = new HashMap<String, Double>() {{
            put("一", 2.5);
            put("二", 1.3);
            put("三", 3.5);
        }};
        generator = new QuestionGenerator(characters, kanjiInfo, turns, false, false);
    }

    @Test
    public void noTurnsTest(){
        int turns = 0;
        setUp(turns);
        ArrayList<QuestionModel> questions = generator.generateQuestions();
        assertEquals(questions.size(), turns);
    }
    @Test
    public void turnsMatchQuestionAmountTest(){
        int turns = 5;
        setUp(turns);
        ArrayList<QuestionModel> questions = generator.generateQuestions();
        assertEquals(questions.size(), turns);
    }
    @Test
    public void lowestScoreCharacterAppearsIfTurnsLesserThanCharacters(){
        setUp(1);
        ArrayList<QuestionModel> questions = generator.generateQuestions();
        TreeSet<String> characters = new TreeSet<>();
        questions.forEach(questionModel -> characters.add(questionModel.getKanji()));
        assertTrue(characters.contains("二"));
    }

    @Test
    public void highestScoreCharacterDoesNotAppearIfTurnsLesserThanCharacters(){
        setUp(2);
        ArrayList<QuestionModel> questions = generator.generateQuestions();
        TreeSet<String> characters = new TreeSet<>();
        questions.forEach(questionModel -> characters.add(questionModel.getKanji()));
        assertFalse(characters.contains("三"));
    }

}