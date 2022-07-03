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
   public int peekTop() {
      if( isEmpty() ) {
         throw new NoSuchElementException();
      }
      
      if( _end == 0 ) {
         return _ring[_ring.length - 1];
      }
      else {
         return _ring[_end - 1];
      }
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
   public int peekNext() {
      return _ring[_start];
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
   
   @Override
   public boolean equals( Object obj ) {
      if( this == obj ) {
         return true;
      }
      
      if( !( obj instanceof IntArrayRingDeque ) ) {
         return false;
      }

      final IntArrayRingDeque other = (IntArrayRingDeque) obj;
      if( size() != other.size() ) {
         return false;
      }
      
      for( int i = 0; i< size(); i++ ) {
         int idxThis = i + _start;
         if( idxThis >= _ring.length ) {
            idxThis -= _ring.length;
         }
         int idxOther = i + other._start;
         if( idxOther >= other._ring.length ) {
            idxOther -= other._ring.length;
         }
         
         if( _ring[idxThis] != other._ring[idxOther] ) {
            return false;
         }
      }
      
      return true;
   }
   
   @Override
   public int hashCode() {
      int hash = 0;
      
      for( int i = 0; i< size(); i++ ) {
         int idxThis = i + _start;
         if( idxThis >= _ring.length ) {
            idxThis -= _ring.length;
         }
         
         hash += 32 * hash + _ring[idxThis];
      }
      
      return hash;
   }

   protected void ensureCapacity( final int desiredCap ) {
      if( desiredCap < 0 ) {
         throw new OutOfMemoryError();
      }
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
