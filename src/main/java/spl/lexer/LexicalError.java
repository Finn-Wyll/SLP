package spl.lexer;

public final class LexicalError {
    private final String message;
    private final String hint;
    private final int line;
    private final int column;

    public LexicalError(String message, String hint, int line, int column) {
        this.message = message;
        this.hint = hint;
        this.line = line;
        this.column = column;
    }

    public String format() {
        return "Lexical error at line " + line + ", column " + column + ": " + message
                + (hint == null || hint.isEmpty() ? "" : " (hint: " + hint + ")");
    }

    public String getMessage() {
        return message;
    }

    public String getHint() {
        return hint;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
