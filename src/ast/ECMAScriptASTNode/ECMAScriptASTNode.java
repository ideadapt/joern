package ast.ECMAScriptASTNode;

import ast.ASTNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.debug.PrintVisitor;

/**
 * Created by ideadapt on 20.04.15.
 */
public class ECMAScriptASTNode extends ASTNode {

    private final Node node;

    public ECMAScriptASTNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public String getEscapedCodeStr() {
        PrintVisitor v = new PrintVisitor(node, false, false);
        return v.toString().trim();
    }
}
