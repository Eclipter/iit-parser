package by.bsuir.masters.iit.main;

import by.bsuir.masters.iit.builder.GraphOperator;
import by.bsuir.masters.iit.model.GraphNode;
import by.bsuir.masters.iit.model.Node;
import by.bsuir.masters.iit.parser.HtmlConverter;
import by.bsuir.masters.iit.parser.VdtParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static final String INPUT_DIR_NAME = "input";
    private static final String OUTPUT_DIR_NAME = "output";
    private static final String VDT_FILE_EXTENSION = "vdt";
    private static final String HTML_FILE_EXTENSION = "html";
    private static final String FILE_1 = "link1.vdt";

    public static void main(String[] args) throws IOException {
        VdtParser vdtParser = new VdtParser();
        HtmlConverter converter = new HtmlConverter();
        GraphOperator graphOperator = new GraphOperator();

        Map<String, Node> docMap = new HashMap<>();

        Files.newDirectoryStream(Paths.get(INPUT_DIR_NAME)).forEach(entry -> {
            try {
                String inputFilename = entry.toString();
                String outputFilename = inputFilename
                        .replaceAll(INPUT_DIR_NAME, OUTPUT_DIR_NAME)
                        .replaceAll(VDT_FILE_EXTENSION, HTML_FILE_EXTENSION);

                String content = Files.readAllLines(Paths.get(inputFilename))
                        .stream()
                        .map(String::trim)
                        .collect(Collectors.joining());

                Node root = vdtParser.parse(content);

                System.out.println(root);
                docMap.put(inputFilename.substring((INPUT_DIR_NAME + "/").length()), root);

                Files.write(Paths.get(outputFilename),
                        Collections.singleton(converter.toHTML(root)),
                        StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException("IO error", e);
            }
        });

        Map<String, GraphNode> graph = graphOperator.buildDocGraph(docMap);
        Map<GraphNode, List<GraphNode>> pathMap = graphOperator.findShortestPath(graph, FILE_1);

        pathMap.forEach((key, value) -> {
            System.out.println("Shortest path from " + FILE_1 + " to " + key.getValue() + ":");
            System.out
                .println(value.stream().map(GraphNode::getValue).collect(Collectors.joining(", ")));
        });

        System.out.println("Done.");
    }
}
