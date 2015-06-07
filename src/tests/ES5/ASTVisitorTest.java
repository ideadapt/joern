package tests.ES5;

import ast.ES.ESASTNode;
import cfg.CFG;
import cfg.ECMAScript5.ES5CFGFactory;
import databaseNodes.DatabaseNode;
import databaseNodes.ES5.DeclDatabaseNode;
import databaseNodes.ES5.DeclStmtDatabaseNode;
import databaseNodes.FunctionDatabaseNode;
import neo4j.batchInserter.Neo4JBatchInserter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.neo4j.kernel.impl.nioneo.store.IdGenerator;
import org.neo4j.kernel.impl.nioneo.store.IdRange;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Neo4JBatchInserter.class)
public class ASTVisitorTest
{
	private NodeStoreMock nodeStore;
	private ASTNodeVisitorMock astVisitor;

	@Before
	public void prepare(){
		// TODO mocking static methods of Neo4JBatchInserter is not elegant
		// we'd better have a real object, thats adapted/delegated by GraphNodeStore
		// then we could inject a mock Neo4JBatchInserter instance.
		PowerMockito.mockStatic(Neo4JBatchInserter.class);
		PowerMockito.when(Neo4JBatchInserter.addNode(Mockito.anyMap())).thenAnswer(invocationOnMock -> System.currentTimeMillis());
		nodeStore = new NodeStoreMock();
		astVisitor = new ASTNodeVisitorMock(nodeStore);
	}

	@Test
	public void testVisitGlobalScopeVarDecl()
	{
		String code = "var a = 1;";
		ESASTNode node = ASTUtil.getASTForCode(code);
		node.accept(astVisitor);

		assertEquals(2, nodeStore.getNodes().size());
		assertEquals(DeclStmtDatabaseNode.class, nodeStore.getNode(0).getClass());
		assertEquals(DeclDatabaseNode.class, nodeStore.getNode(1).getClass());
	}

	@Test
	public void testVisitGlobalScopeVarMultiDecl()
	{
		String code = "var a = 1, b = 2;";
		ESASTNode node = ASTUtil.getASTForCode(code);
		node.accept(astVisitor);

		assertEquals(4, nodeStore.getNodes().size());
		assertEquals(DeclStmtDatabaseNode.class, nodeStore.getNode(0).getClass());
		assertEquals(DeclDatabaseNode.class, nodeStore.getNode(1).getClass());

		assertEquals(DeclStmtDatabaseNode.class, nodeStore.getNode(2).getClass());
		assertEquals(DeclDatabaseNode.class, nodeStore.getNode(3).getClass());
	}

	@Test
	public void testVisitGlobalScopeMultiVarDecl()
	{
		String code = "var a = 1; var b = 2;";
		ESASTNode node = ASTUtil.getASTForCode(code);
		node.accept(astVisitor);

		assertEquals(4, nodeStore.getNodes().size());
		assertEquals(DeclStmtDatabaseNode.class, nodeStore.getNode(0).getClass());
		assertEquals(DeclDatabaseNode.class, nodeStore.getNode(1).getClass());

		assertEquals(DeclStmtDatabaseNode.class, nodeStore.getNode(2).getClass());
		assertEquals(DeclDatabaseNode.class, nodeStore.getNode(3).getClass());
	}

	@Test
	public void testVisitFunctionSimple()
	{
		String code = "function(){" +
				"var a = 1;" +
				"}";
		ESASTNode node = ASTUtil.getASTForCode(code);
		System.out.println(ASTUtil.getASTString(code));
		node.accept(astVisitor);
		List<Object> dbNodes = nodeStore.getNodes().stream().filter(n -> n instanceof DatabaseNode).collect(Collectors.toList());

		assertEquals(3, dbNodes.size());
		assertEquals(DeclStmtDatabaseNode.class, dbNodes.get(0).getClass());
		assertEquals(DeclDatabaseNode.class, dbNodes.get(1).getClass());
		assertEquals(FunctionDatabaseNode.class, dbNodes.get(2).getClass());
	}

}
