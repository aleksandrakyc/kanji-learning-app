package pl.polsl.kanjiapp.types;

import android.util.Log;

import pl.polsl.kanjiapp.models.CharacterModel;

public enum Jlpt {

    invalid,
    N1,
    N2,
    N3,
    N4,
    N5;
    private static final Jlpt[] jlptValues = Jlpt.values();
    public static Jlpt stringToJlpt(String text) {
        int index = text.indexOf('N');
        if (index == -1)
            return invalid;
        int level = Character.getNumericValue(text.charAt(index+1));
        Jlpt jlpt = (0 < level && level < 6) ? jlptValues[level] : invalid;
        return jlpt;
    }
    public static String[] names() {
        Jlpt[] levels = values();
        String[] names = new String[levels.length];

        for (int i = 0; i < levels.length; i++) {
            names[i] = levels[i].name();
        }

        return names;
    }
}
