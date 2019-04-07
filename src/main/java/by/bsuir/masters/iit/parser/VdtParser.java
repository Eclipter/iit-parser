package by.bsuir.masters.iit.parser;

import by.bsuir.masters.iit.model.Constants;
import by.bsuir.masters.iit.model.Node;
import by.bsuir.masters.iit.model.TagType;

import java.util.ArrayList;
import java.util.List;

import static by.bsuir.masters.iit.model.TagType.*;

public class VdtParser {

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

        List<Node> children = new ArrayList<>();

        if (content.equals("")) {
            return children;
        }

        int position = 0;

        while (position < content.length() - 1) {
            if (content.charAt(position) == Constants.OPEN_START) {
                TagType type = getTypeByValue(content.substring(position + 1,
                        content.indexOf(Constants.OPEN_END, position)));

                Node node = new Node(type);

                int innerPosition = position + Constants.getOpenTag(type).length();
                int openTagsCount = 0;
                int closingPositionEnd;

                while (true) {
                    int nextOpenTagPosition = content.indexOf(Constants.getOpenTag(type), innerPosition);
                    int nextCloseTagPosition = content.indexOf(Constants.getCloseTag(type), innerPosition);

                    if (nextCloseTagPosition < nextOpenTagPosition || nextOpenTagPosition == -1) {
                        if (openTagsCount == 0) {
                            closingPositionEnd = nextCloseTagPosition;
                            break;
                        } else {
                            openTagsCount -= 1;
                            innerPosition = nextCloseTagPosition + Constants.getCloseTag(type).length();
                        }
                    } else {
                        openTagsCount += 1;
                        innerPosition = nextOpenTagPosition + Constants.getOpenTag(type).length();
                    }
                }

                String innerContent = content.substring(position + Constants.getOpenTag(type).length(),
                        closingPositionEnd);

                node.setChildren(parseChildren(innerContent, node));

                children.add(node);

                position = closingPositionEnd + Constants.getCloseTag(type).length();
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
