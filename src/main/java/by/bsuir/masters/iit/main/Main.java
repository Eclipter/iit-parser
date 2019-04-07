package by.bsuir.masters.iit.main;

import by.bsuir.masters.iit.builder.TreeBuilder;
import by.bsuir.masters.iit.model.Node;
import by.bsuir.masters.iit.parser.HtmlConverter;
import by.bsuir.masters.iit.parser.VdtParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static final String INPUT_DIR_NAME = "input";
    private static final String OUTPUT_DIR_NAME = "output";
    private static final String VDT_FILE_EXTENSION = "vdt";
    private static final String HTML_FILE_EXTENSION = "html";

    public static void main(String[] args) throws IOException {
        VdtParser vdtParser = new VdtParser();
        HtmlConverter converter = new HtmlConverter();
        TreeBuilder treeBuilder = new TreeBuilder();

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

        Map<String, Node> graph = treeBuilder.buildDocGraph(docMap);

        System.out.println("Done.");
    }
}
