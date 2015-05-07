package outputModules.neo4j.ES5.importers;

import ast.ES.ESASTNode;
import ast.IASTNode;
import databaseNodes.ES5.DeclDatabaseNode;
import jdk.nashorn.internal.ir.VarNode;
import neo4j.batchInserter.GraphNodeStore;
import outputModules.neo4j.importers.ASTNodeImporter;

public class ES5DeclImporter extends ASTNodeImporter
{
	public ES5DeclImporter(GraphNodeStore nodeStore){
		this.nodeStore = nodeStore;
	}

	@Override
	public void addToDatabaseSafe(IASTNode node)
	{
		ESASTNode esNode = (ESASTNode)node;
		VarNode stmt = (VarNode)esNode.getNode();

		DeclDatabaseNode dbNode = new DeclDatabaseNode();
		dbNode.initialize(stmt.getName());
		addMainNode(dbNode);
	}

}
