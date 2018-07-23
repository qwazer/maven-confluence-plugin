package org.bsc.markdown;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;


public abstract class Md2WikiTestBase {

    private Md2Wiki md2Wiki = new Md2WikiPegdownImpl();

    public Md2WikiTestBase(Md2Wiki md2Wiki) {
        this.md2Wiki = md2Wiki;
    }

    @Test
    public void simple() {

       String res=  md2Wiki.convert("hello");
       Assert.assertEquals("hello", res.trim());

    }
    @Test
    public void cheatsheet() throws IOException {

        convertAndCompare("cheatsheet.md", "{toc}\n" +
                "${pageTtitle}\n" +
                "\n" +
                "\n" +
                "h2.strikethrough\n" +
                "\n" +
                "-Mistaken text.-\n" +
                "\n" +
                "\n" +
                "h2.bold\n" +
                "\n" +
                "this bold text *Note: You must pass in an element name, and the name must contain a dash \"-\"*\n" +
                "\n" +
                "\n" +
                "h2.table\n" +
                "\n" +
                "||First Header ||Second Header ||\n" +
                "|Content Cell |Content Cell |\n" +
                "|Content Cell |Content Cell |\n" +
                "|Content Cell |Content Cell |\n" +
                "\n" +
                "\n" +
                "h2.Links\n" +
                "\n" +
                "\n" +
                "\n" +
                "h3.External\n" +
                "\n" +
                "Yeoman generator to scaffold out [Polymer 1.0|http://www.polymer-project.org/|]'s elements using Typescript based on [PolymerTS|https://github.com/nippur72/PolymerTS|] project\n" +
                "[PolymerTS|https://github.com/nippur72/PolymerTS|] is a project that allow to develop [Polymer 1.0|http://www.polymer-project.org/|] element using Typescript @decorated classes.\n" +
                "It is thought to work joined with [Polymer Starter Kit|https://developers.google.com/web/tools/polymer-starter-kit/|]\n" +
                "\n" +
                "\n" +
                "h3.Ref\n" +
                "\n" +
                "This is an ref link [README.MD|todo - README.MD]\n" +
                "This is an ref link [README.MD|todo - My Readme]\n" +
                "This is an ref link [README.MD|todo - 1]\n" +
                "\n" +
                "\n" +
                "h2.Unordered List\n" +
                "\n" +
                "\n" +
                "* [polymerts:el|#element-alias-el|]\n" +
                "* [polymerts:gen|#generate-typescript-from-element|]\n" +
                "* PolymerTS's element scaffold\n" +
                "* Sublist\n" +
                "## item 1\n" +
                "## item 2\n" +
                "## item 3\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "h2.Ordered List\n" +
                "\n" +
                "\n" +
                "# [polymerts:el|#element-alias-el|]\n" +
                "# [polymerts:gen|#generate-typescript-from-element|]\n" +
                "# PolymerTS's element scaffold\n" +
                "# Sublist\n" +
                "** item 1\n" +
                "** item 2\n" +
                "** item 3\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "h3.Element (alias: El)\n" +
                "\n" +
                "Generates a polymer element in {{app/elements}} and optionally appends an import to {{app/elements/elements.html}}.\n" +
                "\n" +
                "\n" +
                "h2.Inline HTML\n" +
                "\n" +
                "\n" +
                "\n" +
                "h2.Blocks\n" +
                "\n" +
                "*Note:* *Normal*\n" +
                "\n" +
                "{quote}\n" +
                "It generates also the related *Polymer Behaviors* but only if they are in the same element's folder (eg. iron-selector).\n" +
                "In other cases you have to generate each requested Behavior. So find it and rerun the generator.\n" +
                "*Note:* *Special*\n" +
                "\n" +
                "bq. Special Note\n" +
                "\n" +
                "\n" +
                "{quote}\n" +
                "\n" +
                "\n" +
                "h2.Image\n" +
                "\n" +
                "!https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png|alt=\"alt text\"|title=\"Logo Title Text 1\"!\n" +
                "!${pageTitle}^image-name.png|alt=\" \"!\n" +
                "!.images/image-name.png|alt=\"${pageTitle}^image-name.png\"!\n" +
                "[ref link node|todo - ref link node]\n" +
                "\n" +
                "\n" +
                "h2.Code / Verbatim\n" +
                "\n" +
                "XML:\n" +
                "\n" +
                "{code:xml}\n" +
                "  <developers>\n" +
                "    <developer>\n" +
                "      <id>bsorrentino</id>\n" +
                "      <name>Bartolomeo Sorrentino</name>\n" +
                "      <email>bartolomeo.sorrentino@gmail.com</email>\n" +
                "    </developer>\n" +
                "  </developers>\n" +
                "\n" +
                "\n" +
                "{code}\n" +
                "\n" +
                "this is inline {{npm install -g generator-polymerts}} command\n" +
                "\n" +
                "{noformat}\n" +
                "--path, element output path. By default generated element (and dependencies) will put  in folder 'typings/polymer'.\n" +
                "--elpath, element source path. Just in case (eg. Behaviors ) the element folder hasn't the same name of the element\n" +
                "\n" +
                "\n" +
                "{noformat}\n" +
                "\n" +
                "Example:\n" +
                "\n" +
                "{noformat}yo polymerts:gen polymer-element [--path ] [--elpath ]\n" +
                "{noformat}\n" +
                "\n") ;
    }

