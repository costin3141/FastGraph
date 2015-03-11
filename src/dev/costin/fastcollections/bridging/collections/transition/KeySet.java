package dev.costin.fastcollections.bridging.collections.transition;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.bridging.IndexedObjectBridge;
import dev.costin.fastcollections.maps.IntObjectMap;

public class KeySet<K, V> implements Set<K> {
   
   private final IntObjectMap<V> _map;
   private final IndexedObjectBridge<K> _indexer;
   
   public KeySet( final IntObjectMap<V> intMap, final IndexedObjectBridge<K> indexer ) {
      _map = intMap;
      _indexer = indexer;
   }

   @Override
   public int size() {
      return _map.size();
   }

   @Override
   public boolean isEmpty() {
      return _map.isEmpty();
   }

   @Override
   public boolean contains( Object o ) {
      return _map.containsKey( _indexer.getIndex( (K) o ) );
   }

   @Override
   public Iterator<K> iterator() {
      return new KeyIterator();
   }

   @Override
   public Object[] toArray() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public <T> T[] toArray( T[] a ) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public boolean add( K e ) {
      throw new UnsupportedOperationException( "This set is a key set of a map. Entering keys without a given value is not allowed!" );
   }

   @Override
   public boolean remove( Object o ) {
      @SuppressWarnings( "unchecked" )
      final int key = _indexer.getIndex( (K) o );
      
      if( !_map.containsKey( key ) ) {
         return false;
      }
      _map.remove( key );
      return true;
   }

   @Override
   public boolean containsAll( Collection<?> c ) {
      for( final Object o : c ) {
         @SuppressWarnings( "unchecked" )
         final int key = _indexer.getIndex( (K) o );
         
         if( !_map.containsKey( key ) ) {
            return false;
         }
      }
      return true;
   }

   @Override
   public boolean addAll( Collection<? extends K> c ) {
      throw new UnsupportedOperationException( "This set is a key set of a map. Entering keys without a given value is not allowed!" );
   }

   @Override
   public boolean retainAll( Collection<?> c ) {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean removeAll( Collection<?> c ) {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public void clear() {
      _map.clear();
   }

   private class KeyIterator implements Iterator<K> {
      
      final IntIterator _intIterator;
      
      KeyIterator() {
         _intIterator = _map.keyIterator();
      }

      @Override
      public boolean hasNext() {
         return _intIterator.hasNext();
      }

      @Override
      public K next() {
         return _indexer.getObject( _intIterator.nextInt() );
      }

      @Override
      public void remove() {
         _intIterator.remove();
      }
      
   }
}
