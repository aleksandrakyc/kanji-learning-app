package pl.polsl.kanjiapp.types;

public enum Jlpt {
    N5,
    N4,
    N3,
    N2,
    N1,
    invalid;
    private static final Jlpt[] jlptValues = Jlpt.values();
    public static Jlpt stringToJlpt(String text) {
        int index = text.indexOf('N');
        int level = text.charAt(index+1);
        return (0 < level && level < 6) ? jlptValues[level] : invalid;
    }
}
