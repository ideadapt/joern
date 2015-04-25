package cfg.ECMAScript5;

import ast.ASTNode;
import ast.ECMAScriptASTNode.ECMAScriptASTNode;
import ast.functionDef.FunctionDef;
import cfg.C.CCFG;
import cfg.CFGEdge;
import cfg.ECMAScript5.StructuredFlowVisitor;
import cfg.CFG;
import cfg.CFGFactory;
import cfg.nodes.ASTNodeContainer;
import cfg.nodes.CFGErrorNode;
import cfg.nodes.CFGNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Statement;

import java.util.List;

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

    public static ECMAScript5CFG newInstance(String h){
        System.out.printf("h" + h);
        return newErrorInstance();
    }

    public static CFG newInstance(IfNode node){
        ECMAScript5CFG cfg = new ECMAScript5CFG();

        CFGNode condition = new ASTNodeContainer(new ECMAScriptASTNode(node.getTest()));
        cfg.addVertex(condition);
        cfg.addEdge(cfg.getEntryNode(), condition);

        CFG ifBlock = convert(node.getPass().getStatements());
        cfg.mountCFG(condition, cfg.getExitNode(), ifBlock, CFGEdge.TRUE_LABEL);

        if(node.getFail() != null){

            if(node.getFail().getStatements().get(0) instanceof IfNode){
                CFG elseIfBlock = newInstance((IfNode)node.getFail().getStatements().get(0));
                cfg.mountCFG(condition, cfg.getExitNode(), elseIfBlock, CFGEdge.FALSE_LABEL);
            }else{
                CFG elseBlock = convert(node.getFail().getStatements());
                cfg.mountCFG(condition, cfg.getExitNode(), elseBlock, CFGEdge.FALSE_LABEL);
            }

        } else{
            cfg.addEdge(condition, cfg.getExitNode(), CFGEdge.FALSE_LABEL);
        }

        return cfg;
    }

    public static ECMAScript5CFG convert(List<Statement> nodes){
        return newInstance(nodes.toArray(new Node[nodes.size()]));
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
