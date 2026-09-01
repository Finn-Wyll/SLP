package spl.parser;

import java.util.ArrayList;
import java.util.List;
import spl.lexer.LexicalError;

public final class ErrorReporter {
    private final List<String> messages = new ArrayList<>();
    private boolean hadError = false;

    public void lexicalError(LexicalError e) {
        hadError = true;
        messages.add(e.format());
    }

    public void syntaxError(SyntaxError e) {
        hadError = true;
        messages.add(e.format());
    }

    public boolean hadError() {
        return hadError;
    }

    public void printAll() {
        for (String m : messages) {
            System.err.println(m);
        }
    }

    public List<String> getMessages() {
        return messages;
    }
}
