package org.bsc.markdown.pegdown;


import org.bsc.markdown.MarkdownParserContext;
import org.bsc.markdown.MarkdownVisitorHelper;
import org.parboiled.common.StringUtils;
import org.pegdown.Extensions;
import org.pegdown.ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.bsc.markdown.MarkdownVisitorHelper.isURL;
import static org.bsc.markdown.MarkdownVisitorHelper.processImageUrl;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bsorrentino
 */
public class PegdownConfluenceWikiVisitor implements Visitor {

    private final MarkdownParserContext parseContext;

    private final Consumer<org.pegdown.ast.Node> notImplementedYet;

    //list level
    private int listLevel = 0;

    // referenceNodes
    private HashMap<String, ReferenceNode> referenceNodes = new HashMap<String, ReferenceNode>();

    private StringBuilder _buffer = new StringBuilder( 500 * 1024 );

    private final java.util.Stack<Node> nodeStack = new java.util.Stack<Node>();

    public static int extensions() {

        int EXT = Extensions.NONE;

        EXT |= Extensions.FENCED_CODE_BLOCKS;
        EXT |= Extensions.TABLES;
        EXT |= Extensions.STRIKETHROUGH;
        // EXT |= Extensions.SMARTS; // Breaks link including a dash -

        return EXT;
    }

    /**
     *
     * @param text
     * @param node
     * @return [line,col]
     */
    public static int[] lineAndColFromNode(String text, Node node) {
        int lastEOL = 0;
        int prevEOL = 0;
        int length = text.length();
        int pos = 0;
        int line = 0;
        int col = 0;

        int offset = node.getStartIndex();

        if (offset > length) {
            offset = length;
        }

        while (pos < length) {
            pos = text.indexOf('\n', pos);
            if (pos == -1) break; // no EOL at the end or we hit the end

            prevEOL = lastEOL;
            lastEOL = pos;

            if (pos > offset) {
                break;
            }

            line++;
            pos++;
        }

        if (prevEOL < offset && lastEOL >= offset) {
            // offset is on this line
            col = offset - prevEOL;
        }

        line++;
        //System.out.println("offset : " + offset + " = (" + String.valueOf(line) + ":" + String.valueOf(col) + ")");

        return new int[] {line,col};
    }

    public PegdownConfluenceWikiVisitor(MarkdownParserContext parseContext, Consumer<org.pegdown.ast.Node> notImplementedYet) {
        this.parseContext = parseContext;
        this.notImplementedYet = notImplementedYet;
    }

    @Override
    public String toString() {
        return _buffer.toString();
    }

    protected StringBuilder bufferVisit( java.util.function.Consumer<Void> closure  ) {

        return bufferVisit( new StringBuilder(), closure );
    }

    protected StringBuilder bufferVisit( final StringBuilder _sb, java.util.function.Consumer<Void> closure  ) {

        final StringBuilder _original = _buffer;
        _buffer = _sb;
        try {
            closure.accept(null);
        }
        finally {
            _buffer = _original;
        }

        return _sb;
    }

    protected interface FindPredicate<T extends Node> {

        boolean f( T node, Node parent, int index );

    }

    /**
    * process:
    *  note,warning,info,tip
    */
    protected class SpecialPanelProcessor {
        private String element;
        private String title;


        final FindPredicate<TextNode> isSpecialPanelText = (TextNode p, Node parent, int index) -> {

                if( index != 0 ) return false;

                if( "note:".equalsIgnoreCase(p.getText()) ) {
                    element = "note"; // SET ELEMENT TAG
                    return true;
                }
                if( "warning:".equalsIgnoreCase(p.getText()) ) {
                    element = "warning"; // SET ELEMENT TAG
                    return true;
                }
                if( "info:".equalsIgnoreCase(p.getText()) ) {
                    element = "info"; // SET ELEMENT TAG
                    return true;
                }
                if( "tip:".equalsIgnoreCase(p.getText()) ) {
                    element = "tip"; // SET ELEMENT TAG
                    return true;
                }
                
                return false;
        };

