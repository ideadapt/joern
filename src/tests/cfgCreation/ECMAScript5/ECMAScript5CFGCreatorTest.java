package tests.cfgCreation.ECMAScript5;

import ast.ECMAScriptASTNode.ECMAScriptASTNode;
import cfg.CFG;
import cfg.ECMAScript5.ECMAScript5CFGFactory;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.options.Options;
import tests.cfgCreation.CFGCreatorTest;
import tests.parseTreeToAST.FunctionContentTestUtil;
import tools.index.SourceLanguage;

import java.io.PrintWriter;

public class ECMAScript5CFGCreatorTest extends CFGCreatorTest
{
	@Override
	protected ECMAScriptASTNode getASTForCode(String input) {
		ScriptEnvironment e = new ScriptEnvironment(new Options(""), new PrintWriter(System.out , true), new PrintWriter(System.err , true));
		Parser p = new Parser(e, Source.sourceFor("testScript", input), new ErrorManager());
		FunctionNode fnNode = p.parse();
		return new ECMAScriptASTNode(fnNode.getBody());
	}

	@Override
	protected CFG getCFGForCode(String input)
	{
		return ECMAScript5CFGFactory.convert(getASTForCode(input).getNode());
	}

}
