package spl.grammar;

import spl.lexer.Token;
import spl.lexer.TokenType;

public final class Terminal extends Symbol {
    private final TokenType type;

    public Terminal(TokenType type) {
        super(type.name());
        this.type = type;
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    public boolean matches(Token t) {
        return t.getType() == type;
    }
}
