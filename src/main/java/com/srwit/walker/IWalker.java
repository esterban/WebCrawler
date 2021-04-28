package com.srwit.walker;

import org.jsoup.nodes.Node;

import java.util.Objects;
import java.util.Set;

public interface IWalker {
    void walk();

    Set<NodeWithLevel> getOrderedNodesFound();

    final class NodeWithLevel
    {
        private final Node node;
        private final int level;

        public NodeWithLevel(Node node, int level) {
            this.node = node;
            this.level = level;
        }

        public Node getNode() {
            return node;
        }

        public int getLevel() {
            return level;
        }

        @Override
        public final boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NodeWithLevel that = (NodeWithLevel) o;
            return level == that.level && node.hasSameValue(that.node);
        }

        @Override
        public final int hashCode() {
            return Objects.hash(node, level);
        }
    };
}