    @Test
    public void createSpecificNoticeBlock() throws IOException {

        convertAndCompare("createSpecificNoticeBlock.md", "\n" +
                "\n" +
                "h1.inlineCode with macro syntax\n" +
                "\n" +
                "\n" +
                "\n" +
                "h2.Imbricated info\n" +
                "\n" +
                "\n" +
                "{info:title=About me}\n" +
                "\n" +
                "bq. tposidufsqdf qsfpqs dfopqsdijf q  mldjkflqsdif sqj\n" +
                "\n" +
                "\n" +
                "{info}\n" +
                "\n" +
                "\n" +
                "\n" +
                "h2.Note without title\n" +
                "\n" +
                "\n" +
                "{note}\n" +
                "Contents of my note\n" +
                "\n" +
                "{note}\n" +
                "\n" +
                "\n" +
                "\n" +
                "h2. tip without imbrication\n" +
                "\n" +
                "\n" +
                "{tip:title=About you}\n" +
                "tposidufsqdf qsfpqs dfopqsdijf q  mldjkflqsdif sqj\n" +
                "\n" +
                "{tip}\n" +
                "\n" +
                "\n" +
                "\n" +
                "h2. warning with complex content\n" +
                "\n" +
                "\n" +
                "{warning:title=About him}\n" +
                "tposidufsqdf qsfpqs dfopqsdijf q  mldjkflqsdif sqj\n" +
                "\n" +
                "* one\n" +
                "* two\n" +
                "\n" +
                "have a *strong* and _pure_ feeling\n" +
                "\n" +
                "{warning}\n" +
                "\n" +
                "\n" +
                "\n" +
                "h2. oneline blockquote\n" +
                "\n" +
                "\n" +
                "bq. test a simple blockquote\n" +
                "\n" +
                "\n" +
                "\n" +
                "h2.blockquote block\n" +
                "\n" +
                "\n" +
                "{quote}\n" +
                "test a 2 paragraph block\n" +
                "this is my second part\n" +
                "\n" +
                "{quote}\n");

    }

    @Test
    public void gettingStarted() throws IOException {
        convertAndCompare("getting_started.md", "\n" +
                "\n" +
                "h1.Table of Content\n" +
                "\n" +
                "{toc}\n" +
                "\n" +
                "\n" +
                "h1.Getting Started with ${version}\n" +
                "\n" +
                "GPWS (aka SPWS) stands for Genesys PoWer Script. Below the features available \n" +
                "\n" +
                "\n" +
                "h2.Scripts' modularisation\n" +
                "\n" +
                "From this release GPWS support the [requirejs|http://requirejs.org/|] module inclusion.\n" +
                "\n" +
                "\n" +
                "h3.Inclusion\n" +
                "\n" +
                "To include a module or more modules (i.e. external javascript files) you have to use *require* function as shown in example below:\n" +
                "\n" +
                "{code:javascript}\n" +
                "require(['module1','module2'], function () {\n" +
                "\n" +
                "  print( \"module1.js and module2.js loaded!\");\n" +
                "\n" +
                "  // now you can use the functions/objects imported\n" +
                "});\n" +
                "\n" +
                "{code}\n" +
                "\n" +
                "\n" +
                "\n" +
                "h3.Definition\n" +
                "\n" +
                "It is also possible to define a module. Definition allowing module to publish object/function during require phase.\n" +
                "To define a module you have to use *define* function as shown in example below:\n" +
                "\n" +
                "bq. example: _module.js_\n" +
                "\n" +
                "\n" +
                "{code:javascript}\n" +
                "\n" +
                "define([\"require\", \"exports\"], function (require, exports) {\n" +
                "\n" +
                "  print(\"MODULE DEFINED!\");\n" +
                "\n" +
                "});\n" +
                "\n" +
                "{code}\n" +
                "\n" +
                "\n" +
                "\n" +
                "h3.links\n" +
                "\n" +
                "\n" +
                "\n" +
                "h4.External Link\n" +
                "\n" +
                "\n" +
                "* This is an external link [Google|http://www.google.com|]\n" +
                "\n" +
                "\n" +
                "\n" +
                "h4.Ref link\n" +
                "\n" +
                "\n" +
                "* This is a ref link [My page|todo - My page]\n" +
                "* This is a ref link [My page|todo - My Page Reference]\n" +
                "\n");
    }

