package dev.costin.fastcollections.sets.impl;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import dev.costin.fastcollections.IntCollection;
import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.sets.IntSet;
import dev.costin.fastcollections.tools.FastCollections;

public class IntGrowingSet implements IntSet {

   private static final int[] EMPTY = {};
   
   private int[]       _set;
   private int[]       _list;
   private int         _size;
   private int         _offset;
   protected int       _modCounter = 0;

   protected static class IntCursorIterator implements Iterator<IntCursor>, IntCursor {

      private final IntGrowingSet _set;
      private final int[]       _list;
      private int               _next;
      private int               _value;
      private int               _modCounter;
      private int               _lastRemoved;

      IntCursorIterator( final IntGrowingSet set ) {
         _set = set;
         _list = _set._list;
         _next = 0;
         _value = 0;
         _modCounter = _set._modCounter;
         _lastRemoved = -1;
      }

      @Override
      public boolean hasNext() {
         return _next < _set.size();
      }

      @Override
      public IntCursor next() {
         if( _modCounter != _set._modCounter ) {
            throw new ConcurrentModificationException();
         }

         _value = _list[_next++];
         return this;
      }

      @Override
      public void remove() {
         assert _set.contains( _value ) : "Element has already been removed!";

         if( _modCounter != _set._modCounter ) {
            throw new ConcurrentModificationException();
         }
         if( _lastRemoved >= _next ) {
            throw new NoSuchElementException();
         }
         // it is important to use the remove method of the set
         // to ensure that subclass of the set are still able to use
         // this iterator!
         _lastRemoved = --_next;
         _set.remove( _value );
         ++_modCounter;
      }

      @Override
      public int value() {
         return _value;
      }

   }

   protected static class IntIterator implements dev.costin.fastcollections.IntIterator {

      private final IntGrowingSet _set;

      private final int[]       _list;

      private int               _next;

      private int               _lastValue;

      private int               _modCounter;
      private int               _lastRemoved;

      IntIterator( final IntGrowingSet set ) {
         _set = set;
         _list = _set._list;
         _next = 0;
         _modCounter = _set._modCounter;
         _lastRemoved = -1;
      }

      @Override
      public int nextInt() {
         if( _modCounter != _set._modCounter ) {
            throw new ConcurrentModificationException();
         }

         return _lastValue = _list[_next++];
      }

      @Override
      public boolean hasNext() {
         return _next < _set.size();
      }

      @Override
      public void remove() {
         assert _set.contains( _lastValue ) : "Element has already been removed!";

         if( _modCounter != _set._modCounter ) {
            throw new ConcurrentModificationException();
         }
         if( _lastRemoved >= _next ) {
            throw new NoSuchElementException();
         }
         // it is important to use the remove method of the set
         // to ensure that subclass of the set are still able to use
         // this iterator!
         _lastRemoved = --_next;
         _set.remove( _lastValue );
         ++_modCounter;
      }

   }

   public IntGrowingSet() {
      _offset = 0;
      _set = EMPTY;
      _list = EMPTY;
      _size = 0;
   }
   
   public IntGrowingSet( final IntSet set ) {
      if( set instanceof IntGrowingSet && set.size() > 0 ) {
         final IntGrowingSet gset = (IntGrowingSet) set;
         thisInit( gset );
         
         for( int i=0; i < gset.size(); i++ ) {
            add( gset.get( i ) );
         }
      }
      else {
         init( 0, FastCollections.DEFAULT_LIST_CAPACITY-1, Math.max( set.size(), FastCollections.DEFAULT_LIST_CAPACITY ) );
         
         addAll( set );
      }
   }

   public IntGrowingSet( final int n ) {
      this( 0, n - 1 );
   }

   public IntGrowingSet( final int from, final int to ) {
      this( from, to, FastCollections.DEFAULT_LIST_CAPACITY );
   }

   public IntGrowingSet( final int from, final int to, final int listCapacity ) {
      init( from, to, listCapacity );
   }
   
   private void thisInit( final IntGrowingSet set ) {
      final int setOffset = set._offset;
      
      if( set.contains( setOffset ) ) {
         final int lastKey = setOffset + set.size() -1;

         if( set.contains( lastKey ) ) {
            init( setOffset, lastKey, set.size() );
            return;
         }
         
         final int maxKey = setOffset + set._set.length - 1;
         
         if( set.contains( maxKey ) ) {
            init( setOffset, maxKey, set.size() );
            return;
         }
      }
      
      int min, max;
      min = max = set.get( 0 );
      
      for( int i=1; i < set.size(); i++ ) {
         final int key = set.get( i );

         if( key < min ) {
            min = key;
         }
         else if( key > max ) {
            max = key;
         }
      }

      init( (min + setOffset + 1) >> 1, (max + setOffset + set._set.length) >> 1, set.size() );
   }
   
