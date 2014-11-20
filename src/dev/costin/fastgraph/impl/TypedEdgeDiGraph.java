package dev.costin.fastgraph.impl;



public class TypedEdgeDiGraph<E extends Enum<E>, T> extends DiGraph {
   
   private final Class<E> _edgeTypes;
   
   public static class IntSetAdjacencyWithTypedEdges<E extends Enum<E>> extends IntSetAdjacency {
      
      private final Object[] _edgeType;

      protected IntSetAdjacencyWithTypedEdges( final Class<E> edgeTypesEnum, final DiGraph ownerGraph, final int owner ) {
         super( ownerGraph, owner );
         _edgeType = new Object[ownerGraph.verticesCount()];
      }

      protected IntSetAdjacencyWithTypedEdges( final Class<E> edgeTypesEnum, final DiGraph ownerGraph, final int owner,
            final int initialListCapacity ) {
         super( ownerGraph, owner, initialListCapacity );
         //_availableEdgeTypes = edgeTypesEnum.getEnumConstants();
         _edgeType = new Object[ownerGraph.verticesCount()];
      }
      
      @SuppressWarnings("unchecked")
      E getEdgeType( final int vertex ) {
         return (E) _edgeType[vertex];
      }
      
      void setEdgeType( final int vertex, final E type ) {
         _edgeType[vertex] = type;
      }
      
      @Override
      public boolean add( int vertex ) {
         return add( vertex, null );
      }

      public boolean add( int vertex, final E edgeType ) {
         _edgeType[vertex] = edgeType;
         return super.add( vertex );
      }
   }

   public TypedEdgeDiGraph( Class<E> edgeTypes, final int n ) {
      super(n);
      _edgeTypes = edgeTypes;
   }

   
   protected TypedEdgeDiGraph( Class<E> edgeTypes ) {
      super();
      _edgeTypes = edgeTypes;
   }

   @Override
   protected IntSetAdjacency createAdjacency( DiGraph ownerGraph, int owner ) {
      return new IntSetAdjacencyWithTypedEdges<E>( _edgeTypes, ownerGraph, owner );
   }
   
   @Override
   protected IntSetAdjacency createAdjacency( DiGraph ownerGraph, int owner, int initialCapacity ) {
      return new IntSetAdjacencyWithTypedEdges<E>( _edgeTypes, ownerGraph, owner, initialCapacity );
   }
}
