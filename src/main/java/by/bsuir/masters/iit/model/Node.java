package by.bsuir.masters.iit.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Node {

    private Node parent;
    private List<Node> children = new ArrayList<>();
    private String value;
    private TagType type;

    public Node(TagType tagType) {
        this.type = tagType;
    }

    @Override
    public String toString() {
        return "\nNode{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", children=" + children +
                '}';
    }
}
