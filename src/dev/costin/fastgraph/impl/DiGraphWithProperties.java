package dev.costin.fastgraph.impl;

import dev.costin.fastcollections.sets.IntSet;
import dev.costin.fastgraph.AdjacencyWithProperties;
import dev.costin.fastgraph.GraphWithProperties;
import dev.costin.fastgraph.properties.BasicProperties;
import dev.costin.fastgraph.properties.PropertiesFactory;


public class DiGraphWithProperties<E extends BasicProperties> extends DiGraph implements GraphWithProperties<E> {
   
   private final PropertiesFactory<E> _edgePropertiesFactory;
   
   public class IntSetAdjacencyWithProperties extends IntSetAdjacency implements AdjacencyWithProperties<E> {
      
      private Object[] _edgeProperties;

      protected IntSetAdjacencyWithProperties( DiGraph ownerGraph, int owner ) {
         super( ownerGraph, owner );
         _edgeProperties = new Object[ownerGraph.verticesCount()];
      }
      
      protected IntSetAdjacencyWithProperties( DiGraph ownerGraph, int owner, int initialListCapacity ) {
         super( ownerGraph, owner, initialListCapacity );
         _edgeProperties = new Object[ownerGraph.verticesCount()];
      }

      @Override
      @SuppressWarnings("unchecked")
      public E getEdgeProperties( int adjacentVertex ) {
         final Object untypedProperties = _edgeProperties[adjacentVertex];
         if( untypedProperties != null ) {
            return (E) untypedProperties;
         }
         else{
            final E properties = _edgePropertiesFactory.newInstance();
            _edgeProperties[adjacentVertex] = properties;
            return properties;
         }
      }
      
      @Override
      public boolean add( int vertex ) {
         _edgeProperties[vertex] = null;
         return super.add( vertex );
      }
      
      @Override
      public boolean remove( int vertex ) {
         _edgeProperties[vertex] = null;
         return super.remove( vertex );
      }
   }
   
   
   public DiGraphWithProperties( final int n, final PropertiesFactory<E> edgePropertiesFactory ) {
      super(n);
      _edgePropertiesFactory = edgePropertiesFactory;
   }

   @Override
   @SuppressWarnings("unchecked")
   public AdjacencyWithProperties<E> adjacencyOf( int vertex ) {
      return (AdjacencyWithProperties<E>)super.adjacencyOf( vertex );
   }

   @Override
   public E getEdgeProperties( int source, int dest ) {
      return adjacencyOf( source ).getEdgeProperties( dest );
   }

   @Override
   protected IntSetAdjacency createAdjacency( DiGraph ownerGraph, int owner ) {
      return new IntSetAdjacencyWithProperties( ownerGraph, owner );
   }
   
   @Override
   protected IntSetAdjacency createAdjacency( DiGraph ownerGraph, int owner, int initialCapacity ) {
      return new IntSetAdjacencyWithProperties( ownerGraph, owner, initialCapacity );
   }

   @SuppressWarnings("unchecked")
   @Override
   protected void buildInducedAdjacency( IntSet vertices, IntSetAdjacency originalAdjacency,
         IntSetAdjacency newAdjacency ) {
      super.buildInducedAdjacency( vertices, originalAdjacency, newAdjacency );
      
      final IntSetAdjacencyWithProperties origAdj = (DiGraphWithProperties<E>.IntSetAdjacencyWithProperties) originalAdjacency;
      final IntSetAdjacencyWithProperties newAdj = (DiGraphWithProperties<E>.IntSetAdjacencyWithProperties) newAdjacency;
      
      for( int i=0; i<newAdj.size(); i++ ) {
         final int v = newAdj.get( i );
         
         final Object edgeProps = origAdj._edgeProperties[v];
         if( edgeProps != null ) {
            newAdj._edgeProperties[v] = ((E)edgeProps).clone();
         }
      }
   }
}
