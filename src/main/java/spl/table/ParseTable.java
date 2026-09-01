package spl.table;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import spl.grammar.NonTerminal;
import spl.grammar.Terminal;

public final class ParseTable {
    private final Map<String, Action> actions = new HashMap<>();
    private final Map<String, Integer> gotos = new HashMap<>();
    private final List<Conflict> conflicts = new ArrayList<>();

    private static String actionKey(int s, Terminal t) {
        return s + "#" + t.getName();
    }

    private static String gotoKey(int s, NonTerminal a) {
        return s + "#" + a.getName();
    }

    public void putAction(int s, Terminal t, Action action) {
        actions.put(actionKey(s, t), action);
    }

    public Action action(int s, Terminal t) {
        return actions.get(actionKey(s, t));
    }

    public void putGoto(int s, NonTerminal a, int target) {
        gotos.put(gotoKey(s, a), target);
    }

    public int gotoState(int s, NonTerminal a) {
        Integer v = gotos.get(gotoKey(s, a));
        return v == null ? -1 : v;
    }

    public Set<Terminal> expectedAt(int s) {
        Set<Terminal> result = new LinkedHashSet<>();
        for (Map.Entry<String, Action> e : actions.entrySet()) {
            if (e.getKey().startsWith(s + "#") && !(e.getValue() instanceof ErrorEntry)) {
                String termName = e.getKey().substring((s + "#").length());
                result.add(findTerminal(termName));
            }
        }
        result.removeIf(t -> t == null);
        return result;
    }

    private Terminal findTerminal(String name) {
        return new Terminal(spl.lexer.TokenType.valueOf(name));
    }

    public boolean hasConflicts() {
        return !conflicts.isEmpty();
    }

    public List<Conflict> getConflicts() {
        return conflicts;
    }

    public void addConflict(Conflict c) {
        conflicts.add(c);
    }
}
