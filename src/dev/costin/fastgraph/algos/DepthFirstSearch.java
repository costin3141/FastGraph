package dev.costin.fastgraph.algos;

import dev.costin.fastcollections.IntIterator;
import dev.costin.fastgraph.Graph;

/**
 * Depth first search traversal.
 * 
 * <p>
 * <strong>NOTE:</strong> This class is not stateless and thus also not
 * thread-save!
 * </p>
 * 
 * @author Stefan C. Ionescu
 *
 */
public class DepthFirstSearch implements EdgeTraversal {
	

   /**
    * Enhanced Visitor interface that has methods called for special
    * DFS edge types.
    */
   public static interface DFSVertexVisitor extends EdgeVisitor {
      
      void onNewTree( int root );

      boolean onBackEdge( int source, int dest );

      boolean onTreeCrossingEdge( int source, int dest );

      boolean onSameTreeCrossingEdge( int source, int dest );

   }

   protected final static byte UNVISITED = 0;
   protected final static byte ON_PATH   = 1;
   protected final static byte DONE      = 2;

   private Graph               _graph;
   private byte[]              _mark;
   private int[]               _treeRoot;
   private int                 _currentTreeRoot;

   public DepthFirstSearch() {

   }

   @Override
   public void traverse( final Graph graph, final EdgeVisitor visitor ) {
      _graph = graph;
      _mark = new byte[graph.verticesCount()];

      // TODO: respect disabled vertices
      boolean doContinue = true;
      for( int i = 0; i < graph.verticesCount() && doContinue; i++ ) {
         if( _mark[i] == UNVISITED ) {
            doContinue = traverse_recursive( -1, i, visitor );
         }
      }
   }

   public void traverse2( final Graph graph, final DFSVertexVisitor visitor ) {
      _graph = graph;
      _mark = new byte[graph.verticesCount()];

      _treeRoot = new int[graph.verticesCount()];

      // TODO: respect disabled vertices
      boolean doContinue = true;
      for( int i = 0; i < graph.verticesCount() && doContinue; i++ ) {
         if( _mark[i] == UNVISITED ) {
            _currentTreeRoot = i;
            _treeRoot[i] = i;
            visitor.onNewTree( i );
            doContinue = traverse_recursive( -1, i, visitor );
         }
      }
   }

   protected boolean traverse_recursive( final int parent, final int v, final EdgeVisitor visitor ) {
      _mark[ v ] = ON_PATH;
      
      if( visitor.visitEdge( parent, v ) ) {
         for( final IntIterator iter = _graph.adjacencyOf( v ).intIterator(); iter.hasNext(); ) {
            final int child = iter.nextInt();

            if( _mark[ child ] == UNVISITED ) {
               if( !traverse_recursive( v, child, visitor ) ) {
                  return false;
               }
            }
         }
         _mark[ v ] = DONE;
         return true;
      }
      else {
         return false;
      }
   }

   protected boolean traverse_recursive( final int parent, final int v, final DFSVertexVisitor visitor ) {
      _mark[v] = ON_PATH;
      _treeRoot[v] = _currentTreeRoot;

      if( visitor.visitEdge( parent, v ) ) {
         for( final IntIterator iter = _graph.adjacencyOf( v ).intIterator(); iter.hasNext(); ) {
            final int child = iter.nextInt();
            final byte childMark = _mark[ child ];
            
   			if( childMark == UNVISITED ) {
               if( !traverse_recursive( v, child, visitor ) ) {
                  return false;
               }
            }
            else if( childMark == ON_PATH ) {
               if( !visitor.onBackEdge( v, child ) ) {
                  return false;
               }
            }
            else if( _treeRoot[child] != _currentTreeRoot ) {
               if( !visitor.onTreeCrossingEdge( v, child ) ) {
                  return false;
               }
            }
            else if( !visitor.onSameTreeCrossingEdge( v, child ) ) {
              return false;
            }
         }
         _mark[v] = DONE;
         return true;
      }
      else {
         return false;
      }
   }

}
