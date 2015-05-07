package outputModules.neo4j.ES5;

import ast.ES.ESASTNode;
import databaseNodes.EdgeTypes;
import databaseNodes.FileDatabaseNode;
import jdk.nashorn.internal.ir.*;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import neo4j.batchInserter.GraphNodeStore;
import neo4j.batchInserter.Neo4JBatchInserter;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.RelationshipType;
import outputModules.neo4j.ES5.importers.ES5DeclStmtImporter;
import outputModules.neo4j.importers.ASTNodeImporter;

import java.util.stream.Collectors;

// Stays alive during the lifetime of the program

public class Neo4JASTNodeVisitor extends NodeVisitor
{
	private FileDatabaseNode currentFileNode;
	protected GraphNodeStore nodeStore;


	public Neo4JASTNodeVisitor() {
		this(new GraphNodeStore());
	}

	public Neo4JASTNodeVisitor(GraphNodeStore nodeStore){
		super(new LexicalContext());
		this.nodeStore = nodeStore;
	}

	public void startUnit(FileDatabaseNode fileNode){
		this.currentFileNode = fileNode;
	}

	protected ASTNodeImporter getImporter(Class<? extends ASTNodeImporter> importerClass){
		try {
			return (ASTNodeImporter) importerClass.getConstructors()[0].newInstance(new Object[]{this.nodeStore});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean enterBlock(Block node) {
		if(node.needsScope()){
			for(Statement statement : node.getStatements().stream().filter(s -> s instanceof VarNode).collect(Collectors.toList())){
				ASTNodeImporter importer = getImporter(ES5DeclStmtImporter.class);
				importNode(importer, statement);
				linkFileWithGlobalScopeDeclarations(importer);
			}
		}
		return true;
	}

	private void linkFileWithGlobalScopeDeclarations(ASTNodeImporter importer) {
		RelationshipType rel = DynamicRelationshipType.withName(EdgeTypes.IS_FILE_OF);
		Neo4JBatchInserter.addRelationship(currentFileNode.getId(), importer.getMainNodeId(), rel, null);
	}

	@Override
	public boolean enterVarNode(VarNode varNode) {
		return super.enterVarNode(varNode);
	}

	private long importNode(ASTNodeImporter importer, Node node)
	{
		ESASTNode esNode = new ESASTNode(node);
		String code = esNode.getEscapedCodeStr();
		System.out.println("importing: " + code.substring(0, Math.min(code.length(), 42)));
		importer.setCurrentFile(currentFileNode);
		importer.addToDatabaseSafe(esNode);
		long mainNodeId = importer.getMainNodeId();
		return mainNodeId;
	}

}
