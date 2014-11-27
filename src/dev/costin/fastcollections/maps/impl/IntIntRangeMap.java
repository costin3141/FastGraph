package dev.costin.fastcollections.maps.impl;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.maps.IntIntMap;
import dev.costin.fastcollections.tools.FastCollections;


public class IntIntRangeMap implements IntIntMap {
   
   protected static class KeyIterator implements dev.costin.fastcollections.IntIterator {

      private final IntIntRangeMap _map;

      private final Object[]       _list;

      private int               _next;

      private IntIntEntryImpl   _last;

      private int               _modCounter;

      KeyIterator( final IntIntRangeMap map ) {
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

         return ( _last = (IntIntEntryImpl)_list[_next++] ).getKey();
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
   
   private static class EntryIterator implements Iterator<IntIntEntry> {
      private final IntIntRangeMap _map;

      private final IntIntEntryImpl[]       _list;

      private int               _next;

      private IntIntEntryImpl   _lastEntry;

      private int               _modCounter;

      EntryIterator( final IntIntRangeMap map ) {
         _map = map;
         _list = _map._entryList;
         _next = 0;
         _modCounter = _map._modCounter;
      }

      @Override
      public IntIntEntry next() {
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
   
   private static class IntIntEntryImpl implements IntIntEntry {
      private final int _key;
      private int _val;
      
      int _ref;
      
      IntIntEntryImpl( int key, int value, int ref ) {
         _key = key;
         _val = value;
         _ref = ref;
      }

      @Override
      public int getKey() {
         return _key;
      }

      @Override
      public int getValue() {
         return _val;
      }

      @Override
      public void setValue( int value ) {
         _val = value;
      }
      
   }
   
   private final IntIntEntryImpl[] _keySet;
   private IntIntEntryImpl[]       _entryList;
   private int         _size;
   private final int   _offset;
   protected int       _modCounter = 0;

   public IntIntRangeMap( final int n ) {
      this( 0, n - 1 );
   }

   public IntIntRangeMap( final int from, final int to ) {
      this( from, to, Math.min( to - from + 1, FastCollections.DEFAULT_LIST_CAPACITY ) );
   }

   public IntIntRangeMap( final int from, final int to, final int listCapacity ) {
      _offset = from;
      _keySet = new IntIntEntryImpl[to - from + 1];
      _entryList = new IntIntEntryImpl[listCapacity];
      _size = 0;
   }

   @Override
   public boolean contains( int key ) {
      return _keySet[key - _offset] !=null;
   }

   @Override
   public boolean put( int key, int value ) {
      final int k = key - _offset;
      final IntIntEntryImpl entry = ((IntIntEntryImpl)_keySet[k]);
      
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
      final IntIntEntryImpl entry = _keySet[k];
      
      if( entry == null || entry._ref < 0 ) {
         return false;
      }
      
      return remove( entry );
   }
   
   protected boolean remove( final IntIntEntryImpl entry ) {
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
   public int get( int key ) {
      try {
         final IntIntEntryImpl entry = _keySet[key - _offset];
         return entry.getValue();
      }
      catch( ArrayIndexOutOfBoundsException | NullPointerException e ) {
         // TODO: java-doc for this different behavior!
         throw new NoSuchElementException("Key "+key+" not found.");
      }
   }

   @Override
   public int size() {
      return _size;
   }

   @Override
   public IntIterator keyIterator() {
      return new KeyIterator( this );
   }
   
   @Override
   public Iterator<IntIntEntry> iterator() {
      return new EntryIterator( this );
   }
   
   @Override
   public void clear() {
      for( int i = 0; i < _size; i++ ) {
         _entryList[i]._ref = -1;
      }
      _size = 0;
      ++_modCounter;
   }

   private IntIntEntryImpl addToList( final int key, final int value ) {
      if( _size == _entryList.length ) {
         _entryList = Arrays.copyOf( _entryList, Math.max( _keySet.length, _size + ( _size >> 1 ) + 1 ) );
      }
      final IntIntEntryImpl entry = new IntIntEntryImpl( key, value, _size );
      _entryList[_size++] = entry;
      return entry;
   }

   private void addToList( final IntIntEntryImpl entry, final int value ) {
      if( _size == _entryList.length ) {
         _entryList = Arrays.copyOf( _entryList, Math.max( _keySet.length, _size + ( _size >> 1 ) + 1 ) );
      }
      entry._ref = _size;
      entry._val = value;
      _entryList[_size++] = entry;
   }

}
