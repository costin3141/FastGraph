package dev.costin.fastcollections.sets.impl;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

import dev.costin.fastcollections.IntCollection;
import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.sets.IntSet;
import dev.costin.fastcollections.tools.CollectionUtils;
import dev.costin.fastcollections.tools.FastCollections;
import dev.costin.fastcollections.tools.MemoryUtils;

import static dev.costin.fastcollections.tools.CollectionUtils.*;

/**
 * 
 * Same as {@link IntGrowingSet} but iteration always follows insertions
 * order, even after after remove of elements. Basically same as
 * java's {@link LinkedHashMap}.
 * 
 * @author ionescus
 *
 */
public class IntLinkedGrowingSet implements IntSet {
   
   static final long UPPER_LINK_INIT = combineToLong( Integer.MIN_VALUE, 0 );
   static final long LOWER_LINK_INIT = combineToLong( 0, Integer.MIN_VALUE );
   static final long SINGLE_ELEMENT_LINKS = combineToLong( Integer.MIN_VALUE, Integer.MIN_VALUE );

   private static final long[] EMPTY = {};
   
   private long[]      _set;
   private int         _first;
   private int         _last;
   private int         _size;
   private int         _offset;
   protected int       _modCounter = 0;

   protected static class IntCursorIterator implements Iterator<IntCursor>, IntCursor {

      private final IntLinkedGrowingSet _set;
      
      private final long[]      _links;
      private final int         _last;
      private final int         _offset;
      private int               _next;
      private int               _lastValue;
      private boolean           _lastRemoved;
      
      private int               _modCounter;

      IntCursorIterator( final IntLinkedGrowingSet set ) {
         _set = set;
         _links = _set._set;
         _last = _set._last;
         _offset = _set._offset;
         _next = _set._first;
         _modCounter = _set._modCounter;
         _lastValue = _set.isEmpty() ? _next : _next - 1 /* any other value than _next works*/;
         _lastRemoved = false;
      }

      @Override
      public IntCursor next() {
         if( _modCounter != _set._modCounter ) {
            throw new ConcurrentModificationException();
         }
         if( _next == _lastValue ) {
            throw new NoSuchElementException();
         }
         
         _lastRemoved = false;
         
         _lastValue = _next;
         if( _next != _last ) {
            _next = getLowerInt( _links[ _next - _offset ] );
         }

         return this;
      }

      @Override
      public boolean hasNext() {
         return _next != _lastValue;
      }

      @Override
      public void remove() {
         assert _set.contains( _lastValue ) : "Element has already been removed!";

         if( _modCounter != _set._modCounter ) {
            throw new ConcurrentModificationException();
         }
         if( _lastRemoved ) {
            throw new NoSuchElementException();
         }
         // it is important to use the remove method of the set
         // to ensure that subclass of the set are still able to use
         // this iterator!
         _set.remove( _lastValue );
         ++_modCounter;
      }

      @Override
      public int value() {
         return _lastValue;
      }

   }

   protected static class IntIterator implements dev.costin.fastcollections.IntIterator {

      private final IntLinkedGrowingSet _set;

      private final long[]      _links;
      private final int         _last;
      private final int         _offset;
      private int               _next;
      private int               _lastValue;
      private boolean           _lastRemoved;

      private int               _modCounter;

      IntIterator( final IntLinkedGrowingSet set ) {
         _set = set;
         _links = _set._set;
         _last = _set._last;
         _offset = _set._offset;
         _next = _set._first;
         _modCounter = _set._modCounter;
         _lastValue = _set.isEmpty() ? _next : _next - 1 /* any other value than _next works*/;
         _lastRemoved = false;
      }

      @Override
      public int nextInt() {
         if( _modCounter != _set._modCounter ) {
            throw new ConcurrentModificationException();
         }
         if( _next == _lastValue ) {
            throw new NoSuchElementException();
         }
         
         _lastRemoved = false;
         
         _lastValue = _next;
         if( _next != _last ) {
            _next = getLowerInt( _links[ _next - _offset ] );
         }

         return _lastValue;
      }

      @Override
      public boolean hasNext() {
         return _next != _lastValue;
      }

      @Override
      public void remove() {
         assert _set.contains( _lastValue ) : "Element has already been removed!";

         if( _modCounter != _set._modCounter ) {
            throw new ConcurrentModificationException();
         }
         if( _lastRemoved ) {
            throw new NoSuchElementException();
         }
         // it is important to use the remove method of the set
         // to ensure that subclass of the set are still able to use
         // this iterator!
         _set.remove( _lastValue );
         ++_modCounter;
      }

   }

   public IntLinkedGrowingSet() {
      _offset = 0;
      _set = EMPTY;
      _size = 0;
      _first = _last = Integer.MIN_VALUE;
   }
   
   public IntLinkedGrowingSet( final IntCollection c ) {
      this();
      
      if( !c.isEmpty() ) {
//         if( c instanceof IntLinkedGrowingSet ) {
//            final IntLinkedGrowingSet gset = (IntLinkedGrowingSet) c;
//            thisInit( gset );
//            
//            for( int i=0; i < gset.size(); i++ ) {
//               add( gset.get( i ) );
//            }
//         }
//         else {
            init( 0, FastCollections.DEFAULT_LIST_CAPACITY-1 );
            
            addAll( c );
//         }
      }
   }

   public IntLinkedGrowingSet( final int n ) {
      this( 0, n - 1 );
   }

   public IntLinkedGrowingSet( final int from, final int to ) {
      init( from, to );
   }

   private void init( final int from, final int to ) {
      _offset = from;
      final int length = to - from + 1;
      _set = new long[length];
      _size = 0;
      _first = _last = Integer.MIN_VALUE;
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
      return idx >= 0 && idx < _set.length && _set[idx] != 0;
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
      
      return c.isEmpty();
   }

