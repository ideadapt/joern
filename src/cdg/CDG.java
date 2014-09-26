package cdg;

import graphutils.IncidenceListGraph;
import cfg.nodes.CFGNode;

public class CDG extends IncidenceListGraph<CFGNode, CDGEdge>
{

	CDG()
	{
	}

	void addEdge(CFGNode source, CFGNode destination)
	{
		addEdge(new CDGEdge(source, destination));
	}

}
