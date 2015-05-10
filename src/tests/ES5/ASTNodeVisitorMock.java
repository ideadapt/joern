package tests.ES5;

import databaseNodes.FileDatabaseNode;
import outputModules.neo4j.ES5.Neo4JASTNodeVisitor;

/**
 * Created by ideadapt on 07.05.15.
 */
public class ASTNodeVisitorMock extends Neo4JASTNodeVisitor {

    public ASTNodeVisitorMock(NodeStoreMock nodeStoreMock) {
        super(nodeStoreMock);
        startUnit(new FileDatabaseNode());
    }
}
