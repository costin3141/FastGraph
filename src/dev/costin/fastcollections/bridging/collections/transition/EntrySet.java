package dev.costin.fastcollections.bridging.collections.transition;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import dev.costin.fastcollections.bridging.IndexedObjectBridge;
import dev.costin.fastcollections.maps.IntObjectMap;
import dev.costin.fastcollections.maps.IntObjectMap.IntObjectEntry;

public class EntrySet<K,V> implements Set<Map.Entry<K, V>> {
   
   private final IntObjectMap<V> _map;
   private final IndexedObjectBridge<K> _indexer;
   
   public EntrySet( final IntObjectMap<V> map, final IndexedObjectBridge<K> indexer ) {
      _map = map;
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
   public Iterator<Entry<K, V>> iterator() {
      return new EntryIterator();
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
   public boolean add( Entry<K, V> e ) {
      throw new UnsupportedOperationException();
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
         if( !contains(o) ) {
            return false;
         }
      }
      return true;
   }

   @Override
   public boolean addAll( Collection<? extends Entry<K, V>> c ) {
      throw new UnsupportedOperationException();
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

   class EntryIterator implements Iterator<Map.Entry<K, V>> {
      
      class Entry implements Map.Entry<K, V> {
         
         final IntObjectEntry<V> _entry;
         
         Entry( final IntObjectEntry<V> entry ) {
            _entry = entry;
         }

         @Override
         public K getKey() {
            return _indexer.getObject( _entry.getKey() );
         }

         @Override
         public V getValue() {
            return _entry.getValue();
         }

         @Override
         public V setValue( V value ) {
            final V old = _entry.getValue();
            _entry.setValue( value );
            return old;
         }
         
      }
      
      final Iterator<IntObjectEntry<V>> _iterator;
      
      EntryIterator() {
         _iterator = _map.iterator();
      }
      
      @Override
      public boolean hasNext() {
         return _iterator.hasNext();
      }

      @Override
      public Map.Entry<K, V> next() {
         final IntObjectEntry<V> entry = _iterator.next();
         return new Entry( entry );
      }

      @Override
      public void remove() {
         _iterator.remove();
      }
      
   }
}
