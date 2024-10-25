package org.amplemarket;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        HtmlParser parser = new HtmlParser();

        parser.parseHtml();
    }

}