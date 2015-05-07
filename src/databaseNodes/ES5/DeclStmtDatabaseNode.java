package databaseNodes.ES5;

import ast.ES.ESASTNode;
import ast.statements.IdentifierDeclStatement;
import databaseNodes.DatabaseNode;
import databaseNodes.NodeKeys;
import jdk.nashorn.internal.ir.VarNode;

import java.util.HashMap;
import java.util.Map;

public class DeclStmtDatabaseNode extends DatabaseNode
{
	@Override
	public void initialize(Object obj) {

	}

	@Override
	public Map<String, Object> createProperties()
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(NodeKeys.TYPE, "DeclStmt");
		return map;
	}

}
