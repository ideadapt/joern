package ast;

/**
 * Created by ideadapt on 02.05.15.
 */
public interface ICodeLocation {
    Integer getStartIndex();
    Integer getStartLine();
    Integer getStopIndex();
    Integer setStartIndex(Integer i);
    Integer setStartLine(Integer i);
    Integer setStopIndex(Integer i);
    String toString();
}
