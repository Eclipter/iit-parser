package by.bsuir.masters.iit.parser;

import by.bsuir.masters.iit.model.Node;
import by.bsuir.masters.iit.model.TagType;

import java.util.List;

public class HtmlConveter {

    public String toHTML(Node node) {

        if (node.getType() != TagType.VDT_ROOT) {
            throw new RuntimeException("Invalid root element: " + node.getType());
        }

        return buildOpenTag(node.getType()) +
                "<body>" +
                buildContent(node.getChildren()) +
                "</body>" +
                buildCloseTag(node.getType());
    }

    private String buildContent(List<Node> nodes) {
        StringBuilder content = new StringBuilder();

        nodes.forEach(node -> {
            if (node.getType() == null) {
                content.append(node.getValue());
            } else {
                content.append(buildOpenTag(node.getType()))
                        .append(buildContent(node.getChildren()))
                        .append(buildCloseTag(node.getType()));
            }
        });

        return content.toString();
    }

    private String buildOpenTag(TagType tagType) {
        return "<" + tagType.getHtmlValue() + ">";
    }

    private String buildCloseTag(TagType tagType) {
        return "</" + tagType.getHtmlValue() + ">";
    }
}
