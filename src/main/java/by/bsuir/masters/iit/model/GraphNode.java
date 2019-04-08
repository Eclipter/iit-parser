package by.bsuir.masters.iit.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GraphNode {

    private String value;
    private List<GraphNode> children = new ArrayList<>();
    private Integer mark = Integer.MAX_VALUE;

    public GraphNode(String value) {
        this.value = value;
    }
}
