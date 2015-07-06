package tests.ES5;

import ast.ES.ESASTNode;
import ast.ES.ESASTVarNode;
import databaseNodes.DatabaseNode;
import databaseNodes.ES5.DeclDatabaseNode;
import databaseNodes.ES5.DeclStmtDatabaseNode;
import databaseNodes.FunctionDatabaseNode;
import neo4j.batchInserter.Neo4JBatchInserter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class ASTNodeTest
{

	@Before
	public void prepare(){
	}

	@Test
	public void testCodeLocation()
	{
		String code = "var x = 0;\nvar a = 1;";
		ESASTNode node = ASTUtil.getASTForCode(code);

		assertEquals(node.getChild(1).getClass(), ESASTVarNode.class);
		assertEquals(node.getChild(1).getLocationString(), "2:-1:15:20"); // line:linePosStart:programIdxStart:programIdxStop
	}
}
