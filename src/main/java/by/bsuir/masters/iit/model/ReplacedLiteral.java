package by.bsuir.masters.iit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReplacedLiteral {

    HASH("-12&%", "#"),
    PLUS("-13&%", "+");

    private String encodedValue;
    private String decodedValue;
}
