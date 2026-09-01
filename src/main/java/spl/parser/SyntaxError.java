package spl.parser;

import java.util.Set;
import java.util.stream.Collectors;
import spl.grammar.Terminal;
import spl.lexer.Token;

public final class SyntaxError {
    private final Token found;
    private final Set<Terminal> expected;
    private final String hint;
    private final int line;

    public SyntaxError(Token found, Set<Terminal> expected, String hint, int line) {
        this.found = found;
        this.expected = expected;
        this.hint = hint;
        this.line = line;
    }

    public Token getFound() {
        return found;
    }

    public Set<Terminal> getExpected() {
        return expected;
    }

    public String format() {
        String expectedStr = expected.stream().map(Terminal::getName)
                .sorted().collect(Collectors.joining(", "));
        return "Syntax error at " + found.position() + ": unexpected token "
                + found.getType() + " ('" + found.getLexeme() + "'). Expected one of: ["
                + expectedStr + "]. Hint: " + hint;
    }
}
