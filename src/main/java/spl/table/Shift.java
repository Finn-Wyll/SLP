package spl.table;

public final class Shift extends Action {
    private final int targetState;

    public Shift(int targetState) {
        this.targetState = targetState;
    }

    public int getTargetState() {
        return targetState;
    }

    @Override
    public String kind() {
        return "shift";
    }

    @Override
    public String toString() {
        return "s" + targetState;
    }
}
