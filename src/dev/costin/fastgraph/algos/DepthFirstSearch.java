package dev.costin.fastgraph.algos;

import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.dequeue.IntStack;
import dev.costin.fastcollections.dequeue.impl.IntArrayRingDeque;
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
public class DepthFirstSearch implements VertexTraversal {

   /**
    * Enhanced Visitor interface that has methods called for special
    * DFS edge types.
    */
   public static interface DFSVertexVisitor extends VertexVisitor {

      boolean onBackEdgeTo( int v );

      boolean onTreeCrossingEdgeTo( int v );

      boolean onSameTreeCrossingEdgeTo( int v );

   }

   protected final static byte UNVISITED = 0;
   protected final static byte ON_PATH   = 1;
   protected final static byte DONE      = 2;

   private Graph               _graph;
   private IntStack            _stack;
   private byte[]              _mark;
   private int[]               _treeRoot;
   private int                 _currentTreeRoot;

   public DepthFirstSearch() {

   }

   @Override
   public void traverse( final Graph graph, final VertexVisitor visitor ) {
      _graph = graph;
      // TODO: use edges count when we have that method implemented in Graph
      _stack = new IntArrayRingDeque( graph.verticesCount() );
      _mark = new byte[graph.verticesCount()];

      // TODO: respect disabled vertices
      boolean doContinue = true;
      for( int i = 0; i < graph.verticesCount() && doContinue; i++ ) {
         if( _mark[i] == UNVISITED ) {
            // _currentTreeRoot = i;
            // _treeRoot[i] = i;
            doContinue = traverse( i, visitor );
         }
      }
   }

   public void traverse( final Graph graph, final DFSVertexVisitor visitor ) {
      _graph = graph;
      // TODO: use edges count when we have that method implemented in Graph
      _stack = new IntArrayRingDeque( graph.verticesCount() );
      _mark = new byte[graph.verticesCount()];

      _treeRoot = new int[graph.verticesCount()];

      // TODO: respect disabled vertices
      boolean doContinue = true;
      for( int i = 0; i < graph.verticesCount() && doContinue; i++ ) {
         if( _mark[i] == UNVISITED ) {
            _currentTreeRoot = i;
            _treeRoot[i] = i;
            doContinue = traverse( i, visitor );
         }
      }
   }

   protected boolean traverse( final int v, final VertexVisitor visitor ) {
      _stack.clear();

      _stack.push( v ); // TODO: skip this with an optimization!
      // int vertex = v;
      int vertex;

      do {
         vertex = _stack.pop();

         _mark[vertex] = DONE; // visited

         if( visitor.visit( vertex ) ) {
            for( final IntIterator iter = _graph.adjacencyOf( vertex ).intIterator(); iter.hasNext(); ) {
               final int child = iter.nextInt();

               if( _mark[child] == UNVISITED ) {
                  _stack.push( child );
               }
            }
         }
         else {
            return false;
         }
      } while( !_stack.isEmpty() );

      return true;
   }

   protected boolean traverse( final int v, final DFSVertexVisitor visitor ) {
      _stack.clear();

      _stack.push( -v - 1 ); // signaling backtrack of v
      _stack.push( v ); // TODO: skip this with an optimization!

      int vertex;

      do {
         while( ( vertex = _stack.pop() ) < 0 ) {
            // backtrack...

            _mark[-vertex - 1] = DONE; // done
            if( _stack.isEmpty() ) {
               return true;
            }
         }

         _mark[vertex] = ON_PATH; // within current path
         _treeRoot[vertex] = _currentTreeRoot;

         if( visitor.visit( vertex ) ) {
            for( final IntIterator iter = _graph.adjacencyOf( vertex ).intIterator(); iter.hasNext(); ) {
               final int child = iter.nextInt();

               if( _mark[child] == UNVISITED ) {
                  _stack.push( -child - 1 ); // signaling backtrack of child
                  _stack.push( child );
               }
               else if( _mark[child] == ON_PATH ) {
                  if( !visitor.onBackEdgeTo( child ) ) {
                     return false;
                  }
               }
               else if( _treeRoot[child] != _currentTreeRoot ) {
                  if( !visitor.onTreeCrossingEdgeTo( child ) ) {
                     return false;
                  }
               }
               else {
                  if( !visitor.onSameTreeCrossingEdgeTo( child ) ) {
                     return false;
                  }
               }
            }
         }
         else {
            return false;
         }
      } while( !_stack.isEmpty() );

      return true;
   }

}
