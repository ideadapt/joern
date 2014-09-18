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
		DominatorTree<CFGNode> dominatorTree = DominatorTree.newInstance(
				reverseCfg, cfg.getExitNode());
		return CDG.newInstance(dominatorTree);
	}
}
