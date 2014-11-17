package dev.costin.fastgraph;

import dev.costin.fastcollections.sets.IntSet;

public interface Adjacency extends IntSet {
   
   Graph ownerGraph();
   
   int owner();
   
}
