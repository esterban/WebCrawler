package com.srwit;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebPageUriExtractorTest {
    @Mock
    WebPageGetter mockWebPageGetter;

    @Test
    public void test1() throws IOException {
        InputStream htmlStream = this.getClass().getClassLoader().getResourceAsStream("scratch_1.html");
        @SuppressWarnings("ConstantConditions") String htmlString = IOUtils.toString(htmlStream, StandardCharsets.UTF_8);

        Assertions.assertFalse(htmlString.isBlank());

        Document document = Jsoup.parse(htmlString);

        when(mockWebPageGetter.getRequestForURIHtmlDocument(any(URI.class))).thenReturn(document);

        WebPageUriExtractor webPageUriExtractor = new WebPageUriExtractor("http://bbc.com", mockWebPageGetter);
        webPageUriExtractor.parsePage();

        Assertions.assertFalse(webPageUriExtractor.getInternalURIs().isEmpty());
        Assertions.assertFalse(webPageUriExtractor.getExternalURIs().isEmpty());
        Assertions.assertFalse(webPageUriExtractor.getPageImages().isEmpty());
    }

    @Test
    public void testLinksWithWhiteSpace() throws IOException, URISyntaxException {
        InputStream htmlStream = this.getClass().getClassLoader().getResourceAsStream("pageWithWhiteSpaceInURL.html");
        @SuppressWarnings("ConstantConditions") String htmlString = IOUtils.toString(htmlStream, StandardCharsets.UTF_8);

        Assertions.assertFalse(htmlString.isBlank());

        Document document = Jsoup.parse(htmlString);

        when(mockWebPageGetter.getRequestForURIHtmlDocument(any(URI.class))).thenReturn(document);

        WebPageUriExtractor webPageUriExtractor = new WebPageUriExtractor("http://a.com", mockWebPageGetter);
        webPageUriExtractor.parsePage();

        Assertions.assertEquals(new URI("http://a.com/b.html"), webPageUriExtractor.getInternalURIs().get(0));
        Assertions.assertEquals(new URI("http://a.com/a.png") , webPageUriExtractor.getPageImages().get(0));
    }

    @Test
    public void testInternalLinks1() throws IOException {
        InputStream htmlStream = this.getClass().getClassLoader().getResourceAsStream("hostWithPort.html");
        @SuppressWarnings("ConstantConditions") String htmlString = IOUtils.toString(htmlStream, StandardCharsets.UTF_8);

        Assertions.assertFalse(htmlString.isBlank());

        Document document = Jsoup.parse(htmlString);

        when(mockWebPageGetter.getRequestForURIHtmlDocument(any(URI.class))).thenReturn(document);

        WebPageUriExtractor webPageUriExtractor = new WebPageUriExtractor("http://a.com", mockWebPageGetter);
        webPageUriExtractor.parsePage();

        Assertions.assertEquals(2, webPageUriExtractor.getInternalURIs().size(), "Should only be 2 internal link");
    }

    @Test
    public void testInternalExternalLinks1() throws IOException, URISyntaxException {
        InputStream htmlStream = this.getClass().getClassLoader().getResourceAsStream("differentDomains.html");
        @SuppressWarnings("ConstantConditions") String htmlString = IOUtils.toString(htmlStream, StandardCharsets.UTF_8);

        Assertions.assertFalse(htmlString.isBlank());

        Document document = Jsoup.parse(htmlString);

        when(mockWebPageGetter.getRequestForURIHtmlDocument(any(URI.class))).thenReturn(document);

        WebPageUriExtractor webPageUriExtractor = new WebPageUriExtractor("http://a.com", mockWebPageGetter);
        webPageUriExtractor.parsePage();

        Assertions.assertEquals(new URI("http://a.com/b.html"), webPageUriExtractor.getInternalURIs().get(0), "Should only be 1 internal link");
        Assertions.assertEquals(new URI("http://a.co.uk:8080/c.html"), webPageUriExtractor.getExternalURIs().get(0), "Should only be 1 external link");
    }

    @Test
    public void testHttpHttpsBothInternal() throws IOException, URISyntaxException {
        InputStream htmlStream = this.getClass().getClassLoader().getResourceAsStream("httpHttps.html");
        @SuppressWarnings("ConstantConditions") String htmlString = IOUtils.toString(htmlStream, StandardCharsets.UTF_8);

        Assertions.assertFalse(htmlString.isBlank());

        Document document = Jsoup.parse(htmlString);

        when(mockWebPageGetter.getRequestForURIHtmlDocument(any(URI.class))).thenReturn(document);

        WebPageUriExtractor webPageUriExtractor = new WebPageUriExtractor("http://a.com", mockWebPageGetter);
        webPageUriExtractor.parsePage();

        Assertions.assertEquals(new URI("https://a.com/b.html"), webPageUriExtractor.getInternalURIs().get(0), "Should only be 1 internal link");
        Assertions.assertEquals(new URI("http://a.com/b.html"), webPageUriExtractor.getInternalURIs().get(1), "Should only be 1 external link");
    }

}