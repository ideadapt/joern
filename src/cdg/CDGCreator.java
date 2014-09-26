package cdg;

import graphutils.Edge;
import graphutils.IncidenceListGraph;
import cfg.CFG;
import cfg.nodes.CFGNode;
import dom.DominatorTree;

public class CDGCreator
{

	public CDG create(CFG cfg)
	{
		IncidenceListGraph<CFGNode, Edge<CFGNode>> reverseCfg = cfg.reverse();
		Edge<CFGNode> augment = new Edge<CFGNode>(cfg.getExitNode(),
				cfg.getEntryNode());
		reverseCfg.addEdge(augment);
		DominatorTree<CFGNode> postDominatorTree = DominatorTree
				.newDominatorTree(reverseCfg, cfg.getExitNode());

		CDG cdg = new CDG();
		for (CFGNode node : postDominatorTree.getVertices())
		{
			cdg.addVertex(node);
		}
		for (CFGNode node : postDominatorTree.getVertices())
		{
			if (reverseCfg.inDegree(node) > 1)
			{
				CFGNode runner;
				for (Edge<CFGNode> edge : reverseCfg.incomingEdges(node))
				{
					CFGNode predecessor = edge.getSource();
					if (cdg.contains(predecessor))
					{

						runner = predecessor;
						while (!runner.equals(postDominatorTree
								.getDominator(node)))
						{
							cdg.addEdge(node, runner);
							runner = postDominatorTree.getDominator(runner);
						}
					}
				}
			}
		}
		return cdg;
	}
}
