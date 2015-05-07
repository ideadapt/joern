package databaseNodes.ES5;

import ast.declarations.IdentifierDecl;
import databaseNodes.DatabaseNode;
import databaseNodes.NodeKeys;
import jdk.nashorn.internal.ir.IdentNode;

import java.util.HashMap;
import java.util.Map;

public class DeclDatabaseNode extends DatabaseNode
{
	IdentNode decl;

	@Override
	public void initialize(Object obj)
	{
		decl = (IdentNode) obj;
	}

	@Override
	public Map<String, Object> createProperties()
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(NodeKeys.TYPE, "Decl");
		map.put(NodeKeys.IDENTIFIER, decl.getName());
		return map;
	}

}
