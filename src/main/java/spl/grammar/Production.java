package spl.grammar;

import java.util.Collections;
import java.util.List;

public final class Production {
    private final int id;
    private final NonTerminal lhs;
    private final List<Symbol> rhs;

    public Production(int id, NonTerminal lhs, List<Symbol> rhs) {
        this.id = id;
        this.lhs = lhs;
        this.rhs = Collections.unmodifiableList(rhs);
    }

    public int getId() {
        return id;
    }

    public NonTerminal getLhs() {
        return lhs;
    }

    public List<Symbol> getRhs() {
        return rhs;
    }

    public boolean isEpsilon() {
        return rhs.isEmpty();
    }

    public int length() {
        return rhs.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lhs.getName()).append(" ->");
        if (rhs.isEmpty()) {
            sb.append(" epsilon");
        } else {
            for (Symbol s : rhs) sb.append(' ').append(s.getName());
        }
        return sb.toString();
    }
}
