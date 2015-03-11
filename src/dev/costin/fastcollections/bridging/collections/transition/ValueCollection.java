package dev.costin.fastcollections.bridging.collections.transition;

import java.util.Collection;
import java.util.Iterator;

import dev.costin.fastcollections.bridging.IndexedObjectBridge;
import dev.costin.fastcollections.maps.IntObjectMap;
import dev.costin.fastcollections.maps.IntObjectMap.IntObjectEntry;

public class ValueCollection<K,V> implements Collection<V> {
   
   private final IntObjectMap<V> _map;
   private final IndexedObjectBridge<K> _indexer;
   
   public ValueCollection( final IntObjectMap<V> intMap, final IndexedObjectBridge<K> indexer ) {
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
      if( o==null ) {
         for( IntObjectEntry<V> entry : _map ) {
            if( entry.getValue() == null ) {
               return true;
            }
         }
      }
      else {
         for( IntObjectEntry<V> entry : _map ) {
            if( o.equals( entry.getValue() ) ) {
               return true;
            }
         }
      }
      return false;
   }

   @Override
   public Iterator<V> iterator() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Object[] toArray() {
      final Object[] array = new Object[size()];
      int i=0;
      for( IntObjectEntry<V> entry : _map ) {
         array[i] = entry.getValue();
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
      for( IntObjectEntry<V> entry : _map ) {
         array[i] = (T)entry.getValue();
      }
      return array;
   }

   @Override
   public boolean add( V e ) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean remove( Object o ) {
      if( o==null ) {
         for( final IntObjectEntry<V> entry : _map ) {
            if( entry.getValue() == null ) {
               _map.remove( entry.getKey() );
               return true;
            }
         }
      }
      else {
         for( final IntObjectEntry<V> entry : _map ) {
            if( o.equals( entry.getValue() ) ) {
               _map.remove( entry.getKey() );
               return true;
            }
         }
      }
      
      return false;
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
   public boolean addAll( Collection<? extends V> c ) {
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

}
