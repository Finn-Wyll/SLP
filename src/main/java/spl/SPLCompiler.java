package spl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import spl.grammar.FirstFollowSets;
import spl.grammar.SPLGrammar;
import spl.lexer.Lexer;
import spl.parser.ErrorReporter;
import spl.parser.ParseException;
import spl.parser.SLRParser;
import spl.table.CanonicalCollection;
import spl.table.ParseTable;
import spl.table.SLRTableGenerator;
import spl.tree.SyntaxTree;
import spl.tree.XmlTreeWriter;

/** Entry point: reads an SPL source file, parses it, and writes tree.xml. */
public final class SPLCompiler {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java -jar spl-slr-parser.jar <SPL.txt> [tree.xml]");
            System.exit(2);
        }
        String source = readSource(args[0]);
        String outputPath = args.length >= 2 ? args[1] : "tree.xml";
        int code = run(source, outputPath);
        System.exit(code);
    }

    private static String readSource(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.err.println("Could not read input file '" + path + "': " + e.getMessage());
            System.exit(2);
            return null;
        }
    }

    private static int run(String source, String outputPath) {
        ErrorReporter reporter = new ErrorReporter();

        SPLGrammar grammar = new SPLGrammar().build().augment();
        FirstFollowSets sets = new FirstFollowSets(grammar);

        CanonicalCollection collection = new CanonicalCollection(grammar);
        collection.build();

        SLRTableGenerator generator = new SLRTableGenerator(grammar, collection, sets);
        ParseTable table = generator.generate();
        if (table.hasConflicts()) {
            System.err.println("Warning: the SPL grammar produced " + table.getConflicts().size()
                    + " SLR(1) conflict(s); resolved by preferring shift over reduce.");
            for (var c : table.getConflicts()) {
                System.err.println("  " + c);
            }
        }

        Lexer lexer = new Lexer(source, reporter);
        SLRParser parser = new SLRParser(grammar, lexer, table, reporter);

        try {
            SyntaxTree tree = parser.parse();
            if (reporter.hadError()) {
                reporter.printAll();
                return 1;
            }
            new XmlTreeWriter().write(tree, outputPath);
            System.out.println("Parse successful. Syntax tree written to " + outputPath);
            return 0;
        } catch (ParseException e) {
            reporter.printAll();
            return 1;
        } catch (IOException e) {
            System.err.println("Failed to write output file: " + e.getMessage());
            return 1;
        }
    }
}
