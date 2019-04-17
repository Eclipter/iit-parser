package by.bsuir.masters.iit.parser;

import by.bsuir.masters.iit.model.Node;
import by.bsuir.masters.iit.model.ReplacedLiteral;
import by.bsuir.masters.iit.model.TagType;

import java.util.List;

public class HtmlConverter {

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
                if (node.getType() == TagType.LINK) {
                    String linkValue = buildContent(node.getChildren())
                            .replaceAll("vdt", "html");
                    content.append(buildOpenLinkTag(linkValue))
                            .append(linkValue)
                            .append(buildCloseTag(TagType.LINK));
                } else {
                    content.append(buildOpenTag(node.getType()))
                            .append(buildContent(node.getChildren()))
                            .append(buildCloseTag(node.getType()));
                }
            }
        });

        String result = content.toString();

        for (ReplacedLiteral replacedLiteral : ReplacedLiteral.values()) {
            result = result.replaceAll(replacedLiteral.getEncodedValue(), replacedLiteral.getDecodedValue());
        }

        return result;
    }

    private String buildOpenTag(TagType tagType) {
        return "<" + tagType.getHtmlValue() + ">";
    }

    private String buildOpenLinkTag(String value) {
        return "<" + TagType.LINK.getHtmlValue() + " href=" + value + ">";
    }

    private String buildCloseTag(TagType tagType) {
        return "</" + tagType.getHtmlValue() + ">";
    }
}