    @Test
    public void simpleNodes() throws IOException {
        convertAndCompare("simpleNodes.md", "\n" +
                "\n" +
                "h1.Horizontal rules\n" +
                "\n" +
                "----\n" +
                "1\n" +
                "----\n" +
                "2\n" +
                "----\n" +
                "3\n" +
                "----\n" +
                "4\n" +
                "----\n" +
                "\n" +
                "\n" +
                "h1.Apostrophes\n" +
                "\n" +
                "'I'm with single quotes'\n" +
                "\"you're with double quotes\"\n" +
                "I'll talk to him 'bout borrowin' one of his models.\n" +
                "\n" +
                "\n" +
                "h1.Ellipsis\n" +
                "\n" +
                "Three dots {{...}} will be converted to ...\n" +
                "\n" +
                "\n" +
                "h1.Emdash\n" +
                "\n" +
                "Use 3 dashes {{---}} for an em-dash --- .\n" +
                "\n" +
                "\n" +
                "h1. Range (endash)\n" +
                "\n" +
                "\"it's all in chapters 12--14\"\n" +
                "\n" +
                "\n" +
                "h1.Line break\n" +
                "\n" +
                "Forcing a line-break\n" +
                "Next line in the list\n" +
                "\n" +
                "\n" +
                "h1.Nbsp\n" +
                "\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This will appear with six space characters in front of it.\n");
    }

    @Test
    public void withImgRefLink() throws IOException {
        convertAndCompare("withImgRefLink.md", "\n" +
                "\n" +
                "h1.Image ref link test\n" +
                "\n" +
                "\n" +
                "* add an absolute !http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|alt=\"conf-icon\"|title=\"My conf-icon\"! with title.\n" +
                "* add a relative !conf-icon-64.png|alt=\"conf-icon\"|title=\"My conf-icon\"! with title.\n" +
                "* add a relative !conf-icon-64.png|alt=\"conf-icon\"! without title.\n" +
                "* add a ref img !http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|alt=\"conf-icon-y\"|title=\"My conf-icon\"! with title.\n" +
                "* add a ref img !http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png|alt=\"conf-icon-y1\"! without title.\n" +
                "* add a ref img !conf-icon-64.png|alt=\"conf-icon-y2\"! relative.\n" +
                "* add a ref img !conf-icon-64.png|alt=\"conf-icon-none\"! relative with default refname.\n" +
                "\n");
    }

    @Test
    public void withRefLink() throws IOException {
        convertAndCompare("withRefLink.md", "\n" +
                "\n" +
                "h1.Site ref link test\n" +
                "\n" +
                "\n" +
                "* This one is [inline|http://google.com|Google].\n" +
                "* This one is [inline *wo* title|http://google.com|].\n" +
                "* This is my [google|http://google.com] link defined after.\n" +
                "* This is my [more complex google|http://google.com|Other google] link defined after.\n" +
                "* This is my [relative|relativepage|] link defined after.\n" +
                "* This is my [rel|todo - relativeagain] link defined after.\n" +
                "\n");
    }


    public void convertAndCompare(String file, String expected) throws IOException {
        String content = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(file));
        String res=  md2Wiki.convert(content);
        Assert.assertEquals(expected, res);
    }
}