package fastgraph;

import fastcollections.sets.IntSet;

public interface Adjacency extends IntSet {
   
   Graph ownerGraph();
   
   int owner();
   
}
