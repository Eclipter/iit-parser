package by.bsuir.masters.iit.builder;

import by.bsuir.masters.iit.model.GraphNode;
import by.bsuir.masters.iit.model.Node;
import by.bsuir.masters.iit.model.TagType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class GraphOperator {

    public Map<String, GraphNode> buildDocGraph(Map<String, Node> docMap) {
        Map<String, GraphNode> graph = new HashMap<>();

        docMap.forEach((key, value) -> {
            GraphNode node = new GraphNode();
            node.setValue(key);
            graph.put(key, node);
        });

        docMap.forEach((key, value) -> addLinks(value.getChildren(), graph, graph.get(key)));

        return graph;
    }

    private void addLinks(List<Node> children, Map<String, GraphNode> graph, GraphNode targetNode) {
        children.forEach(node -> {
            if (node.getType() == TagType.LINK) {
                targetNode.getChildren().add(graph.get(node.getChildren().get(0).getValue()));
            } else {
                addLinks(node.getChildren(), graph, targetNode);
            }
        });
    }

    public Map<GraphNode, List<GraphNode>> findShortestPath(Map<String,
        GraphNode> graph, String sourceFile) {
        GraphNode sourceNode = graph.get(sourceFile);

        Queue<GraphNode> queue = new PriorityQueue<>(Comparator.comparing(GraphNode::getMark));
        Set<GraphNode> visitedNodes = new HashSet<>();
        Map<GraphNode, List<GraphNode>> pathMap = new HashMap<>();

        pathMap.put(sourceNode, Collections.emptyList());
        sourceNode.setMark(0);

        queue.add(sourceNode);

        while (!queue.isEmpty()) {
            GraphNode node = queue.remove();
            visitedNodes.add(node);

            List<GraphNode> nonVisitedChildren =
                node.getChildren().stream().filter(n -> !visitedNodes.contains(n))
                    .collect(Collectors.toList());

            nonVisitedChildren.forEach(child -> {
                if (node.getMark() + 1 < child.getMark()) {
                    child.setMark(node.getMark() + 1);
                    List<GraphNode> path = new ArrayList<>(pathMap.get(node));
                    path.add(node);
                    pathMap.put(child, path);
                }
            });

            queue.addAll(nonVisitedChildren);
        }

        return pathMap;
    }
}
