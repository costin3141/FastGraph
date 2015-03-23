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
      this( FastCollections.DEFAULT_LIST_CAPACITY );
   }
   
   public IntGrowingSet( final int n ) {
      this( 0, n - 1 );
   }

   public IntGrowingSet( final int from, final int to ) {
      this( from, to, FastCollections.DEFAULT_LIST_CAPACITY );
   }

   public IntGrowingSet( final int from, final int to, final int listCapacity ) {
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
      int v = value - _offset;
      if( v < 0 ) {
         growNegative( -v );
         v = 0;
      }
      else if( v >= _set.length ) {
         growPositive( v );
      }

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
   
   private void growNegative( int count ) {
      final int[] _newSet = new int[ _set.length + count ];
      System.arraycopy( _set, 0, _newSet, count, _set.length );
      _set = _newSet;
      _offset -= count;
   }
   
   private void growPositive( int count ) {
      _set = Arrays.copyOf( _set, _set.length + count );
   }
}
