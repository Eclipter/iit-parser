package by.bsuir.masters.iit.parser;

import by.bsuir.masters.iit.model.Constants;
import by.bsuir.masters.iit.model.Node;
import by.bsuir.masters.iit.model.TagType;

import java.util.ArrayList;
import java.util.List;

import static by.bsuir.masters.iit.model.TagType.*;

public class Parser {

    public Node parse(String content) {

        TagType rootType = VDT_ROOT;

        Node root = new Node(rootType);

        int rootTagIndex = content.indexOf(Constants.getOpenTag(rootType));

        if (rootTagIndex == -1) {
            throw new RuntimeException("Root tag not found");
        }

        String inner = content.substring(rootTagIndex + Constants.getOpenTag(rootType).length(),
                content.indexOf(Constants.getCloseTag(rootType)));

        root.setChildren(parseChildren(inner, root));

        return root;
    }

    private List<Node> parseChildren(String content, Node parent) {

        if (content.equals("")) {
            return null;
        }

        List<Node> children = new ArrayList<>();

        int position = 0;

        while (position < content.length() - 1) {
            if (content.charAt(position) == Constants.OPEN_START) {
                TagType type = getTypeByValue(content.substring(position + 1,
                        content.indexOf(Constants.OPEN_END, position)));

                Node node = new Node(type);

                node.setChildren(parseChildren(content.substring(position + Constants.getOpenTag(type).length(),
                        content.indexOf(Constants.getCloseTag(type), position)), node));

                children.add(node);

                position = content.indexOf(Constants.getCloseTag(type), position) + Constants.getCloseTag(type).length();
            } else {

                int openingTagIndex = content.indexOf(Constants.OPEN_START, position);

                Node node = new Node();

                if (openingTagIndex != -1) {
                    node.setValue(content.substring(position, openingTagIndex));
                    position = openingTagIndex;
                } else {
                    node.setValue(content.substring(position));
                    position = content.length();
                }

                children.add(node);
            }
        }

        children.forEach(node -> node.setParent(parent));

        return children;
    }
}
