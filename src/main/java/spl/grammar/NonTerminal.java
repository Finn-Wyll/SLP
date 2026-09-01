package spl.grammar;

public final class NonTerminal extends Symbol {
    private boolean nullable;

    public NonTerminal(String name) {
        super(name);
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
