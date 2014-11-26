package dev.costin.fastcollections.maps.impl;

import java.util.Arrays;
import java.util.Iterator;

import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.maps.IntIntMap;
import dev.costin.fastcollections.tools.FastCollections;


public class IntIntRangeMap implements IntIntMap {
   
   private final int[] _keySet;
   private int[]       _keyList;
   private int[]       _valSet;  // or better a list? slightly slower but less memory.
   private int         _size;
   private final int   _offset;
   protected int       _modCounter = 0;

   public IntIntRangeMap( final int n ) {
      this( 0, n - 1 );
   }

   public IntIntRangeMap( final int from, final int to ) {
      this( from, to, Math.min( to - from + 1, FastCollections.DEFAULT_LIST_CAPACITY ) );
   }

   public IntIntRangeMap( final int from, final int to, final int listCapacity ) {
      _offset = from;
      _keySet = new int[to - from + 1];
      _valSet = new int[to - from + 1];
      _keyList = new int[listCapacity];
      _size = 0;
   }


   @Override
   public Iterator<IntCursor> iterator() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public boolean contains( int key ) {
      return _keySet[key - _offset] > 0;
   }

   @Override
   public boolean put( int key, int value ) {
      final int k = key - _offset;
      
      if( _keySet[k] <= 0 ) {
         _keySet[k] = addToList( key );
         ++_modCounter;
         _valSet[k] = value;
         
         return false;
      }
      else {
         _valSet[k] = value;
         return true;
      }
   }
   
   @Override
   public boolean remove( int key ) {
      final int k = key - _offset;
      final int ref = _keySet[k];
      if( ref == 0 ) {
         return false;
      }

      _keySet[k] = 0;
      if( ref != _size-- ) { // Careful: the decrement must be postponed!
         final int other = _keyList[_size];
         _keyList[ref - 1] = other;
         _keySet[other] = ref;
      }

      ++_modCounter;

      return true;
   }

   @Override
   public int get( int key ) {
      assert( contains(key-_offset) );
      return _valSet[key-_offset];
   }

   @Override
   public int size() {
      return _size;
   }

   @Override
   public IntIterator keyIterator() {
      // TODO Auto-generated method stub
      return null;
   }

   private int addToList( int value ) {
      if( _size == _keyList.length ) {
         _keyList = Arrays.copyOf( _keyList, Math.max( _keySet.length, _size + ( _size >> 1 ) + 1 ) );
      }
      _keyList[_size++] = value;
      return _size;
   }
   
}
