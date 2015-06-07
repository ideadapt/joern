package outputModules.neo4j.ES5;

import ast.ES.ES5ASTFunctionNode;
import ast.ES.ESASTNode;
import ast.IASTNode;
import databaseNodes.EdgeTypes;
import databaseNodes.FileDatabaseNode;
import jdk.nashorn.internal.ir.*;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import neo4j.batchInserter.GraphNodeStore;
import neo4j.batchInserter.Neo4JBatchInserter;
import org.mockito.internal.util.collections.ArrayUtils;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.RelationshipType;
import outputModules.neo4j.ES5.importers.ES5DeclStmtImporter;
import outputModules.neo4j.importers.ASTNodeImporter;
import outputModules.neo4j.importers.FunctionImporter;
import tools.index.SourceLanguage;

import javax.xml.transform.Source;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
			List<Statement> varNodes = node.getStatements().stream().filter(s -> s instanceof VarNode).collect(Collectors.toList());
			for(Statement statement : varNodes){
				ASTNodeImporter importer = getImporter(ES5DeclStmtImporter.class);
				importNode(importer, statement);
				linkFileWithGlobalScopeDeclarations(importer);
			}
		}
		return true;
	}

	@Override
	public boolean enterFunctionNode(FunctionNode functionNode) {
		ASTNodeImporter importer = getImporter(FunctionImporter.class);
		((FunctionImporter)importer).setSourceLanguage(SourceLanguage.ECMAScript5);
		importNode(importer, ESASTNode.create(functionNode));
		return false;
	}

	private void linkFileWithGlobalScopeDeclarations(ASTNodeImporter importer) {
		RelationshipType rel = DynamicRelationshipType.withName(EdgeTypes.IS_FILE_OF);
		Neo4JBatchInserter.addRelationship(currentFileNode.getId(), importer.getMainNodeId(), rel, null);
	}

	@Override
	public boolean enterVarNode(VarNode varNode) {
		return super.enterVarNode(varNode);
	}

	private long importNode(ASTNodeImporter importer, IASTNode node){
		String code = node.getEscapedCodeStr();
		System.out.println("importing: " + code.substring(0, Math.min(code.length(), 42)));
		importer.setCurrentFile(currentFileNode);
		importer.addToDatabaseSafe(node);
		long mainNodeId = importer.getMainNodeId();
		return mainNodeId;
	}

	private long importNode(ASTNodeImporter importer, Node node)
	{
		ESASTNode esNode = ESASTNode.create(node);
		return importNode(importer, esNode);
	}

}
