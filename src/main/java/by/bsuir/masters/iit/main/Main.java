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
        String inputFilename = "input/example.vdt";
        String outputFilename = "output/example.html";

        String content = Files.readAllLines(Paths.get(inputFilename))
                .stream()
                .map(String::trim)
                .collect(Collectors.joining());

        VdtParser vdtParser = new VdtParser();
        HtmlConverter conveter = new HtmlConverter();

        Node root = vdtParser.parse(content);

        System.out.println(root);

        Files.write(Paths.get(outputFilename), Collections.singleton(conveter.toHTML(root)), StandardOpenOption.CREATE);
    }
}
