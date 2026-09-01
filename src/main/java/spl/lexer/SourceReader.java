package spl.lexer;

public final class SourceReader {
    private final String source;
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private int column = 1;

    public SourceReader(String source) {
        this.source = source;
    }

    public char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    public char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    public char advance() {
        char c = source.charAt(current);
        current++;
        if (c == '\n' || c == '\r') {
            line++;
            column = 1;
        } else {
            column++;
        }
        return c;
    }

    public boolean match(char c) {
        if (isAtEnd() || peek() != c) return false;
        advance();
        return true;
    }

    public boolean isAtEnd() {
        return current >= source.length();
    }

    public String currentLexeme() {
        return source.substring(start, current);
    }

    public void beginLexeme() {
        start = current;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public int getLexemeStartColumn() {
        return column - (current - start);
    }
}
