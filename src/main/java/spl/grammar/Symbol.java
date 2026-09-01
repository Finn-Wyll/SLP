package spl.grammar;

public abstract class Symbol {
    protected final String name;

    protected Symbol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean isTerminal();

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Symbol)) return false;
        Symbol other = (Symbol) o;
        return isTerminal() == other.isTerminal() && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 31 + (isTerminal() ? 1 : 0);
    }
}
