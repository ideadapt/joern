package outputModules.neo4j.ES5.importers;

import ast.ES.ESASTNode;
import ast.IASTNode;
import databaseNodes.ES5.DeclStmtDatabaseNode;
import databaseNodes.EdgeTypes;
import jdk.nashorn.internal.ir.VarNode;
import neo4j.batchInserter.GraphNodeStore;
import neo4j.batchInserter.Neo4JBatchInserter;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.RelationshipType;
import outputModules.neo4j.importers.ASTNodeImporter;

public class ES5DeclStmtImporter extends ASTNodeImporter
{
	public ES5DeclStmtImporter(GraphNodeStore nodeStore){
		this.nodeStore = nodeStore;
	}

	@Override
	public void addToDatabaseSafe(IASTNode stmtNode) {
		ESASTNode esNode = (ESASTNode)stmtNode;
		VarNode stmt = (VarNode)esNode.getNode();
		DeclStmtDatabaseNode dbStmtNode =  new DeclStmtDatabaseNode();
		dbStmtNode.initialize(stmt);
		addMainNode(dbStmtNode);

		ES5DeclImporter declImporter = new ES5DeclImporter(nodeStore);
		declImporter.addToDatabaseSafe(stmtNode);

		addLinkFromStmtToDecl(mainNodeId, declImporter.getMainNodeId());
	}

	private void addLinkFromStmtToDecl(Long mainNodeId, long declId) {
		RelationshipType rel = DynamicRelationshipType.withName(EdgeTypes.DECLARES);
		Neo4JBatchInserter.addRelationship(mainNodeId, declId, rel, null);
	}
}
