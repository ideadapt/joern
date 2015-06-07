package ast.ES;

import ast.ASTNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.VarNode;

/**
 * Created by ideadapt on 20.04.15.
 */
public class ESASTVarNode extends ESASTNode {

    public ESASTVarNode(Node node) {
        super(node);
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public ASTNode getChild(int i) {
        return null;
    }
}
