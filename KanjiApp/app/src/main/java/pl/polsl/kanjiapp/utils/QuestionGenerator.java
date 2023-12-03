package pl.polsl.kanjiapp.utils;

import pl.polsl.kanjiapp.models.CharacterModel;
import pl.polsl.kanjiapp.types.QuestionType;

public class QuestionGenerator {
    private CharacterModel mCharacterModel;
    private QuestionType question;
    private String questionDetails;
    private String answer;
    boolean wordEnabled, sentenceEnabled;
    public QuestionGenerator(CharacterModel characterModel, boolean wordEnabled, boolean sentenceEnabled){
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
}
