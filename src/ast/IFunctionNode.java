package ast;

/**
 * Created by ideadapt on 02.05.15.
 */
public interface IFunctionNode extends IASTNode {

    String getFunctionSignature();
    String getName();
    IASTNode getContent();
}
