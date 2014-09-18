package outputModules.neo4j.importers;

import neo4j.batchInserter.GraphNodeStore;
import neo4j.batchInserter.Neo4JBatchInserter;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.RelationshipType;

import cdg.CDG;
import cdg.CDGEdge;
import cfg.nodes.ASTNodeContainer;
import cfg.nodes.CFGNode;
import databaseNodes.EdgeTypes;

public class CDGImporter
{

	GraphNodeStore nodeStore;

	public CDGImporter(GraphNodeStore nodeStore)
	{
		this.nodeStore = nodeStore;
	}

	public void addCDGToDatabase(CDG cdg)
	{

		RelationshipType rel;

		rel = DynamicRelationshipType.withName(EdgeTypes.CONTROLS);
		for (CFGNode src : cdg.getVertices())
		{
			for (CDGEdge edge : cdg.outgoingEdges(src))
			{
				CFGNode dst = edge.getDestination();
				if (!src.equals(dst))
				{
					Neo4JBatchInserter.addRelationship(getId(src), getId(dst),
							rel, null);
				}
			}
		}
	}

	private long getId(CFGNode node)
	{
		if (node instanceof ASTNodeContainer)
		{
			return nodeStore.getIdForObject(((ASTNodeContainer) node)
					.getASTNode());
		}
		else
		{
			return nodeStore.getIdForObject(node);
		}
	}

}
