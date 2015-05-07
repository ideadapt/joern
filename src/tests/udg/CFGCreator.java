package tests.udg;

import ast.ASTNode;
import ast.ASTNodeBuilder;
import ast.functionDef.FunctionDef;
import ast.walking.ASTNodeVisitor;
import ast.walking.ASTWalker;
import cfg.ASTToCFGConverter;
import cfg.CFG;
import org.antlr.v4.runtime.ParserRuleContext;
import parsing.C.Modules.ANTLRCModuleParserDriver;
import parsing.FileParserInterface;
import tools.index.SourceLanguage;

import java.util.Stack;

public class CFGCreator
{

	private class NodeVisitor extends ASTNodeVisitor
	{
		private CFG cfg;
		private ASTToCFGConverter astToCFG = new ASTToCFGConverter(SourceLanguage.C);

		public void visit(FunctionDef node)
		{
			cfg = astToCFG.convert(node);
		}

		public CFG getResult()
		{
			return cfg;
		}
	}

	private class Walker extends ASTWalker
	{

		NodeVisitor visitor = new NodeVisitor();

		@Override
		public void startOfUnit(ParserRuleContext ctx, String filename)
		{
		}

		@Override
		public void endOfUnit(ParserRuleContext ctx, String filename)
		{
		}


		@Override
		public void processItem(ASTNode node, Stack<ASTNodeBuilder> nodeStack)
		{
			node.accept(visitor);
		}

		public CFG getResult()
		{
			return visitor.getResult();
		}

	}

	public CFG getCFGForCode(String code)
	{
		ANTLRCModuleParserDriver driver = new ANTLRCModuleParserDriver();
		FileParserInterface parser = new parsing.C.FileParser(driver);
		Walker walker = new Walker();
		parser.addObserver(walker);
		parser.parseString(code);

		return walker.getResult();
	}

}
