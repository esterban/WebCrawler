package com.srwit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WebSpider {
    private static final Logger LOG = LogManager.getLogger();

    private int debugPageCount = 0;
    private final static int debugMaxPageCounter = 3;
    private final IURLNormaliser urlNormaliser;
    private final Set<String> knownInternalPages = new HashSet<>();
    private final WebPageGetter webPageGetter;
    private final ObjectWriter objectWriter;

    public WebSpider(IURLNormaliser urlNormaliser, WebPageGetter webPageGetter, ObjectWriter objectWriter) {
        this.urlNormaliser = urlNormaliser;
        this.webPageGetter = webPageGetter;
        this.objectWriter = objectWriter;
    }

    public Set<String> getKnownInternalPages() {
        return Collections.unmodifiableSet(knownInternalPages);
    }

    public void processUri(URI uri) {
        if (debugPageCount >= debugMaxPageCounter) {
            System.exit(0);
        }

        ++debugPageCount;

        String currentPage = urlNormaliser.normalise(uri);
        knownInternalPages.add(currentPage);

        String uriString = uri.toString();

        WebPageUriExtractor webPageUriExtractor = new WebPageUriExtractor(uriString, webPageGetter);
        webPageUriExtractor.parsePage();

        try {
            String pageJsonString = objectWriter.writeValueAsString(webPageUriExtractor);

            LOG.info(pageJsonString);
        } catch (JsonProcessingException e) {
            LOG.error("Could not write URI {} to JSON", uriString, e);
        }

        List<URI> childURIList = webPageUriExtractor.getInternalURIs();

        for (URI childUri : childURIList) {
            String childPage = urlNormaliser.normalise(childUri);

            if (knownInternalPages.contains(childPage))
            {
                continue;
            }

            knownInternalPages.add(childPage);

            processUri(childUri);
        }
    }


}
