package org.bsc.markdown.flexmark;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import org.bsc.markdown.flexmark.internal.ConfluenceConverterNodeRenderer;

public class ConfluenceConverterExtension  implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension {

    private ConfluenceConverterExtension() {
    }

    public static Extension create() {
        return new ConfluenceConverterExtension();
    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
    }

    @Override
    public void rendererOptions(final MutableDataHolder options) {
        final String rendererType = HtmlRenderer.TYPE.getFrom(options);
        if (rendererType.equals("HTML")) {
            options.set(HtmlRenderer.TYPE, "JIRA");
        } else if (!rendererType.equals("JIRA")) {
            throw new IllegalStateException("Non HTML Renderer is already set to " + rendererType);
        }
    }

    @Override
    public void parserOptions(final MutableDataHolder options) {

    }

    @Override
    public void extend(HtmlRenderer.Builder rendererBuilder, String rendererType) {
        if (rendererType.equals("JIRA")) {
            rendererBuilder.nodeRendererFactory(new ConfluenceConverterNodeRenderer.Factory());
        } else {
            throw new IllegalStateException("Jira Converter Extension used with non Jira Renderer " + rendererType);
        }
    }
}

