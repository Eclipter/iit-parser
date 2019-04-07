package by.bsuir.masters.iit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TagType {

    VDT_ROOT("vdt", "html"),
    BOLD("bold", "b"),
    ITALIC("italic", "i"),
    STRIKETHROUGH("strikethrough", "s"),
    PARAGRAPH("par", "p"),
    UNDERLINED("und", "u"),
    HEADER_1("head_1", "h1"),
    HEADER_2("head_2", "h2"),
    ;

    private String value;
    private String htmlValue;

    public static TagType getTypeByValue(String value) {
        for (TagType tagType : TagType.values()) {
            if (tagType.getValue().equals(value)) {
                return tagType;
            }
        }

        throw new UnsupportedOperationException("Unknown tag: " + value);
    }
}
