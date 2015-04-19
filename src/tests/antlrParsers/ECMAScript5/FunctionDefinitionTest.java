package tests.antlrParsers.ECMAScript5;

import antlr.ECMAScript5.ECMAScriptLexer;
import antlr.ECMAScript5.ECMAScriptParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ideadapt on 19.04.15.
 */
public class FunctionDefinitionTest {

    protected ECMAScriptParser createParser(String input)
    {
        ANTLRInputStream inputStream = new ANTLRInputStream(input);
        ECMAScriptLexer lex = new ECMAScriptLexer(inputStream);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        ECMAScriptParser parser = new ECMAScriptParser(tokens);
        return parser;
    }

    @Test
    public void namedDefinition()
    {
        String input = "function a(){}";
        String expected = "(functionDeclaration function a ( ) { functionBody })";

        ECMAScriptParser parser = createParser(input);
        String output = parser.functionDeclaration().toStringTree(parser);

        assertEquals(expected, output);
    }
}
