package outputModules.neo4j.importers;

import ast.ASTNode;
import ast.IASTNode;
import databaseNodes.DeclDatabaseNode;

public class DeclImporter extends ASTNodeImporter
{

	@Override
	public void addToDatabaseSafe(IASTNode node)
	{
		DeclDatabaseNode dbNode = new DeclDatabaseNode();
		dbNode.initialize(node);
		addMainNode(dbNode);
	}

}
