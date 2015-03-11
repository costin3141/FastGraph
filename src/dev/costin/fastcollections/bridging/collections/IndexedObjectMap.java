package dev.costin.fastcollections.bridging.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import dev.costin.fastcollections.bridging.IndexedObjectBridge;
import dev.costin.fastcollections.bridging.collections.transition.EntrySet;
import dev.costin.fastcollections.bridging.collections.transition.KeySet;
import dev.costin.fastcollections.maps.IntObjectMap;
import dev.costin.fastcollections.maps.IntObjectMap.IntObjectEntry;
import dev.costin.fastcollections.maps.impl.IntObjectRangeMap;

public class IndexedObjectMap<K, V> implements Map<K, V> {

   private final IndexedObjectBridge<K> _indexer;
   private final IntObjectMap<V> _map;
   
   public IndexedObjectMap( IndexedObjectBridge<K> indexer ) {
      _indexer = indexer;
      _map = new IntObjectRangeMap<V>( _indexer.getMinIndex(), _indexer.getMaxIndex(), _indexer.getMaxIndex() - _indexer.getMinIndex() + 1 );
   }

   @Override
   public int size() {
      return _map.size();
   }

   @Override
   public boolean isEmpty() {
      return _map.size()==0;
   }

   @Override
   public boolean containsKey( Object key ) {
      return _map.containsKey( _indexer.getIndex( (K) key ) );
   }

   @Override
   public boolean containsValue( Object value ) {
      if( value == null ) {
         for( IntObjectEntry<V> entry : _map ) {
            if( entry.getValue()==null ) {
               return true;
            }
         }
      }
      else {
         for( IntObjectEntry<V> entry : _map ) {
            if( value.equals( entry.getValue() ) ) {
               return true;
            }
         }
      }
      
      return false;
   }

   @Override
   public V get( Object key ) {
      return _map.get( _indexer.getIndex( (K) key ) );
   }

   @Override
   public V put( K key, V value ) {
      return _map.put( _indexer.getIndex( key ), value );
   }

   @Override
   public V remove( Object key ) {
      return _map.remove( _indexer.getIndex( (K) key ) );
   }

   @Override
   public void putAll( Map<? extends K, ? extends V> m ) {
      for( Map.Entry<? extends K, ? extends V> entry : m.entrySet() ) {
         _map.put( _indexer.getIndex( entry.getKey() ), entry.getValue() );
      }
   }

   @Override
   public void clear() {
      _map.clear();
   }

   @Override
   public Set<K> keySet() {
      return new KeySet<K, V>( _map, _indexer );
   }

   @Override
   public Collection<V> values() {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * While using the entry set for java maps is usually an efficient way to
    * work on the entries this is <strong>NOT</strong> the case for {@link IndexedObjectMap}
    * because every {@link IntObjectEntry} of the underlying int-based map must be wrapped
    * with an implementation of {@link Entry}.
    */
   @Override
   public Set<java.util.Map.Entry<K, V>> entrySet() {
      return new EntrySet<K, V>( _map, _indexer );
   }
}