   private void init( final int from, final int to, final int listCapacity ) {
      _offset = from;
      final int length = to - from + 1;
      _set = new int[length];
      _list = new int[Math.min( length, listCapacity )];
      _size = 0;
   }

   @Override
   public Iterator<IntCursor> iterator() {
      return new IntCursorIterator( this );
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
   public boolean contains( final int value ) {
      final int idx = value - _offset;
      return idx >= 0 && idx < _set.length && _set[idx] > 0;
   }
   
   @Override
   public boolean containsAll( IntCollection c ) {
      if( c instanceof IntSet && c.size() == size() ) {
         
         for( final IntCursor cursor : c ) {
            if( ! contains( cursor.value() ) ) {
               return false;
            }
         }
         
         return true;
      }
      
      return false;
   }

   @Override
   public boolean add( int value ) {
      ensureRangeFor( value );
      
      final int v = value - _offset;

      if( _set[v] > 0 ) {
         return false;
      }
      _set[v] = addToList( value );
      ++_modCounter;

      return true;
   }

   private int addToList( int value ) {
      ensureListCapacity( _size + 1 );
      
      _list[_size++] = value;
      return _size;
   }

   @Override
   public boolean remove( int value ) {
      if( value >= _offset ) {
         final int v = value - _offset;
         
         if( v < _set.length ) {
            final int ref = _set[v];
            
            if( ref > 0 ) {
               _set[v] = 0;
               if( ref != _size-- ) { // Careful: the decrement must be postponed!
                  final int other = _list[_size];
                  _list[ref - 1] = other;
                  _set[other] = ref;
               }
      
               ++_modCounter;
      
               return true;
            }
         }
      }
      
      return false;
   }

   @Override
   public IntIterator intIterator() {
      return new IntIterator( this );
   }

   @Override
   public void clear() {
      for( int i = 0; i < _size; i++ ) {
         _set[_list[i] - _offset] = 0;
      }
      _size = 0;
      ++_modCounter;
   }

   @Override
   public boolean addAll( IntCollection elements ) {
      for( dev.costin.fastcollections.IntIterator iter = elements.intIterator(); iter.hasNext(); ) {
         add( iter.nextInt() );
      }
      return true;
   }

   @Override
   public boolean addAll( int... elements ) {
      for( int i = 0; i < elements.length; i++ ) {
         add( elements[i] );
      }
      return true;
   }
   
   @Override
   public boolean equals( final Object o ) {
      if( o instanceof IntSet ) {
         final IntSet set = (IntSet) o;
         return set == this || size() == set.size() && set.containsAll( set );
      }
      return false;
   }

   public int get( int i ) {
      if( i >= _size ) {
         throw new IndexOutOfBoundsException();
      }
      return _list[i];
   }
   
   
   private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
   
   private void ensureRangeFor( final int value ) {
      if( _set == EMPTY ) {
         _set = new int[ FastCollections.DEFAULT_LIST_CAPACITY ];
         _offset = value - ( FastCollections.DEFAULT_LIST_CAPACITY >> 1 );
      }
      else {
         final int v = value - _offset;
         if( v < 0 ) {
            grow( capacity( _set.length - v ) - _set.length, 0 );
         }
         else if( v >= _set.length ) {
            grow( 0, capacity( v + 1 ) - _set.length );
         }
      }
   }
   
   private int capacity( final int minCapacity ) {
      if( minCapacity < 0 ) { // overflow
         throw new OutOfMemoryError();
      }
      final int oldCapacity = _set.length;
      int newCapacity = oldCapacity + (oldCapacity >> 2);
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
   
   private void grow( final int toNeg, final int toPos ) {
      assert toNeg >= 0 && toPos >= 0;
      assert toNeg > 0 || toPos > 0;
      
      final int[] _newSet = new int[ _set.length + toNeg + toPos ];
      System.arraycopy( _set, 0, _newSet, toNeg, _set.length );
      _set = _newSet;
      _offset -= toNeg;
   }
   
   private void ensureListCapacity( final int minCapacity ) {
      if( minCapacity < 0 ) { // overflow
         throw new OutOfMemoryError();
      }
      if( _list == EMPTY ) {
         _list = new int[ Math.max( minCapacity, FastCollections.DEFAULT_LIST_CAPACITY ) ];
      }
      else if( minCapacity > _list.length ) {
         final int maxDelta = _set.length - _list.length;
         if( maxDelta <= 0 ) {
            throw new OutOfMemoryError();
         }
         int growDelta = 1 + _list.length >> 1;
         if( growDelta > maxDelta ) {
            growDelta = maxDelta;
         }
      
         _list = Arrays.copyOf( _list, _list.length + growDelta );
      }
   }
}
