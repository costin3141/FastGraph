package dev.costin.fastgraph;


public interface TypedEdgesAdjacency<E extends Enum<E>> extends Adjacency {

   E getEdgeType( final int vertex );
   
   void setEdgeType( final int vertex, final E type );

   boolean add( int vertex, final E edgeType );
   
}
