package dev.costin.fastgraph.impl;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.sets.impl.IntGrowingSet;
import dev.costin.fastcollections.tools.FastCollections;
import dev.costin.fastgraph.tools.GraphUtils;


public class DiGraphTest {

   @Test
   public void testSubGraph() {
      final int n = 100;
      final DiGraph graph = GraphUtils.newFullDiGraph( n );
      final IntGrowingSet vertices = FastCollections.newIntRangeSetWithElements( 0,1,7 );
      final DiGraph subGraph = (DiGraph) graph.subGraph( vertices );
      
      assertTrue( subGraph.adjacencyOf( 0 ).size()==2 );
      assertTrue( subGraph.adjacencyOf( 0 ).contains(1) );
      assertTrue( subGraph.adjacencyOf( 0 ).contains(7) );

      assertTrue( subGraph.adjacencyOf( 1 ).size()==2 );
      assertTrue( subGraph.adjacencyOf( 1 ).contains(0) );
      assertTrue( subGraph.adjacencyOf( 1 ).contains(7) );

      assertTrue( subGraph.adjacencyOf( 7 ).size()==2 );
      assertTrue( subGraph.adjacencyOf( 7 ).contains(0) );
      assertTrue( subGraph.adjacencyOf( 7 ).contains(1) );
      
      assertTrue( subGraph.getInDegree( 0 ) == 2 );
      assertTrue( subGraph.getInDegree( 1 ) == 2 );
      assertTrue( subGraph.getInDegree( 7 ) == 2 );
   }
   
   @Test
   public void testInDeGree() {
      final int n = 100;
      final DiGraph graph = GraphUtils.newFullDiGraph( n );
    
      for( int i=0; i<n; i++ ) {
         assertTrue( "InDegree for vertex "+i+" is wrong!", graph.getInDegree( i ) == n-1 );
      }
      
      final IntIterator iter = graph.adjacencyOf( 50 ).intIterator();
      iter.nextInt();
      iter.nextInt();
      final int v = iter.nextInt();
      
      iter.remove();
      
      assertTrue( graph.getInDegree( 50 ) == n-1 );
      assertTrue( graph.getInDegree( v ) == n-2 );
      
      final Iterator<IntCursor> cursorIter = graph.adjacencyOf( 50 ).iterator();
      cursorIter.next();
      cursorIter.next();
      final int v2 = cursorIter.next().value();
      
      cursorIter.remove();
      
      assertTrue( graph.getInDegree( 50 ) == n-1 );
      assertTrue( graph.getInDegree( v2 ) == n-2 );
   }
}
