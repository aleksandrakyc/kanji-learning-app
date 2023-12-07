package pl.polsl.kanjiapp.utils;

import java.util.Objects;

import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.types.QuestionType;

public class Question implements Comparable<Question>{
    private CharacterModel mCharacterModel;
    private QuestionType question;
    private String questionDetails;
    private String answer;
    boolean wordEnabled, sentenceEnabled;
    public Question(CharacterModel characterModel, boolean wordEnabled, boolean sentenceEnabled){
        mCharacterModel = characterModel;
        question = QuestionType.randomQuestion(wordEnabled,sentenceEnabled);
        questionDetails = characterModel.getKanji();
        switch (question){
            case CHAR_MEANING:
                answer = mCharacterModel.getMeaningString();
                break;
            case CHAR_READING:
                answer = mCharacterModel.getReadingString();
                break;
            default:
                answer = "";
        }

    }
    public Question(CharacterModel characterModel, QuestionType type){
        mCharacterModel = characterModel;
        question = type;
        questionDetails = characterModel.getKanji();
        switch (question){
            case CHAR_MEANING:
                answer = mCharacterModel.getMeaningString();
                break;
            case CHAR_READING:
                answer = mCharacterModel.getReadingString();
                break;
            default:
                answer = "";
        }

    }
    public Boolean checkAnswer(String answer){
        switch(question){
            case CHAR_MEANING:
                return mCharacterModel.getMeaning().contains(answer);
            case CHAR_READING:
                return (mCharacterModel.getKunyomi().contains(answer) || mCharacterModel.getOnyomi().contains(answer));
            default:
                return false;
        }
    }
    public String getQuestion() {
        return question.getValue();
    }

    public String getQuestionDetails() {
        return questionDetails;
    }

    public String getAnswer() {
        return answer;
    }
    public String getKanji(){ return mCharacterModel.getKanji(); }

    @Override
    public int compareTo(Question o) {
        return (Objects.equals(this.getKanji(), o.getKanji()) && Objects.equals(this.question.getValue(), o.question.getValue())) ? 0:1;
    }
}
