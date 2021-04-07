package com.srwit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;

public class WebPageGetter {
    private static int DEFAULT_TIMEOUT_MILLISECONDS = 10 * 1000;
    private int timeout = DEFAULT_TIMEOUT_MILLISECONDS;

    public WebPageGetter()
    {
    }

    public Document getRequestForURIHtmlDocument(URI uri) throws IOException {
        return Jsoup.connect(uri.toString()).get();
    }
}
