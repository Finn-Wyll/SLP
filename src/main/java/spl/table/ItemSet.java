package spl.table;

import java.util.LinkedHashSet;
import java.util.Set;
import spl.grammar.NonTerminal;
import spl.grammar.Production;
import spl.grammar.SPLGrammar;
import spl.grammar.Symbol;

public final class ItemSet {
    private int id;
    private final Set<LR0Item> kernel;
    private final Set<LR0Item> items;

    public ItemSet(Set<LR0Item> kernel) {
        this.kernel = new LinkedHashSet<>(kernel);
        this.items = new LinkedHashSet<>(kernel);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<LR0Item> getKernel() {
        return kernel;
    }

    public Set<LR0Item> getItems() {
        return items;
    }

    public void closure(SPLGrammar g) {
        boolean changed = true;
        while (changed) {
            changed = false;
            Set<LR0Item> toAdd = new LinkedHashSet<>();
            for (LR0Item item : items) {
                Symbol next = item.nextSymbol();
                if (next != null && !next.isTerminal()) {
                    NonTerminal nt = (NonTerminal) next;
                    for (Production p : g.productionsFor(nt)) {
                        LR0Item newItem = new LR0Item(p, 0);
                        if (!items.contains(newItem)) toAdd.add(newItem);
                    }
                }
            }
            if (!toAdd.isEmpty()) {
                items.addAll(toAdd);
                changed = true;
            }
        }
    }

    public Set<LR0Item> gotoKernel(Symbol x) {
        Set<LR0Item> result = new LinkedHashSet<>();
        for (LR0Item item : items) {
            Symbol next = item.nextSymbol();
            if (next != null && next.equals(x)) {
                result.add(item.advance());
            }
        }
        return result;
    }

    public boolean equals(ItemSet o) {
        return this.kernel.equals(o.kernel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemSet)) return false;
        return kernel.equals(((ItemSet) o).kernel);
    }

    @Override
    public int hashCode() {
        return kernel.hashCode();
    }
}
