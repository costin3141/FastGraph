package dev.costin.fastgraph;

import dev.costin.fastgraph.properties.BasicProperties;


public interface GraphWithProperties<E extends BasicProperties> extends Graph {

   @Override
   AdjacencyWithProperties<E> adjacencyOf( int vertex );
   
   E getEdgeProperties( int source, int dest );
   
}
