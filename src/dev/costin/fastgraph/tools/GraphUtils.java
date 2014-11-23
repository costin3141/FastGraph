package dev.costin.fastgraph.tools;

import dev.costin.fastgraph.Graph;
import dev.costin.fastgraph.algos.DepthFirstSearch;
import dev.costin.fastgraph.algos.DepthFirstSearch.DFSVertexVisitor;
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
   
   public static boolean isCycleFree( final Graph graph ) {
      final CycleFinderVisitor visitor = new CycleFinderVisitor();
      
      new DepthFirstSearch().traverse2( graph, visitor );
      
      return !visitor.foundCycle;
   }
   
   
   ///////////////////////////////////////////
   
   private static class CycleFinderVisitor implements DFSVertexVisitor {
      
      public boolean foundCycle = false;

      @Override
      public boolean visit( int vertex ) {
         return true;
      }

      @Override
      public void onNewTree( int root ) {
         
      }

      @Override
      public boolean onBackEdgeTo( int v ) {
         foundCycle = true;
         
         return false;
      }

      @Override
      public boolean onTreeCrossingEdgeTo( int v ) {
         return true;
      }

      @Override
      public boolean onSameTreeCrossingEdgeTo( int v ) {
         return true;
      }
      
   }
}
