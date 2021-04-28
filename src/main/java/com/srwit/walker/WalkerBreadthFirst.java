package com.srwit.walker;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Node;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WalkerBreadthFirst implements IWalker {
    private final Node topLevelNode;
    private final LinkedHashSet<NodeWithLevel> nodesVisited = new LinkedHashSet<>();
    private final int numberOfDepthToWalk;

    public WalkerBreadthFirst(Node topLevelNode, int numberOfDepthToWalk) {
        Validate.notNull(topLevelNode, "Top most node must not be null when walking the tree");
        Validate.isTrue(numberOfDepthToWalk >= 0, "Walking depth must be 0 or greater");

        this.topLevelNode = topLevelNode.clone();

        this.numberOfDepthToWalk = numberOfDepthToWalk;
    }

//    public WalkerBreadthFirst() {
//    }

//    public WalkerBreadthFirst

    @Override
    public void walk() {
        LinkedHashSet<Node> currentLevelNodeSet = new LinkedHashSet<>();
        currentLevelNodeSet.add(topLevelNode);
        nodesVisited.add(new NodeWithLevel(topLevelNode, 0));

        for (int levelCounter = 1; levelCounter <= numberOfDepthToWalk; ++levelCounter)
        {
            LinkedHashSet<Node> nextLevelChildNodes = getNextLevelDown(currentLevelNodeSet);
            currentLevelNodeSet = nextLevelChildNodes;

            final int finalLevelCounter = levelCounter;
            nextLevelChildNodes.forEach(n -> nodesVisited.add(new NodeWithLevel(n, finalLevelCounter)));
        }
    }

    private LinkedHashSet<Node> getNextLevelDown(LinkedHashSet<Node> node) {
        LinkedHashSet<Node> childNodeSet = new LinkedHashSet<>();

        for (Node currentNode : node) {
            List<Node> newChildNodes = currentNode.childNodes().stream().filter(n -> !nodesVisited.contains(n)).collect(Collectors.toList());

            childNodeSet.addAll(newChildNodes);
        }

        return childNodeSet;
    }


    @Override
    public Set<NodeWithLevel> getOrderedNodesFound() {
        return nodesVisited;
    }

//    public static class NodeWithLevel()
//    {
//        private final Node node;
//        private final int level;
//
//        public NodeWithLevel(Node node, int level) {
//            this.node = node;
//            this.level = level;
//        }
//
//        public Node getNode() {
//            return node;
//        }
//
//        public int getLevel() {
//            return level;
//        }
//    }

}
