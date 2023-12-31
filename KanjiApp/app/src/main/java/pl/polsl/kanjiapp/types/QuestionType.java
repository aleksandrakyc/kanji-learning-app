package pl.polsl.kanjiapp.types;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public enum QuestionType {
    CHAR_MEANING("What is the english meaning of character "),
    CHAR_READING("Name a reading of this character "),
    CHAR_ONE_OFF("Which option is not a valid reading of this character? "),
    WORD_MEANING("What is the english meaning of word "),
    WORD_READING("What is the reading of this compound word "),
    SENTENCE_MISSING_CHAR("Fill in the blank ");
    private final String value;
    private QuestionType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    private static final List<QuestionType> values = Arrays.asList(values());
    private static final int SIZE = values.size();
    private static final Random RANDOM = new Random();
    public static QuestionType randomQuestion(boolean wordEnabled, boolean sentenceEnabled)  {
        if (!wordEnabled)
            return values.get(RANDOM.nextInt(2));//temp
        if (!sentenceEnabled)
            return values.get(RANDOM.nextInt(SIZE-1));
        return values.get(RANDOM.nextInt(SIZE));
    }
    public static int numOfAvailQuestions(boolean wordEnabled, boolean sentenceEnabled){
        if (!wordEnabled){
            //temp
            return (2);
        }
        if (!sentenceEnabled)
            return (SIZE-1);
        return SIZE;
    }
}
