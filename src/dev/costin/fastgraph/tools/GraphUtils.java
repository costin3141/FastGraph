package dev.costin.fastgraph.tools;

import dev.costin.fastgraph.impl.DiGraph;


public class GraphUtils {

   public static DiGraph newFullDiGraph( final int n ) {
      final DiGraph graph = new DiGraph( n );
      for( int u=0; u<n-1; u++ ) {
         for( int v=u+1; v<n; v++ ) {
            graph.addEdge( u, v );
            graph.addEdge( v, u );
         }
      }
      
      return graph;
   }
}
