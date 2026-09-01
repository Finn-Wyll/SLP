package spl.table;

import spl.grammar.FirstFollowSets;
import spl.grammar.NonTerminal;
import spl.grammar.Production;
import spl.grammar.SPLGrammar;
import spl.grammar.Symbol;
import spl.grammar.Terminal;

public final class SLRTableGenerator {
    private final SPLGrammar grammar;
    private final CanonicalCollection collection;
    private final FirstFollowSets sets;
    private final ParseTable table = new ParseTable();

    public SLRTableGenerator(SPLGrammar grammar, CanonicalCollection collection, FirstFollowSets sets) {
        this.grammar = grammar;
        this.collection = collection;
        this.sets = sets;
    }

    public ParseTable generate() {
        addShiftsAndGotos();
        addReductionsOnFollow();
        return table;
    }

    private void addShiftsAndGotos() {
        for (ItemSet state : collection.getStates()) {
            for (var entry : collection.getTransitions().entrySet()) {
                if (entry.getKey().state != state.getId()) continue;
                Symbol x = entry.getKey().symbol;
                int target = entry.getValue();
                if (x.isTerminal()) {
                    Action incoming = new Shift(target);
                    Action existing = table.action(state.getId(), (Terminal) x);
                    if (existing != null && !sameAction(existing, incoming)) {
                        table.addConflict(new Conflict(state.getId(), x.getName(), existing, incoming));
                    } else {
                        table.putAction(state.getId(), (Terminal) x, incoming);
                    }
                } else {
                    table.putGoto(state.getId(), (NonTerminal) x, target);
                }
            }
        }
    }

    private void addReductionsOnFollow() {
        Production rule0 = grammar.getProductions().get(0);
        for (ItemSet state : collection.getStates()) {
            for (LR0Item item : state.getItems()) {
                if (!item.isComplete()) continue;
                Production p = item.getProduction();
                if (p.getId() == rule0.getId()) {
                    // Item S' -> SPL_PROG . : accept on EOF
                    Terminal eof = grammar.terminal(spl.lexer.TokenType.EOF);
                    recordReduceOrAccept(state.getId(), eof, new Accept());
                    continue;
                }
                for (Terminal a : sets.follow(p.getLhs())) {
                    recordReduceOrAccept(state.getId(), a, new Reduce(p));
                }
            }
        }
    }

    private void recordReduceOrAccept(int s, Terminal t, Action incoming) {
        Action existing = table.action(s, t);
        if (existing != null && !sameAction(existing, incoming)) {
            recordConflict(s, t, existing, incoming);
        } else {
            table.putAction(s, t, incoming);
        }
    }

    private boolean sameAction(Action a, Action b) {
        if (a.getClass() != b.getClass()) return false;
        if (a instanceof Shift) return ((Shift) a).getTargetState() == ((Shift) b).getTargetState();
        if (a instanceof Reduce) return ((Reduce) a).getProduction().getId() == ((Reduce) b).getProduction().getId();
        return true;
    }

    private void recordConflict(int s, Terminal t, Action existing, Action incoming) {
        table.addConflict(new Conflict(s, t.getName(), existing, incoming));
        // Prefer an existing Shift over an incoming Reduce (classic shift-reduce resolution);
        // otherwise keep the first action found.
        if (existing instanceof Reduce && incoming instanceof Shift) {
            table.putAction(s, t, incoming);
        }
    }
}
