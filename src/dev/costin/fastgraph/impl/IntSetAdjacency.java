package dev.costin.fastgraph.impl;

import dev.costin.fastcollections.sets.impl.IntRangeSet;
import dev.costin.fastgraph.Adjacency;
import dev.costin.fastgraph.Graph;

public class IntSetAdjacency extends IntRangeSet implements Adjacency {

	private final Graph _ownerGraph;
	private final int _owner;

	public IntSetAdjacency(final Graph ownerGraph, final int owner) {
		this(ownerGraph, owner, ownerGraph.verticesCount());
	}

	public IntSetAdjacency(final Graph ownerGraph, final int owner, final int initialListCapacity) {
		super(0, ownerGraph.verticesCount() - 1);
		_ownerGraph = ownerGraph;
		_owner = owner;
	}

	@Override
	public Graph ownerGraph() {
		return _ownerGraph;
	}

	@Override
	public int owner() {
		return _owner;
	}

	/**
	 * <strong>NOTE:</strong> Prefer {@link Graph#addEdge(int, int)} as it is
	 * faster than calling this method.
	 */
	@Override
	public boolean add(int vertex) {
		return _ownerGraph.addEdge(_owner, vertex);
	}

	boolean superAdd(final int v) {
		return super.add(v);
	}

	/**
	 * <strong>NOTE:</strong> Prefer {@link Graph#removeEdge(int, int)} as it is
	 * faster than calling this method.
	 */
	@Override
	public boolean remove(int vertex) {
		return _ownerGraph.addEdge(_owner, vertex);
	}
	
	boolean superRemove( final int v ) {
		return super.remove(v);
	}
}
