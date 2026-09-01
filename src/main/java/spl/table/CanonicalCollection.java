package spl.table;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import spl.grammar.NonTerminal;
import spl.grammar.Production;
import spl.grammar.SPLGrammar;
import spl.grammar.Symbol;

public final class CanonicalCollection {
    private final SPLGrammar grammar;
    private final List<ItemSet> states = new ArrayList<>();
    private final Map<StateSymbolPair, Integer> transitions = new HashMap<>();

    public CanonicalCollection(SPLGrammar grammar) {
        this.grammar = grammar;
    }

    public void build() {
        NonTerminal augmentedStart = grammar.getStartSymbol();
        Production rule0 = grammar.getProductions().get(0);
        Set<LR0Item> startKernel = new LinkedHashSet<>();
        startKernel.add(new LR0Item(rule0, 0));
        ItemSet start = new ItemSet(startKernel);
        start.closure(grammar);
        start.setId(0);
        states.add(start);

        boolean changed = true;
        while (changed) {
            changed = false;
            List<ItemSet> snapshot = new ArrayList<>(states);
            for (ItemSet state : snapshot) {
                Set<Symbol> symbolsAfterDot = new LinkedHashSet<>();
                for (LR0Item item : state.getItems()) {
                    Symbol s = item.nextSymbol();
                    if (s != null) symbolsAfterDot.add(s);
                }
                for (Symbol x : symbolsAfterDot) {
                    Set<LR0Item> kernel = state.gotoKernel(x);
                    if (kernel.isEmpty()) continue;
                    ItemSet candidate = new ItemSet(kernel);
                    candidate.closure(grammar);

                    int targetId = indexOf(candidate);
                    if (targetId == -1) {
                        candidate.setId(states.size());
                        states.add(candidate);
                        targetId = candidate.getId();
                        changed = true;
                    }
                    StateSymbolPair key = new StateSymbolPair(state.getId(), x);
                    if (!transitions.containsKey(key)) {
                        transitions.put(key, targetId);
                        changed = true;
                    }
                }
            }
        }
    }

    private int indexOf(ItemSet candidate) {
        for (ItemSet s : states) {
            if (s.equals(candidate)) return s.getId();
        }
        return -1;
    }

    public int stateCount() {
        return states.size();
    }

    public List<ItemSet> getStates() {
        return states;
    }

    public Integer transition(int s, Symbol x) {
        return transitions.get(new StateSymbolPair(s, x));
    }

    public Map<StateSymbolPair, Integer> getTransitions() {
        return transitions;
    }

    public static final class StateSymbolPair {
        public final int state;
        public final Symbol symbol;

        public StateSymbolPair(int state, Symbol symbol) {
            this.state = state;
            this.symbol = symbol;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof StateSymbolPair)) return false;
            StateSymbolPair p = (StateSymbolPair) o;
            return state == p.state && symbol.equals(p.symbol);
        }

        @Override
        public int hashCode() {
            return state * 31 + symbol.hashCode();
        }
    }
}
