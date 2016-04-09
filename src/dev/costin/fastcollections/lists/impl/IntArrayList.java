package dev.costin.fastcollections.lists.impl;

import java.util.Arrays;
import java.util.Iterator;

import dev.costin.fastcollections.IntCollection;
import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.lists.IntList;
import dev.costin.fastcollections.tools.FastCollections;

public class IntArrayList implements IntList {
   
   private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
   private static final int[] EMPTY_LIST = {};
   
   private int[] _list;
   private int   _size;
   
   public IntArrayList() {
      _list = EMPTY_LIST;
      _size = 0;
   }
   
   public IntArrayList( final int initialCapacity ) {
      _list = new int[ initialCapacity ];
      _size = 0;
   }
   
   public IntArrayList( final IntCollection collection ) {
      this( collection.size() + 2 );
      addAll( collection );
   }

   @Override
   public boolean add(int value) {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public int addAll(IntCollection elements) {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public int addAll(int... elements) {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public boolean remove(int value) {
      // TODO Auto-generated method stub
      return false;
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
   public boolean contains(int value) {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean containsAll(IntCollection c) {
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
      return _list[_size-1];
   }

   @Override
   public void removeFirst() {
      // TODO Auto-generated method stub

   }

   @Override
   public void removeLast() {
      // TODO Auto-generated method stub

   }

   private void grow( final int minCapacity ) {
      // overflow-conscious code
      int oldCapacity = _list.length;
      int newCapacity = oldCapacity + (oldCapacity >> 1);
      if (newCapacity - minCapacity < 0)
          newCapacity = minCapacity;
      if (newCapacity - MAX_ARRAY_SIZE > 0)
          newCapacity = hugeCapacity(minCapacity);
      // minCapacity is usually close to size, so this is a win:
      _list = Arrays.copyOf(_list, newCapacity);
  }

  private static int hugeCapacity( final int minCapacity ) {
      if (minCapacity < 0) // overflow
          throw new OutOfMemoryError();
      return (minCapacity > MAX_ARRAY_SIZE) ?
          Integer.MAX_VALUE :
          MAX_ARRAY_SIZE;
  }
}
