package spl.table;

import java.util.Objects;
import spl.grammar.Production;
import spl.grammar.Symbol;

public final class LR0Item {
    private final Production production;
    private final int dot;

    public LR0Item(Production production, int dot) {
        this.production = production;
        this.dot = dot;
    }

    public Production getProduction() {
        return production;
    }

    public int getDot() {
        return dot;
    }

    public Symbol nextSymbol() {
        if (dot >= production.getRhs().size()) return null;
        return production.getRhs().get(dot);
    }

    public boolean isComplete() {
        return dot >= production.getRhs().size();
    }

    public LR0Item advance() {
        return new LR0Item(production, dot + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LR0Item)) return false;
        LR0Item other = (LR0Item) o;
        return dot == other.dot && production.getId() == other.production.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(production.getId(), dot);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(production.getLhs().getName()).append(" ->");
        var rhs = production.getRhs();
        for (int i = 0; i < rhs.size(); i++) {
            if (i == dot) sb.append(" .");
            sb.append(' ').append(rhs.get(i).getName());
        }
        if (dot == rhs.size()) sb.append(" .");
        return sb.toString();
    }
}
