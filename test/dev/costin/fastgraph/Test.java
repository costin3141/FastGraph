package dev.costin.fastgraph;

import java.util.Random;

import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.IntIterator;
import dev.costin.fastgraph.Graph;
import dev.costin.fastgraph.impl.DiGraph;
import dev.costin.fastgraph.impl.DiGraph.IntSetAdjacency;


public class Test {
   static void addRandomEdges( final DiGraph graph, final int count ) {
      final Random rnd = new java.util.Random( 12 );

      for( int i = 0; i < count; i++ ) {
         final int u = rnd.nextInt( graph.verticesCount() );
         final int v = rnd.nextInt( graph.verticesCount() );

         graph.adjacencyOf( u ).add( v );
      }
   }

   static void print( Graph graph ) {
      for( int i = 0; i < 5; i++ ) {
         System.out.print( i + ": " );
         for( IntCursor v : graph.adjacencyOf( i ) ) {
            System.out.print( v.value() + " " );
         }
         System.out.println();
      }
   }

   static DiGraph buildRandomGraph( final int n, final int m ) {
      DiGraph graph = new DiGraph( n );
      addRandomEdges( graph, m );
      return graph;
   }

   static long iterateByIterator( final Graph graph ) {
      long count = 0;

      for( int c = 0; c < 100; c++ ) {
         for( int u = 0; u < graph.verticesCount(); u++ ) {
            for( IntIterator iter = graph.adjacencyOf( u ).intIterator(); iter.hasNext(); ) {
               final int v = iter.nextInt();
               count += v;
            }
         }
      }
      return count;
   }

   static long iterateByCursor( final Graph graph ) {
      long count = 0;

      for( int c = 0; c < 100; c++ ) {
         for( int u = 0; u < graph.verticesCount(); u++ ) {
            for( final IntCursor cursor : graph.adjacencyOf( u ) ) {
               count += cursor.value();
            }
         }
      }
      return count;
   }

   static long iterateByDirectAccess( final DiGraph graph ) {
      long count = 0;

      for( int c = 0; c < 100; c++ ) {
         for( int u = 0; u < graph.verticesCount(); u++ ) {
            final IntSetAdjacency adj = (IntSetAdjacency) graph.adjacencyOf( u );
            for( int i = 0; i < adj.size(); i++ ) {
               final int v = adj.get( i );
               count += v;
            }
         }
      }
      return count;
   }

   static void testIntIter( final Graph graph ) {
      long start = System.currentTimeMillis();
      long count = 0;

      start = System.currentTimeMillis();
      count = iterateByIterator( graph );
      System.out.println( "count=" + count + "  time: " + ( System.currentTimeMillis() - start ) );

      start = System.currentTimeMillis();
      count = iterateByIterator( graph );
      System.out.println( "count=" + count + "  time: " + ( System.currentTimeMillis() - start ) );

      start = System.currentTimeMillis();
      count = iterateByIterator( graph );
      System.out.println( "count=" + count + "  time: " + ( System.currentTimeMillis() - start ) );

      start = System.currentTimeMillis();
      count = iterateByIterator( graph );
      System.out.println( "count=" + count + "  time: " + ( System.currentTimeMillis() - start ) );
   }

   static void testIntCursorIter( final Graph graph ) {
      long start = System.currentTimeMillis();
      long count = 0;

      start = System.currentTimeMillis();
      count = iterateByCursor( graph );
      System.out.println( "count=" + count + "  time: " + ( System.currentTimeMillis() - start ) );

      start = System.currentTimeMillis();
      count = iterateByCursor( graph );
      System.out.println( "count=" + count + "  time: " + ( System.currentTimeMillis() - start ) );

      start = System.currentTimeMillis();
      count = iterateByCursor( graph );
      System.out.println( "count=" + count + "  time: " + ( System.currentTimeMillis() - start ) );

      start = System.currentTimeMillis();
      count = iterateByCursor( graph );
      System.out.println( "count=" + count + "  time: " + ( System.currentTimeMillis() - start ) );

   }

   static void testDirectAccess( DiGraph graph ) {
      long start = System.currentTimeMillis();
      long count = 0;

      start = System.currentTimeMillis();
      count = iterateByCursor( graph );
      System.out.println( "count=" + count + "  time: " + ( System.currentTimeMillis() - start ) );

      start = System.currentTimeMillis();
      count = iterateByDirectAccess( graph );
      System.out.println( "count=" + count + "  time: " + ( System.currentTimeMillis() - start ) );

      start = System.currentTimeMillis();
      count = iterateByDirectAccess( graph );
      System.out.println( "count=" + count + "  time: " + ( System.currentTimeMillis() - start ) );

      start = System.currentTimeMillis();
      count = iterateByDirectAccess( graph );
      System.out.println( "count=" + count + "  time: " + ( System.currentTimeMillis() - start ) );
   }

   public static void main( String[] args ) {
      final int n = 10000;

      DiGraph graph = new DiGraph( 5 );

      graph.adjacencyOf( 0 ).add( 1 );
      graph.adjacencyOf( 0 ).add( 2 );
      graph.adjacencyOf( 2 ).add( 1 );
      graph.adjacencyOf( 1 ).add( 0 );

      print( graph );

      graph.adjacencyOf( 0 ).remove( 2 );

      print( graph );

      long start;

      // start = System.currentTimeMillis();
      // buildRandomGraph(n);
      // System.out.println("time: "+(System.currentTimeMillis()-start));
      //
      // start = System.currentTimeMillis();
      // buildRandomGraph(n);
      // System.out.println("time: "+(System.currentTimeMillis()-start));
      //
      // start = System.currentTimeMillis();
      // buildRandomGraph(n);
      // System.out.println("time: "+(System.currentTimeMillis()-start));

      start = System.currentTimeMillis();
      graph = buildRandomGraph( n, n * n / 4 );
      System.out.println( "time: " + ( System.currentTimeMillis() - start ) );

      testIntIter( graph );
      // testIntCursorIter( graph );
      // testDirectAccess(graph);

   }

}
