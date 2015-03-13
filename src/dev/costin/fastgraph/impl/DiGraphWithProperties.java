package dev.costin.fastgraph.impl;

import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.sets.IntSet;
import dev.costin.fastcollections.sets.impl.IntGrowingSet;
import dev.costin.fastgraph.Adjacency;
import dev.costin.fastgraph.AdjacencyWithProperties;
import dev.costin.fastgraph.Graph;
import dev.costin.fastgraph.GraphWithProperties;
import dev.costin.fastgraph.properties.BasicProperties;
import dev.costin.fastgraph.properties.PropertiesFactory;


public class DiGraphWithProperties<V extends BasicProperties, E extends BasicProperties> extends DiGraph implements GraphWithProperties<V,E> {
   
   private final PropertiesFactory<E> _edgePropertiesFactory;
   private final PropertiesFactory<V> _vertexPropertiesFactory;
   private final BasicProperties[] _vertexProperties;
   private final BasicProperties[] _edgeProperties;
   
   public class IntSetAdjacencyWithProperties extends IntGrowingSet implements AdjacencyWithProperties<E> {
      private final DiGraphWithProperties<V,E> _ownerGraph;
      private final int     _owner;

      protected IntSetAdjacencyWithProperties( final DiGraphWithProperties<V,E> ownerGraph, final int owner ) {
         this( ownerGraph, owner, ownerGraph.verticesCount() );
      }

      protected IntSetAdjacencyWithProperties( final DiGraphWithProperties<V,E> ownerGraph, final int owner, final int initialListCapacity ) {
         super( 0, ownerGraph.verticesCount() - 1, initialListCapacity );
         _ownerGraph = ownerGraph;
         _owner = owner;
      }

      @Override
      public GraphWithProperties<V,E> ownerGraph() {
         return _ownerGraph;
      }

      @Override
      public int owner() {
         return _owner;
      }

      @Override
      public boolean add( int vertex ) {
         if( super.add( vertex ) ) {
            ++_ownerGraph._inDegree[vertex];
            ++_ownerGraph._edgesCount;
            return true;
         }
         return false;
      }

      @Override
      public boolean remove( int vertex ) {
         if( super.remove( vertex ) ) {
            --_ownerGraph._inDegree[vertex];
            --_ownerGraph._edgesCount;
            _edgeProperties[ ((DiGraphWithProperties<V,E>)ownerGraph()).getEdgePropertiesIdx( this, vertex ) ] = null;
            return true;
         }
         return false;
      }
      
      @Override
      public void clear() {
         final int offset = _ownerGraph.getEdgePropertiesIdx( this, 0 );
         for( int i=0; i<size(); i++ ) {
            final int v = get(i);
            --_ownerGraph._inDegree[v];
            _ownerGraph._edgesCount = 0;
            _edgeProperties[ offset + v ] = null;
         }
         super.clear();
      }
      
      @Override
      public E getEdgeProperties( final int adjacentVertex ) {
         return _ownerGraph.getEdgeProperties( _owner, adjacentVertex );
      }
      
   }
   
   
   public DiGraphWithProperties( final int n, final PropertiesFactory<V> vertexPropertiesFactory, final PropertiesFactory<E> edgePropertiesFactory ) {
      super(n);
      _vertexPropertiesFactory = vertexPropertiesFactory;
      _edgePropertiesFactory = edgePropertiesFactory;
      _vertexProperties = new BasicProperties[n];
      _edgeProperties = new BasicProperties[n*n];
   }

   @Override
   @SuppressWarnings("unchecked")
   public AdjacencyWithProperties<E> adjacencyOf( int vertex ) {
      return (AdjacencyWithProperties<E>)super.adjacencyOf( vertex );
   }

   @Override
   public V getVertexProperties( int vertex ) {
      @SuppressWarnings( "unchecked" )
      V props = (V) _vertexProperties[ vertex ];
      if( props == null ) {
         props = _vertexPropertiesFactory.newInstance();
         _vertexProperties[ vertex ] = props;
      }
      return props;
   }
   
   @Override
   public E getEdgeProperties( int source, int dest ) {
      final int idx = getEdgePropertiesIdx( source, dest );
      @SuppressWarnings( "unchecked" )
      E props = (E) _edgeProperties[ idx ];
      if( props == null ) {
         props = _edgePropertiesFactory.newInstance();
         _edgeProperties[ idx ] = props;
      }
      
      return props;
   }
   
   @Override
   public GraphWithProperties<V, E> subGraph( IntSet vertices ) {
      @SuppressWarnings( "unchecked" )
      final DiGraphWithProperties<V, E> subgraph = (DiGraphWithProperties<V, E>) super.subGraph( vertices );

      for( final IntIterator iter = vertices.intIterator(); iter.hasNext(); ) {
         final int v = iter.nextInt();
         final BasicProperties props = _vertexProperties[v];
         if( props != null ) {
            subgraph._vertexProperties[v] = props.clone();
         }
      }
      
      return subgraph;
   }
   
   @SuppressWarnings( "unchecked" )
   @Override
   protected AdjacencyWithProperties<E> createAdjacency( DiGraph ownerGraph, int owner ) {
      return new IntSetAdjacencyWithProperties( (DiGraphWithProperties<V,E>) ownerGraph, owner );
   }
   
   @SuppressWarnings( "unchecked" )
   @Override
   protected AdjacencyWithProperties<E> createAdjacency( DiGraph ownerGraph, int owner, int initialCapacity ) {
      return new IntSetAdjacencyWithProperties( (DiGraphWithProperties<V,E>) ownerGraph, owner, initialCapacity );
   }
   
   @Override
   protected void buildInducedAdjacency( final IntSet vertices, final Adjacency originalAdjacency,
            final Adjacency newAdjacency ) {
      
      super.buildInducedAdjacency( vertices, originalAdjacency, newAdjacency );
      
      @SuppressWarnings("unchecked")
      final IntSetAdjacencyWithProperties origAdj = (DiGraphWithProperties<V,E>.IntSetAdjacencyWithProperties) originalAdjacency;
      @SuppressWarnings("unchecked")
      final IntSetAdjacencyWithProperties newAdj = (DiGraphWithProperties<V,E>.IntSetAdjacencyWithProperties) newAdjacency;
      
      final int offset = getEdgePropertiesIdx( newAdj, 0 );
      final BasicProperties[] newGraphEdgeProperties = newAdj._ownerGraph._edgeProperties;
      
      for( int i=0; i<newAdj.size(); i++ ) {
         final int v = newAdj.get( i );
         
         final BasicProperties edgeProps = origAdj.getEdgeProperties( v );
         if( edgeProps != null ) {
            newGraphEdgeProperties[offset+v] = edgeProps.clone();
         }
      }
   }
   
   private int getEdgePropertiesIdx( final int source, final int dest ) {
      return source * verticesCount() + dest;
   }
   
   private int getEdgePropertiesIdx( final AdjacencyWithProperties<E> adj, final int v ) {
      return getEdgePropertiesIdx( adj.owner(), v );
   }
}
