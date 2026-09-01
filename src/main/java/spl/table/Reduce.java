package spl.table;

import spl.grammar.Production;

public final class Reduce extends Action {
    private final Production production;

    public Reduce(Production production) {
        this.production = production;
    }

    public Production getProduction() {
        return production;
    }

    @Override
    public String kind() {
        return "reduce";
    }

    @Override
    public String toString() {
        return "r" + production.getId();
    }
}
