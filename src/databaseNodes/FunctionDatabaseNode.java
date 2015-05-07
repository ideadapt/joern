package databaseNodes;

import java.util.HashMap;
import java.util.Map;

import ast.*;
import tools.index.SourceLanguage;
import udg.CFGToUDGConverter;
import udg.useDefGraph.UseDefGraph;
import cdg.CDG;
import cdg.CDGCreator;
import cfg.ASTToCFGConverter;
import cfg.CFG;
import ddg.CFGAndUDGToDefUseCFG;
import ddg.DDGCreator;
import ddg.DataDependenceGraph.DDG;
import ddg.DefUseCFG.DefUseCFG;

// Note: we currently use the FunctionDatabaseNode
// as a container for the Function. That's not very
// clean. We should have a sep. Function-Class.
public class FunctionDatabaseNode extends DatabaseNode
{
	IFunctionNode astRoot;
	CFG cfg;
	UseDefGraph udg;
	DDG ddg;
	CDG cdg;

	String signature;
	
	ASTToCFGConverter astToCFG;
	CFGToUDGConverter cfgToUDG;
	CFGAndUDGToDefUseCFG udgAndCfgToDefUseCFG;
	DDGCreator ddgCreator;
	CDGCreator cdgCreator;

	public FunctionDatabaseNode(SourceLanguage sourceLanguage) {
		astToCFG = new ASTToCFGConverter(sourceLanguage);
		cfgToUDG = new CFGToUDGConverter();
		udgAndCfgToDefUseCFG = new CFGAndUDGToDefUseCFG();
		ddgCreator = new DDGCreator();
		cdgCreator = new CDGCreator();
	}

	@Override
	public void initialize(Object node)
	{
		astRoot = (IFunctionNode) node;
		cfg = astToCFG.convert(astRoot);
		udg = cfgToUDG.convert(cfg);
		DefUseCFG defUseCFG = udgAndCfgToDefUseCFG.convert(cfg, udg);
		ddg = ddgCreator.createForDefUseCFG(defUseCFG);
		cdg = cdgCreator.create(cfg);

		setSignature(astRoot);
	}

	@Override
	public Map<String, Object> createProperties()
	{
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(NodeKeys.TYPE, "Function");
		properties.put(NodeKeys.LOCATION, this.getLocation());
		properties.put(NodeKeys.NAME, this.getName());
		return properties;
	}

	public String getName()
	{
		return astRoot.getName();
	}

	public IASTNode getASTRoot()
	{
		return astRoot;
	}

	public CFG getCFG()
	{
		return cfg;
	}

	public UseDefGraph getUDG()
	{
		return udg;
	}

	public DDG getDDG()
	{
		return ddg;
	}

	public CDG getCDG()
	{
		return cdg;
	}

	public String getLocation()
	{
		return astRoot.getLocationString();
	}
	
	public ICodeLocation getContentLocation(){
		return astRoot.getContent().getLocation();
	}
	
	public String getSignature()
	{
		return signature;
	}

	private void setSignature(IFunctionNode node)
	{
		signature = node.getFunctionSignature();
	}
	
}
