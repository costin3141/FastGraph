package fastgraph.impl;

import java.util.Random;

import fastgraph.Adjacency;
import fastgraph.Graph;
import fastgraph.IntIterator;

public class DiGraph implements Graph {
	
	public ArrayListSetAdjacency[] _graph;
	
	public DiGraph( final int n ) {
	   _graph = new ArrayListSetAdjacency[n];
	   for( int i=0; i<n; i++ ) {
	      _graph[i] = new ArrayListSetAdjacency( this, i, (int) (Math.sqrt( n )+1) );
	   }
	}
	
	@Override
	public int verticesCount() {
	   return _graph.length;
	}

	@Override
	public Adjacency adjacencyOf(int vertex) {
		return _graph[vertex];
	}

	@Override
	public boolean hasEdge(int source, int dest) {
		return adjacencyOf(source).contains( dest );
	}

	static void addRandomEdges( final DiGraph graph, final int count ) {
	   final Random rnd = new java.util.Random(12);
	   
	   for( int i=0; i<count; i++ ) {
	      final int u = rnd.nextInt(graph.verticesCount());
	      final int v = rnd.nextInt(graph.verticesCount());

	      graph.adjacencyOf( u ).add( v );
	   }
	}
	
	static void print( Graph graph ) {
	   for( int i=0; i<5; i++ ) {
         System.out.print( i+": ");
         for( int v : graph.adjacencyOf( i ) ) {
            System.out.print( v +" ");
         }
         System.out.println();
      }
	}
	
	static DiGraph buildRandomGraph( final int n ) {
	   DiGraph graph = new DiGraph( n );
	   addRandomEdges( graph, n*n/4 );
	   return graph;
	}
	
	static long iterateByIterator( final Graph graph ) {
	   long count = 0;
	   
	   for( int c=0; c<10000; c++ ) {
   	   for( int u=0; u<graph.verticesCount(); u++ ) {
//   	      for( int v : graph.adjacencyOf( u ) ) {
//   	         count += v;
//   	      }
   	      for( IntIterator iter = graph.adjacencyOf( u ).intIterator(); iter.hasNext(); ) {
   	         final int v = iter.nextInt();
   	         count += v;
   	      }
//   	      for( Iterator<Integer> iter = graph.adjacencyOf( u ).iterator(); iter.hasNext(); ) {
//   	         final int v = iter.next().intValue();
//   	         count += v;
//   	      }
   	   }
	   }
	   return count;
	}
	
//	static long iterateByDirectAccess( final DiGraph graph ) {
//      long count = 0;
//      
//      for( int c=0; c<10000; c++ ) {
//         for( int u=0; u<graph.verticesCount(); u++ ) {
//            final ArrayListSetAdjacency adj = (ArrayListSetAdjacency) graph.adjacencyOf( u );
//            for( int i=0; i<adj.size(); i++ ) {
//               final int v = adj.get(i);
//               count += v;
//            }
//         }
//      }
//      return count;
//   }
	
	public static void main( String[] args ) {
	   final int n = 10000;
	   
	   DiGraph graph = new DiGraph( 5 );
	   
      graph.adjacencyOf( 0 ).add( 1 );
      graph.adjacencyOf( 0 ).add( 2 );
      graph.adjacencyOf( 2 ).add( 1 );
      graph.adjacencyOf( 1 ).add( 0 );
	   
	   print( graph );
	   
	   graph.adjacencyOf(0).remove( 2 );
	   
	   print( graph );
	   
	   long start;
	   
	   start = System.currentTimeMillis();
	   buildRandomGraph(n);
	   System.out.println("time: "+(System.currentTimeMillis()-start));
	   
	   start = System.currentTimeMillis();
      buildRandomGraph(n);
      System.out.println("time: "+(System.currentTimeMillis()-start));
      
      start = System.currentTimeMillis();
      buildRandomGraph(n);
      System.out.println("time: "+(System.currentTimeMillis()-start));
      
//      start = System.currentTimeMillis();
//      graph = buildRandomGraph(n);
//      System.out.println("time: "+(System.currentTimeMillis()-start));
//      
//      long count;
//      
//      start = System.currentTimeMillis();
//      count = iterateByIterator( graph );
//      System.out.println("count="+count+"  time: "+(System.currentTimeMillis()-start));
//      
//      start = System.currentTimeMillis();
//      count = iterateByIterator( graph );
//      System.out.println("count="+count+"  time: "+(System.currentTimeMillis()-start));
//      
//      start = System.currentTimeMillis();
//      count = iterateByIterator( graph );
//      System.out.println("count="+count+"  time: "+(System.currentTimeMillis()-start));
//      
//      start = System.currentTimeMillis();
//      count = iterateByIterator( graph );
//      System.out.println("count="+count+"  time: "+(System.currentTimeMillis()-start));
      
      ////////////
      
//      start = System.currentTimeMillis();
//      count = iterateByDirectAccess( graph );
//      System.out.println("count="+count+"  time: "+(System.currentTimeMillis()-start));
//      
//      start = System.currentTimeMillis();
//      count = iterateByDirectAccess( graph );
//      System.out.println("count="+count+"  time: "+(System.currentTimeMillis()-start));
//      
//      start = System.currentTimeMillis();
//      count = iterateByDirectAccess( graph );
//      System.out.println("count="+count+"  time: "+(System.currentTimeMillis()-start));
//      
//      start = System.currentTimeMillis();
//      count = iterateByDirectAccess( graph );
//      System.out.println("count="+count+"  time: "+(System.currentTimeMillis()-start));
   }
}
