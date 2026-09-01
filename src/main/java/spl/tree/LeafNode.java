package spl.tree;

import spl.lexer.Token;

public final class LeafNode extends SyntaxTreeNode {
    private final Token token;

    public LeafNode(int id, Token token) {
        super(id);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public String contents() {
        return token.getLexeme();
    }

    @Override
    public void accept(TreeVisitor v) {
        v.visitLeaf(this);
    }
}
