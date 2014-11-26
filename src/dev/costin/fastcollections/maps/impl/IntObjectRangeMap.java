package dev.costin.fastcollections.maps.impl;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.maps.IntObjectMap;
import dev.costin.fastcollections.tools.FastCollections;

public class IntObjectRangeMap<V> implements IntObjectMap<V> {

   protected static class KeyIterator<V> implements dev.costin.fastcollections.IntIterator {

      private final IntObjectRangeMap<V> _map;

      private final IntObjectEntryImpl<V>[]       _list;

      private int                   _next;

      private IntObjectEntryImpl<V> _last;

      private int                   _modCounter;

      KeyIterator( final IntObjectRangeMap<V> map ) {
         _map = map;
         _list = _map._entryList;
         _next = 0;
         _modCounter = _map._modCounter;
      }

      @Override
      public int nextInt() {
         if( _modCounter != _map._modCounter ) {
            throw new ConcurrentModificationException();
         }

         return (_last = (IntObjectEntryImpl<V>)_list[_next++] ).getKey();
      }

      @Override
      public boolean hasNext() {
         return _next < _map.size();
      }

      @Override
      public void remove() {
         if( _modCounter != _map._modCounter ) {
            throw new ConcurrentModificationException();
         }
         // it is important to use the remove method of the set
         // to ensure that subclass of the set are still able to use
         // this iterator!

         if( _map.remove( _last ) ) {
            ++_modCounter;
            --_next;
         }
      }

   }
   
   private static class EntryIterator<V> implements Iterator<IntObjectEntry<V>> {
      private final IntObjectRangeMap<V> _map;

      private final IntObjectEntryImpl<V>[]       _list;

      private int               _next;

      private IntObjectEntryImpl<V>   _lastEntry;

      private int               _modCounter;

      EntryIterator( final IntObjectRangeMap<V> map ) {
         _map = map;
         _list = _map._entryList;
         _next = 0;
         _modCounter = _map._modCounter;
      }

      @Override
      public IntObjectEntry<V> next() {
         if( _modCounter != _map._modCounter ) {
            throw new ConcurrentModificationException();
         }

         return _lastEntry = _list[_next++];
      }

      @Override
      public boolean hasNext() {
         return _next < _map.size();
      }

      @Override
      public void remove() {
         if( _modCounter != _map._modCounter ) {
            throw new ConcurrentModificationException();
         }
         // it is important to use the remove method of the set
         // to ensure that subclass of the set are still able to use
         // this iterator!

         if( _map.remove( _lastEntry ) ) {
            ++_modCounter;
            --_next;
         }
      }
   }
   
   private static class IntObjectEntryImpl<V> implements IntObjectEntry<V> {
      private final int _key;
      private V _val;
      
      int _ref;
      
      IntObjectEntryImpl( int key, V value, int ref ) {
         _key = key;
         _val = value;
         _ref = ref;
      }

      @Override
      public int getKey() {
         return _key;
      }

      @Override
      public V getValue() {
         return _val;
      }

      @Override
      public void setValue( V value ) {
         _val = value;
      }
      
   }
   
   private final IntObjectEntryImpl<V>[] _keySet;
   private IntObjectEntryImpl<V>[]       _entryList;
   private int         _size;
   private final int   _offset;
   protected int       _modCounter = 0;

   public IntObjectRangeMap( final int n ) {
      this( 0, n - 1 );
   }

   public IntObjectRangeMap( final int from, final int to ) {
      this( from, to, Math.min( to - from + 1, FastCollections.DEFAULT_LIST_CAPACITY ) );
   }

   @SuppressWarnings( "unchecked" )
   public IntObjectRangeMap( final int from, final int to, final int listCapacity ) {
      _offset = from;
      _keySet = new IntObjectEntryImpl[to - from + 1];
      _entryList = new IntObjectEntryImpl[listCapacity];
      _size = 0;
   }

   @Override
   public boolean contains( int key ) {
      return _keySet[key - _offset] !=null;
   }

   @Override
   public boolean put( int key, V value ) {
      final int k = key - _offset;
      final IntObjectEntryImpl<V> entry = _keySet[k];
      
      if( entry == null ) {
         _keySet[k] = addToList( key, value );
         ++_modCounter;
         
         return true;
      }
      else if( entry._ref < 0 ) {
         addToList( entry, value );
         ++_modCounter;
         
         return true;
      }
      else {
         entry.setValue( value );
         return false;
      }
   }

   @Override
   public boolean remove( int key ) {
      final int k = key - _offset;
      final IntObjectEntryImpl<V> entry = _keySet[k];
      
      if( entry == null || entry._ref < 0 ) {
         return false;
      }
      
      return remove( entry );
   }
   
   protected boolean remove( final IntObjectEntryImpl<V> entry ) {
      final int ref = entry._ref;
      assert( ref > 0 );
      
      if( ref != --_size ) {
         (_entryList[ref] = _entryList[_size])._ref = ref;
      }
      entry._ref = -1;  // deleted

      ++_modCounter;

      return true;
   }

   @Override
   public V get( int key ) {
      final IntObjectEntryImpl<V> entry = _keySet[key - _offset];
      if( entry != null && entry._ref >= 0 ) {
         return entry.getValue();
      }
      
      return null;   // TODO: java-doc for this different behavior!
   }

   @Override
   public int size() {
      return _size;
   }

   @Override
   public IntIterator keyIterator() {
      return new KeyIterator<V>( this );
   }
   
   @Override
   public Iterator<IntObjectEntry<V>> iterator() {
      return new EntryIterator<V>( this );
   }
   
   @Override
   public void clear() {
      for( int i = 0; i < _size; i++ ) {
         _entryList[i]._ref = -1;
      }
      _size = 0;
      ++_modCounter;
   }

   private IntObjectEntryImpl<V> addToList( final int key, final V value ) {
      if( _size == _entryList.length ) {
         _entryList = Arrays.copyOf( _entryList, Math.max( _keySet.length, _size + ( _size >> 1 ) + 1 ) );
      }
      final IntObjectEntryImpl<V> entry = new IntObjectEntryImpl<V>( key, value, _size );
      _entryList[_size++] = entry;
      return entry;
   }

   private void addToList( final IntObjectEntryImpl<V> entry, final V value ) {
      if( _size == _entryList.length ) {
         _entryList = Arrays.copyOf( _entryList, Math.max( _keySet.length, _size + ( _size >> 1 ) + 1 ) );
      }
      entry._ref = _size;
      entry._val = value;
      _entryList[_size++] = entry;
   }
}
