package spl.parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import spl.tree.SyntaxTreeNode;

public final class ParseStack {
    private final Deque<Integer> states = new ArrayDeque<>();
    private final Deque<SyntaxTreeNode> nodes = new ArrayDeque<>();

    public void push(int state, SyntaxTreeNode n) {
        states.push(state);
        nodes.push(n);
    }

    public List<SyntaxTreeNode> popMany(int k) {
        List<SyntaxTreeNode> popped = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            states.pop();
            popped.add(nodes.pop());
        }
        Collections.reverse(popped);
        return popped;
    }

    public int topState() {
        return states.peek();
    }

    public boolean isEmpty() {
        return states.isEmpty();
    }
}
