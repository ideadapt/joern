package cdg;

import graphutils.IncidenceListGraph;

import java.util.Set;

import cfg.nodes.CFGNode;
import dom.DominatorTree;

public class CDG extends IncidenceListGraph<CFGNode, CDGEdge>
{

	private CDG()
	{
	}

	public static CDG newInstance(DominatorTree<CFGNode> dominatorTree)
	{
		CDG cdg = new CDG();
		for (CFGNode vertex : dominatorTree.getVertices())
		{
			Set<CFGNode> frontier = dominatorTree.dominanceFrontier(vertex);
			if (frontier != null)
			{
				cdg.addVertex(vertex);
				for (CFGNode f : frontier)
				{
					cdg.addVertex(f);
					cdg.addEdge(new CDGEdge(f, vertex));
				}
			}
		}
		return cdg;
	}

}
