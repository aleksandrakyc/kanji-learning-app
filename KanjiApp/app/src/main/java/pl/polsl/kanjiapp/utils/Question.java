package pl.polsl.kanjiapp.utils;

import pl.polsl.kanjiapp.models.CharacterModel;

public class Question {
    private CharacterModel mCharacterModel;
    private String question;
    private String questionDetails;
    private String answer;
    public Question(CharacterModel characterModel /*, type, other stuff*/){
        mCharacterModel = characterModel;
        question = "What is the english meaning of character ";
        questionDetails = characterModel.getKanji();
        answer = characterModel.getMeaningString();

    }
    public Boolean checkAnswer(String answer){
        //todo change it to some kind of regex
        return mCharacterModel.getMeaning().contains(answer);
    }
    public String getQuestion() {
        return question;
    }

    public String getQuestionDetails() {
        return questionDetails;
    }

    public String getAnswer() {
        return answer;
    }
}
