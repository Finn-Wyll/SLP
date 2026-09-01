package spl.tree;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class XmlTreeWriter implements TreeVisitor {
    private Writer out;

    public void write(SyntaxTree t, String path) throws IOException {
        try (Writer writer = Files.newBufferedWriter(Path.of(path), StandardCharsets.UTF_8)) {
            this.out = writer;
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            out.write("<syntaxtree>\n");
            t.traverse(this);
            out.write("</syntaxtree>\n");
        }
    }

    @Override
    public void visitRoot(RootNode n) {
        writeQuiet("  <root id=\"" + n.getId() + "\">\n");
        writeQuiet("    <contents>" + escape(n.contents()) + "</contents>\n");
        writeChildren(n.getChildIds());
        writeQuiet("  </root>\n");
    }

    @Override
    public void visitInner(InnerNode n) {
        writeQuiet("  <inner id=\"" + n.getId() + "\">\n");
        writeQuiet("    <contents>" + escape(n.contents()) + "</contents>\n");
        writeChildren(n.getChildIds());
        writeQuiet("    <parent>" + n.getParentId() + "</parent>\n");
        writeQuiet("  </inner>\n");
    }

    @Override
    public void visitLeaf(LeafNode n) {
        writeQuiet("  <leaf id=\"" + n.getId() + "\">\n");
        writeQuiet("    <contents>" + escape(n.contents()) + "</contents>\n");
        writeQuiet("    <parent>" + n.getParentId() + "</parent>\n");
        writeQuiet("  </leaf>\n");
    }

    private void writeChildren(java.util.List<Integer> childIds) {
        writeQuiet("    <children>\n");
        for (int c : childIds) {
            writeQuiet("      <child>" + c + "</child>\n");
        }
        writeQuiet("    </children>\n");
    }

    private void writeQuiet(String s) {
        try {
            out.write(s);
        } catch (IOException e) {
            throw new RuntimeException("Failed writing tree.xml", e);
        }
    }

    private String escape(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
