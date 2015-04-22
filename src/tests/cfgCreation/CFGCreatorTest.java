package tests.cfgCreation;

import ast.ASTNode;
import cfg.C.CCFGFactory;
import cfg.CFG;
import cfg.nodes.CFGNode;
import parsing.ECMAScript5.ANTLRECMAScriptParserDriver;
import tests.parseTreeToAST.FunctionContentTestUtil;

public abstract class CFGCreatorTest
{

	protected abstract ASTNode getASTForCode(String input);

	protected abstract CFG getCFGForCode(String input);

	protected CFGNode getNodeByCode(CFG cfg, String code)
	{
		code = "[" + code + "]";
		for (CFGNode node : cfg.getVertices())
		{
			if (node.toString().equals(code))
			{
				return node;
			}
		}
		return null;
	}

	protected boolean contains(CFG cfg, String code)
	{
		return getNodeByCode(cfg, code) != null;
	}

	protected boolean isConnected(CFG cfg, String srcCode, String dstCode)
	{
		return cfg.isConnected(getNodeByCode(cfg, srcCode),
				getNodeByCode(cfg, dstCode));
	}
}
