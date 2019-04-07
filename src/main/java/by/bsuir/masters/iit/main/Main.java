package by.bsuir.masters.iit.main;

import by.bsuir.masters.iit.model.Node;
import by.bsuir.masters.iit.parser.HtmlConverter;
import by.bsuir.masters.iit.parser.VdtParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        VdtParser vdtParser = new VdtParser();
        HtmlConverter converter = new HtmlConverter();

        String inputDir = "input";
        String outputDir = "output";

        Files.newDirectoryStream(Paths.get(inputDir)).forEach(entry -> {
            try {
                String inputFilename = entry.toString();
                String outputFilename = inputFilename
                        .replaceAll("input", "output")
                        .replaceAll("vdt", "html");

                String content = Files.readAllLines(Paths.get(inputFilename))
                        .stream()
                        .map(String::trim)
                        .collect(Collectors.joining());

                Node root = vdtParser.parse(content);

                System.out.println(root);

                Files.write(Paths.get(outputFilename),
                        Collections.singleton(converter.toHTML(root)),
                        StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException("IO error", e);
            }
        });
    }
}
