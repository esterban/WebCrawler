package com.srwit;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.srwit.walker.WebSpider;
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
class WebSpiderTest {
    @Mock
    ObjectWriter mockObjectWriter;

    @Mock
    WebPageGetter mockWebPageGetter;


    @Test
    void processUri() throws IOException, URISyntaxException {
        InputStream htmlStream = this.getClass().getClassLoader().getResourceAsStream("httpHttps.html");
        @SuppressWarnings("ConstantConditions") String htmlString = IOUtils.toString(htmlStream, StandardCharsets.UTF_8);

        Assertions.assertFalse(htmlString.isBlank());

        Document document = Jsoup.parse(htmlString);

        when(mockWebPageGetter.getRequestForURIHtmlDocument(any(URI.class))).thenReturn(document);

        IURLNormaliser urlNormaliser = new URLNormaliser();

        WebSpider webSpider = new WebSpider(urlNormaliser, mockWebPageGetter, mockObjectWriter);
        webSpider.processUri(new URI("https://a.com/b.html"));

        Assertions.assertEquals("http://a.com/b.html", webSpider.getKnownInternalPages().iterator().next());
        Assertions.assertEquals(1, webSpider.getKnownInternalPages().size());
    }
}