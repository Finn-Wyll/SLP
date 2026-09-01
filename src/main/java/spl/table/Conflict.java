package spl.table;

public final class Conflict {
    private final int state;
    private final String terminalName;
    private final Action existing;
    private final Action incoming;

    public Conflict(int state, String terminalName, Action existing, Action incoming) {
        this.state = state;
        this.terminalName = terminalName;
        this.existing = existing;
        this.incoming = incoming;
    }

    @Override
    public String toString() {
        return "Conflict in state " + state + " on " + terminalName
                + ": " + existing + " vs " + incoming;
    }
}
