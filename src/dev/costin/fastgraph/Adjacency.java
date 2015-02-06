package dev.costin.fastgraph;

import dev.costin.fastcollections.sets.IntSet;

/**
 * An adjacency class based on {@link IntSet}.
 * 
 * @author Stefan C. Ionescu
 *
 */
public interface Adjacency extends IntSet {
   
   Graph ownerGraph();
   
   int owner();
   
}
