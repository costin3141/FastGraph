package dev.costin.fastgraph;

import dev.costin.fastgraph.properties.BasicProperties;


public interface GraphWithProperties<V extends BasicProperties, E extends BasicProperties> extends Graph {

   @Override
   AdjacencyWithProperties<E> adjacencyOf( int vertex );
   
   V getVertexProperties( int vertex );
   E getEdgeProperties( int source, int dest );
   
}
