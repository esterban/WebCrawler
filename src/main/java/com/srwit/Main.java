package com.srwit;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class Main implements Runnable {
    private static final Logger LOG = LogManager.getLogger();

    private static final String targetUriString = "https://www.wipro.com/";

    private final URI targetUri;

    private ObjectWriter objectWriter;

    private final WebPageGetter webPageGetter;

    private IURLNormaliser urlNormaliser = new URLNormaliser();

    public static void main(String[] args) throws URISyntaxException {
        Main main = new Main();

        main.run();
    }

    public Main() throws URISyntaxException {
        try {
            targetUri = new URI(targetUriString);
        } catch (URISyntaxException e) {
            LOG.error("Could not parse top most URI {}", targetUriString, e);

            throw e;
        }

        webPageGetter = new WebPageGetter();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        objectWriter = objectMapper.writer(prettyPrinter);
    }

    @Override
    public void run() {

        WebSpider webSpider = new WebSpider(urlNormaliser, webPageGetter, objectWriter);

        webSpider.processUri(targetUri);
    }
}

