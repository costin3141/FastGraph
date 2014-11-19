package dev.costin.fastcollections.sets.impl;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import dev.costin.fastcollections.IntCollection;
import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.sets.IntSet;
import dev.costin.fastcollections.tools.FastCollections;

public class IntRangeSet implements IntSet {

   private final int[] _set;
   private int[]       _list;
   private int         _size;
   private final int   _offset;
   protected int       _modCounter = 0;

   protected static class IntCursorIterator implements Iterator<IntCursor>, IntCursor {

      private final IntRangeSet _set;
      private final int[]       _list;
      private int               _next;
      private int               _value;
      private int               _modCounter;

      IntCursorIterator( final IntRangeSet set ) {
         _set = set;
         _list = _set._list;
         _next = 0;
         _value = 0;
         _modCounter = _set._modCounter;
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
         // it is important to use the remove method of the set
         // to ensure that subclass of the set are still able to use
         // this iterator!
         if( _set.remove( _value ) ) {
            ++_modCounter;
            --_next;
         }
      }

      @Override
      public int value() {
         return _value;
      }

   }

   protected static class IntIterator implements dev.costin.fastcollections.IntIterator {

      private final IntRangeSet _set;

      private final int[]       _list;

      private int               _next;

      private int               _lastValue;

      private int               _modCounter;

      IntIterator( final IntRangeSet set ) {
         _set = set;
         _list = _set._list;
         _next = 0;
         _modCounter = _set._modCounter;
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
         // it is important to use the remove method of the set
         // to ensure that subclass of the set are still able to use
         // this iterator!

         if( _set.remove( _lastValue ) ) {
            ++_modCounter;
            --_next;
         }
      }

   }

   public IntRangeSet( final int n ) {
      this( 0, n - 1 );
   }

   public IntRangeSet( final int from, final int to ) {
      this( from, to, Math.min( to - from + 1, FastCollections.DEFAULT_LIST_CAPACITY ) );
   }

   public IntRangeSet( final int from, final int to, final int listCapacity ) {
      _offset = from;
      _set = new int[to - from + 1];
      _list = new int[listCapacity];
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
   public boolean contains( final int value ) {
      final int idx = value - _offset;
      return idx < _set.length && _set[idx] > 0;
   }

   @Override
   public boolean add( int value ) {
      final int v = value - _offset;

      if( _set[v] > 0 ) {
         return false;
      }
      _set[v] = addToList( value );
      ++_modCounter;

      return true;
   }

   private int addToList( int value ) {
      if( _size == _list.length ) {
         _list = Arrays.copyOf( _list, Math.max( _set.length, _size + ( _size >> 1 ) + 1 ) );
      }
      _list[_size++] = value;
      return _size;
   }

   @Override
   public boolean remove( int value ) {
      final int v = value - _offset;
      final int ref = _set[v];
      if( ref == 0 ) {
         return false;
      }

      _set[v] = 0;
      if( ref != _size-- ) { // Careful: the decrement must be postponed!
         final int other = _list[_size];
         _list[ref - 1] = other;
         _set[other] = ref;
      }

      ++_modCounter;

      return true;
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
   public int addAll( IntCollection elements ) {
      int added = 0;
      for( dev.costin.fastcollections.IntIterator iter = elements.intIterator(); iter.hasNext(); ) {

         if( add( iter.nextInt() ) ) {
            ++added;
         }
      }
      return added;
   }

   @Override
   public int addAll( int... elements ) {
      int added = 0;

      for( int i = 0; i < elements.length; i++ ) {
         if( add( elements[i] ) ) {
            ++added;
         }
      }
      return added;
   }

   public int get( int i ) {
      if( i >= _size ) {
         throw new IndexOutOfBoundsException();
      }
      return _list[i];
   }

}
