package com.srwit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class WebPageUriExtractor {
    private transient static final Logger LOG = LogManager.getLogger();

    private final String webPageURIString;
    private URI webPageURI;

    private final WebPageGetter webPageGetter;

    private final List<URI> internalURIs = new ArrayList<>();
    private final List<URI> externalURIs = new ArrayList<>();
    private final List<URI> pageImages = new ArrayList<>();

    private transient boolean hasPageBeenParsed = false;

    public WebPageUriExtractor(String webPageURIString, WebPageGetter webPageGetter) {
        this.webPageURIString = webPageURIString;
        this.webPageGetter = webPageGetter;
    }

    public void parsePage() {
        if (!hasPageBeenParsed) {
            try {
                webPageURI = new URI(webPageURIString);

                Document document = webPageGetter.getRequestForURIHtmlDocument(webPageURI);

                recurseNodes(document);

                LOG.debug("Number of known nodes = {}", (internalURIs.size() + externalURIs.size()));

                internalURIs.forEach(link -> LOG.debug("Internal URI = {}", link));
                externalURIs.forEach(link -> LOG.debug("External URI = {}", link));
                pageImages.forEach(link -> LOG.debug("Image URI = {}", link));

                hasPageBeenParsed = true;
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public List<URI> getInternalURIs() {
        return Collections.unmodifiableList(internalURIs);
    }

    public List<URI> getExternalURIs() {
        return Collections.unmodifiableList(externalURIs);
    }

    public List<URI> getPageImages() {
        return Collections.unmodifiableList(pageImages);
    }

    public String getWebPageURIString() {
        return webPageURIString;
    }

    private void recurseNodes(final org.jsoup.nodes.Node node) {
        if (node == null) {
            return;
        }

        URI linkUri = getNodeUri(node);

        if (linkUri != null) {
            if (linkUri.getHost().equals(webPageURI.getHost())) {
                internalURIs.add(linkUri);
            } else {
                externalURIs.add(linkUri);
            }
        } else {
            URI imageNodeUri = getImageURI(node);

            if (imageNodeUri != null) {
                pageImages.add(imageNodeUri);
            }
        }

        List<org.jsoup.nodes.Node> nodeList = node.childNodes();

        if (nodeList == null) {
            return;
        }

        for (Node childNode : nodeList) {
            recurseNodes(childNode);
        }
    }

    private URI getNodeUri(Node node) {
        if (node.nodeName().equals("a") && node.hasAttr("href")) {
            String hrefString = node.attr("href");
            hrefString = hrefString.trim();

            URI nodeUri = webPageURI.resolve(hrefString);

            if (nodeUri.getHost() != null && !nodeUri.getHost().isBlank()) {
                return nodeUri;
            }
        }

        return null;
    }

    private URI getImageURI(Node node) {
        if (node.nodeName().equals("img") && node.hasAttr("src")) {
            String imageSrc = node.attr("src");
            imageSrc = imageSrc.trim();

            URI imageNodeUri = webPageURI.resolve(imageSrc);

            if (imageNodeUri.getHost() != null && !imageNodeUri.getHost().isBlank()) {
                return imageNodeUri;
            }
        }

        return null;
    }
}
