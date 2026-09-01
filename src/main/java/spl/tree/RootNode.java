package spl.tree;

import java.util.ArrayList;
import java.util.List;
import spl.grammar.NonTerminal;

public final class RootNode extends SyntaxTreeNode {
    private final NonTerminal symbol;
    private final List<Integer> childIds = new ArrayList<>();

    public RootNode(int id, NonTerminal symbol) {
        super(id);
        this.symbol = symbol;
    }

    public NonTerminal getSymbol() {
        return symbol;
    }

    public List<Integer> getChildIds() {
        return childIds;
    }

    public void addChild(int childId) {
        childIds.add(childId);
    }

    @Override
    public String contents() {
        return symbol.getName();
    }

    @Override
    public void accept(TreeVisitor v) {
        v.visitRoot(this);
    }
}
