package spl.tree;

public interface TreeVisitor {
    void visitRoot(RootNode n);

    void visitInner(InnerNode n);

    void visitLeaf(LeafNode n);
}
