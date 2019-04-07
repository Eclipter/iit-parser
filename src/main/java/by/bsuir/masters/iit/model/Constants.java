package by.bsuir.masters.iit.model;

public class Constants {

    public static final char OPEN_START = '#';
    public static final char OPEN_END = '+';
    public static final char CLOSE_START = '+';
    public static final char CLOSE_END = '#';

    public static String getOpenTag(TagType type) {
        return OPEN_START + type.getValue() + OPEN_END;
    }

    public static String getCloseTag(TagType type) {
        return CLOSE_START + type.getValue() + CLOSE_END;
    }

    private Constants(){
    }
}
