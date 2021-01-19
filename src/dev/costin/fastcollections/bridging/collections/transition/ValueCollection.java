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
      return new ValueIterator();
   }

   @Override
   public Object[] toArray() {
      final Object[] array = new Object[size()];
      int i=0;
      for( IntObjectEntry<V> entry : _map ) {
         array[i++] = entry.getValue();
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
         array[i++] = (T)entry.getValue();
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
         for( final Iterator<IntObjectEntry<V>> iter = _map.iterator(); iter.hasNext(); ) {
            final IntObjectEntry<V> entry = iter.next();
            
            if( entry.getValue() == null ) {
               iter.remove();
               return true;
            }
         }
      }
      else {
         for( final Iterator<IntObjectEntry<V>> iter = _map.iterator(); iter.hasNext(); ) {
            final IntObjectEntry<V> entry = iter.next();
            
            if( o.equals( entry.getValue() ) ) {
               iter.remove();
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
      boolean changed = false;
      
      for( final Iterator<IntObjectEntry<V>> iter = _map.iterator(); iter.hasNext(); ) {
         final IntObjectEntry<V> entry = iter.next();
         final V entryValue = entry.getValue();
         
         if( !c.contains( entryValue ) ) {
            iter.remove();
            changed = true;
         }
      }

      return changed;
   }

   @Override
   public boolean removeAll( Collection<?> c ) {
      boolean changed = false;
      
      for( final Iterator<IntObjectEntry<V>> iter = _map.iterator(); iter.hasNext(); ) {
         final IntObjectEntry<V> entry = iter.next();
         
         if( c.contains( entry.getValue() ) ) {
            iter.remove();
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
   public String toString() {
      final StringBuilder s = new StringBuilder('{');
      
      int i=0;
      for( V e : this ) {
         s.append( e );
         if( ++i < size() ) {
            s.append( ", " );
         }
      }
      
      s.append( '}' );
      
      return s.toString();
   }

   private class ValueIterator implements Iterator<V> {
      
      final Iterator<IntObjectEntry<V>> _iterator;
      
      ValueIterator() {
         _iterator = _map.iterator();
      }

      @Override
      public boolean hasNext() {
         return _iterator.hasNext();
      }

      @Override
      public V next() {
         return _iterator.next().getValue();
      }

      @Override
      public void remove() {
         _iterator.remove();
      }
      
   }
}
