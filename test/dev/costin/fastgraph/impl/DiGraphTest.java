package dev.costin.fastgraph.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import dev.costin.fastcollections.sets.impl.IntRangeSet;
import dev.costin.fastcollections.tools.FastCollections;
import dev.costin.fastgraph.tools.GraphUtils;


public class DiGraphTest {

   @Test
   public void testSubGraph() {
      final int n = 100;
      final DiGraph graph = GraphUtils.newFullDiGraph( n );
      final IntRangeSet vertices = FastCollections.newIntRangeSetWithElements( 0,1,7 );
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
   }
}
