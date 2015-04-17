package parsing.ECMAScript5;

import antlr.C.ModuleLexer;
import antlr.C.ModuleParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.ParseTree;
import parsing.ANTLRParserDriver;
import parsing.TokenSubStream;

public class ANTLRECMAScriptParserDriver extends ANTLRParserDriver
{

	public ANTLRECMAScriptParserDriver()
	{
		super();
		setListener(new ECMAScriptBaseListener(this));
	}

	@Override
	public ParseTree parseTokenStreamImpl(TokenSubStream tokens)
	{
		ModuleParser parser = new ModuleParser(tokens);
		ParseTree tree = null;

		try
		{
			setSLLMode(parser);
			tree = parser.code();
		}
		catch (RuntimeException ex)
		{
			if (isRecognitionException(ex))
			{
				tokens.reset();
				setLLStarMode(parser);
				tree = parser.code();
			}
		}
		return tree;
	}

	@Override
	public Lexer createLexer(ANTLRInputStream input)
	{
		return new ModuleLexer(input);
	}

}
