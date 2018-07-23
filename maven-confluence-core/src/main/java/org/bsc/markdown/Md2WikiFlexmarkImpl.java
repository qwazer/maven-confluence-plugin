package org.bsc.markdown;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ast.util.TextCollectingVisitor;
import com.vladsch.flexmark.parser.Parser;

public class Md2WikiFlexmarkImpl implements Md2Wiki {
    @Override
    public String convert(String markdown) {

        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);

        TextCollectingVisitor textCollectingVisitor = new TextCollectingVisitor();
        String text = textCollectingVisitor.collectAndGetText(document);

        return text;
    }
}
