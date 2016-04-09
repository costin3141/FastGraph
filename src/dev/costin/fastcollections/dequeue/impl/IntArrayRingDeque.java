package dev.costin.fastcollections.dequeue.impl;

import java.util.NoSuchElementException;

import dev.costin.fastcollections.dequeue.IntDequeue;
import dev.costin.fastcollections.tools.FastCollections;

/**
 * {@link IntDequeue} implementation using an array as a ring and growing if the
 * capacity is insufficient.
 * 
 * @author Stefan C. Ionescu
 *
 */
public class IntArrayRingDeque implements IntDequeue {

   private int[] _ring;
   private int   _size;
   private int   _start;
   private int   _end;

   public IntArrayRingDeque() {
      this( FastCollections.DEFAULT_LIST_CAPACITY );
   }

   public IntArrayRingDeque( final int initalCapacity ) {
      _ring = new int[initalCapacity];
      _start = _end = _size = 0;
   }

   @Override
   public boolean push( int element ) {
      ensureCapacity( _size + 1 );

      _ring[_end++] = element;
      if( _end >= _ring.length ) {
         _end = 0;
      }

      ++_size;

      return true;
   }

   @Override
   public int pop() {
      if( isEmpty() ) {
         throw new NoSuchElementException();
      }

      if( _end == 0 ) {
         _end = _ring.length - 1;
      }
      else {
         --_end;
      }

      --_size;

      return _ring[_end];
   }

   @Override
   public boolean offer( int element ) {
      return push( element );
   }

   @Override
   public int take() {
      if( isEmpty() ) {
         throw new NoSuchElementException();
      }

      final int e = _ring[_start++];
      if( _start >= _ring.length ) {
         _start = 0;
      }
      --_size;

      return e;
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
   public void clear() {
      _size = 0;
      _start = _end = 0;
   }

   protected void ensureCapacity( final int desiredCap ) {
      if( _ring.length < desiredCap ) {
         final int newCap = Math.max( desiredCap, _ring.length + ( _ring.length >> 1 ) + 1 );
         grow( newCap );
      }
   }

   private void grow( int newCap ) {
      final int[] newRing = new int[newCap];

      int count = Math.min( _start + _size, _ring.length ) - _start;
      System.arraycopy( _ring, _start, newRing, 0, count );

      if( count < _size ) {
         System.arraycopy( _ring, 0, newRing, count, _size - count );
      }

      _ring = newRing;
      _start = 0;
      _end = _start + _size;
   }
}
