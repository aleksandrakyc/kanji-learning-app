package pl.polsl.kanjiapp.types;

public enum Grade {
    invalid(""),
    PRIMARY_1("Kyōiku-Jōyō (1st grade of primary school)"),
    PRIMARY_2("Kyōiku-Jōyō (2nd grade of primary school)"),
    PRIMARY_3("Kyōiku-Jōyō (3rd grade of primary school)"),
    PRIMARY_4("Kyōiku-Jōyō (4th grade of primary school)"),
    PRIMARY_5("Kyōiku-Jōyō (5th grade of primary school)"),
    PRIMARY_6("Kyōiku-Jōyō (6th grade of primary school)"),
    JHIGH_1("Jōyō (1st grade of junior high school)"),
    JHIGH_2("Jōyō (2nd grade of junior high school)"),
    JHIGH_3("Jōyō (3rd grade of junior high school)"),
    HIGH_SCHOOL("Jōyō (high school)"),
    JINMEIYO("Jinmeiyō");
    private final String value;
    private Grade(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    private static final Grade[] gradeValues = Grade.values();
    public static Grade intToGrade(int level) {
        Grade grade = (0 < level && level < gradeValues.length) ? gradeValues[level] : invalid;
        return grade;
    }
    public static String[] names() {
        Grade[] levels = values();
        String[] names = new String[levels.length];

        for (int i = 0; i < values().length; i++) {
            names[i] = levels[i].value;
        }

        return names;
    }
}
