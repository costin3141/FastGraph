package dev.costin.fastgraph.algos;

import static org.junit.Assert.*;

import org.junit.Test;

import dev.costin.fastcollections.sets.IntSet;
import dev.costin.fastcollections.sets.impl.IntGrowingSet;
import dev.costin.fastgraph.Graph;
import dev.costin.fastgraph.algos.DepthFirstSearch.DFSVertexVisitor;
import dev.costin.fastgraph.impl.DiGraph;


public class DepthFirstSearchTest {

   @Test
   public void test() {
      final Graph graph = new DiGraph( 6 );
      graph.addEdge( 0, 1 );
      graph.addEdge( 0, 3 );
      graph.addEdge( 1, 2 );
      graph.addEdge( 2, 3 );
      graph.addEdge( 2, 4 );
      graph.addEdge( 4, 0 );
      
      graph.addEdge( 5, 2 );
      
      final int[] visited = new int[graph.verticesCount()];
      final IntSet treeRoots = new IntGrowingSet( graph.verticesCount() );
      
      final DepthFirstSearch dfs = new DepthFirstSearch();
      dfs.traverse2( graph, new DFSVertexVisitor() {
         
         @Override
         public boolean visitEdge(int source, int dest) {
        	visited[dest]++;
            System.out.println( dest );
            return true;
         }
         
         @Override
         public boolean onTreeCrossingEdge( int source, int dest ) {
            System.out.println( "onTreeCrossingEdgeTo "+source+"->"+dest );
            assertTrue( dest == 2 );
            return true;
         }
         
         @Override
         public boolean onSameTreeCrossingEdge( int source, int dest ) {
            System.out.println( "onSameTreeCrossingEdgeTo "+source+"->"+dest );
            assertTrue( dest == 3 );
            return true;
         }
         
         @Override
         public boolean onBackEdge( int source, int dest ) {
            System.out.println( "onBackEdgeTo "+source+"->"+dest );
            assertTrue( dest == 0 );
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
