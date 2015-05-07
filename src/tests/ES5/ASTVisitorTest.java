package tests.ES5;

import ast.ES.ESASTNode;
import databaseNodes.ES5.DeclDatabaseNode;
import databaseNodes.ES5.DeclStmtDatabaseNode;
import neo4j.batchInserter.Neo4JBatchInserter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Neo4JBatchInserter.class)
public class ASTVisitorTest
{
	private MockNodeStore nodeStore;
	private MockASTNodeVisitor astVisitor;

	@Before
	public void prepare(){
		// TODO mocking static methods of Neo4JBatchInserter is not elegant
		// we'd better have a real object, thats adapted/delegated by GraphNodeStore
		// then we could inject a mock Neo4JBatchInserter instance.
		PowerMockito.mockStatic(Neo4JBatchInserter.class);
		nodeStore = new MockNodeStore();
		astVisitor = new MockASTNodeVisitor(nodeStore);
	}

	@Test
	public void testVisitGlobalScopeVarDecl()
	{
		String code = "var a = 1;";
		ESASTNode node = ASTUtil.getASTForCode(code);
		node.accept(astVisitor);

		assertEquals(nodeStore.getNodes().size(), 2);
		assertEquals(nodeStore.getNode(0).getClass(), DeclStmtDatabaseNode.class);
		assertEquals(nodeStore.getNode(1).getClass(), DeclDatabaseNode.class);
	}

}
