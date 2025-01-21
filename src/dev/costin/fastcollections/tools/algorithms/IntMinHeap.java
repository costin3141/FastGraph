package dev.costin.fastcollections.tools.algorithms;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import dev.costin.fastcollections.IntCollection;
import dev.costin.fastcollections.IntComparator;
import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.dequeue.IntQueue;
import dev.costin.fastcollections.lists.impl.IntArrayList;

public class IntMinHeap implements Iterable<IntCursor>, IntQueue {

   private final IntComparator _cmp;
   private IntArrayList _heap;
   private int _size;
   
   private boolean _needsUpdate;
   
   private int _modCounter = 0;

   public IntMinHeap( final IntComparator cmp ) {
      _heap = new IntArrayList();
      _cmp = cmp;
      _size = 0;
      _needsUpdate = false;
   }
   
   public IntMinHeap( final IntComparator cmp, final int initialCapacity ) {
      _heap = new IntArrayList( initialCapacity );
      _cmp = cmp;
      _size = 0;
      _needsUpdate = false;
   }
   
   public void clear() {
      _size = 0;
      // For GC
      _heap.clear();
   }

   public int size() {
      return _size;
   }

   public boolean isEmpty() {
      return _size == 0;
   }
   
   @Override
   public int peekNext() {
      ensureHeap();
      
      if( _size <= 0 ) {
         throw new NoSuchElementException();
      }
      
      return _heap.get( 0 );
   }
   
   public int remove() {
      return take();
   }

   @Override
   public int take() {
      ensureHeap();
      
      if( _size <= 0 ) {
         throw new NoSuchElementException();
      }
      
      assert _size > 0;
      final int ret = _heap.get( 0 );
      
      if( --_size > 0 ) {
         _heap.set( 0, _heap.get( _size ) );
         moveDown( 0 );
      }
      
      assert checkHeap();
      return ret;
   }
   
   public boolean contains( final int o ) {
      for( int i=0; i<_size; i++ ) {
         if( _cmp.compare( _heap.get( i ), o ) == 0 ) {
            return true;
         }
      }
      
      return false;
   }

   public boolean addAll( IntCollection c ) {
      boolean ret = false;
      
      for( IntCursor cursor : c ) {
         ret |= add( cursor.value() );
      }
      
      return ret;
   }

   public boolean add( final int e ) {
      return offer( e );
   }

   @Override
   public boolean offer( int e ) {
      if( _heap.size() <= _size ) {
         _heap.add( e );
      }
      else {
         _heap.set( _size, e );
      }
      
      if( _needsUpdate ) {
         ++_size;
      }
      else {
         moveUp( _size );
         ++_size;
         assert checkHeap();
      }

      return true;
   }

   public void invalidate() {
      _needsUpdate = true;
   }
   
   private void ensureHeap() {
      if( _needsUpdate ) {
         final int lastNonLeaf = (_size >>> 1) - 1;
         
         for( int i=lastNonLeaf; i >= 0; i-- ) {
            moveDown( i );
         }
         
         assert checkHeap();
         _needsUpdate = false;
      }
   }
   
   @Override
   public Iterator<IntCursor> iterator() {
      return new Itr( this );
   }
   
   private void moveDown( final int idx ) {
      final int value = _heap.get(idx);

      int current = idx;
      int next = (current << 1) + 1;
      int minIdx = current;
      
      while( next < _size ) {
         minIdx = current;
         int minValue = value;
         int otherValue = _heap.get( next );

         // first child compare
         if( _cmp.compare( otherValue, value ) < 0 ) {
            minIdx = next;
            minValue = otherValue;
         }
         
         // second child compare
         if( next < _size - 1 ) {
            next++;
            otherValue = _heap.get( next );
            
            if( _cmp.compare( otherValue, minValue ) < 0 ) {
               minIdx = next;
               minValue = otherValue;
            }
            else if( minIdx == current ) {
               break;
            }
         }
         else if( minIdx == current ) {
            break;
         }

         _heap.set( current, minValue );
         
         // key will only be stored when we found its final destination

         current = minIdx;
         next = (current << 1) + 1;
      }
      
      // store key at its final destination
      if( idx != minIdx ) {
         _heap.set( minIdx, value );
      }
   }
   
   private void moveUp( final int idx ) {
      final int value = _heap.get( idx );
      int current = idx;
      int next = (current - 1) >>> 1;

      while( next < current ) {
         final int otherValue = _heap.get( next );

         if( _cmp.compare( otherValue, value ) > 0 ) {
            _heap.set( current, otherValue );
         }
         else {
            break;
         }

         current = next;
         next = (current - 1) >>> 1;
      }
      
      if( current != idx ) {
         _heap.set( current, value );
      }
   }
   
   private static class Itr implements Iterator<IntCursor>, IntCursor {
      final IntMinHeap _minHeap;
      final IntArrayList _heap;
      final int _size;
      final int _modCounter;
      int i = 0;
      int currentValue = 0;
      
      Itr( final IntMinHeap minHeap ) {
         _minHeap = minHeap;
         _heap = minHeap._heap;
         _size = minHeap._size;
         _modCounter = minHeap._modCounter;
      }
      
      @Override
      public boolean hasNext() {
         if( _modCounter != _minHeap._modCounter ) {
            throw new ConcurrentModificationException();
         }
         
         return i < _size;
      }

      @Override
      public IntCursor next() {
         if( _modCounter != _minHeap._modCounter ) {
            throw new ConcurrentModificationException();
         }
         
         if( i >= _size ) {
            throw new NoSuchElementException();
         }
         currentValue = _heap.get( i++ );
         return this;
      }
      
      @Override
      public int value() {
         if( i == 0 ) {
            throw new NoSuchElementException();
         }
         return currentValue;
      }
   }
   
   public boolean checkHeap() {
      for( int i = 0; i < _size; i++ ) {
         final int v = _heap.get( i );

         final int child1 = 2 * i + 1;
         final int child2 = 2 * i + 2;
         if( child1 < _size ) {
            if( _cmp.compare( _heap.get( child1 ), v ) < 0 ) {
               return false;
            }
         }
         if( child2 < _size ) {
            if( _cmp.compare( _heap.get( child2 ), v ) < 0 ) {
               return false;
            }
         }
      }
      
      return true;
   }

//   public static void main( final String[] args ) {
//      final int n = 100;
//      final IntArrayList r = new IntArrayList();
//      for( int i = 0; i < n; i++ ) {
//         r.add( i );
//      }
//      FastCollections.shuffle( r, new Random( 800 ) );
//
//      final IntMinHeap m = new IntMinHeap( new IntComparator() {
//         
//         @Override
//         public int compare( int i1, int i2 ) {
//            return Integer.compare( i2, i1 );
//         }
//      });
//
//      for( int i = 0; i < r.size(); i++ ) {
//         m.add( r.get( i ) );
//      }
//      
////      for( int i = 0; i < r.size(); i++ ) {
////         int v = r.get( i );
////         v.set( 1000 - v );
////      }
////      
////      m.ensureHeap();
//
//      while( m.size() > 0 ) {
//         final int i = m.take();
//         System.out.println( i );
//      }
//
//      // m.add( 1, 10 );
//      // System.out.println( m.peek() );
//      // m.add( 2, 5 );
//      // System.out.println( m.peek() );
//      // m.add( 10, 9 );
//      // System.out.println( m.peek() );
//      // m.add( 3, 1 );
//      // System.out.println( m.peek() );
//      //
//      // while( m.size() > 0 ) {
//      // System.out.println( m.poll() );
//      // }
//   }

}
