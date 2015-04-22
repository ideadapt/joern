package tests.cfgCreation.C;

import ast.ASTNode;
import parsing.C.Functions.ANTLRCFunctionParserDriver;
import parsing.ECMAScript5.ANTLRECMAScriptParserDriver;
import tests.cfgCreation.CFGCreatorTest;
import cfg.CFG;
import cfg.C.CCFGFactory;
import tests.parseTreeToAST.FunctionContentTestUtil;
import tools.index.SourceLanguage;

public class CCFGCreatorTest extends CFGCreatorTest
{
	@Override
	protected ASTNode getASTForCode(String input) {
		return FunctionContentTestUtil.parseAndWalk(input);
	}

	@Override
	protected CFG getCFGForCode(String input)
	{
		return CCFGFactory.convert(getASTForCode(input));
	}
}
