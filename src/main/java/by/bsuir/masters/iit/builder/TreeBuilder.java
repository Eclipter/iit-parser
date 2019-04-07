package by.bsuir.masters.iit.builder;

import by.bsuir.masters.iit.model.Node;
import by.bsuir.masters.iit.model.TagType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

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

    public List<Node> findShortestPath(Map<String, Node> graph, String sourceFile, String targetFile) {
        Node sourceNode = graph.get(sourceFile);
        Node targetNode = graph.get(targetFile);

        Queue<Node> queue = new LinkedList<>();
        List<Node> visitedNodes = new ArrayList<>();

        queue.add(sourceNode);

        while (!queue.isEmpty()) {
            Node node = queue.remove();
            visitedNodes.add(node);

            if (node.equals(targetNode)) {
//                visitedNodes.add(targetNode);
//                return visitedNodes;
            } else {
                queue.addAll(node.getChildren()
                        .stream()
                        .filter(n -> !visitedNodes.contains(n))
                        .collect(Collectors.toList())
                );
            }
        }

        throw new RuntimeException("Did not manage to find a path");
    }
}
