package tests.ES5.cfg;

import cfg.CFG;
import org.junit.Test;
import tests.ES5.ASTUtil;

import static org.junit.Assert.assertEquals;

public class FunctionTests extends ECMAScript5CFGCreatorTest
{
	@Test
	public void testSimpleFunction()
	{
		String input = "function a() { var a = 1; };";
		CFG cfg = getCFGForCode(input);

		System.out.println(getASTForCode(input).getEscapedCodeStr());
		System.out.println(cfg.toString());

		System.out.print(ASTUtil.getASTString(input));

		assertEquals(3, cfg.size()); // entry, statement, exit
	}

}
