package by.bsuir.masters.iit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TagType {

    VDT_ROOT("vdt"),
    BOLD("bold"),
    ITALIC("italic"),
    STRIKETHROUGH("strikethrough"),
    ;

    private String value;

    public static TagType getTypeByValue(String value) {
        for (TagType tagType : TagType.values()) {
            if (tagType.getValue().equals(value)) {
                return tagType;
            }
        }

        throw new RuntimeException("Unknown tag: " + value);
    }
}
