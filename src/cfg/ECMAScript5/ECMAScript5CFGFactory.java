package cfg.ECMAScript5;

import ast.ASTNode;
import ast.ECMAScriptASTNode.ECMAScriptASTNode;
import ast.functionDef.FunctionDef;
import cfg.C.CCFG;
import cfg.ECMAScript5.StructuredFlowVisitor;
import cfg.CFG;
import cfg.CFGFactory;
import cfg.nodes.ASTNodeContainer;
import cfg.nodes.CFGErrorNode;
import cfg.nodes.CFGNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.Node;

/**
 * Created by ideadapt on 19.04.15.
 */
public class ECMAScript5CFGFactory extends CFGFactory {

    private static StructuredFlowVisitor structuredFlowVisitior = new StructuredFlowVisitor(new LexicalContext());

    @Override
    public CFG newInstance(FunctionDef functionDefinition) {
        return super.newInstance(functionDefinition);
    }

    public static ECMAScript5CFG convert(Node node)
    {
        ECMAScript5CFG cfg;
        if (node != null)
        {
            node.accept(structuredFlowVisitior);
            cfg = (ECMAScript5CFG) structuredFlowVisitior.getCFG();
        }
        else
        {
            cfg = newInstance();
        }
        return cfg;
    }

    public static ECMAScript5CFG newInstance(Node... nodes)
    {
        try
        {
            ECMAScript5CFG block = new ECMAScript5CFG();
            CFGNode last = block.getEntryNode();
            for (Node node : nodes)
            {
                ECMAScriptASTNode esASTNode = new ECMAScriptASTNode(node);
                CFGNode container = new ASTNodeContainer(esASTNode);
                block.addVertex(container);
                block.addEdge(last, container);
                last = container;
            }
            block.addEdge(last, block.getExitNode());
            return block;
        }
        catch (Exception e)
        {
            // e.printStackTrace();
            return newErrorInstance();
        }
    }

    public static ECMAScript5CFG newErrorInstance()
    {
        ECMAScript5CFG errorBlock = new ECMAScript5CFG();
        CFGNode errorNode = new CFGErrorNode();
        errorBlock.addVertex(errorNode);
        errorBlock.addEdge(errorBlock.getEntryNode(), errorNode);
        errorBlock.addEdge(errorNode, errorBlock.getExitNode());
        return errorBlock;
    }
}
