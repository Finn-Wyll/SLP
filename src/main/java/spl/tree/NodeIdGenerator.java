package spl.tree;

public final class NodeIdGenerator {
    private int next = 0;

    public int newId() {
        return next++;
    }
}
