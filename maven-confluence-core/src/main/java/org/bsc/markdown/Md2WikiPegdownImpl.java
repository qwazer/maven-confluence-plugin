package org.bsc.markdown;

import org.apache.commons.io.IOUtils;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.Node;
import org.pegdown.ast.RootNode;

public class Md2WikiPegdownImpl implements Md2Wiki {
    @Override
    public String convert(String markdown) {
        final char[] contents = markdown.toCharArray();
        final PegDownProcessor p = new PegDownProcessor(ToConfluenceSerializer.extensions());
        final RootNode root = p.parseMarkdown(contents);
        ToConfluenceSerializer ser = new ToConfluenceSerializer() {

            @Override
            protected void notImplementedYet(Node node) {

                final int lc[] = ToConfluenceSerializer.lineAndColFromNode(new String(contents), node);
                throw new UnsupportedOperationException(String.format("Node [%s] not supported yet. line=[%d] col=[%d]",
                        node.getClass().getSimpleName(), lc[0], lc[1]));
            }

            @Override
            protected String getHomePageTitle() {
                return "todo";
            } //todo

        };

        root.accept(ser);
        return ser.toString();
    }
}
