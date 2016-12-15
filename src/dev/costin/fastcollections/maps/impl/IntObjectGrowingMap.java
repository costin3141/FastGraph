package dev.costin.fastcollections.maps.impl;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.maps.IntObjectMap;
import dev.costin.fastcollections.tools.FastCollections;

public class IntObjectGrowingMap<V> implements IntObjectMap<V> {

   protected static class KeyIterator<V> implements dev.costin.fastcollections.IntIterator {

      private final IntObjectGrowingMap<V> _map;

      private final IntObjectEntryImpl<V>[]       _list;

      private int                   _next;

      private IntObjectEntryImpl<V> _last;

      private transient int                   _modCounter;

      KeyIterator( final IntObjectGrowingMap<V> map ) {
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

         _map.remove( _last );
         ++_modCounter;
         --_next;
      }

   }
   
   private static class EntryIterator<V> implements Iterator<IntObjectEntry<V>> {
      private final IntObjectGrowingMap<V> _map;

      private final IntObjectEntryImpl<V>[]       _list;

      private int               _next;

      private IntObjectEntryImpl<V>   _lastEntry;

      private int               _modCounter;
      
      private int               _lastRemoved;

      EntryIterator( final IntObjectGrowingMap<V> map ) {
         _map = map;
         _list = _map._entryList;
         _next = 0;
         _modCounter = _map._modCounter;
         _lastRemoved = -1;
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
   
   private static class IntObjectEntryImpl<V> implements IntObjectEntry<V> {
      private final int _key;
      private V _val;
      
      int _ref;
      
      IntObjectEntryImpl( final int key, final V value, final int ref ) {
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
      
      @Override
      public int hashCode() {
         return _key;
      }
      
      @Override
      public boolean equals( Object obj ) {
         if( obj != null && hashCode() == obj.hashCode() ) {
            @SuppressWarnings( "unchecked" )
            final V objValue = ((IntObjectEntry<V>)obj).getValue();
            return _val == objValue || _val != null && _val.equals( obj );
         }
         
         return false;
      }
   }
   
   private static final IntObjectEntryImpl[] EMPTY = {};
   
   private IntObjectEntryImpl<V>[] _keySet;
   private IntObjectEntryImpl<V>[]       _entryList;
   private int         _size;
   private int   _offset;
   protected int       _modCounter = 0;

   @SuppressWarnings("unchecked")
   public IntObjectGrowingMap() {
      _keySet = EMPTY;
      _entryList = EMPTY;
      _size = 0;
      _offset = 0;
   }
   
   public IntObjectGrowingMap( final IntObjectMap<V> map ) {
      this();
      
      if( !map.isEmpty() ) { 
         if( map instanceof IntObjectGrowingMap ) {
            final IntObjectGrowingMap<V> gmap = (IntObjectGrowingMap<V>) map;
            thisInit( gmap );
         }
         else {
            init( 0, FastCollections.DEFAULT_LIST_CAPACITY-1, Math.max( map.size(), FastCollections.DEFAULT_LIST_CAPACITY ) );
         }
         
         for( IntObjectEntry<V> entry : map ) {
            put( entry.getKey(), entry.getValue() );
         }
      }
   }
   
   public IntObjectGrowingMap( final int n ) {
      this( 0, n - 1 );
   }

   public IntObjectGrowingMap( final int from, final int to ) {
      this( from, to, Math.min( to - from + 1, FastCollections.DEFAULT_LIST_CAPACITY ) );
   }

   public IntObjectGrowingMap( final int from, final int to, final int listCapacity ) {
      init( from, to, listCapacity );
   }
   
   @SuppressWarnings( "unchecked" )
   private void init( final int from, final int to, final int listCapacity ) {
      _offset = from;
      _keySet = new IntObjectEntryImpl[to - from + 1];
      _entryList = new IntObjectEntryImpl[listCapacity];
      _size = 0;
   }
   
   private void thisInit( final IntObjectGrowingMap<V> map ) {
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
      final IntObjectEntryImpl<V>[] mapEntries = map._entryList;
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
      final IntObjectEntryImpl<V> entry = getEntry( key );
      return entry != null && entry._ref >= 0;
   }

   @Override
   public V put( final int key, final V value ) {
      ensureRangeFor( key );
      
      int k = key - _offset;
      
      final IntObjectEntryImpl<V> entry = _keySet[k];
      
      if( entry == null ) {
         _keySet[k] = addToList( key, value );
         ++_modCounter;
         
         return null;
      }
      else if( entry._ref < 0 ) {
         addToList( entry, value );
         ++_modCounter;
         
         return null;
      }
      else {
         final V previuos = entry.getValue();
         entry.setValue( value );
         return previuos;
      }
   }

   @Override
   public V remove( final int key ) {
      final IntObjectEntryImpl<V> entry = getEntry( key );
      
      if( entry != null && entry._ref >= 0 ) {
         return remove( entry );
      }
      
      return null;
   }
   
   protected V remove( final IntObjectEntryImpl<V> entry ) {
      final int ref = entry._ref;
      assert( ref >= 0 );
      
      final V previous = entry._val;
      
      if( ref != --_size ) {
         (_entryList[ref] = _entryList[_size])._ref = ref;
      }
      entry._ref = -1;  // deleted
      entry._val = null; // for GC

      ++_modCounter;

      return previous;
   }

   @Override
   public V get( int key ) {
      final IntObjectEntryImpl<V> entry = getEntry( key );
      if( entry != null /*&& entry._ref >= 0*/ ) {
         // we can skip the entry._ref>=0 check because the values has
         // been set to NULL on removal.
         return entry.getValue();
      }
      return null;
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
         _entryList[i]._val = null; // for GC
      }
      _size = 0;
      ++_modCounter;
   }
   
   @Override
   public boolean equals( final Object o ) {
      if( ! (o instanceof IntObjectMap) ) {
         return false;
      }
      
      @SuppressWarnings( "rawtypes" )
      final IntObjectMap map = (IntObjectMap) o;
      
      if( this != map && map.size() == size() ) {
         for( final Object e : map ) {
            @SuppressWarnings( "rawtypes" )
            final IntObjectEntry entry = (IntObjectMap.IntObjectEntry) e; 
            if( ! containsKey( entry.getKey() ) ) {
               return false;
            }
            final V value = _keySet[ entry.getKey() - _offset ].getValue();
            if( value != entry.getValue() && value !=null && !value.equals( entry.getValue() )  ) {
               return false;
            }
         }
      }
      return true;
   }
   
   protected IntObjectEntryImpl<V> getEntry( final int key ) {
      if( key >= _offset ) {
         final int k = key - _offset;
         
         if( k < _keySet.length ) {
            return _keySet[k];
         }
      }
      return null;
   }

   private IntObjectEntryImpl<V> addToList( final int key, final V value ) {
      ensureListCapacity( _size + 1 );
      
      final IntObjectEntryImpl<V> entry = new IntObjectEntryImpl<V>( key, value, _size );
      _entryList[_size++] = entry;
      return entry;
   }

   private void addToList( final IntObjectEntryImpl<V> entry, final V value ) {
      ensureListCapacity( _size + 1 );
      
      entry._ref = _size;
      entry._val = value;
      _entryList[_size++] = entry;
   }
   
   @SuppressWarnings( "unchecked" )
   private void ensureRangeFor( final int key ) {
      if( _keySet == EMPTY ) {
         _keySet = new IntObjectEntryImpl[ FastCollections.DEFAULT_LIST_CAPACITY ];
         _offset = key - ( FastCollections.DEFAULT_LIST_CAPACITY >>> 1 );
      }
      else {
         final int v = key - _offset;
         if( v < 0 ) {
            growNegative(
                  capacity( _keySet.length - v + Math.max( _keySet.length >>> 1, -v ) )
                  - _keySet.length );
         }
         else if( v >= _keySet.length ) {
            growPositive(
                  capacity( v + 1 + Math.max( _keySet.length >>> 1, v + 1 - _keySet.length ) )
                  - _keySet.length );
         }
      }
   }
   
   private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
   
   private int capacity( final int minCapacity ) {
      if( minCapacity < 0 ) { // overflow
         throw new OutOfMemoryError();
      }
      final int oldCapacity = _keySet.length;
      int newCapacity = oldCapacity + (oldCapacity >>> 2);
      if( newCapacity - minCapacity < 0 ) {
         newCapacity = minCapacity;
      }
      if( newCapacity - MAX_ARRAY_SIZE > 0 ) {
         newCapacity = hugeCapacity(minCapacity);
      }
      
      return newCapacity;
   }
   
   private static int hugeCapacity(final int minCapacity) {
      if( minCapacity < 0 ) { // overflow
         throw new OutOfMemoryError();
      }
      return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
   }
   
   private void growNegative( int count ) {
      @SuppressWarnings( "unchecked" )
      final IntObjectEntryImpl<V>[] _newSet = new IntObjectEntryImpl[ _keySet.length + count ];
      System.arraycopy( _keySet, 0, _newSet, count, _keySet.length );
      _keySet = _newSet;
      _offset -= count;
   }
   
   private void growPositive( int count ) {
      _keySet = Arrays.copyOf( _keySet, _keySet.length + count );
   }
   
   @SuppressWarnings("unchecked")
   private void ensureListCapacity( final int minCapacity ) {
      if( minCapacity < 0 ) { // overflow
         throw new OutOfMemoryError();
      }
      if( _entryList == EMPTY ) {
         _entryList = new IntObjectEntryImpl[ Math.max( minCapacity, FastCollections.DEFAULT_LIST_CAPACITY ) ];
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
