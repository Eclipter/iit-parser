package by.bsuir.masters.iit.main;

import by.bsuir.masters.iit.model.Node;
import by.bsuir.masters.iit.parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        String inputFilename = "input/example.vdt";
        String outputFilename = "output/example.txt";

        String content = Files.readAllLines(Paths.get(inputFilename))
                .stream()
                .map(String::trim)
                .collect(Collectors.joining());

        Parser parser = new Parser();

        Node root = parser.parse(content);

        System.out.println(root);

//        Files.write(Paths.get(outputFilename), Collections.singletonList(root.getValue()), StandardOpenOption.CREATE);
    }
}
