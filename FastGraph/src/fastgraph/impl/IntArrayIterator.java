package fastgraph.impl;

import java.util.Iterator;

import fastgraph.IntIterator;

public class IntArrayIterator implements IntIterator, Iterator<Integer> {
   
   
   
   private final int[] _array;
   private final int _size;
   int _next;
   
   IntArrayIterator( int[] array, int size ) {
      _array = array;
      _size = size;
      _next = 0;
   }

   @Override
   public boolean hasNext() {
      return _next < _size;
   }

   @Override
   public Integer next() {
      return Integer.valueOf( _array[_next++] );
   }

   @Override
   public void remove() {
      throw new UnsupportedOperationException();
   }

   @Override
   public int nextInt() {
      return _array[_next++];
   }

}
