package dev.costin.fastgraph;

import dev.costin.fastcollections.sets.IntSet;

public interface Graph {

   int verticesCount();
   
   int edgesCount();

   Adjacency adjacencyOf( int vertex );

   boolean hasEdge( int source, int dest );

   boolean addEdge( int source, int dest );

   boolean removeEdge( int source, int dest );

   int getOutDegree( int vertex );

   int getInDegree( int vertex );

   Graph subGraph( IntSet vertices );

}
