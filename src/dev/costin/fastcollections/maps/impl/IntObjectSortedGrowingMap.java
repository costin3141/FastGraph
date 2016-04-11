package dev.costin.fastcollections.maps.impl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.bridging.collections.IntComparator;
import dev.costin.fastcollections.maps.IntObjectMap;
import dev.costin.fastcollections.maps.IntObjectSortedMap;
import dev.costin.fastcollections.maps.IntObjectMap.IntObjectEntry;
import dev.costin.fastcollections.tools.FastCollections;

public class IntObjectSortedGrowingMap<V> extends IntObjectGrowingMap<V> implements IntObjectSortedMap<V> {
   
   protected static final IntComparator DEFAULT_COMP = new IntComparator() {

      @Override
      public int compare( int i1, int i2 ) {
         return Integer.compare( i1, i2 );
      }
   };
   
   private boolean _needSorting = false;
   
   private final IntComparator _keyComp;
   
   private final Comparator<IntObjectEntry> _comparator;
   
   public IntObjectSortedGrowingMap() {
      this( FastCollections.DEFAULT_LIST_CAPACITY );
   }
   
   public IntObjectSortedGrowingMap( final IntObjectMap<V> map ) {
      super( map );
      _keyComp = DEFAULT_COMP;
      _comparator = new Comparator<IntObjectMap.IntObjectEntry>() {

         @Override
         public int compare( IntObjectMap.IntObjectEntry o1, IntObjectMap.IntObjectEntry o2 ) {
            return _keyComp.compare( o1.getKey(), o2.getKey() );
         }
      };
      
      if( map instanceof IntObjectSortedGrowingMap ) {
         _needSorting = ((IntObjectSortedGrowingMap<V>) map)._needSorting;
      }
      else {
         _needSorting = map.size() > 0;
      }
   }

   
   public IntObjectSortedGrowingMap( final int n ) {
      this( 0, n - 1 );
   }

   public IntObjectSortedGrowingMap( final int from, final int to ) {
      this( from, to, Math.min( to - from + 1, FastCollections.DEFAULT_LIST_CAPACITY ) );
   }

   public IntObjectSortedGrowingMap( final int from, final int to, final int listCapacity ) {
      super( from, to, listCapacity );
      _keyComp = DEFAULT_COMP;
      _comparator = new Comparator<IntObjectMap.IntObjectEntry>() {

         @Override
         public int compare( IntObjectMap.IntObjectEntry o1, IntObjectMap.IntObjectEntry o2 ) {
            return _keyComp.compare( o1.getKey(), o2.getKey() );
         }
      };
   }

   @Override
   public IntObjectSortedMap<V> subMap( int start, int end ) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public IntObjectSortedMap<V> subMap( int start, boolean includeStart, int end, boolean includeEnd ) {
      // TODO Auto-generated method stub
      return null;
   }


   private static class SubMapView<V> implements IntObjectSortedMap<V> {
      
      private final IntObjectSortedGrowingMap<V> _map;
      
      private int _size;
      private int _sizeMod;
      
      private final boolean _fromStart;
      private final int _start;
      private final boolean _includeStart;
      private final boolean _toEnd;
      private final int _end;
      private final boolean _includeEnd;
      
      public SubMapView( final IntObjectSortedGrowingMap<V> map, final boolean fromStart, final int start, final boolean includeStart,
               final boolean toEnd, final int end, final boolean includeEnd ) {
         _map = map;
         
         _size = -1;
         
         _fromStart = fromStart;
         _start = start;
         _includeStart = includeStart;
         _toEnd = toEnd;
         _end = end;
         _includeEnd = includeEnd;
      }

      @Override
      public boolean containsKey( int key ) {
         checkRange( key );
         return _map.containsKey( key );
      }

      @Override
      public V put( int key, V value ) {
         checkRange( key );
         return _map.put( key, value );
      }

      @Override
      public V remove( int key ) {
         checkRange( key );
         return _map.remove( key );
      }

      @Override
      public V get( int key ) {
         checkRange( key );
         return _map.get( key );
      }

      @Override
      public int size() {
         if( _fromStart && _toEnd ) {
            return _map.size();
         }
         
         // TODO Auto-generated method stub
         return 0;
      }

      @Override
      public boolean isEmpty() {
         return size() > 0;
      }

      @Override
      public IntIterator keyIterator() {
         // TODO Auto-generated method stub
         return null;
      }

      @Override
      public void clear() {
         // TODO Auto-generated method stub
         
      }

      @Override
      public Iterator<dev.costin.fastcollections.maps.IntObjectMap.IntObjectEntry<V>> iterator() {
         // TODO Auto-generated method stub
         return null;
      }

      @Override
      public IntObjectSortedMap<V> subMap( int start, int end ) {
         // TODO Auto-generated method stub
         return null;
      }

      @Override
      public IntObjectSortedMap<V> subMap( int start, boolean includeStart, int end, boolean includeEnd ) {
         // TODO Auto-generated method stub
         return null;
      }
      
      
      private void checkRange( final int key ) {
         if( _fromStart || _includeStart && _map._keyComp.compare( key, _start ) >= 0 ||
                  _map._keyComp.compare( key, _start ) > 0 ) {
            if( _toEnd || _includeEnd && _map._keyComp.compare( key, _end ) <= 0 ||
                     _map._keyComp.compare( key, _end ) < 0 ) {
               return;
            }
         }
         throw new IllegalArgumentException();
      }
   }
   
   protected static <V> int binarySearch( final IntObjectEntry<V>[] list, final int end, final int key ) {
      int low = 0;
      int high = end - 1;

      while( low <= high ) {
         int mid = (low + high) >>> 1;
         int midVal = list[mid].getKey();

         if( midVal < key )
            low = mid + 1;
         else if( midVal > key )
            high = mid - 1;
         else
            return mid; // key found
      }
      return -(low + 1); // key not found.

   }
}
