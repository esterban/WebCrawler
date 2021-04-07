package com.srwit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class URITester {

    private final URLNormaliser urlNormaliser = new URLNormaliser();

    // https://a.com == http://a.com
    @Test
    public void testURIs1() throws URISyntaxException {
        String uriString1 = "https://a.com";
        String uriString2 = "http://a.com";

        URI uri1 = new URI(uriString1);
        URI uri2 = new URI(uriString2);

        Assertions.assertNotEquals(uri2, uri1);

        String normalisedUri1 = urlNormaliser.normalise(uri1);
        String normalisedUri2 = urlNormaliser.normalise(uri2);

        Assertions.assertEquals(normalisedUri2, normalisedUri1);
    }


    // http://a.com/a/ == http://a.com/a
    @Test
    public void testURIs2() throws URISyntaxException {
        String uriString1 = "http://a.com/a/";
        String uriString2 = "http://a.com/a";

        URI uri1 = new URI(uriString1);
        URI uri2 = new URI(uriString2);

        Assertions.assertNotEquals(uri2, uri1);

        String normalisedUri1 = urlNormaliser.normalise(uri1);
        String normalisedUri2 = urlNormaliser.normalise(uri2);

        Assertions.assertEquals(normalisedUri2, normalisedUri1);
    }

    // http://a.com/a/a.html == <self>
    @Test
    public void testURIs3() throws URISyntaxException {
        String uriString1 = "http://a.com/a/a.html";
        URI uri1 = new URI(uriString1);

        String normalisedUri1 = urlNormaliser.normalise(uri1);

        Assertions.assertEquals(uriString1, normalisedUri1);
    }

    // https://a.com/a/a.html == http://a.com/a/a.html
    @Test
    public void testURIs4() throws URISyntaxException {
        String uriString1 = "https://a.com/a.html?query=value";
        String uriString2 = "http://a.com/a.html";

        URI uri1 = new URI(uriString1);
        URI uri2 = new URI(uriString2);

        Assertions.assertNotEquals(uri2, uri1);

        String normalisedUri1 = urlNormaliser.normalise(uri1);
        String normalisedUri2 = urlNormaliser.normalise(uri2);

        Assertions.assertEquals(normalisedUri2, normalisedUri1);
    }


    // https://a.com/b.html -> http://a.com/b.html
    @Test
    public void testURIs5() throws URISyntaxException {
        String uriString1 = "https://a.com/b.html";

        URI uri1 = new URI(uriString1);

        String normalisedUri1 = urlNormaliser.normalise(uri1);

        Assertions.assertEquals(normalisedUri1, "http://a.com/b.html");
    }


}
