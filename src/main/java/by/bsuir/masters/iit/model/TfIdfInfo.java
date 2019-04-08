package by.bsuir.masters.iit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TfIdfInfo {

    private Map<String, Double> idfs;
    private Map<String, Map<String, Double>> termFilesTfIdfDictionary;
}
