package pl.polsl.kanjiapp.types;

public enum CategoryType {
    invalid(0),
    Jlpt(1),
    Grade(2),
    Custom(3),
    Search(4);
    private final int value;
    private static final CategoryType[] values = CategoryType.values();
    private CategoryType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public static CategoryType intToCategoryType(int type) {
        CategoryType categoryType = (0 < type && type < 5) ? values[type] : invalid;
        return categoryType;
    }
}

