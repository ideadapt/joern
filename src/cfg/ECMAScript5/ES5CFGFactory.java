package cfg.ECMAScript5;

import ast.ES.ES5ASTFunctionNode;
import ast.ES.ESASTNode;
import ast.IFunctionNode;
import cfg.CFGEdge;
import cfg.CFG;
import cfg.CFGFactory;
import cfg.nodes.ASTNodeContainer;
import cfg.nodes.CFGErrorNode;
import cfg.nodes.CFGNode;
import jdk.nashorn.internal.ir.*;

import java.util.List;

/**
 * Created by ideadapt on 19.04.15.
 */
public class ES5CFGFactory extends CFGFactory {

    private static StructuredFlowVisitor structuredFlowVisitior = new StructuredFlowVisitor(new LexicalContext());

    @Override
    public CFG newInstance(IFunctionNode functionDefinition) {
        ES5ASTFunctionNode fnNode = (ES5ASTFunctionNode) functionDefinition;
        ECMAScript5CFG cfg = newInstance();

        List<IdentNode> params = ((FunctionNode)fnNode.getNode()).getParameters();

        ECMAScript5CFG cfgParameters = newInstance(params.toArray(new IdentNode[params.size()]));
        // FIXME, memoized children get lost here.
        // => we could build a real AST containing ESASTNodes (which wont be adapters any more)
        //    but overriding hashCode and equals is also working :)
        ECMAScript5CFG cfgBody = convert(((ESASTNode)functionDefinition.getContent()).getNode());

        cfgParameters.appendCFG(cfgBody);
        cfg.appendCFG(cfgParameters);

        return cfg;
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

    public static CFG newInstance(IfNode node){
        ECMAScript5CFG cfg = new ECMAScript5CFG();

        CFGNode condition = new ASTNodeContainer(ESASTNode.create(node.getTest()));
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

    public static CFG newInstance(Block block){
        try
        {
            CFG compoundBlock = newInstance();
            for (Statement statement : block.getStatements())
            {
                compoundBlock.appendCFG(convert(statement));
            }
            return compoundBlock;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return newErrorInstance();
        }
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
                // FIXME, should use same ESASTNode instance as in AST and not create new adapter.
                // => now we overwrite hashCode and equals of ESASTNode to trick nodeStores hashMap.
                ESASTNode esASTNode = ESASTNode.create(node);
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
