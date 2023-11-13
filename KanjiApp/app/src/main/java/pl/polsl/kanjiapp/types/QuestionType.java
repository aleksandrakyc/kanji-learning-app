package pl.polsl.kanjiapp.types;

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
}
