package by.bsuir.masters.iit.builder;

import by.bsuir.masters.iit.model.Node;
import by.bsuir.masters.iit.model.TagType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TreeBuilder {

    public Map<String, Node> buildDocGraph(Map<String, Node> docMap) {
        Map<String, Node> graph = new HashMap<>();

        docMap.forEach((key, value) -> {
            Node node = new Node();
            node.setValue(key);
            graph.put(key, node);
        });

        docMap.forEach((key, value) -> addLinks(value.getChildren(), graph, graph.get(key)));

        return graph;
    }

    private void addLinks(List<Node> children, Map<String, Node> graph, Node targetNode) {
        children.forEach(node -> {
            if (node.getType() == TagType.LINK) {
                targetNode.getChildren().add(graph.get(node.getChildren().get(0).getValue()));
            } else {
                addLinks(node.getChildren(), graph, targetNode);
            }
        });
    }

    public void findShortestPath(Map<String, Node> graph, String sourceFile, String targetFile) {
        Node sourceNode = graph.get(sourceFile);
        Node targetNode = graph.get(targetFile);


    }
}
