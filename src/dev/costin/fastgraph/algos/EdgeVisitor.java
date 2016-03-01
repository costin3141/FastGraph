package dev.costin.fastgraph.algos;

public interface EdgeVisitor {
	boolean visitEdge( final int source, final int dest );
}