        boolean apply( BlockQuoteNode bqn ) {
            element = null;
            title = null;

            java.util.List<Node> children = bqn.getChildren();

            if( children.size() < 2 ) {
                // We are sure to not have a title tag
                return false;
            }

            final boolean result =
                    findByClass(bqn.getChildren().get(0),
                        StrongEmphSuperNode.class,
                        new FindPredicate<StrongEmphSuperNode>() {

                @Override
                public boolean f(StrongEmphSuperNode p, final Node parent, final int index) {
                    if( index!=0 || !p.isStrong() ) return false;

                    boolean found =  findByClass(p,
                                    TextNode.class, isSpecialPanelText );

                    if( found ) { // GET ELEMENT TITLE

                        final StringBuilder _sb = bufferVisit(  (param) ->  {
                               parent.getChildren().remove(0);
                               visitChildren(parent);
                            }

                        );
                        title = _sb.toString().trim();

                    }

                    return found;

                }

            });

            if( result ) {

                _buffer.append( format("\n{%s%s}\n", element, isNotBlank(title)? ":title=" + title : ""));
                bqn.getChildren().remove(0);
                visitChildren(bqn);
                _buffer.append( format("\n{%s}\n", element) ).append('\n');
            }

            return result;

        }
    }

    @SuppressWarnings("unchecked")
    protected <T extends Node> void forEachChild( T node, FindPredicate<T> cb ) {
        final java.util.List<Node> children = node.getChildren();

        for (int index = 0 ; index < children.size() ; ++index) {

            final Node child = children.get(index);

            if( !cb.f( (T)child, node, index ) ) {
                return ;
            }
        }
    }

    protected <T extends Node> void visitChildren(T node) {
        for (Node child : node.getChildren()) {
            child.accept(this);
        }
    }

    protected <T extends Node, R extends Node> boolean findByClass(T node, final Class<R> clazz, final FindPredicate<R> predicate ) {
        boolean result = false;
        final java.util.List<Node> children = node.getChildren();

        for (int index = 0 ; index < children.size() ; ++index) {

            final Node child = children.get(index);

            if( clazz.isInstance(child) && predicate.f(clazz.cast(child), node, index)) {
                result = true;
            } else {
                result = findByClass( child, clazz, predicate);
            }
            if( result ) break;

        }

        return result;
    }

    final SpecialPanelProcessor specialPanelProcessor = new SpecialPanelProcessor();


    @Override
    public void visit(RootNode rn) {

        for (final ReferenceNode referenceNode : rn.getReferences()) {
            String ref = bufferVisit( (p) ->  visitChildren(referenceNode) ).toString();

            referenceNodes.put(ref, referenceNode);
        }
        visitChildren(rn);
    }

    @Override
    public void visit(SuperNode sn) {
        visitChildren(sn);
    }

    @Override
    public void visit(ParaNode pn) {
        visitChildren(pn);
        _buffer.append('\n');
    }


    @Override
    public void visit(HeaderNode hn) {
        _buffer.append( String.format( "\n\nh%s.", hn.getLevel()) );
        visitChildren(hn);
        _buffer.append("\n\n");
    }


    @Override
    public void visit(final BlockQuoteNode bqn) {

      final String text = bufferVisit( (p) -> visitChildren(bqn)).toString();

      final String lines[] = text.split("\n");

      if( lines.length == 1 ) {
        _buffer.append('\n')
               .append( "bq. ")
               .append( text )
               .append('\n');
        return;
      }

      if( specialPanelProcessor.apply(bqn) ) return;

      _buffer.append('\n')
             .append( "{quote}" )
             .append('\n')
             .append( text )
             .append('\n')
             .append( "{quote}" )
             .append('\n');

    }

    @Override
    public void visit(TextNode tn) {
        _buffer.append( tn.getText() );
    }

    @Override
    public void visit(ExpLinkNode eln) {

        _buffer.append( '[');
        visitChildren(eln);

        final String url = MarkdownVisitorHelper.processLinkUrl( eln.url, parseContext );

//        String url = eln.url;
//
//        if( !isURL(url) && FileExtension.MARKDOWN.isExentionOf(url)) {
//
//            final Optional<Site> site = parseContext.getSite();
//            if( site.isPresent() ) {
//
//                String _uri1 = url;
//
//                final Optional<Site.Page> page =
//                        site.get().getHome().findPage( p -> _uri1.equals( valueOf(p.getRelativeUri()) ));
//
//                if( page.isPresent() ) {
//
//                    final Optional<String> parentPageTitle = parseContext.getHomePageTitle();
//
//                    if( parentPageTitle.isPresent() && !url.startsWith(parentPageTitle.get())) {
//                        url = String.format( "%s - %s", parentPageTitle.get(), page.get().getName() );
//                    }
//                    else {
//                        url = page.get().getName();
//                    }
//
//
//                }
//            }
//        }

        _buffer.append(String.format("|%s|%s]", url, eln.title));
    }

