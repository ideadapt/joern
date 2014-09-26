package dom;

import graphutils.Edge;
import graphutils.IncidenceListGraph;
import graphutils.PostorderIterator;

import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import cfg.CFG;
import cfg.CFGEdge;
import cfg.nodes.CFGNode;

public class DominatorTree<V>
{

	private HashMap<V, V> dominators;
	private HashMap<V, Integer> postorderEnumeration;

	DominatorTree()
	{
		dominators = new HashMap<V, V>();
		postorderEnumeration = new HashMap<V, Integer>();
	}

	public static <V> DominatorTree<V> newDominatorTree(
			IncidenceListGraph<V, Edge<V>> graph, V root)
	{
		return new DominatorTreeCreator<V, Edge<V>>(graph, root).create();
	}

	public static DominatorTree<CFGNode> newDominatorTree(CFG cfg)
	{
		return new DominatorTreeCreator<CFGNode, CFGEdge>(cfg,
				cfg.getEntryNode()).create();
	}

	public static DominatorTree<CFGNode> newPostDominatorTree(CFG cfg)
	{
		return new DominatorTreeCreator<CFGNode, Edge<CFGNode>>(cfg.reverse(),
				cfg.getExitNode()).create();
	}

	public Collection<V> getVertices()
	{
		return dominators.keySet();
	}

	public V getDominator(V vertex)
	{
		return dominators.get(vertex);
	}

	public int postorderNumber(V vertex)
	{
		return postorderEnumeration.get(vertex);
	}

	// public Set<V> dominanceFrontier(V vertex)
	// {
	// return dominanceFrontiers.get(vertex);
	// }

	private V commonDominator(List<V> vertices)
	{
		Deque<V> stack = new LinkedList<V>();
		for (V vertex : vertices)
		{
			if (hasDominator(vertex))
			{
				stack.push(vertex);
			}
		}
		if (stack.isEmpty())
		{
			return null;
		}
		while (stack.size() > 1)
		{
			stack.push(commonDominator(stack.pop(), stack.pop()));
		}
		return stack.pop();
	}

	private V commonDominator(V vertex1, V vertex2)
	{
		V finger1 = vertex1;
		V finger2 = vertex2;
		while (!finger1.equals(finger2))
		{
			while (postorderNumber(finger1) < postorderNumber(finger2))
			{
				finger1 = getDominator(finger1);
			}
			while (postorderNumber(finger2) < postorderNumber(finger1))
			{
				finger2 = getDominator(finger2);
			}
		}
		return finger1;
	}

	private boolean addVertex(V vertex)
	{
		if (!contains(vertex))
		{
			dominators.put(vertex, null);
			return true;
		}
		return false;
	}

	private boolean setDominator(V vertex, V dominator)
	{
		boolean changed = false;
		if (contains(vertex))
		{
			V currentDominator = dominators.get(vertex);
			if (currentDominator == null && dominator != null)
			{
				dominators.put(vertex, dominator);
				changed = true;
			}
			else if (!currentDominator.equals(dominator))
			{
				dominators.put(vertex, dominator);
				changed = true;
			}
			else
			{
				changed = false;
			}
		}
		return changed;
	}

	private boolean contains(V vertex)
	{
		return dominators.containsKey(vertex);
	}

	private boolean hasDominator(V vertex)
	{
		return dominators.get(vertex) != null;
	}

	private static class DominatorTreeCreator<V, E extends Edge<V>>
	{

		private DominatorTree<V> dominatorTree;
		private IncidenceListGraph<V, E> graph;
		private List<V> orderedVertices;
		private V startNode;

		public DominatorTreeCreator(IncidenceListGraph<V, E> graph, V startNode)
		{
			this.dominatorTree = new DominatorTree<V>();
			this.graph = graph;
			this.orderedVertices = new LinkedList<V>();
			this.startNode = startNode;
		}

		public DominatorTree<V> create()
		{
			enumerateVertices();
			initializeDominatorTree();
			buildDominatorTree();
			return dominatorTree;
		}

		private void buildDominatorTree()
		{
			boolean changed = true;
			while (changed)
			{
				changed = false;

				ListIterator<V> reverseVertexIterator = orderedVertices
						.listIterator(orderedVertices.size());
				// Skip the root
				reverseVertexIterator.previous();

				while (reverseVertexIterator.hasPrevious())
				{
					V currentNode = reverseVertexIterator.previous();
					List<V> list = new LinkedList<V>();
					for (Edge<V> edge : graph.incomingEdges(currentNode))
					{
						list.add(edge.getSource());
					}
					V newIdom = dominatorTree.commonDominator(list);
					dominatorTree.addVertex(currentNode);
					if (dominatorTree.setDominator(currentNode, newIdom))
					{
						changed = true;
					}
				}
			}
		}

		private void enumerateVertices()
		{
			int counter = 0;
			Iterator<V> postorderIterator = new PostorderIterator<V, E>(graph,
					startNode);
			while (postorderIterator.hasNext())
			{
				V vertex = postorderIterator.next();
				orderedVertices.add(vertex);
				dominatorTree.postorderEnumeration.put(vertex, counter++);
			}
			if (orderedVertices.size() < graph.size())
			{
				System.out.println("warning: incomplete control flow graph");
			}
		}

		private void initializeDominatorTree()
		{
			dominatorTree.addVertex(startNode);
			dominatorTree.setDominator(startNode, startNode);
		}

	}

}