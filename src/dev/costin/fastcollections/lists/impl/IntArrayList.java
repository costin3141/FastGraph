package dev.costin.fastcollections.lists.impl;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;

import dev.costin.fastcollections.IntCollection;
import dev.costin.fastcollections.IntComparator;
import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.IntPredicate;
import dev.costin.fastcollections.lists.IntList;
import dev.costin.fastcollections.tools.FastCollections;
import dev.costin.fastcollections.tools.algorithms.IntQuickSort;

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
   public String toString() {
      StringBuilder s = new StringBuilder( "[" );
      IntIterator i = intIterator();
      int c = 0;
      while( i.hasNext() ) {
         if( c == 0 ) {
            s.append( Integer.toString( i.nextInt() ) );
         }
         else {
            s.append( ", " + Integer.toString( i.nextInt() ) );
         }
         c++;
      }
      s.append( ']' );

      return s.toString();
   }

   @Override
   public boolean add( int value ) {
      ensureCapacity( _size + 1 );
      ++_modCounter;

      _list[_size++] = value;
      return true;
   }

   @Override
   public boolean addAll( IntCollection elements ) {
      ensureCapacity( _size + elements.size() );
      ++_modCounter;

      for( final dev.costin.fastcollections.IntIterator itr = elements.intIterator(); itr.hasNext(); ) {
         _list[_size++] = itr.nextInt();
      }
      return true;
   }

   @Override
   public boolean addAll( int... elements ) {
      ensureCapacity( _size + elements.length );
      ++_modCounter;

      for( final int e : elements ) {
         _list[_size++] = e;
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

   @Override
   public boolean removeAll( IntCollection elements ) {
      return batchRemove( elements, false, 0, _size );
   }

   @Override
   public boolean removeIf( IntPredicate filter ) {
      return removeIf(filter, 0, _size);
   }

   @Override
   public boolean retainAll( IntCollection elements ) {
      return batchRemove( elements, true, 0, _size );
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
      for( int i = 0; i < _size; i++ ) {
         final int v = _list[i];

         if( v == value ) {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean containsAll( IntCollection c ) {
      for( final IntCursor cursor : c ) {
         if( !contains( cursor.value() ) ) {
            return false;
         }
      }

      return true;
   }

   @Override
   public int indexOf( final int value ) {
      for( int i = 0; i < _size; i++ ) {
         final int v = _list[i];

         if( v == value ) {
            return i;
         }
      }
      return -1;
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
      assert index < _size && index >= 0;
      return _list[index];
   }

   @Override
   public int set( int index, int value ) {
      assert index < _size && index >= 0;
      int old = _list[index];
      _list[index] = value;
      return old;
   }

   @Override
   public void removeFirst() {
      removeIndex( 0 );
   }

   @Override
   public void removeLast() {
      removeIndex( _size - 1 );
   }

   @Override
   public void sort() {
      Arrays.sort( _list, 0, size() );
   }

   @Override
   public void sort( final IntComparator comparator ) {
      IntQuickSort.sort( _list, 0, size(), comparator );
   }

   @Override
   public boolean equals( Object obj ) {
      if( this == obj ) {
         return true;
      }
      final int currentModCounter = _modCounter;

      if( !(obj instanceof IntList) ) {
         return false;
      }

      final IntList other = (IntList) obj;

      if( size() != other.size() ) {
         return false;
      }

      boolean ret = true;

      final Iterator<IntCursor> itrThis = iterator();
      final Iterator<IntCursor> itrOther = other.iterator();
      for( ; itrThis.hasNext(); ) {
         if( itrThis.next().value() != itrOther.next().value() ) {
            ret = false;
            break;
         }
      }

      if( _modCounter != currentModCounter ) {
         throw new ConcurrentModificationException();
      }

      return ret;
   }

   @Override
   public int hashCode() {
      final int currentModCounter = _modCounter;

      int hashCode = 1;
      for( int i = 0; i < size(); i++ ) {
         final int e = _list[i];
         hashCode = 31 * hashCode + e;
      }

      if( _modCounter != currentModCounter ) {
         throw new ConcurrentModificationException();
      }
      return hashCode;
   }

   protected static class IntCursorIterator implements Iterator<IntCursor>, IntCursor {

      private final IntArrayList _arrayList;

      private final int[] _list;

      private int _next;

      private int _value;

      private int _modCounter;

      private int _lastRemoved;

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

      @Override
      public String toString() {
         return Integer.toString( value() );
      }
   }

   protected static class IntIterator implements dev.costin.fastcollections.IntIterator {

      private final IntArrayList _arrayList;

      private final int[] _list;

      private int _next;

      private int _modCounter;

      private int _lastRemoved;

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
      if( minCapacity < 0 ) {
         throw new OutOfMemoryError();
      }
      if( minCapacity - _list.length > 0 ) {
         if( _list == EMPTY ) {
            _list = new int[Math.max( FastCollections.DEFAULT_LIST_CAPACITY, minCapacity )];
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
   
   /** Remove elements starting at index {@code fromIndex} until {@code toIndex} (exclusive). */
   private void removeRange( final int fromIndex, final int toIndex ) {
      ++_modCounter;

      final int count = _size - toIndex;
      System.arraycopy( _list, toIndex, _list, fromIndex, count );
      _size -= toIndex - fromIndex;
   }

   private boolean batchRemove( final IntCollection c, final boolean complement,
            final int from, final int end ) {
      Objects.requireNonNull( c );
      final int[] list = _list;
      int i;

      // skip to first required changes
      for( i = from;; i++ ) {
         if( i == end ) {
            return false;
         }

         if( c.contains( list[i] ) != complement ) {
            break;
         }
      }

      int j = i++;

      for( int e; i < end; i++ ) {
         if( c.contains( e = list[i] ) == complement ) {
            list[j++] = e;
         }
      }

      final int removedCount = end - j;
      _modCounter += removedCount;
      shiftTailOverGap( list, j, end );
      _size -= removedCount;

      return true;
   }
   
   boolean removeIf( IntPredicate filter, int i, final int end ) {
      int expectedModCounter = _modCounter;
      final int[] list = _list;

      // skip to first required changes
      for( ; i < end && !filter.test( list[i] ); i++ )
         ;

      if( i < end ) {
         final int start = i;
         final long[] deathRow = createBitSet( end - start );

         deathRow[0] = 1L; // set bit 0

         for( i = start + 1; i < end; i++ ) {
            if( filter.test( list[i] ) ) {
               setBit( deathRow, i - start );
            }
         }

         if( _modCounter != expectedModCounter ) {
            throw new ConcurrentModificationException();
         }

         _modCounter++;

         int j = start;
         for( i = start; i < end; i++ ) {
            if( isClear( deathRow, i - start ) ) {
               list[j++] = list[i];
            }
         }

         shiftTailOverGap( list, j, end );
         _size -= end - j;
         return true;
      }
      else {
         if( _modCounter != expectedModCounter ) {
            throw new ConcurrentModificationException();
         }
         return false;
      }
   }

   private static long[] createBitSet( int n ) {
      return new long[((n - 1) >> 6) + 1];
   }

   private static void setBit( long[] bits, int i ) {
      bits[i >> 6] |= 1L << i;
   }

   private static boolean isClear( long[] bits, int i ) {
      return (bits[i >> 6] & (1L << i)) == 0;
   }

   private void shiftTailOverGap( int[] list, int lo, int hi ) {
      System.arraycopy( list, hi, list, lo, _size - hi );
   }
}
