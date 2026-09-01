package spl.grammar;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spl.lexer.TokenType;

/**
 * Hard-codes the SPL context-free grammar as given in the Practical Syntax
 * Specification (Prof.G., COS341 2026).
 */
public final class SPLGrammar {
    private NonTerminal startSymbol;
    private final List<Production> productions = new ArrayList<>();
    private final Set<Terminal> terminals = new LinkedHashSet<>();
    private final Set<NonTerminal> nonTerminals = new LinkedHashSet<>();
    private final Map<String, NonTerminal> nonTerminalByName = new LinkedHashMap<>();
    private final Map<TokenType, Terminal> terminalByType = new LinkedHashMap<>();
    private int nextId = 0;

    public static final String AUGMENTED_START = "SPL_PROG'";

    private NonTerminal nt(String name) {
        return nonTerminalByName.computeIfAbsent(name, NonTerminal::new);
    }

    private Terminal t(TokenType type) {
        return terminalByType.computeIfAbsent(type, Terminal::new);
    }

    private void add(String lhsName, Symbol... rhs) {
        NonTerminal lhs = nt(lhsName);
        List<Symbol> list = new ArrayList<>();
        for (Symbol s : rhs) list.add(s);
        productions.add(new Production(nextId++, lhs, list));
    }

    public SPLGrammar build() {
        // Rule 0 is added by augment(); here we build the actual grammar rules.
        NonTerminal SPL_PROG = nt("SPL_PROG");
        NonTerminal P = nt("P");
        NonTerminal V_DECL = nt("V_DECL");
        NonTerminal F_DECL = nt("F_DECL");
        NonTerminal F_TYPE = nt("F_TYPE");
        NonTerminal ALGO = nt("ALGO");
        NonTerminal OUTP = nt("OUTP");
        NonTerminal INSTR = nt("INSTR");
        NonTerminal CALL = nt("CALL");
        NonTerminal INPUT = nt("INPUT");
        NonTerminal ASSIGN = nt("ASSIGN");
        NonTerminal TERM = nt("TERM");
        NonTerminal BRANCH = nt("BRANCH");
        NonTerminal BOOL = nt("BOOL");
        NonTerminal LOOP = nt("LOOP");
        NonTerminal COND = nt("COND");

        Terminal EOF = t(TokenType.EOF);
        Terminal USER_DEFINED_NAME = t(TokenType.USER_DEFINED_NAME);
        Terminal NUM = t(TokenType.NUM);
        Terminal STRING = t(TokenType.STRING);
        Terminal LPAREN = t(TokenType.LPAREN);
        Terminal RPAREN = t(TokenType.RPAREN);
        Terminal LBRACE = t(TokenType.LBRACE);
        Terminal RBRACE = t(TokenType.RBRACE);
        Terminal COLON = t(TokenType.COLON);
        Terminal SEMICOLON = t(TokenType.SEMICOLON);
        Terminal ASSIGN_OP = t(TokenType.ASSIGN_OP);
        Terminal KW_VOID = t(TokenType.KW_VOID);
        Terminal KW_NUM = t(TokenType.KW_NUM);
        Terminal KW_RETURN = t(TokenType.KW_RETURN);
        Terminal KW_PRINT = t(TokenType.KW_PRINT);
        Terminal KW_NOP = t(TokenType.KW_NOP);
        Terminal KW_COMMENT = t(TokenType.KW_COMMENT);
        Terminal KW_IF = t(TokenType.KW_IF);
        Terminal KW_THEN = t(TokenType.KW_THEN);
        Terminal KW_ELSE = t(TokenType.KW_ELSE);
        Terminal KW_WHILE = t(TokenType.KW_WHILE);
        Terminal KW_UNTIL = t(TokenType.KW_UNTIL);
        Terminal KW_DO = t(TokenType.KW_DO);
        Terminal KW_NOT = t(TokenType.KW_NOT);
        Terminal KW_AND = t(TokenType.KW_AND);
        Terminal KW_OR = t(TokenType.KW_OR);
        Terminal KW_EQ = t(TokenType.KW_EQ);
        Terminal KW_LARGER = t(TokenType.KW_LARGER);
        Terminal KW_LESSER = t(TokenType.KW_LESSER);
        Terminal KW_MOD = t(TokenType.KW_MOD);
        Terminal KW_ADD = t(TokenType.KW_ADD);
        Terminal KW_SUB = t(TokenType.KW_SUB);
        Terminal KW_MUL = t(TokenType.KW_MUL);
        Terminal KW_DIV = t(TokenType.KW_DIV);
        Terminal KW_NEG = t(TokenType.KW_NEG);

        // SPL_PROG -> P $   (Rule 0 from the spec)
        add("SPL_PROG", P, EOF);

        // P -> V_DECL : F_DECL : ALGO
        add("P", V_DECL, COLON, F_DECL, COLON, ALGO);

        // V_DECL -> epsilon | USER_DEFINED_NAME V_DECL
        add("V_DECL");
        add("V_DECL", USER_DEFINED_NAME, V_DECL);

        // F_DECL -> epsilon | F_TYPE F_DECL
        add("F_DECL");
        add("F_DECL", F_TYPE, F_DECL);

        // F_TYPE -> void NAME ( V_DECL ) { P return }
        add("F_TYPE", KW_VOID, USER_DEFINED_NAME, LPAREN, V_DECL, RPAREN, LBRACE, P, KW_RETURN, RBRACE);
        // F_TYPE -> num NAME ( V_DECL ) { P return ( TERM ) }
        add("F_TYPE", KW_NUM, USER_DEFINED_NAME, LPAREN, V_DECL, RPAREN, LBRACE, P, KW_RETURN, LPAREN, TERM, RPAREN, RBRACE);

        // ALGO -> epsilon | INSTR ; ALGO
        add("ALGO");
        add("ALGO", INSTR, SEMICOLON, ALGO);

        // OUTP -> ( TERM ) | STRING
        add("OUTP", LPAREN, TERM, RPAREN);
        add("OUTP", STRING);

        // INSTR -> print OUTP | nop | comment STRING | ASSIGN | BRANCH | LOOP | CALL
        add("INSTR", KW_PRINT, OUTP);
        add("INSTR", KW_NOP);
        add("INSTR", KW_COMMENT, STRING);
        add("INSTR", ASSIGN);
        add("INSTR", BRANCH);
        add("INSTR", LOOP);
        add("INSTR", CALL);

        // CALL -> NAME ( INPUT )
        add("CALL", USER_DEFINED_NAME, LPAREN, INPUT, RPAREN);

        // INPUT -> epsilon | TERM INPUT
        add("INPUT");
        add("INPUT", TERM, INPUT);

        // ASSIGN -> NAME = TERM
        add("ASSIGN", USER_DEFINED_NAME, ASSIGN_OP, TERM);

        // TERM -> NAME | NUM | CALL | mod(TERM TERM) | add(TERM TERM) | sub(TERM TERM)
        //       | mul(TERM TERM) | div(TERM TERM) | neg(TERM)
        add("TERM", USER_DEFINED_NAME);
        add("TERM", NUM);
        add("TERM", CALL);
        add("TERM", KW_MOD, LPAREN, TERM, TERM, RPAREN);
        add("TERM", KW_ADD, LPAREN, TERM, TERM, RPAREN);
        add("TERM", KW_SUB, LPAREN, TERM, TERM, RPAREN);
        add("TERM", KW_MUL, LPAREN, TERM, TERM, RPAREN);
        add("TERM", KW_DIV, LPAREN, TERM, TERM, RPAREN);
        add("TERM", KW_NEG, LPAREN, TERM, RPAREN);

        // BRANCH -> if BOOL then { ALGO } else { ALGO }
        add("BRANCH", KW_IF, BOOL, KW_THEN, LBRACE, ALGO, RBRACE, KW_ELSE, LBRACE, ALGO, RBRACE);

        // BOOL -> not(BOOL) | and(BOOL BOOL) | or(BOOL BOOL) | eq(TERM TERM) | larger(TERM TERM) | lesser(TERM TERM)
        add("BOOL", KW_NOT, LPAREN, BOOL, RPAREN);
        add("BOOL", KW_AND, LPAREN, BOOL, BOOL, RPAREN);
        add("BOOL", KW_OR, LPAREN, BOOL, BOOL, RPAREN);
        add("BOOL", KW_EQ, LPAREN, TERM, TERM, RPAREN);
        add("BOOL", KW_LARGER, LPAREN, TERM, TERM, RPAREN);
        add("BOOL", KW_LESSER, LPAREN, TERM, TERM, RPAREN);

        // LOOP -> COND BOOL do { ALGO } | do { ALGO } COND BOOL
        add("LOOP", COND, BOOL, KW_DO, LBRACE, ALGO, RBRACE);
        add("LOOP", KW_DO, LBRACE, ALGO, RBRACE, COND, BOOL);

        // COND -> while | until
        add("COND", KW_WHILE);
        add("COND", KW_UNTIL);

        this.startSymbol = SPL_PROG;
        this.nonTerminals.addAll(nonTerminalByName.values());
        this.terminals.addAll(terminalByType.values());
        return this;
    }

    public SPLGrammar augment() {
        NonTerminal augmented = nt(AUGMENTED_START);
        Production rule0 = new Production(-1, augmented, List.of(startSymbol));
        productions.add(0, rule0);
        // re-number all productions to keep IDs contiguous starting at 0
        List<Production> renumbered = new ArrayList<>();
        int id = 0;
        for (Production p : productions) {
            renumbered.add(new Production(id++, p.getLhs(), p.getRhs()));
        }
        productions.clear();
        productions.addAll(renumbered);
        nonTerminals.add(augmented);
        this.startSymbol = augmented;
        return this;
    }

    public NonTerminal getStartSymbol() {
        return startSymbol;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public Set<Terminal> getTerminals() {
        return terminals;
    }

    public Set<NonTerminal> getNonTerminals() {
        return nonTerminals;
    }

    public List<Production> productionsFor(NonTerminal a) {
        List<Production> result = new ArrayList<>();
        for (Production p : productions) {
            if (p.getLhs().equals(a)) result.add(p);
        }
        return result;
    }

    public Terminal terminal(TokenType type) {
        return terminalByType.get(type);
    }

    public NonTerminal nonTerminal(String name) {
        return nonTerminalByName.get(name);
    }
}
