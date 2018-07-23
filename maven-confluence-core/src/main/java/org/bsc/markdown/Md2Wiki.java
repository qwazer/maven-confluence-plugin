package org.bsc.markdown;

public interface Md2Wiki {

    /**
     * Convert markdown string to Confluence wiki string
     * @param markdown -  markdown string
     * @return -  Confluence wiki string
     */
    String convert(String markdown);
}
