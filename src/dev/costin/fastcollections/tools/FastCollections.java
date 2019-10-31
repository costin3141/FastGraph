package dev.costin.fastcollections.tools;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import dev.costin.fastcollections.IntCollection;
import dev.costin.fastcollections.IntComparator;
import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.lists.IntList;
import dev.costin.fastcollections.maps.IntDoubleMap;
import dev.costin.fastcollections.maps.IntIntMap;
import dev.costin.fastcollections.maps.IntObjectMap;
import dev.costin.fastcollections.sets.IntSet;
import dev.costin.fastcollections.sets.impl.IntGrowingSet;

/**
 * 
 * Utility methods for creating various collections.
 * 
 * @author Stefan C. Ionescu
 *
 */
public class FastCollections {
   
   public static final int DEFAULT_LIST_CAPACITY = 8;

   public static IntGrowingSet newIntRangeSetWithElements( int...elements ) {
      int min = elements[0];
      int max = min;
      for( int i=1; i<elements.length; i++ ) {
         if( elements[i] < min ) {
            min = elements[i];
         }
         else if( elements[i] > max ) {
            max = elements[i];
         }
      }
      
      final IntGrowingSet set = new IntGrowingSet(min, max, elements.length+1);
      set.addAll( elements );
      
      return set;
   }
   
   public static IntCollection emptyIntCollection() {
      return EMPTY_INT_COLLECTION;
   }

   public static IntList emptyIntList() {
      return EMPTY_INT_LIST;
   }
   
   public static IntSet emptyIntSet() {
      return EMPTY_INT_SET;
   }
   
   public static IntIntMap emptyIntIntMap() {
      return EMPTY_INT_INT_MAP;
   }
   
   public static IntDoubleMap emptyIntDoubleMap() {
      return EMPTY_INT_DOUBLE_MAP;
   }
   
   @SuppressWarnings( "unchecked" )
   public static <T> IntObjectMap<T> emptyIntObjectMap() {
      return (IntObjectMap<T>) EMPTY_INT_OBJECT_MAP;
   }
   
   
   
   ////////////////////////////////////////////////////////////////
   
   
   private static IntIterator emptyIntIterator = new IntIterator() {

      @Override
      public int nextInt() {
         throw new NoSuchElementException();
      }

      @Override
      public boolean hasNext() {
         return false;
      }

      @Override
      public void remove() {
         throw new NoSuchElementException();
      }
      
   };
   
   private static IntCollection EMPTY_INT_COLLECTION = new EmptyIntCollection();
   
   private static class EmptyIntCollection implements IntCollection {

      @Override
      public boolean add( int value ) {
         throw new UnsupportedOperationException();
      }

      @Override
      public boolean addAll( IntCollection elements ) {
         throw new UnsupportedOperationException();
      }

      @Override
      public boolean addAll( int... elements ) {
         throw new UnsupportedOperationException();
      }

      @Override
      public boolean remove( int value ) {
         return false;
      }

      @Override
      public int size() {
         return 0;
      }

      @Override
      public boolean isEmpty() {
         return true;
      }

      @Override
      public boolean contains( int value ) {
         return false;
      }

      @Override
      public boolean containsAll( IntCollection c ) {
         return false;
      }

      @Override
      public IntIterator intIterator() {
         return emptyIntIterator;
      }

      @Override
      public void clear() {
      }

      @Override
      public Iterator<IntCursor> iterator() {
         return Collections.<IntCursor>emptyIterator();
      }
      
   }
   
   private static IntList EMPTY_INT_LIST = new EmptyIntList();
   
   private static class EmptyIntList extends EmptyIntCollection implements IntList {

      @Override
      public int getFirst() {
         throw new NoSuchElementException();
      }

      @Override
      public int getLast() {
         throw new NoSuchElementException();
      }

      @Override
      public int get(int index) {
         throw new NoSuchElementException();
      }
      
      @Override
      public int set(int index, int value) {
         throw new NoSuchElementException();
      }

      @Override
      public void removeFirst() {
         throw new NoSuchElementException();
      }

      @Override
      public void removeLast() {
         throw new NoSuchElementException();
      }

      @Override
      public void sort() {
      }

      @Override
      public void sort( IntComparator comparator ) {
      }
      
   }
   
   private static IntSet EMPTY_INT_SET = new EmptyIntSet();
   
   private static class EmptyIntSet extends EmptyIntCollection implements IntSet {
      
   }
   
   private static IntIntMap EMPTY_INT_INT_MAP = new IntIntMap() {

      @Override
      public Iterator<IntIntEntry> iterator() {
         return Collections.<IntIntEntry>emptyIterator();
      }

      @Override
      public boolean containsKey( int key ) {
         return false;
      }

      @Override
      public boolean put( int key, int value ) {
         throw new UnsupportedOperationException();
      }

      @Override
      public boolean remove( int key ) {
         return false;
      }

      @Override
      public int get( int key ) {
         throw new NoSuchElementException("Key "+key+" not found.");
      }

      @Override
      public int size() {
         return 0;
      }

      @Override
      public boolean isEmpty() {
         return true;
      }

      @Override
      public IntIterator keyIterator() {
         return emptyIntIterator;
      }

      @Override
      public void clear() {
      }
      
   };
   
   private static IntDoubleMap EMPTY_INT_DOUBLE_MAP = new IntDoubleMap() {

      @Override
      public Iterator<IntDoubleEntry> iterator() {
         return Collections.<IntDoubleEntry>emptyIterator();
      }

      @Override
      public boolean containsKey( int key ) {
         return false;
      }

      @Override
      public boolean put( int key, double value ) {
         throw new UnsupportedOperationException();
      }

      @Override
      public boolean remove( int key ) {
         return false;
      }

      @Override
      public double get( int key ) {
         throw new NoSuchElementException("Key "+key+" not found.");
      }

      @Override
      public int size() {
         return 0;
      }

      @Override
      public boolean isEmpty() {
         return true;
      }

      @Override
      public IntIterator keyIterator() {
         return emptyIntIterator;
      }

      @Override
      public void clear() {
      }
      
   };
   
   @SuppressWarnings( "rawtypes" )
   private static IntObjectMap EMPTY_INT_OBJECT_MAP = new IntObjectMap() {
      
      @Override
      public Iterator<IntObjectEntry> iterator() {
         return Collections.<IntObjectEntry>emptyIterator();
      }
      
      @Override
      public boolean containsKey( int key ) {
         return false;
      }
      
      @Override
      public Object put( int key, Object value ) {
         throw new UnsupportedOperationException();
      }
      
      @Override
      public Object remove( int key ) {
         return null;
      }
      
      @Override
      public Object get( int key ) {
         return null;
      }
      
      @Override
      public int size() {
         return 0;
      }
      
      @Override
      public boolean isEmpty() {
         return true;
      }
      
      @Override
      public IntIterator keyIterator() {
         return emptyIntIterator;
      }
      
      @Override
      public void clear() {
      }
      
   };
}
