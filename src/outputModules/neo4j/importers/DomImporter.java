package outputModules.neo4j.importers;

import neo4j.batchInserter.GraphNodeStore;
import neo4j.batchInserter.Neo4JBatchInserter;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.RelationshipType;

import cfg.nodes.ASTNodeContainer;
import cfg.nodes.CFGNode;
import databaseNodes.EdgeTypes;
import dom.DominatorTree;

public class DomImporter
{
	GraphNodeStore nodeStore;

	public DomImporter(GraphNodeStore nodeStore)
	{
		this.nodeStore = nodeStore;
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

	public void addDomToDatabase(DominatorTree<CFGNode> dominatorTree)
	{
		RelationshipType rel;

		// Add post dominator edges
		rel = DynamicRelationshipType.withName(EdgeTypes.DOM);
		for (CFGNode vertex : dominatorTree.getVertices())
		{
			CFGNode dominator = dominatorTree.getDominator(vertex);
			Neo4JBatchInserter.addRelationship(getId(dominator), getId(vertex),
					rel, null);
		}
	}

	public void addPostDomToDatabse(DominatorTree<CFGNode> postDominatorTree)
	{
		RelationshipType rel;

		// Add post dominator edges
		rel = DynamicRelationshipType.withName(EdgeTypes.POST_DOM);
		for (CFGNode vertex : postDominatorTree.getVertices())
		{
			if (postDominatorTree.hasDominator(vertex))
			{
				CFGNode dominator = postDominatorTree.getDominator(vertex);
				Neo4JBatchInserter.addRelationship(getId(dominator),
						getId(vertex), rel, null);
			}
		}
	}
}
