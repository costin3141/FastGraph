package dev.costin.fastgraph;

import dev.costin.fastgraph.properties.BasicProperties;


public interface AdjacencyWithProperties<E extends BasicProperties> extends Adjacency {

   E getEdgeProperties( int adjacentVertex );
   
}