    @Override
    public void visit(final RefLinkNode rln) {
        _buffer.append( '[' );
        visitChildren(rln);
        _buffer.append('|');

        final String ref = getRefString(rln, rln.referenceKey);

        String url = ref;

        final ReferenceNode referenceNode = referenceNodes.get(ref);
        if (referenceNode != null && referenceNode.getUrl() != null && url.length() > 0) {
            url = referenceNode.getUrl();
        }

        // If URL is a relative URL, we will create a link to the project
        if( !isURL(url) ) {
            final Optional<String> pagePrefixToApply = parseContext.getPagePrefixToApply();

            // not a valid URL (hence a relative link)
            if( pagePrefixToApply.isPresent() && !url.startsWith(pagePrefixToApply.get())) {
                _buffer.append(pagePrefixToApply.get()).append(" - ");
            }
        }
        _buffer.append(url);

        if (referenceNode != null && referenceNode.getTitle() != null) {
            _buffer.append('|').append(referenceNode.getTitle());
        }
        _buffer.append( ']' );
    }



    @Override
    public void visit(VerbatimNode vn) {

        final String lines[] = vn.getText().split("\n");
        if( lines.length == 1 ) {
            _buffer.append( "\n{noformat}")
                   .append(vn.getText())
                   .append( "{noformat}\n\n");
            return;
        }

        if( vn.getType()==null || vn.getType().isEmpty() ) {
            _buffer.append( "\n{noformat}\n")
                    .append(vn.getText())
                    .append("\n{noformat}\n\n")
                    ;
            return;
        }

        _buffer.append( String.format("\n{code:%s}\n", vn.getType()) )
                .append(vn.getText())
                .append("\n{code}\n\n")
                ;
    }

    @Override
    public void visit(CodeNode cn) {

        final String text = cn.getText();
        
        final String lines[] = text.split("\n");
        if( lines.length == 1 ) {
            
            _buffer.append( "{{")
                   .append(text
                        .replace("{", "\\{")
                        .replace("}", "\\}"))
                   .append( "}}");
            return;
        }

        _buffer
            .append( "\n{code}")
            .append('\n')
            .append(text)
            .append( "{code}")
            .append('\n')
            ;
    }

    @Override
    public void visit(StrongEmphSuperNode sesn) {
        char sym = '*';
        if( !sesn.isStrong() ) {
            final String chars = sesn.getChars();
            if( chars.equals("_")) sym = '_';
        }
        _buffer.append( sym);
        visitChildren(sesn);
        _buffer.append( sym );

    }



    @Override
    public void visit(StrikeNode sn) {
        _buffer.append("-");
        visitChildren(sn);
        _buffer.append("-");
    }

    @Override
    public void visit(ListItemNode lin) {
        visitChildren(lin);
    }

    @Override
    public void visit(final ExpImageNode ein) {
        // We always have a URL, relative or not

        final List<String> alt = new ArrayList<>();
        boolean found = findByClass(ein, TextNode.class, (node, parent, index) -> {
                alt.add(node.getText());
                return true;
        });

        final String altText = (found) ? alt.get(0) : "";
        
        final String titlePart = isNotBlank(ein.title) ? String.format("title=\"%s\"", ein.title) : "";
        
        final String url = processImageUrl(ein.url, parseContext );
        
        _buffer.append( format( "!%s|%s!", url, altText, titlePart));

    }

    @Override
    public void visit(final RefImageNode rin) {

        final ArrayList<String> alt = new ArrayList<String>();
        boolean found = findByClass(rin, TextNode.class, (TextNode node, Node parent, int index) -> {
                alt.add(node.getText());
                return true;
        });

        final String altText = (found) ? alt.get(0) : "";

        final SuperNode referenceKey = rin.referenceKey;
        final String ref = getRefString(rin, referenceKey);
        String ref_url = ref;
        String title = null;

        final ReferenceNode referenceNode = referenceNodes.get(ref);
        if (referenceNode != null) {
            if (isNotBlank(referenceNode.getUrl())) {
                ref_url = referenceNode.getUrl();
            }
            title = referenceNode.getTitle();
        }

        final String url = processImageUrl(ref_url, parseContext);

        final String titlePart = isNotBlank(title) ? format("|title=\"%s\"", title) : "";
        
        _buffer.append( format( "!%s|%s!", url, altText, titlePart));
        
        
        

    }

