package dev.costin.fastcollections.bridging.collections.transition;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.bridging.IndexedObjectBridge;
import dev.costin.fastcollections.maps.IntObjectMap;
import dev.costin.fastcollections.maps.IntObjectMap.IntObjectEntry;

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
      final Object[] array = new Object[size()];
      int i=0;
      for( IntIterator iter=_map.keyIterator(); iter.hasNext(); i++ ) {
         final int idx = iter.nextInt();
         array[i] = _indexer.getObject( idx );
      }
      return array;
   }

   @Override
   public <T> T[] toArray( T[] a ) {
      final int size = size();
      final T[] array;
      
      if( a.length < size ) {
         array = a;
      }
      else {
         array = (T[])java.lang.reflect.Array
                  .newInstance(a.getClass().getComponentType(), size);
      }
      int i=0;
      for( IntIterator iter=_map.keyIterator(); iter.hasNext(); i++ ) {
         final int idx = iter.nextInt();
         array[i] = (T)_indexer.getObject( idx );
      }
      return array;
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
      boolean changed = false;
      
      for( final IntIterator iter = _map.keyIterator(); iter.hasNext(); ) {
         final K key = _indexer.getObject( iter.nextInt() );

         if( !c.contains( key ) ) {
            iter.remove();
            changed = true;
         }
      }
      
      return changed;
   }

   @Override
   public boolean removeAll( Collection<?> c ) {
      boolean changed = false;
      
      for( final Object obj : c ) {
         @SuppressWarnings("unchecked")
         final K key = (K) obj;
         final int idx = _indexer.getIndex( key );
         
         if( _map.containsKey( idx ) ) {
            _map.remove( idx );
            changed = true;
         }
      }
      return changed;
   }

   @Override
   public void clear() {
      _map.clear();
   }
   
   @Override
   public boolean equals( Object obj ) {
      if( this == obj ) {
         return true;
      }
      
      if( !( obj instanceof Set ) ) {
         return false;
      }
      
      @SuppressWarnings( "unchecked" )
      final Set<K> other = (Set<K>) obj;
      
      if( size() != other.size() ) {
         return false;
      }
      
      return containsAll( other );
   }
   
   @Override
   public int hashCode() {
      int h = 0;
      
      for( IntObjectEntry<V> e : _map ) {
          h += e.getKey();
      }
      
      return h;
   }
   
   @Override
   public String toString() {
      final StringBuilder s = new StringBuilder('{');
      
      int i=0;
      for( K e : this ) {
         s.append( e );
         if( ++i < size() ) {
            s.append( ", " );
         }
      }
      
      s.append( '}' );
      
      return s.toString();
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
