package spl.tree;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SyntaxTree {
    private final RootNode root;
    private final Map<Integer, SyntaxTreeNode> nodes = new LinkedHashMap<>();

    public SyntaxTree(RootNode root) {
        this.root = root;
        addNode(root);
    }

    public RootNode getRoot() {
        return root;
    }

    public void addNode(SyntaxTreeNode n) {
        nodes.put(n.getId(), n);
    }

    public SyntaxTreeNode getNode(int id) {
        return nodes.get(id);
    }

    public void traverse(TreeVisitor v) {
        traverseFrom(root.getId(), v);
    }

    private void traverseFrom(int id, TreeVisitor v) {
        SyntaxTreeNode n = nodes.get(id);
        n.accept(v);
        if (n instanceof RootNode rn) {
            for (int childId : rn.getChildIds()) traverseFrom(childId, v);
        } else if (n instanceof InnerNode in) {
            for (int childId : in.getChildIds()) traverseFrom(childId, v);
        }
    }
}
