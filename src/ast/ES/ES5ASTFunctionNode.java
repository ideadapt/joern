package ast.ES;

import ast.ASTNode;
import ast.IASTNode;
import ast.IFunctionNode;
import jdk.nashorn.internal.ir.FunctionNode;

import java.util.stream.Collectors;

/**
 * Created by ideadapt on 06.06.15.
 */
public class ES5ASTFunctionNode extends ESASTNode implements IFunctionNode {

    FunctionNode fnNode = null;
    ESASTNode body = null;

    public ES5ASTFunctionNode(FunctionNode node) {
        super(node);
        fnNode = node;
    }

    @Override
    public String getFunctionSignature() {
        return fnNode.getName() + fnNode.getParameters().stream().map(p -> p.getName()).collect(Collectors.joining(","));
    }

    @Override
    public String getName() {
        return fnNode.getName();
    }

    @Override
    public IASTNode getContent() {
        if (body == null) {
            body = ESASTNode.create(fnNode.getBody());
        }
        return body;
    }

    @Override
    public int getChildCount() {
        return fnNode.getBody().getStatementCount();
    }

    @Override
    public ASTNode getChild(int i) {
		return super.getChild(i, () -> ESASTNode.create(fnNode.getBody().getStatements().get(i)));
    }
}
