package dev.costin.fastgraph.algos;

import dev.costin.fastgraph.Graph;


public interface VertexTraversal {

   void traverse( Graph graph, VertexVisitor visitor );
   
}
