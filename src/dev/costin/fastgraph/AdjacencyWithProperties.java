package dev.costin.fastgraph;

import dev.costin.fastgraph.properties.BasicProperties;


/**
 * An {@link Adjacency} with support for edge properties.
 * 
 * @author Stefan C. Ionescu
 *
 * @param <E>
 */
public interface AdjacencyWithProperties<E extends BasicProperties> extends Adjacency {

   E getEdgeProperties( int adjacentVertex );
   
}
