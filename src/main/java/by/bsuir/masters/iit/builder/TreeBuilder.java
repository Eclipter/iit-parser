package by.bsuir.masters.iit.builder;

import by.bsuir.masters.iit.model.Node;
import by.bsuir.masters.iit.model.TagType;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TreeBuilder {

    private Map<String, Node> docMap;

    public void linkTrees() {
        docMap.forEach((key, value) -> linkChildren(value.getChildren()));
    }

    private void linkChildren(List<Node> children) {
        children.forEach(node -> {
            if (node.getType() == TagType.LINK) {
                node.setValue(node.getChildren().get(0).getValue());
                node.setChildren(Collections.singletonList(docMap.get(node.getValue())));
            } else {
                linkChildren(node.getChildren());
            }
        });
    }
}
