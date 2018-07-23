package org.bsc.markdown;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ast.util.TextCollectingVisitor;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.jira.converter.JiraConverterExtension;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.bsc.markdown.flexmark.ConfluenceConverterExtension;

import java.util.Arrays;

public class Md2WikiFlexmarkImpl implements Md2Wiki {

    static final MutableDataSet OPTIONS = new MutableDataSet()
            .set(Parser.EXTENSIONS, Arrays.asList(
                    TablesExtension.create(),
                    StrikethroughExtension.create(),
                    ConfluenceConverterExtension.create()
            ));

    static final Parser PARSER = Parser.builder(OPTIONS).build();
    static final HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build();


    @Override
    public String convert(String markdown) {

        Node document = PARSER.parse(markdown);
        String text = RENDERER.render(document);

//        Parser parser = Parser.builder().build();
//        Node document = parser.parse(markdown);
//
//        TextCollectingVisitor textCollectingVisitor = new TextCollectingVisitor();
//        String text = textCollectingVisitor.collectAndGetText(document);
//
        return text;
    }
}
