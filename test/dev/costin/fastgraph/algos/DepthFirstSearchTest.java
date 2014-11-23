package dev.costin.fastgraph.algos;

import static org.junit.Assert.*;

import org.junit.Test;

import dev.costin.fastcollections.sets.IntSet;
import dev.costin.fastcollections.sets.impl.IntRangeSet;
import dev.costin.fastgraph.Graph;
import dev.costin.fastgraph.algos.DepthFirstSearch.DFSVertexVisitor;
import dev.costin.fastgraph.impl.DiGraph;


public class DepthFirstSearchTest {

   @Test
   public void test() {
      final Graph graph = new DiGraph( 6 );
      graph.addEdge( 0, 3 );
      graph.addEdge( 0, 1 );
      graph.addEdge( 1, 2 );
      graph.addEdge( 2, 3 );
      graph.addEdge( 2, 4 );
      graph.addEdge( 4, 0 );
      
      graph.addEdge( 5, 2 );
      
      final int[] visited = new int[graph.verticesCount()];
      final IntSet treeRoots = new IntRangeSet( graph.verticesCount() );
      
      final DepthFirstSearch dfs = new DepthFirstSearch();
      dfs.traverse2( graph, new DFSVertexVisitor() {
         
         @Override
         public boolean visit( int vertex ) {
            visited[vertex]++;
            System.out.println( vertex );
            return true;
         }
         
         @Override
         public boolean onTreeCrossingEdgeTo( int v ) {
            System.out.println( "onTreeCrossingEdgeTo "+v );
            assertTrue( v == 2 );
            return true;
         }
         
         @Override
         public boolean onSameTreeCrossingEdgeTo( int v ) {
            System.out.println( "onSameTreeCrossingEdgeTo "+v );
            assertTrue( v == 3 );
            return true;
         }
         
         @Override
         public boolean onBackEdgeTo( int v ) {
            System.out.println( "onBackEdgeTo "+v );
            assertTrue( v == 0 );
            return true;
         }

         @Override
         public void onNewTree( int root ) {
            treeRoots.add( root );
         }
      } );
      
      for( int i=0; i < visited.length; i++ ) {
         assertTrue( "Vertex "+i+" has not been visisted!", visited[i]==1 );
      }
      
      assertTrue( treeRoots.contains( 0 ) );
      assertTrue( treeRoots.contains( 5 ) );
   }

}
