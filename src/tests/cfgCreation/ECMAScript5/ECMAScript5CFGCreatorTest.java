package tests.cfgCreation.ECMAScript5;

import tests.cfgCreation.CFGCreatorTest;
import cfg.CFG;
import cfg.C.CCFGFactory;

public class ECMAScript5CFGCreatorTest extends CFGCreatorTest
{

	protected CFG getCFGForCode(String input)
	{
		return CCFGFactory.convert(getASTForCode(input));
	}

}
