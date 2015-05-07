package tests.ES5;

import ast.ES.ESASTNode;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.debug.ASTWriter;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.options.Options;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by ideadapt on 05.05.15.
 */
public class ASTUtil {
    public static ESASTNode getASTForCode(String input) {
        ScriptEnvironment e = new ScriptEnvironment(new Options(""), new PrintWriter(System.out , true), new PrintWriter(System.err , true));
        Parser p = new Parser(e, Source.sourceFor("testScript", input), new ErrorManager());
        FunctionNode fnNode = p.parse();
        return new ESASTNode(fnNode.getBody());
    }

    public static String getASTString(String input){
        ScriptEnvironment e = new ScriptEnvironment(new Options(""), new PrintWriter(System.out , true), new PrintWriter(System.err , true));
        Parser p = new Parser(e, Source.sourceFor("testScript", input), new ErrorManager());
        FunctionNode fnNode = p.parse();
        return new ASTWriter(fnNode).toString();
    }

    public static ESASTNode getASTForCode(File file) {
        ScriptEnvironment e = new ScriptEnvironment(new Options(""), new PrintWriter(System.out , true), new PrintWriter(System.err , true));
        Parser p = null;
        try {
            p = new Parser(e, Source.sourceFor("testScript", file), new ErrorManager());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        FunctionNode fnNode = p.parse();
        return new ESASTNode(fnNode.getBody());
    }
}
