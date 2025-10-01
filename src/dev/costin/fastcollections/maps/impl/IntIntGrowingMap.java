package dev.costin.fastcollections.maps.impl;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.maps.IntIntMap;
import dev.costin.fastcollections.tools.FastCollections;
import dev.costin.fastcollections.tools.MemoryUtils;


public class IntIntGrowingMap implements IntIntMap {
   
   protected static class KeyIterator implements dev.costin.fastcollections.IntIterator {

      private final IntIntGrowingMap _map;

      private final Object[]       _list;

      private int               _next;

      private IntIntEntryImpl   _last;

      private int               _modCounter;
      private int               _lastRemoved;

      KeyIterator( final IntIntGrowingMap map ) {
         _map = map;
         _list = _map._entryList;
         _next = 0;
         _modCounter = _map._modCounter;
         _lastRemoved = -1;
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
         if( _lastRemoved >= _next ) {
            throw new NoSuchElementException();
         }
         // it is important to use the remove method of the set
         // to ensure that subclass of the set are still able to use
         // this iterator!
         _lastRemoved = --_next;
         _map.remove( _last );
         ++_modCounter;
      }

   }
   
   private static class EntryIterator implements Iterator<IntIntEntry> {
      private final IntIntGrowingMap _map;

      private final IntIntEntryImpl[]       _list;

      private int               _next;

      private IntIntEntryImpl   _lastEntry;

      private int               _modCounter;
      private int               _lastRemoved;

      EntryIterator( final IntIntGrowingMap map ) {
         _map = map;
         _list = _map._entryList;
         _next = 0;
         _modCounter = _map._modCounter;
         _lastRemoved = -1;
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
         if( _lastRemoved >= _next ) {
            throw new NoSuchElementException();
         }
         // it is important to use the remove method of the set
         // to ensure that subclass of the set are still able to use
         // this iterator!
         _lastRemoved = --_next;
         _map.remove( _lastEntry );
         ++_modCounter;
      }
   }
   
   private static class IntIntEntryImpl implements IntIntEntry {
      private final int _key;
      private int _val;
      
      int _ref;
      
      IntIntEntryImpl( final int key, final int value, final int ref ) {
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
      public void setValue( final int value ) {
         _val = value;
      }
      
      @Override
      public int hashCode() {
         return getKey() ^ getValue();
      }
   }
   
   private static final IntIntEntryImpl[] EMPTY = {};
   
   private IntIntEntryImpl[]     _keySet;
   private IntIntEntryImpl[]     _entryList;
   private int         _size;
   private int         _offset;
   protected int       _modCounter = 0;

   public IntIntGrowingMap() {
      _keySet = EMPTY;
      _entryList = EMPTY;
      _size = 0;
      _offset = 0;
   }
   
   public IntIntGrowingMap( final IntIntMap map ) {
      this();
      
      if( !map.isEmpty() ) {
         if( map instanceof IntIntGrowingMap ) {
            final IntIntGrowingMap gmap = (IntIntGrowingMap) map;
            thisInit( gmap );
         }
         else {
            final int firstInt = map.keyIterator().nextInt();
            init( firstInt, firstInt +FastCollections.DEFAULT_LIST_CAPACITY-1, Math.max( map.size(), FastCollections.DEFAULT_LIST_CAPACITY ) );
         }
         
         for( IntIntEntry entry : map ) {
            put( entry.getKey(), entry.getValue() );
         }
      }
   }

   public IntIntGrowingMap( final int n ) {
      this( 0, n - 1 );
   }

   public IntIntGrowingMap( final int from, final int to ) {
      this( from, to, Math.min( to - from + 1, FastCollections.DEFAULT_LIST_CAPACITY ) );
   }

   public IntIntGrowingMap( final int from, final int to, final int listCapacity ) {
      init( from, to, listCapacity );
   }
   
