package ast.ES;

import ast.ASTNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.debug.PrintVisitor;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

/**
 * Created by ideadapt on 20.04.15.
 */
public class ESASTNode extends ASTNode {

    private final Node node;

    public ESASTNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public int getChildCount() {
        return ((Block)node).getStatementCount();
    }

    @Override
    public ASTNode getChild(int i) {
        return new ESASTNode(((Block)node).getStatements().get(i));
    }

    @Override
    public String getEscapedCodeStr() {
        PrintVisitor v = new PrintVisitor(node, false, false);
        return v.toString().trim();
    }

    public void accept(NodeVisitor visitor){
        node.accept(visitor);
    }
}
