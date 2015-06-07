package ast.ES;

import ast.ASTNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.Node;

/**
 * Created by ideadapt on 20.04.15.
 */
public class ESASTBlockNode extends ESASTNode {

    public ESASTBlockNode(Node node) {
        super(node);
    }

    @Override
    public int getChildCount() {
        return ((Block)node).getStatementCount();
    }

    @Override
    public ASTNode getChild(int i) {
        if(astChildren.containsKey(i)){
            return astChildren.get(i);
        }else{
            astChildren.put(i, ESASTNode.create(((Block) node).getStatements().get(i)));
        }
        return astChildren.get(i);
    }
}
