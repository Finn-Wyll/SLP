package spl.table;

import java.util.Set;
import spl.grammar.Terminal;

public final class ErrorEntry extends Action {
    private final Set<Terminal> expected;
    private final String hint;

    public ErrorEntry(Set<Terminal> expected, String hint) {
        this.expected = expected;
        this.hint = hint;
    }

    public Set<Terminal> getExpected() {
        return expected;
    }

    public String getHint() {
        return hint;
    }

    @Override
    public String kind() {
        return "error";
    }
}
