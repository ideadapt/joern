package graphutils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import misc.MultiHashMap;

/**
 * An graph implementation based on two incidence lists.
 * 
 * @param <V>
 *            The vertex type.
 * @param <E>
 *            The edge type.
 */
public class IncidenceListGraph<V, E extends Edge<V>> implements Iterable<V>
{

	private Collection<V> vertices;
	private MultiHashMap<V, E> outNeighborhood;
	private MultiHashMap<V, E> inNeighborhood;

	public IncidenceListGraph()
	{
		this.vertices = new LinkedList<V>();
		this.outNeighborhood = new MultiHashMap<V, E>();
		this.inNeighborhood = new MultiHashMap<V, E>();
	}

	public void addEdge(E edge)
	{
		failIfNotContained(edge.getSource());
		failIfNotContained(edge.getDestination());
		this.outNeighborhood.add(edge.getSource(), edge);
		this.inNeighborhood.add(edge.getDestination(), edge);
	}

	public boolean addVertex(V vertex)
	{
		if (!contains(vertex))
		{
			this.vertices.add(vertex);
			return true;
		}
		return false;
	}

	public boolean contains(V vertex)
	{
		return this.vertices.contains(vertex);
	}

	public Iterator<E> edgeIterator()
	{
		return getEdges().iterator();
	}

	private void failIfNotContained(V vertex)
	{
		if (!contains(vertex))
		{
			throw new NoSuchElementException("Graph has no such vertex : "
					+ vertex);
		}
	}

	public Collection<E> getEdges()
	{
		Collection<E> list = new LinkedList<E>();
		for (V vertex : this)
		{
			list.addAll(outgoingEdges(vertex));
		}
		return list;
	}

	public Collection<V> getVertices()
	{
		return vertices;
	}

	public Collection<V> in(V vertex)
	{
		failIfNotContained(vertex);
		Collection<V> list = new LinkedList<V>();
		for (E edge : ingoingEdges(vertex))
		{
			list.add(edge.getSource());
		}
		return list;
	}

	public int inDegree(V vertex)
	{
		failIfNotContained(vertex);
		return ingoingEdges(vertex).size();
	}

	public Collection<E> ingoingEdges(V vertex)
	{
		failIfNotContained(vertex);
		Collection<E> list = this.inNeighborhood.get(vertex);
		return list == null ? new LinkedList<E>() : list;
	}

	public boolean isEmpty()
	{
		return size() == 0;
	}

	@Override
	public Iterator<V> iterator()
	{
		return vertices.iterator();
	}

	public int numberOfEdges()
	{
		return getEdges().size();
	}

	public Collection<V> out(V vertex)
	{
		failIfNotContained(vertex);
		Collection<V> list = new LinkedList<V>();
		for (E edge : outgoingEdges(vertex))
		{
			list.add(edge.getDestination());
		}
		return list;
	}

	public int outDegree(V vertex)
	{
		failIfNotContained(vertex);
		return outgoingEdges(vertex).size();
	}

	public Collection<E> outgoingEdges(V vertex)
	{
		failIfNotContained(vertex);
		Collection<E> list = this.outNeighborhood.get(vertex);
		return list == null ? new LinkedList<E>() : list;
	}

	public void removeEdge(E edge)
	{
		Iterator<E> iterator;
		iterator = outgoingEdges(edge.getSource()).iterator();
		while (iterator.hasNext())
		{
			if (iterator.next().equals(edge))
			{
				iterator.remove();
			}
		}
		iterator = ingoingEdges(edge.getDestination()).iterator();
		while (iterator.hasNext())
		{
			if (iterator.next().equals(edge))
			{
				iterator.remove();
			}
		}

	}

	public void removeEdge(V source, V destination)
	{
		Iterator<E> iterator;
		iterator = outgoingEdges(source).iterator();
		while (iterator.hasNext())
		{
			if (iterator.next().getDestination().equals(destination))
				;
			{
				iterator.remove();
			}
		}
		iterator = ingoingEdges(destination).iterator();
		while (iterator.hasNext())
		{
			if (iterator.next().getSource().equals(source))
			{
				iterator.remove();
			}
		}

	}

	public void removeEdgesTo(V vertex)
	{
		List<E> removed = inNeighborhood.removeAll(vertex);
		if (removed != null)
		{
			for (E edge : removed)
			{
				Iterator<E> iterator = outgoingEdges(edge.getSource())
						.iterator();
				while (iterator.hasNext())
				{
					if (iterator.next().getDestination().equals(vertex))
					{
						iterator.remove();
					}
				}
			}
		}
	}

	public void removeEdgesFrom(V vertex)
	{
		List<E> removed = outNeighborhood.removeAll(vertex);
		if (removed != null)
		{
			for (E edge : removed)
			{
				Iterator<E> iterator = ingoingEdges(edge.getDestination())
						.iterator();
				while (iterator.hasNext())
				{
					if (iterator.next().getSource().equals(vertex))
					{
						iterator.remove();
					}
				}
			}
		}
	}

	public void removeVertex(V vertex)
	{
		removeEdgesFrom(vertex);
		removeEdgesTo(vertex);
		this.vertices.remove(vertex);
	}

	public int size()
	{
		return vertices.size();
	}

	@Override
	public String toString()
	{
		String res = "";
		for (V vertex : this)
		{
			res += vertex.toString() + '\n';
			for (Edge<V> edge : outgoingEdges(vertex))
			{
				res += edge.toString() + '\n';
			}
		}
		return res;
	}

	public boolean isConnected(V source, V destination)
	{
		for (E edge : outgoingEdges(source))
		{
			if (edge.getDestination().equals(destination))
			{
				return true;
			}
		}
		return false;
	}

}