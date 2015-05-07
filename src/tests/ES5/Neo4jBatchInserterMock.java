package tests.ES5;

import neo4j.batchInserter.Neo4JBatchInserter;
import org.neo4j.graphdb.RelationshipType;

import java.util.Map;

/**
 * Created by ideadapt on 07.05.15.
 */
public class Neo4jBatchInserterMock extends Neo4JBatchInserter {
    public static void addRelationship(long srcId, long dstId, RelationshipType rel, Map<String, Object> properties)
    {
        // this mocked method is never called
        System.out.println("add rel" + rel.name());
    }
}
