# SLP — SPL SLR(1) Parser

COS341 2026 Semester Practical: a lexer + SLR(1) parser for the Students'
Programming Language (SPL), implemented in Java per the architecture in
[`spl-slr-parser-class-diagram.mermaid`](spl-slr-parser-class-diagram.mermaid)
and the grammar in [`Prac-Spec-Syntax.pdf`](Prac-Spec-Syntax.pdf).

## Build

Requires JDK 17+. With Maven:

```bash
mvn package
```

Or compile directly:

```bash
javac -d target/classes $(find src/main/java -name "*.java")
```

## Run

```bash
java -cp target/classes spl.SPLCompiler path/to/SPL.txt tree.xml
```

An example program is provided at [`examples/SPL.txt`](examples/SPL.txt):

```bash
java -cp target/classes spl.SPLCompiler examples/SPL.txt tree.xml
```

- If the input is syntactically valid SPL, a structured `tree.xml` syntax
  tree is written (root/inner/leaf nodes with unique IDs, contents,
  children, and parent links).
- Otherwise, a meaningful syntax or lexical error message (with a hint) is
  printed to stderr and the process exits non-zero.

## Architecture

- **Lexical layer** (`spl.lexer`): `SourceReader`, `Lexer`, `Token`,
  `TokenType`, `LexicalError`. Every SPL token must be terminated by a
  blank space (ASCII 32/13) per the spec; the lexer enforces this and
  classifies NUM / USER-DEFINED-NAME / STRING via the spec's regular
  expressions.
- **Grammar layer** (`spl.grammar`): `Symbol` / `Terminal` / `NonTerminal`,
  `Production`, `SPLGrammar` (hard-codes the given CFG), `FirstFollowSets`
  (nullable/FIRST/FOLLOW computation).
- **Table construction** (`spl.table`): `LR0Item`, `ItemSet`,
  `CanonicalCollection` (LR(0) automaton), `Action`/`Shift`/`Reduce`/
  `Accept`/`ErrorEntry`, `ParseTable`, `SLRTableGenerator` (builds the
  SLR(1) action/goto tables, reducing on FOLLOW sets).
- **Parser driver** (`spl.parser`): `SLRParser` (shift-reduce driver over
  the generated table), `ParseStack`, `ErrorReporter`, `SyntaxError`.
- **Tree + XML output** (`spl.tree`): `SyntaxTree`, `RootNode`,
  `InnerNode`, `LeafNode`, `NodeIdGenerator`, `TreeVisitor`,
  `XmlTreeWriter` — writes `tree.xml` with unique node IDs, contents,
  children lists, and parent links as required by the spec.
- **Entry point**: `spl.SPLCompiler`.