    private String getRefString(final SuperNode refnode, final SuperNode referenceKey) {
        final String ref;
        if( referenceKey != null ) {

            ref = bufferVisit((p) -> visitChildren(referenceKey)).toString();
        } else {
            // in case the refkey is not with the link, we use the references found in the root node
            ref = bufferVisit((p) -> visitChildren(refnode)).toString();
        }
        return ref;
    }

    private static boolean isNotBlank(String str) {
        return str != null && str.length() > 0;
    }

    @Override
    public void visit(HtmlBlockNode hbn) {
    }

    @Override
    public void visit(InlineHtmlNode ihn) {
    }

    @Override
    public void visit(TableHeaderNode thn) {
        nodeStack.push(thn);
        try {
            visitChildren(thn);
        }
        finally {
            assert thn == nodeStack.pop();
        }
    }

    @Override
    public void visit(TableBodyNode tbn) {
       nodeStack.push(tbn);
        try {
            visitChildren(tbn);
        }
        finally {
            assert tbn == nodeStack.pop();
        }
     }
    @Override
    public void visit(TableRowNode trn) {
        final Node n = nodeStack.peek();

        if( n instanceof TableHeaderNode )
            _buffer.append("||");
        else if( n instanceof TableBodyNode )
            _buffer.append('|');

        visitChildren(trn);
        _buffer.append('\n');
    }

    @Override
    public void visit(TableCaptionNode tcn) {
        notImplementedYet.accept(tcn);
    }

    @Override
    public void visit(TableCellNode tcn) {

        final Node n = nodeStack.peek();

        visitChildren(tcn);

        if( n instanceof TableHeaderNode )
            _buffer.append("||");
        else if( n instanceof TableBodyNode )
            _buffer.append('|');

    }

    @Override
    public void visit(TableColumnNode tcn) {
        notImplementedYet.accept(tcn);
    }

    @Override
    public void visit(TableNode tn) {
        visitChildren(tn);
    }

    @Override
    public void visit(AnchorLinkNode aln) {
        _buffer.append( aln.getText() );
    }

    @Override
    public void visit(SpecialTextNode stn) {
        _buffer.append(stn.getText());
    }


    @Override
    public void visit(AbbreviationNode an) {
        notImplementedYet.accept(an);
    }


    @Override
    public void visit(AutoLinkNode aln) {
        notImplementedYet.accept(aln);
    }


    @Override
    public void visit(DefinitionListNode dln) {
        notImplementedYet.accept(dln);
    }

    @Override
    public void visit(DefinitionNode dn) {
        notImplementedYet.accept(dn);
    }

    @Override
    public void visit(DefinitionTermNode dtn) {
        notImplementedYet.accept(dtn);
    }

    @Override
    public void visit(MailLinkNode mln) {
        notImplementedYet.accept(mln);
    }

    @Override
    public void visit(OrderedListNode oln) {

        ++listLevel;
        try {
            _buffer.append('\n');
            for (Node child : oln.getChildren()) {
                _buffer.append( StringUtils.repeat('#', listLevel) ).append(' ');
                child.accept(this);
                _buffer.append('\n');
            }
            _buffer.append('\n');
        }finally {
            --listLevel;
        }

    }

    @Override
    public void visit(BulletListNode bln) {

        ++listLevel;
        try {
            _buffer.append('\n');
            for (Node child : bln.getChildren()) {
                _buffer.append( StringUtils.repeat('*', listLevel) ).append(' ');
                child.accept(this);
                _buffer.append('\n');
            }
            _buffer.append('\n');
        }finally {
            --listLevel;
        }
    }


    @Override
    public void visit(QuotedNode qn) {
        notImplementedYet.accept(qn);
    }

    @Override
    public void visit(ReferenceNode rn) {
        // nothing to do. already done in RootNode
    }

    @Override
    public void visit(SimpleNode sn) {
        switch (sn.getType()) {
            case HRule:
                _buffer.append("----\n");
                break;
            case Linebreak:
                _buffer.append("\n");
                break;
            case Nbsp:
                _buffer.append("&nbsp;");
                break;
            case Emdash:
                _buffer.append("&mdash;");
                break;
            case Endash:
                _buffer.append("&ndash;");
                break;
            case Ellipsis:
                _buffer.append("&hellip;");
                break;
            default:
                notImplementedYet.accept(sn);
        }
    }

    @Override
    public void visit(WikiLinkNode wln) { notImplementedYet.accept(wln); }

    @Override
    public void visit(Node node) {
        notImplementedYet.accept(node);
    }

}
