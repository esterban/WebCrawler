package com.srwit.walker.controller;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.srwit.URLNormaliser;
import com.srwit.WebPageGetter;
import com.srwit.walker.WebSpider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class WalkerController {
    private static final Logger LOG = LogManager.getLogger();

    private final WebPageGetter webPageGetter = new WebPageGetter();
    private final ObjectWriter objectWriter;
    private final URLNormaliser urlNormaliser = new URLNormaliser();

    public WalkerController() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        this.objectWriter = objectMapper.writer(prettyPrinter);
    }

    @GetMapping("/crawl")
    public void crawlWebsite(@RequestParam("uri") String targetUriString) throws URISyntaxException {
        URI targetUri;
        try {
            targetUri = new URI(targetUriString);

//            crawlWebsite(targetUriString);
        } catch (URISyntaxException e) {
            LOG.error("Could not parse top most URI {}", targetUriString, e);

            throw e;
        }

        WebSpider webSpider = new WebSpider(urlNormaliser, webPageGetter, objectWriter);

        webSpider.processUri(targetUri);
    }
}
