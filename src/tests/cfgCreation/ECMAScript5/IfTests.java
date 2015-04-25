package tests.cfgCreation.ECMAScript5;

import cfg.CFG;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IfTests extends ECMAScript5CFGCreatorTest
{
	@Test
	public void testIf()
	{
		String input = "if(a) { log(a); }";
		CFG cfg = getCFGForCode(input);

		System.out.println(getASTForCode(input).getEscapedCodeStr());
		System.out.println(cfg.toString());

		assertEquals(4, cfg.size());
	}

	@Test
	public void testIfElse()
	{
		String input = "if(a) { log(a); } else { log(b); }";
		CFG cfg = getCFGForCode(input);

		System.out.println(getASTForCode(input).getEscapedCodeStr());
		System.out.println(cfg.toString());

		assertEquals(5, cfg.size());
	}

	@Test
	public void testIfElseIf()
	{
		String input = "if(a) { log(a); } else if (b) { log(b); }";
		CFG cfg = getCFGForCode(input);

		System.out.println(getASTForCode(input).getEscapedCodeStr());
		System.out.println(cfg.toString());

		assertEquals(6, cfg.size());
	}

	@Test
	public void testIfElseIfElse()
	{
		String input = "if(a) { log(a); } else if (b) { log(b); } else { log(c); }";
		CFG cfg = getCFGForCode(input);

		System.out.println(getASTForCode(input).getEscapedCodeStr());
		System.out.println(cfg.toString());

		assertEquals(7, cfg.size());
	}

	@Test
	public void testIfElseNestedIf()
	{
		String input = "if(a) { log(a); } else { if (b) { log(b); } }";
		CFG cfg = getCFGForCode(input);

		System.out.println(getASTForCode(input).getEscapedCodeStr());
		System.out.println(cfg.toString());

		assertEquals(6, cfg.size());
	}

}
