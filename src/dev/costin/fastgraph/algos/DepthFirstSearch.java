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
      
      void onNewTree( int root );

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
      _stack = new IntArrayRingDeque( Math.max( 2*graph.edgesCount() / graph.verticesCount(), graph.verticesCount() ) );
      _mark = new byte[graph.verticesCount()];

      // TODO: respect disabled vertices
      boolean doContinue = true;
      for( int i = 0; i < graph.verticesCount() && doContinue; i++ ) {
         if( _mark[i] == UNVISITED ) {
            doContinue = traverse( i, visitor );
         }
      }
   }

   public void traverse2( final Graph graph, final DFSVertexVisitor visitor ) {
      _graph = graph;
      _stack = new IntArrayRingDeque( Math.max( 4*graph.edgesCount() / graph.verticesCount(), 2*graph.verticesCount() ) );
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
         if( _mark[vertex] == UNVISITED ) {
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
         }
      } while( !_stack.isEmpty() );
         

      return true;
   }

   protected boolean traverse( final int v, final DFSVertexVisitor visitor ) {
      _stack.clear();

      _stack.push( -v - 1 ); // signaling backtrack of v
      _stack.push( v ); // TODO: skip this with an optimization!
      
      visitor.onNewTree( v );

      int vertex;

      do {
         while( ( vertex = _stack.pop() ) < 0 ) {
            // backtrack...

            _mark[-vertex - 1] = DONE; // done
            if( _stack.isEmpty() ) {
               return true;
            }
         }

         if( _mark[vertex] == UNVISITED ) {
            _mark[vertex] = ON_PATH; // within current path
            _treeRoot[vertex] = _currentTreeRoot;
   
            if( visitor.visit( vertex ) ) {
               for( final IntIterator iter = _graph.adjacencyOf( vertex ).intIterator(); iter.hasNext(); ) {
                  final int child = iter.nextInt();

                  // we check for the childs status before adding the child as
                  // it can dramatically improve performance and reduce stack size
                  // even though it would suffice to only check the popped
                  // vertex while adding always all children.
                  if( _mark[child] == UNVISITED ) {
                     _stack.push( -child - 1 ); // signaling backtrack of child
                     _stack.push( child );
                  }
                  else if( ! visitSpecialDFSEdges( child, visitor ) ) {
                     return false;
                  }
               }
            }
            else {
               return false;
            }
         }
         else if( ! visitSpecialDFSEdges( vertex, visitor ) ) {
            return false;
         }
      } while( !_stack.isEmpty() );

      return true;
   }

   private boolean visitSpecialDFSEdges( int vertex, final DFSVertexVisitor visitor ) {
      if( _mark[vertex] == ON_PATH ) {
         if( !visitor.onBackEdgeTo( vertex ) ) {
            return false;
         }
      }
      else if( _treeRoot[vertex] != _currentTreeRoot ) {
         if( !visitor.onTreeCrossingEdgeTo( vertex ) ) {
            return false;
         }
      }
      else {
         if( !visitor.onSameTreeCrossingEdgeTo( vertex ) ) {
            return false;
         }
      }
      return true;
   }

}
