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
