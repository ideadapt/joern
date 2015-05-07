package outputModules.neo4j.importers;

import ast.IASTNode;
import neo4j.batchInserter.Neo4JBatchInserter;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.RelationshipType;

import ast.ASTNode;
import ast.functionDef.FunctionDef;
import ast.statements.CompoundStatement;
import cfg.CFG;
import cfg.nodes.ASTNodeContainer;
import cfg.nodes.CFGNode;
import databaseNodes.EdgeTypes;
import databaseNodes.FileDatabaseNode;
import databaseNodes.FunctionDatabaseNode;
import tools.index.SourceLanguage;

// Stays alive while importing a function into
// the database

public class FunctionImporter extends ASTNodeImporter
{
	private final UDGImporter udgImporter;
	private final DDGImporter ddgImporter;
	private final CDGImporter cdgImporter;
	private final ASTImporter astImporter;
	private final CFGImporter cfgImporter;
	private final SourceLanguage sourceLanguage;

	public FunctionImporter(SourceLanguage sourceLanguage) {
		this.sourceLanguage = sourceLanguage;
		astImporter = new ASTImporter(nodeStore);
		cfgImporter = new CFGImporter(nodeStore);
		udgImporter = new UDGImporter(nodeStore);
		ddgImporter = new DDGImporter(nodeStore);
		cdgImporter = new CDGImporter(nodeStore);
	}

	public void addToDatabaseSafe(IASTNode node)
	{
		try
		{
			FunctionDatabaseNode function = new FunctionDatabaseNode(sourceLanguage);
			// this actually constructs all other representations of
			// the function.
			function.initialize(node);
			addFunctionToDatabase(function);
			linkFunctionToFileNode(function, curFile);
		}
		catch (RuntimeException ex)
		{
			ex.printStackTrace();
			String str = node.getEscapedCodeStr();
			System.err.println("Error adding function to database: " + (str.substring(0, Math.min(30, str.length()))));
			return;
		}
	}

	private void addFunctionToDatabase(FunctionDatabaseNode function)
	{

		addMainNode(function);

		astImporter.setCurrentFunction(function);
		cfgImporter.setCurrentFunction(function);
		udgImporter.setCurrentFunction(function);

		astImporter.addASTToDatabase((ASTNode) function.getASTRoot());
		cfgImporter.addCFGToDatabase(function.getCFG());
		udgImporter.addUDGToDatabase(function.getUDG());
		ddgImporter.addDDGToDatabase(function.getDDG());
		cdgImporter.addCDGToDatabase(function.getCDG());

		linkFunctionToASTAndCFG(function);

	}

	private void linkFunctionToASTAndCFG(FunctionDatabaseNode function)
	{

		linkFunctionWithAST(function);

		CFG cfg = function.getCFG();
		if (cfg != null)
		{
			linkFunctionWithCFG(function, cfg);
		}
	}

	private void linkFunctionWithAST(FunctionDatabaseNode function)
	{
		RelationshipType rel = DynamicRelationshipType
				.withName(EdgeTypes.IS_FUNCTION_OF_AST);

		long functionId = nodeStore.getIdForObject(function);
		long astNodeId = nodeStore.getIdForObject(function.getASTRoot());

		Neo4JBatchInserter.addRelationship(functionId, astNodeId, rel, null);

	}

	private void linkFunctionWithCFG(FunctionDatabaseNode function, CFG cfg)
	{
		RelationshipType rel = DynamicRelationshipType
				.withName(EdgeTypes.IS_FUNCTION_OF_CFG);
		long functionId = nodeStore.getIdForObject(function);

		CFGNode firstBlock = cfg.getEntryNode();

		long cfgRootId;
		try
		{
			cfgRootId = nodeStore.getIdForObject(firstBlock);
		}
		catch (RuntimeException ex)
		{
			cfgRootId = nodeStore
					.getIdForObject(((ASTNodeContainer) firstBlock)
							.getASTNode());
		}

		Neo4JBatchInserter.addRelationship(functionId, cfgRootId, rel, null);

	}

	private void linkFunctionToFileNode(FunctionDatabaseNode function,
			FileDatabaseNode fileNode)
	{
		RelationshipType rel = DynamicRelationshipType
				.withName(EdgeTypes.IS_FILE_OF);

		long fileId = fileNode.getId();
		long functionId = nodeStore.getIdForObject(function);

		Neo4JBatchInserter.addRelationship(fileId, functionId, rel, null);
	}

}