   private void init( final int from, final int to, final int listCapacity ) {
      _offset = from;
      final int mapCapacity = to - from + 1;
      _keySet = mapCapacity == 0 ? EMPTY : new IntIntEntryImpl[mapCapacity];
      _entryList = listCapacity == 0 ? EMPTY : new IntIntEntryImpl[listCapacity];
      _size = 0;
   }
   
   private void thisInit( final IntIntGrowingMap map ) {
      final int mapOffset = map._offset;
      
      if( map.containsKey( mapOffset ) ) {
         final int lastKey = mapOffset + map.size() -1;

         if( map.containsKey( lastKey ) ) {
            init( mapOffset, lastKey, map.size() );
            return;
         }
         
         final int maxKey = mapOffset + map._keySet.length - 1;
         
         if( map.containsKey( maxKey ) ) {
            init( mapOffset, maxKey, map.size() );
            return;
         }
      }
      
      int min, max;
      final IntIntEntryImpl[] mapEntries = map._entryList;
      min = max = mapEntries[0].getKey();
      
      for( int i=1; i < map.size(); i++ ) {
         final int key = mapEntries[i].getKey();

         if( key < min ) {
            min = key;
         }
         else if( key > max ) {
            max = key;
         }
      }

      init( (min + mapOffset + 1) >> 1, (max + mapOffset + map._keySet.length) >> 1, map.size() );
   }

   @Override
   public boolean containsKey( final int key ) {
      if( key >= _offset ) {
         final int k = key - _offset;
         
         if( k<_keySet.length ) {
            final IntIntEntryImpl entry = _keySet[k];
            return entry != null && entry._ref >= 0;
         }
      }
      
      return false;
   }

   @Override
   public boolean put( final int key, final int value ) {
      ensureRangeFor( key );
      
      int k = key - _offset;
      
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
   public boolean remove( final int key ) {
      if( key >= _offset ) {
         final int k = key - _offset;
         
         if( k < _keySet.length ) {
            final IntIntEntryImpl entry = _keySet[k];
            
            if( entry != null && entry._ref >= 0 ) {
               return remove( entry );
            }
         }
      }
      
      return false;
   }
   
   protected boolean remove( final IntIntEntryImpl entry ) {
      final int ref = entry._ref;
      assert( ref >= 0 );
      
      if( ref != --_size ) {
         (_entryList[ref] = _entryList[_size])._ref = ref;
      }
      entry._ref = -1;  // deleted

      ++_modCounter;

      return true;
   }

   @Override
   public int get( int key ) {
      if( key >= _offset ) {
         final int k = key - _offset;
         
         if( k < _keySet.length ) {
            final IntIntEntryImpl entry = _keySet[k];
            if( entry != null && entry._ref >= 0 ) {
               return entry.getValue();
            }
         }
      }
      // TODO: java-doc for this different behavior!
      throw new NoSuchElementException("Key "+key+" not found.");
   }
   
   @Override
   public int getOrDefault( int key, int defaultValue ) {
      if( key >= _offset ) {
         final int k = key - _offset;
         
         if( k < _keySet.length ) {
            final IntIntEntryImpl entry = _keySet[k];
            if( entry != null && entry._ref >= 0 ) {
               return entry.getValue();
            }
         }
      }
      
      return defaultValue;
   }

   @Override
   public int size() {
      return _size;
   }
   
   @Override
   public boolean isEmpty() {
      return _size == 0;
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
         if(  _entryList[i] != null ) {
            _entryList[i]._ref = -1;
         }
      }
      _size = 0;
      ++_modCounter;
   }
   
   @Override
   public boolean equals( final Object obj ) {
      if( this == obj ) {
         return true;
      }
      
      final int currentModCounter = _modCounter;
      
      if( !( obj instanceof IntIntMap ) ) {
         return false;
      }
      
      final IntIntMap map = (IntIntMap) obj;
      
      if( map.size() != size() ) {
         return false;
      }
      
      boolean ret = true;
      
      for( IntIntEntry entry : map ) {
         if( ! containsKey( entry.getKey() ) || entry.getValue() != _keySet[ entry.getKey() - _offset ].getValue() ) {
            ret = false;
            break;
         }
      }
      
      if( currentModCounter != _modCounter ) {
         throw new ConcurrentModificationException();
      }
      
      return ret;
   }
   
