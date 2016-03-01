package dev.costin.fastgraph.algos;

import dev.costin.fastgraph.Graph;

public interface EdgeTraversal {

	void traverse( Graph graph, EdgeVisitor visitor );
	
}
