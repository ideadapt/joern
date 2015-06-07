package ast.ES;

import ast.ASTNode;
import jdk.nashorn.internal.ir.*;
import jdk.nashorn.internal.ir.debug.PrintVisitor;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

import java.util.HashMap;

public class ESASTNode extends ASTNode {

    protected final Node node;
    protected HashMap<Integer, ESASTNode> astChildren = new HashMap<>();

    public ESASTNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ASTNode getChild(int i) {
        throw new UnsupportedOperationException();
    }

    protected ESASTNode getChild(int i, CallableSave<ESASTNode> defaultIfNotExists) {
        if(!astChildren.containsKey(i)){
            astChildren.put(i, defaultIfNotExists.call());
        }
        return astChildren.get(i);
    }

    @Override
    public String getEscapedCodeStr() {
        PrintVisitor v = new PrintVisitor(node, false, false);
        return v.toString().trim();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "-" + this.hashCode();
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ESASTNode)){
            return false;
        }

        ESASTNode other = (ESASTNode) o;
        return other.node.equals(this.node);
    }

    public void accept(NodeVisitor visitor){
        node.accept(visitor);
    }

    public static ESASTNode create(Node node) {
        if(node instanceof Block){
            return new ESASTBlockNode(node);
        } else if (node instanceof VarNode){
            return new ESASTVarNode(node);
        }else if (node instanceof FunctionNode){
            return new ES5ASTFunctionNode((FunctionNode)node);
        }else if (node instanceof Expression){ // expand as soon as subtypes required
            return new ESASTExpressionNode(node);
        }
        throw new IllegalArgumentException("unsupported nashorn node type: "+node.getClass().getSimpleName());
    }
}