   @Override
   public int hashCode() {
      int h = 0;
      for( IntIntEntry entry : this ) {
          h += entry.hashCode();
      }
      return h;
   }

   private IntIntEntryImpl addToList( final int key, final int value ) {
      ensureListCapacity( _size + 1 );
      
      final IntIntEntryImpl entry = new IntIntEntryImpl( key, value, _size );
      _entryList[_size++] = entry;
      return entry;
   }

   private void addToList( final IntIntEntryImpl entry, final int value ) {
      ensureListCapacity( _size + 1 );
      
      entry._ref = _size;
      entry._val = value;
      _entryList[_size++] = entry;
   }
   
   private void ensureRangeFor( final int key ) {
      if( _keySet == EMPTY ) {
         _keySet = new IntIntEntryImpl[ FastCollections.DEFAULT_LIST_CAPACITY ];
         final int half = FastCollections.DEFAULT_LIST_CAPACITY >>> 1;
         
         if( key < Integer.MIN_VALUE + half ) {
            _offset = Integer.MIN_VALUE;
         }
         else if( key > Integer.MAX_VALUE - half ) {
            _offset = Integer.MAX_VALUE - FastCollections.DEFAULT_LIST_CAPACITY + 1;
         }
         else {
            _offset = key - half;
         }
      }
      else {
         final int v = key - _offset;
         if( v < 0 ) {
            growNegative(
                     MemoryUtils.capacity( _keySet.length - v, _keySet.length )
                  - _keySet.length );
         }
         else if( v >= _keySet.length ) {
            growPositive(
                     MemoryUtils.capacity( v + 1, _keySet.length )
                  - _keySet.length );
         }
      }
   }
   
   private void growNegative( int count ) {
      assert count > 0;
      
      int n = count;
      
      if( _offset < Integer.MIN_VALUE + count ) {
         n = _offset - Integer.MIN_VALUE;
      }
      
      final IntIntEntryImpl[] _newSet = new IntIntEntryImpl[ _keySet.length + n ];
      System.arraycopy( _keySet, 0, _newSet, n, _keySet.length );
      _keySet = _newSet;
      _offset -= n;
   }
   
   private void growPositive( int count ) {
      assert count > 0;
      
      int c = count;
      int n = 0;
      
      final int maxValidOffset = Integer.MAX_VALUE - _keySet.length - count + 1;
      
      if( _offset > maxValidOffset ) {
         n = _offset - maxValidOffset;
         c = n;
      }
      
      final IntIntEntryImpl[] _newSet = new IntIntEntryImpl[ _keySet.length + c ];
      System.arraycopy( _keySet, 0, _newSet, n, _keySet.length );
      _keySet = _newSet;
      _offset -= n;
   }
   
   private void ensureListCapacity( final int minCapacity ) {
      if( minCapacity < 0 ) { // overflow
         throw new OutOfMemoryError();
      }
      if( _entryList == EMPTY ) {
         _entryList = new IntIntEntryImpl[ Math.max( minCapacity, FastCollections.DEFAULT_LIST_CAPACITY ) ];
      }
      else if( minCapacity > _entryList.length ) {
         final int maxDelta = _keySet.length - _entryList.length;

         int growDelta = 1 + ( _entryList.length >> 1 );
         if( growDelta > maxDelta ) {
            growDelta = maxDelta;
         }
         // minCapacity can never be > _set.length !
         else if( minCapacity - _entryList.length > growDelta ) {
            growDelta = minCapacity - _entryList.length;
         }
         
         _entryList = Arrays.copyOf( _entryList, _entryList.length + growDelta );
      }
   }
}
