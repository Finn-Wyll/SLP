package spl.tree;

public abstract class SyntaxTreeNode {
    protected final int id;
    protected Integer parentId;

    protected SyntaxTreeNode(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParent(int id) {
        this.parentId = id;
    }

    public abstract String contents();

    public abstract void accept(TreeVisitor v);
}
