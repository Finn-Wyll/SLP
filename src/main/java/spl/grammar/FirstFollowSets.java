package spl.grammar;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

public final class FirstFollowSets {
    private final SPLGrammar grammar;
    private final Map<Symbol, Set<Terminal>> first = new HashMap<>();
    private final Map<NonTerminal, Set<Terminal>> follow = new HashMap<>();
    private final Set<NonTerminal> nullable = new LinkedHashSet<>();

    public FirstFollowSets(SPLGrammar grammar) {
        this.grammar = grammar;
        computeNullable();
        computeFirst();
        computeFollow();
    }

    public void computeNullable() {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Production p : grammar.getProductions()) {
                if (nullable.contains(p.getLhs())) continue;
                if (p.isEpsilon()) {
                    nullable.add(p.getLhs());
                    changed = true;
                    continue;
                }
                boolean allNullable = true;
                for (Symbol s : p.getRhs()) {
                    if (s.isTerminal() || !nullable.contains(s)) {
                        allNullable = false;
                        break;
                    }
                }
                if (allNullable) {
                    nullable.add(p.getLhs());
                    changed = true;
                }
            }
        }
        for (NonTerminal nt : grammar.getNonTerminals()) {
            nt.setNullable(nullable.contains(nt));
        }
    }

    public void computeFirst() {
        for (Terminal t : grammar.getTerminals()) {
            first.put(t, new LinkedHashSet<>(Set.of(t)));
        }
        for (NonTerminal nt : grammar.getNonTerminals()) {
            first.put(nt, new LinkedHashSet<>());
        }
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Production p : grammar.getProductions()) {
                Set<Terminal> lhsFirst = first.get(p.getLhs());
                boolean allNullableSoFar = true;
                for (Symbol s : p.getRhs()) {
                    if (!allNullableSoFar) break;
                    Set<Terminal> sFirst = first.get(s);
                    if (lhsFirst.addAll(sFirst)) changed = true;
                    allNullableSoFar = !s.isTerminal() && nullable.contains(s);
                }
            }
        }
    }

    public void computeFollow() {
        for (NonTerminal nt : grammar.getNonTerminals()) {
            follow.put(nt, new LinkedHashSet<>());
        }
        follow.get(grammar.getStartSymbol()).add(grammar.terminal(spl.lexer.TokenType.EOF));

        boolean changed = true;
        while (changed) {
            changed = false;
            for (Production p : grammar.getProductions()) {
                List<Symbol> rhs = p.getRhs();
                for (int i = 0; i < rhs.size(); i++) {
                    Symbol b = rhs.get(i);
                    if (b.isTerminal()) continue;
                    NonTerminal bNt = (NonTerminal) b;
                    Set<Terminal> followB = follow.get(bNt);

                    Set<Terminal> firstOfRest = first(rhs.subList(i + 1, rhs.size()));
                    if (followB.addAll(firstOfRest)) changed = true;

                    boolean restNullable = true;
                    for (int j = i + 1; j < rhs.size(); j++) {
                        Symbol s = rhs.get(j);
                        if (s.isTerminal() || !nullable.contains(s)) {
                            restNullable = false;
                            break;
                        }
                    }
                    if (restNullable) {
                        if (followB.addAll(follow.get(p.getLhs()))) changed = true;
                    }
                }
            }
        }
    }

    public Set<Terminal> first(List<Symbol> seq) {
        Set<Terminal> result = new LinkedHashSet<>();
        boolean allNullableSoFar = true;
        for (Symbol s : seq) {
            if (!allNullableSoFar) break;
            result.addAll(first.get(s));
            allNullableSoFar = !s.isTerminal() && nullable.contains(s);
        }
        return result;
    }

    public Set<Terminal> follow(NonTerminal a) {
        return follow.get(a);
    }

    public Set<Terminal> firstOf(Symbol s) {
        return first.get(s);
    }

    public boolean isNullable(Symbol s) {
        return !s.isTerminal() && nullable.contains(s);
    }
}
