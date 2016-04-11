package dev.costin.fastcollections.lists.impl;

import java.util.Arrays;
import java.util.Iterator;

import dev.costin.fastcollections.IntCollection;
import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.lists.IntList;
import dev.costin.fastcollections.tools.FastCollections;

public class IntArrayList implements IntList {

   private static final int[] EMPTY = {};

   private int[] _list;

   private int _size;
   
   private int _modCount = 0;

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
      ++_modCount;
      
      _list[ _size++ ] = value;
      return true;
   }

   @Override
   public boolean addAll( IntCollection elements ) {
      ensureCapacity( _size + elements.size() );
      ++_modCount;
      
      for( final IntIterator itr = elements.intIterator(); itr.hasNext(); ) {
         _list[ _size++ ] = itr.nextInt();
      }
      return true;
   }

   @Override
   public boolean addAll( int... elements ) {
      ensureCapacity( _size + elements.length );
      ++_modCount;
      
      for( final int e : elements ) {
         _list[ _size++ ] = e;
      }
      return true;
   }

   @Override
   public boolean remove( int value ) {
      for( int i=0; i<_size; i++ ) {
         if( _list[i] == value ) {
            removeIndex( i );
            return true;
         }
      }
      
      return false;
   }
   
   public void removeIndex( final int index ) {
      checkRange( index );
      
      removeRange( index, index + 1 );
   }
   
   /** Remove elements starting at index {@code fromIndex} until {@code toIndex} (exclusive). */
   protected void removeRange( final int fromIndex, final int toIndex) {
      ++_modCount;
      
      final int count = _size - toIndex;
      System.arraycopy( _list, toIndex, _list, fromIndex, count);
      _size -= toIndex - fromIndex;
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
      for( final int v : _list ) {
         if( v == value ) {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean containsAll( IntCollection c ) {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public IntIterator intIterator() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void clear() {
      ++_modCount;
      _size = 0;
   }

   @Override
   public Iterator<IntCursor> iterator() {
      // TODO Auto-generated method stub
      return null;
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
   public void removeFirst() {
      removeIndex( 0 );
   }

   @Override
   public void removeLast() {
      removeIndex( _size - 1 );
   }
   
   private void checkRange( final int index ) {
      if( index < 0 || index >= _size ) {
         throw new IndexOutOfBoundsException( "Index " + index + " is out of bounds [0.."+_size+"[");
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
