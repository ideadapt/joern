package tests.cfgCreation.ECMAScript5;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cfg.CFG;
import tests.cfgCreation.C.CCFGCreatorTest;

public class AssignmentTests extends CCFGCreatorTest
{
	@Test
	public void testSingleAssignmentBlockNumber()
	{
		String input = "x = y;";
		CFG cfg = getCFGForCode(input);
		assertTrue(cfg.size() == 3);
	}

	@Test
	public void testAssignmentASTLink()
	{
		String input = "x = 10;";
		CFG cfg = getCFGForCode(input);
		assertTrue(getNodeByCode(cfg, "x = 10") != null);
	}

	@Test
	public void testAssignmentInDecl()
	{
		String input = "int x = 10;";
		CFG cfg = getCFGForCode(input);
		assertTrue(cfg.size() == 3);
	}

}
