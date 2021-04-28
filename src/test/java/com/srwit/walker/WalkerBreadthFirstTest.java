package com.srwit.walker;

import com.srwit.walker.IWalker.NodeWithLevel;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class WalkerBreadthFirstTest {
    final String baseUri = "http://baseuri";

    @Test()
    public void testWalkWithOutWalkCalled() {
        assertThrows(IllegalArgumentException.class, () -> new WalkerBreadthFirst(null, 0));
    }

    @Test
    public void testWalkerOneNodeNoChildren() {
        Document node = new Document(baseUri);
        final WalkerBreadthFirst walkerBreadthFirst = new WalkerBreadthFirst(node, 0);

        walkerBreadthFirst.walk();

        Set<NodeWithLevel> nodesVisited = walkerBreadthFirst.getOrderedNodesFound();

        NodeWithLevel expectedNodeWithLevel = new NodeWithLevel(node, 0);
        assertEquals(expectedNodeWithLevel, nodesVisited.iterator().next());
    }

    @Test
    public void testWalkerOneNodeOneLevelOfChildren() {
        Document topLevelNode = new Document(baseUri);

        Node childNode1 = new Element("Child1");
        topLevelNode.appendChild(childNode1);

        final WalkerBreadthFirst walkerBreadthFirst = new WalkerBreadthFirst(topLevelNode, 1);
        walkerBreadthFirst.walk();

        Set<NodeWithLevel> nodesVisited = walkerBreadthFirst.getOrderedNodesFound();

        Iterator<NodeWithLevel> nodeIterator = nodesVisited.iterator();

        NodeWithLevel nodeWithLevelTopExpected = new NodeWithLevel(topLevelNode, 0);
        assertEquals(nodeWithLevelTopExpected, nodeIterator.next());

        NodeWithLevel nodeWithLevelChildExpected = new NodeWithLevel(childNode1, 1);
        assertEquals(nodeWithLevelChildExpected, nodeIterator.next());
    }

    @Test
    public void testGetNodeAndGetLevel()
    {
        Node mockNode = mock(Node.class);
        NodeWithLevel nodeWithLevel = new NodeWithLevel(mockNode, 4);

        assertEquals(mockNode, nodeWithLevel.getNode());
        assertEquals(4, nodeWithLevel.getLevel());
    }

    @Test
    public void testProcessFullPageDifferentDomains() throws IOException {
        testWalkHtmlFile("differentDomains.html");
    }

    @Test
    public void testProcessFullPageHostWithPort() throws IOException {
        testWalkHtmlFile("hostWithPort.html");
    }

    @Test
    public void testProcessFullPageHttpHttps() throws IOException {
        testWalkHtmlFile("httpHttps.html");
    }

    @Test
    public void testProcessFullPagePageWithWhiteSapceInURL() throws IOException {
        testWalkHtmlFile("pageWithWhiteSpaceInURL.html");
    }

    private void testWalkHtmlFile(String filename) throws IOException {
        InputStream htmlStream = this.getClass().getClassLoader().getResourceAsStream(filename);
        @SuppressWarnings("ConstantConditions") String htmlString = IOUtils.toString(htmlStream, StandardCharsets.UTF_8);

        Assertions.assertFalse(htmlString.isBlank());

        Document document = Jsoup.parse(htmlString);

        final WalkerBreadthFirst walkerBreadthFirst = new WalkerBreadthFirst(document, 10);

        walkerBreadthFirst.walk();

        Set<NodeWithLevel> nodesVisited = walkerBreadthFirst.getOrderedNodesFound();
        assertTrue(!nodesVisited.isEmpty());
    }
}