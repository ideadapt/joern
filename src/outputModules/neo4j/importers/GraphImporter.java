package outputModules.neo4j.importers;

import graphutils.Edge;
import graphutils.Graph;
import graphutils.Vertex;
import neo4j.batchInserter.GraphNodeStore;
import neo4j.batchInserter.Neo4JBatchInserter;

import org.neo4j.graphdb.RelationshipType;

import cfg.nodes.ASTNodeContainer;

public class GraphImporter
{

	GraphNodeStore nodeStore;

	public GraphImporter(GraphNodeStore nodeStore)
	{
		this.nodeStore = nodeStore;
	}

	// public <V extends Vertex, E extends Edge<V>> void addVerticesToDatabase(
	// Graph<V, E> graph)
	// {
	// return;
	// }

	public <V extends Vertex, E extends Edge<V>> void addEdgeToDatabase(
			Graph<V, E> graph, RelationshipType edgeType)
	{
		for (V source : graph)
		{
			for (V destination : graph.out(source))
			{
				Neo4JBatchInserter.addRelationship(getId(source),
						getId(destination), edgeType, null);

			}
		}
	}

	// public <V extends Vertex, E extends Edge<V>> void addGraphToDatabase(
	// Graph<V, E> graph)
	// {
	// addVerticesToDatabase(graph);
	// addEdgeToDatabase(graph);
	// }

	private Long getId(Vertex node)
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
