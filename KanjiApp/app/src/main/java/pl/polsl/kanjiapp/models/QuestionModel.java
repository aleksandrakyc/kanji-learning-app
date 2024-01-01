package pl.polsl.kanjiapp.models;

import java.util.Objects;
import java.util.Random;
import java.util.TreeSet;

import pl.polsl.kanjiapp.types.QuestionType;

public class QuestionModel implements Comparable<QuestionModel>{
    private CharacterModel mCharacterModel;
    private QuestionType question;
    private String questionDetails;
    private String answer;
    boolean wordEnabled, sentenceEnabled;
    public QuestionModel(CharacterModel characterModel, boolean wordEnabled, boolean sentenceEnabled){
        mCharacterModel = characterModel;
        question = QuestionType.randomQuestion(wordEnabled,sentenceEnabled);
        questionDetails = characterModel.getKanji();
        switch (question){
            case CHAR_MEANING:
                answer = mCharacterModel.getMeaningString();
                break;
            case CHAR_READING:
            case CHAR_ONE_OFF:
                answer = mCharacterModel.getReadingString();
                break;
            default:
                answer = "";
        }

    }
    public QuestionModel(CharacterModel characterModel, QuestionType type){
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
            case CHAR_ONE_OFF:
                return (mCharacterModel.getKunyomi().contains(answer) || mCharacterModel.getOnyomi().contains(answer));
            default:
                return false;
        }
    }
    public String getQuestion() {
        return question.getValue();
    }
    public QuestionType getQuestionType() {
        return question;
    }

    public String getQuestionDetails() {
        return questionDetails;
    }
    public TreeSet<String> getFourPossibleAnswers() {
        TreeSet<String> answers = new TreeSet<>();
        Random RANDOM = new Random();
        //add correct answers
        while(answers.size()<3){
            answers.add(mCharacterModel.getMeaning().get(RANDOM.nextInt(mCharacterModel.getMeaning().size())));
        }
        //add wrong answer

        //shuffle (a list)
        //Collections.shuffle(answers);

        return answers;
    }
    public String getAnswer() {
        return answer;
    }
    public String getKanji(){ return mCharacterModel.getKanji(); }

    @Override
    public int compareTo(QuestionModel o) {
        return (Objects.equals(this.getKanji(), o.getKanji()) && Objects.equals(this.question.getValue(), o.question.getValue())) ? 0:1;
    }
}
