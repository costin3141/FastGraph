package dev.costin.fastcollections.lists.impl;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

import dev.costin.fastcollections.IntCollection;
import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.lists.IntList;
import dev.costin.fastcollections.tools.FastCollections;

public class IntArrayList implements IntList, RandomAccess {

   private static final int[] EMPTY = {};

   private int[] _list;

   private int _size;
   
   private int _modCounter = 0;

   public IntArrayList() {
      _list = EMPTY;
      _size = 0;
   }

   public IntArrayList( final int initialCapacity ) {
      _list = new int[initialCapacity];
      _size = 0;
   }

   public IntArrayList( final IntCollection collection ) {
      this( collection.size() + 2 );
      addAll( collection );
   }

   @Override
   public boolean add( int value ) {
      ensureCapacity( _size + 1 );
      ++_modCounter;
      
      _list[ _size++ ] = value;
      return true;
   }

   @Override
   public boolean addAll( IntCollection elements ) {
      ensureCapacity( _size + elements.size() );
      ++_modCounter;
      
      for( final dev.costin.fastcollections.IntIterator itr = elements.intIterator(); itr.hasNext(); ) {
         _list[ _size++ ] = itr.nextInt();
      }
      return true;
   }

   @Override
   public boolean addAll( int... elements ) {
      ensureCapacity( _size + elements.length );
      ++_modCounter;
      
      for( final int e : elements ) {
         _list[ _size++ ] = e;
      }
      return true;
   }

   @Override
   public boolean remove( int value ) {
      final int index = indexOf( value );
      
      if( index >= 0 ) {
         removeIndex( index );
         return true;
      }
      
      return false;
   }
   
   public void removeIndex( final int index ) {
      removeRange( index, index + 1 );
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
   public boolean contains( int value ) {
      for( int i=0; i<_size; i++ ) {
         final int v = _list[i];
         
         if( v == value ) {
            return true;
         }
      }
      return false;
   }
   
   public int indexOf( final int value ) {
      for( int i=0; i<_size; i++ ) {
         final int v = _list[i];
         
         if( v == value ) {
            return i;
         }
      }
      return -1;
   }

   @Override
   public boolean containsAll( IntCollection c ) {
      for( final dev.costin.fastcollections.IntIterator itr = c.intIterator(); itr.hasNext(); ) {
         final int v = itr.nextInt();
         
         if( !contains( v ) ) {
            return false;
         }
      }
      return true;
   }

   @Override
   public IntIterator intIterator() {
      return new IntIterator( this );
   }

   @Override
   public void clear() {
      ++_modCounter;
      _size = 0;
   }

   @Override
   public Iterator<IntCursor> iterator() {
      return new IntCursorIterator( this );
   }
   
   @Override
   public int getFirst() {
      if( isEmpty() ) {
         throw new IndexOutOfBoundsException();
      }
      return _list[0];
   }

   @Override
   public int getLast() {
      if( isEmpty() ) {
         throw new IndexOutOfBoundsException();
      }
      return _list[_size - 1];
   }

   @Override
   public int get( final int index ) {
      return _list[ index ];
   }
   
   @Override
   public void removeFirst() {
      removeIndex( 0 );
   }

   @Override
   public void removeLast() {
      removeIndex( _size - 1 );
   }
   
   /** Remove elements starting at index {@code fromIndex} until {@code toIndex} (exclusive). */
   protected void removeRange( final int fromIndex, final int toIndex) {
      ++_modCounter;
      
      final int count = _size - toIndex;
      System.arraycopy( _list, toIndex, _list, fromIndex, count);
      _size -= toIndex - fromIndex;
   }
   
   protected static class IntCursorIterator implements Iterator<IntCursor>, IntCursor {

      private final IntArrayList _arrayList;
      private final int[]       _list;
      private int               _next;
      private int               _value;
      private int               _modCounter;
      private int               _lastRemoved;

      IntCursorIterator( final IntArrayList list ) {
         _arrayList = list;
         _list = _arrayList._list;
         _next = 0;
         _value = 0;
         _modCounter = _arrayList._modCounter;
         _lastRemoved = -1;
      }

      @Override
      public boolean hasNext() {
         return _next < _arrayList.size();
      }

      @Override
      public IntCursor next() {
         if( _modCounter != _arrayList._modCounter ) {
            throw new ConcurrentModificationException();
         }

         _value = _list[_next++];
         return this;
      }

      @Override
      public void remove() {
         assert _arrayList.size() >= _next && _next > 0;

         if( _modCounter != _arrayList._modCounter ) {
            throw new ConcurrentModificationException();
         }
         if( _lastRemoved >= _next ) {
            throw new NoSuchElementException();
         }
         // it is important to use the remove method of the set
         // to ensure that subclass of the set are still able to use
         // this iterator!
         _lastRemoved = --_next;
         _arrayList.removeIndex( _lastRemoved );
         ++_modCounter;
      }

      @Override
      public int value() {
         return _value;
      }

   }

   protected static class IntIterator implements dev.costin.fastcollections.IntIterator {

      private final IntArrayList _arrayList;

      private final int[]       _list;

      private int               _next;

      private int               _modCounter;
      private int               _lastRemoved;

      IntIterator( final IntArrayList list ) {
         _arrayList = list;
         _list = _arrayList._list;
         _next = 0;
         _modCounter = _arrayList._modCounter;
         _lastRemoved = -1;
      }

      @Override
      public int nextInt() {
         if( _modCounter != _arrayList._modCounter ) {
            throw new ConcurrentModificationException();
         }

         return _list[_next++];
      }

      @Override
      public boolean hasNext() {
         return _next < _arrayList.size();
      }

      @Override
      public void remove() {
         assert _arrayList.size() >= _next && _next > 0;

         if( _modCounter != _arrayList._modCounter ) {
            throw new ConcurrentModificationException();
         }
         if( _lastRemoved >= _next ) {
            throw new NoSuchElementException();
         }
         // it is important to use the remove method of the set
         // to ensure that subclass of the set are still able to use
         // this iterator!
         _lastRemoved = --_next;
         _arrayList.removeIndex( _lastRemoved );
         ++_modCounter;
      }

   }
   
   private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
   
   private void ensureCapacity( final int minCapacity ) {
      if( minCapacity - _list.length > 0 ) {
         if( _list == EMPTY ) {
            _list = new int[ Math.max( FastCollections.DEFAULT_LIST_CAPACITY, minCapacity ) ];
         }
         else {
            grow( minCapacity );
         }
      }
   }

   private void grow( final int minCapacity ) {
      // overflow-conscious code
      final int oldCapacity = _list.length;
      int newCapacity = oldCapacity + (oldCapacity >> 1);
      if( newCapacity - minCapacity < 0 )
         newCapacity = minCapacity;
      if( newCapacity - MAX_ARRAY_SIZE > 0 ) {
         newCapacity = hugeCapacity( minCapacity );
      }
      // minCapacity is usually close to size, so this is a win:
      _list = Arrays.copyOf( _list, newCapacity );
   }

   private static int hugeCapacity( final int minCapacity ) {
      if( minCapacity < 0 ) { // overflow
         throw new OutOfMemoryError();
      }
      return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
   }
}