   @Override
   public boolean add( int value ) {
      ensureRangeFor( value );
      
      final int idx = value - _offset;

      long links = _set[idx];
      
      if( links != 0 ) {
         return false;
      }
      
      assert _size == 0 && _first == _last || _size > 0;
      
      if( _size == 0 ) {
         _first = _last = value;
         _set[idx] = SINGLE_ELEMENT_LINKS;
      }
      else {
         final int idxLast = _last - _offset;
         long lastLinks = _set[idxLast];
         assert getLowerInt( lastLinks ) == Integer.MIN_VALUE;
         
         lastLinks = setLowerInt( lastLinks, value );
         _set[idxLast] = lastLinks;
         
         links = ((long)_last << 32); // move _last to upper 32 bit, as previous pointer
         links |= LOWER_LINK_INIT;
         _set[idx] = links;
         
         _last = value;
      }
      
      ++_size;
      ++_modCounter;

      return true;
   }

   @Override
   public boolean remove( int value ) {
      if( value >= _offset ) {
         final int idx = value - _offset;
         
         if( idx < _set.length ) {
            final long links = _set[idx];
            
            if( links != 0 ) {
               final int prevValue = getUpperInt( links );
               final int nextValue = getLowerInt( links );

               if( value != _first ) {
                  final int idxPrev = prevValue - _offset;
                  long prevLinks = _set[idxPrev];
                  assert prevLinks != 0;
                  
                  prevLinks = setLowerInt( prevLinks, nextValue );
                  _set[idxPrev] = prevLinks;
               }
               else {
                  assert _first == value;
                  _first = nextValue;
               }
               
               if( value != _last ) {
                  final int idxNext = nextValue - _offset;
                  long nextLinks = _set[idxNext];
                  assert nextLinks != 0;
                  
                  nextLinks = setUpperInt( nextLinks, prevValue );
                  _set[idxNext] = nextLinks;
               }
               else {
                  assert _last == value;
                  _last = prevValue;
               }
               
               _set[idx] = 0;
               --_size;
      
               ++_modCounter;
               
               assert _size == 0 && _first == Integer.MIN_VALUE && _last == Integer.MIN_VALUE || _size > 0;
      
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
      if( _size < _set.length / 3 ) {
         int i = _first;
         
         while( i >= 0 ) {
            final int idx = i - _offset;
            i = (int) _set[idx];
            _set[idx] = 0;
         }
      }
      else {
         CollectionUtils.fill( _set, 0, _set.length, 0 );
      }
      _first = _last = -1;
      _size = 0;
      ++_modCounter;
   }

   @Override
   public boolean addAll( IntCollection elements ) {
      for( dev.costin.fastcollections.IntIterator iter = elements.intIterator(); iter.hasNext(); ) {
         add( iter.nextInt() );
      }
      return true;
   }

   @Override
   public boolean addAll( int... elements ) {
      for( int i = 0; i < elements.length; i++ ) {
         add( elements[i] );
      }
      return true;
   }
   
   @Override
   public boolean equals( final Object o ) {
      if( this == o ) {
         return true;
      }
      if( !( o instanceof IntSet ) ) {
         return false;
      }
      final int currentModCounter = _modCounter;
      
      final IntSet set = (IntSet) o;
      final boolean ret = size() == set.size() && containsAll( set );
      
      if( currentModCounter != _modCounter ) {
         throw new ConcurrentModificationException();
      }
      
      return ret;
   }
   
   @Override
   public int hashCode() {
      final int currentModCounter = _modCounter;
      
      int h = 0;
      for( IntCursor entry : this ) {
          h += entry.value();
      }
      
      if( currentModCounter != _modCounter ) {
         throw new ConcurrentModificationException();
      }
      
      return h;
   }

   private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
   
   private void ensureRangeFor( final int value ) {
      if( _set == EMPTY ) {
         _set = new long[ FastCollections.DEFAULT_LIST_CAPACITY ];
         final int half = FastCollections.DEFAULT_LIST_CAPACITY >>> 1;
         if( value < Integer.MIN_VALUE + half ) {
            _offset = Integer.MIN_VALUE;
         }
         else if( value > Integer.MAX_VALUE - half ) {
            _offset = Integer.MAX_VALUE - FastCollections.DEFAULT_LIST_CAPACITY + 1;
         }
         else {
            _offset = value - half;
         }
      }
      else {
         final int v = value - _offset;

         if( v < 0 ) {
            growNeg( MemoryUtils.capacity( _set.length - v, _set.length ) - _set.length );
         }
         else if( v >= _set.length ) {
            growPos( MemoryUtils.capacity( v + 1, _set.length ) - _set.length );
         }
      }
   }
   
   private void growNeg( final int count ) {
      assert count > 0;
      
      int n = count;
      
      if( _offset < Integer.MIN_VALUE + count ) {
         n = _offset - Integer.MIN_VALUE;
      }
      
      final long[] _newSet = new long[ _set.length + n ];
      System.arraycopy( _set, 0, _newSet, n, _set.length );
      _set = _newSet;
      _offset -= n;
   }
   
   private void growPos( final int count ) {
      assert count > 0;
      
      int c = count;
      int n = 0;
      
      final int maxValidOffset = Integer.MAX_VALUE - _set.length - count + 1;
      
      if( _offset > maxValidOffset ) {
         n = _offset - maxValidOffset;
         c = n;
      }
      
      final long[] _newSet = new long[ _set.length + c ];
      System.arraycopy( _set, 0, _newSet, n, _set.length );
      _set = _newSet;
      _offset -= n;
   }
   
}
