package outputModules.neo4j.importers;

import ast.ASTNode;
import ast.IASTNode;
import databaseNodes.DeclDatabaseNode;
import neo4j.batchInserter.GraphNodeStore;

public class DeclImporter extends ASTNodeImporter
{
	public DeclImporter(){
		nodeStore = new GraphNodeStore();
	}

	@Override
	public void addToDatabaseSafe(IASTNode node)
	{
		DeclDatabaseNode dbNode = new DeclDatabaseNode();
		dbNode.initialize(node);
		addMainNode(dbNode);
	}

}
