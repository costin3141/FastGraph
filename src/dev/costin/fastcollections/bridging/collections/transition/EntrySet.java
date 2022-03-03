package dev.costin.fastcollections.bridging.collections.transition;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
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
   public Iterator<java.util.Map.Entry<K, V>> iterator() {
      return new EntryIterator();
   }

   @Override
   public Object[] toArray() {
      final Object[] array = new Object[size()];
      int i=0;
      for( IntObjectEntry<V> entry : _map ) {
         array[i++] = new Entry( entry );
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
         array[i++] = (T) new Entry( entry );
      }
      return array;
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
   public boolean add( java.util.Map.Entry<K, V> e ) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean addAll( Collection<? extends java.util.Map.Entry<K, V>> c ) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean retainAll( Collection<?> c ) {
      boolean changed = false;
      
      for( final Iterator<IntObjectEntry<V>> iter = _map.iterator(); iter.hasNext(); ) {
         final IntObjectEntry<V> entry = iter.next();
         
         if( !c.contains( new Entry(entry) ) ) {
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
         final java.util.Map.Entry<K,V> entry = (java.util.Map.Entry<K, V>) obj;
         final K key = entry.getKey();
         
         if( contains( key ) ) {
            final V value = _map.get( _indexer.getIndex( key ) );
            final V entryValue = entry.getValue();
            if( entryValue == value || value !=null && value.equals( entryValue ) ) {
               _map.remove( _indexer.getIndex( key ) );
               changed = true;
            }
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
          h += e.hashCode();
      }
      
      return h;
   }
   
   class Entry implements Map.Entry<K, V> {
      
      final IntObjectEntry<V> _entry;
      K _key;
      
      Entry( final IntObjectEntry<V> entry ) {
         _entry = entry;
      }

      @Override
      public K getKey() {
         if( _key == null ) {
            K k = _indexer.getObject( _entry.getKey() );
            _key = k;
         }
         return _key;
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
      
      @Override
      public int hashCode() {
         return _entry.hashCode();
      }
      
      @Override
      public boolean equals( Object obj ) {
         if( this == obj ) {
            return true;
         }
         
         if( obj instanceof EntrySet.Entry ) {
            @SuppressWarnings( "unchecked" )
            final Entry other = (Entry) obj;
            
            if( _entry.getKey() != other._entry.getKey() ) {
               return false;
            }
            
            final V value = getValue();
            final V otherValue = other.getValue();
            
            if( value == otherValue || value != null && value.equals( otherValue ) ) {
               return true;
            }
         }
         else if( obj != null) {
            @SuppressWarnings( "unchecked" )
            final Map.Entry<K, V> other = (java.util.Map.Entry<K, V>) obj;
            final K key = getKey();
            final K otherKey = other.getKey();
            
            if( key == otherKey || key != null && key.equals( otherKey ) ) {
               final V value = getValue();
               final V otherValue = other.getValue();
               
               if( value == otherValue || value != null && value.equals( otherValue ) ) {
                  return true;
               }
            }
         }
         
         return false;
      }
      
   }
   
   @Override
   public String toString() {
      final StringBuilder s = new StringBuilder('{');
      
      int i=0;
      for( java.util.Map.Entry<K, V> e : this ) {
         s.append( e.getKey() ).append( '=' ).append( e.getValue() );
         if( ++i < size() ) {
            s.append( ", " );
         }
      }
      
      s.append( '}' );
      
      return s.toString();
   }

   class EntryIterator implements Iterator<Map.Entry<K, V>> {
      
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
