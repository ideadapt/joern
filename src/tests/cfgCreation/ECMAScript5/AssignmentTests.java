package tests.cfgCreation.ECMAScript5;

import cfg.CFG;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AssignmentTests extends ECMAScript5CFGCreatorTest
{
	@Test
	public void testSingleAssignmentInDeclBlockNumber()
	{
		String input = "var a = 1;";
		CFG cfg = getCFGForCode(input);

		System.out.println(getASTForCode(input).getEscapedCodeStr());

		assertEquals(3, cfg.size());
	}

	@Test
	public void testAssignmentASTLink()
	{
		String input = "var x = 10;";
		CFG cfg = getCFGForCode(input);
		assertTrue(getNodeByCode(cfg, "var x = 10") != null);
	}
}
