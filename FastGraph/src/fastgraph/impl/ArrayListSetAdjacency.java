package fastgraph.impl;

import java.util.Arrays;
import java.util.Iterator;

import fastgraph.Adjacency;
import fastgraph.Graph;
import fastgraph.IntIterator;

public class ArrayListSetAdjacency implements Adjacency {
   
   private final Graph _ownerGraph;
   private final int _owner;
   
   private final int[] _elementsSet;

   private int _size;
   private int[] _elementsList;
   
   private long _modCount;
   
   IntArrayIterator iter = null;

   public ArrayListSetAdjacency( final Graph ownerGraph, final int owner ) {
      this( ownerGraph, owner, ownerGraph.verticesCount() );
   }

   public ArrayListSetAdjacency( final Graph ownerGraph, final int owner, final int initialListCapacity ) {
      _ownerGraph = ownerGraph;
      _owner = owner;
      _elementsSet = new int[_ownerGraph.verticesCount()];
      _elementsList = new int[initialListCapacity];
      _modCount = 0;
   }
   
   @Override
   public Graph ownerGraph() {
      return _ownerGraph;
   }
   
   @Override
   public int owner() {
      return _owner;
   }
   
   @Override
   public int size() {
      return _size;
   }

   @Override
   public Iterator<Integer> iterator() {
      return new IntArrayIterator( _elementsList, _size );
   }
   
   @Override
   public IntIterator intIterator() {
      return new IntArrayIterator( _elementsList, _size );
   }

   @Override
   public boolean contains( int v ) {
      return _elementsSet[v] != 0;
   }
   
   @Override
   public boolean add( int v ) {
      if( ! contains( v ) ) {
         _elementsSet[v] = addToList( v );
         
         return true;
      }
      
      return false;
   }

   @Override
   public boolean remove( int v ) {
      final int idx = _elementsSet[v];
      if( idx>0 ) {
         _elementsList[ idx-1 ] = _elementsList[ --_size ];
         _elementsSet[v] = 0;
         return true;
      }
      return false;
   }

   private int addToList( final int v ) {
      if( _size >= _elementsList.length ) {
         _elementsList = Arrays.copyOf( _elementsList, _size + ((_size+1) >> 1) );
      }
      
      _elementsList[ _size++ ] = v;
      
      return _size;
   }
}
