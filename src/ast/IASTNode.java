package ast;

/**
 * Created by ideadapt on 02.05.15.
 */
public interface IASTNode {
    String getEscapedCodeStr();
    String getLocationString();
    ICodeLocation getLocation();
}
