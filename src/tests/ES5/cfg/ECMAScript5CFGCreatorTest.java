package tests.ES5.cfg;

import ast.ES.ESASTNode;
import cfg.CFG;
import cfg.ECMAScript5.ES5CFGFactory;
import tests.ES5.ASTUtil;
import tests.cfgCreation.CFGCreatorTest;

public class ECMAScript5CFGCreatorTest extends CFGCreatorTest
{
	@Override
	protected ESASTNode getASTForCode(String input) {
		return ASTUtil.getASTForCode(input);
	}

	@Override
	protected CFG getCFGForCode(String input)
	{
		return ES5CFGFactory.convert(getASTForCode(input).getNode());
	}

}
