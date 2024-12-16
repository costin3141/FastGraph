package dev.costin.fastcollections.tools;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Consumer;

import dev.costin.fastcollections.IntCollection;
import dev.costin.fastcollections.IntComparator;
import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.IntPredicate;
import dev.costin.fastcollections.lists.IntList;
import dev.costin.fastcollections.lists.impl.IntArrayList;
import dev.costin.fastcollections.maps.IntDoubleMap;
import dev.costin.fastcollections.maps.IntIntMap;
import dev.costin.fastcollections.maps.IntLongMap;
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

   public static IntGrowingSet newIntRangeSetWithElements( int... elements ) {
      int min = elements[0];
      int max = min;
      for( int i = 1; i < elements.length; i++ ) {
         if( elements[i] < min ) {
            min = elements[i];
         }
         else if( elements[i] > max ) {
            max = elements[i];
         }
      }

      final IntGrowingSet set = new IntGrowingSet( min, max, elements.length + 1 );
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

   public static <T> IntObjectMap<T> unmodifiableMap( final IntObjectMap<T> map ) {
      if( map instanceof UnmodifiableIntObjectMap ) {
         return map;
      }
      return new UnmodifiableIntObjectMap<T>( map );
   }
   
   public static IntIntMap unmodifiableMap( final IntIntMap map ) {
      if( map instanceof UnmodifiableIntIntMap ) {
         return map;
      }
      return new UnmodifiableIntIntMap( map );
   }
   
   public static IntLongMap unmodifiableMap( final IntLongMap map ) {
      if( map instanceof UnmodifiableIntLongMap ) {
         return map;
      }
      return new UnmodifiableIntLongMap( map );
   }
   
   public static IntDoubleMap unmodifiableMap( final IntDoubleMap map ) {
      if( map instanceof UnmodifiableIntDoubleMap ) {
         return map;
      }
      return new UnmodifiableIntDoubleMap( map );
   }

   public static IntSet unmodifiableIntSet( final IntSet c ) {
      if( c instanceof UnmodifiableIntSet ) {
         return c;
      }
      return new UnmodifiableIntSet( c );
   }
   
   public static IntList unmodifiableIntList( final IntList c ) {
      if( c instanceof UnmodifiableIntList ) {
         return c;
      }
      return new UnmodifiableIntList( c );
   }
   
   public static IntCollection unmodifiableIntCollection( final IntCollection c ) {
      if( c instanceof UnmodifiableIntCollection ) {
         return c;
      }
      return new UnmodifiableIntCollection( c );
   }
   
   public static IntIterator unmodifiableIntIterator( IntIterator itr ) {
      return new ReadOnlyIntIterator( itr );
   }
   
   public static <T> Iterator<T> unmodifiableIterator( Iterator<T> itr ) {
      return new ReadOnlyIterator<T>( itr );
   }
   
   public static void shuffle( IntList list, Random rnd ) {
      shuffle( list, 0, list.size(), rnd );
   }
   
   /**
    * Shuffles the segment of the list starting with {@code from} inclusive
    * until {@code to} exclusive.
    */
   public static void shuffle( IntList list, final int from, final int to, Random rnd ) {
      if( to <= from ) {
         if( to < from ) {
            throw new IndexOutOfBoundsException();
         }
         return;
      }
      
      final int count = to - from;
      
      if( list instanceof RandomAccess || list.size() < 5 ) {
         for( int i = 0; i < count; i++ ) {
            final int j = rnd.nextInt( count - i ) + i;
   
            if( i != j ) {
               final int iIdx = i + from;
               final int jIdx = j + from;
               
               final int tmp = list.get( iIdx );
   
               list.set( iIdx, list.get( jIdx ) );
               list.set( jIdx, tmp );
            }
         }
      }
      else {
         throw new UnsupportedOperationException( "Currently not supported as IntList interface has no iterator capable of setting values." );
//         int[] a = new int[count];
//         
//         IntIterator itr = list.intIterator();
//         for( int i=0; i < from; i++, itr.nextInt() );
//         
//         for( int i=0; i < count; i++ ) {
//            int v = itr.nextInt();
//            a[i] = v;
//         }
//         
//         for( int i = 0; i < count; i++ ) {
//            final int j = rnd.nextInt( count - i ) + i;
//   
//            if( i != j ) {
//               final int tmp = list.get( i );
//   
//               list.set( i, list.get( j ) );
//               list.set( j, tmp );
//            }
//         }
//         
//         IntIterator itr = list.intIterator();
//         for( int i=0; i < from; i++, itr.nextInt() );
//         for( int i=0; i < count; i++ ) {
//            int v = itr.nextInt();
//            itr.set()
//         }
      }
   }

   /**
    * Removes fast an element from an {@link IntArrayList} by swapping it
    * with the last element and removing the last.
    * <strong>Hence, it is not preserving the order!!!</strong>
    */
   public static void removeFast( final IntList list, final int idx ) {
      if( list instanceof RandomAccess || list.size() < 5 ) {
         final int lastIdx = list.size() - 1;
         
         if( idx == lastIdx ) {
            list.removeLast();
            return;
         }
   
         list.set( idx, list.get( lastIdx ) );
   
         list.removeLast();
      }
      else {
         list.removeIndex( idx );
      }
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
         throw new UnsupportedOperationException();
      }

      @Override
      public boolean removeAll( IntCollection elements ) {
         throw new UnsupportedOperationException();
      }
      
      @Override
      public boolean removeIf( IntPredicate filter ) {
         throw new UnsupportedOperationException();
      }
      
      @Override
      public boolean retainAll( IntCollection elements ) {
         throw new UnsupportedOperationException();
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
         return c.isEmpty();
      }
      
      @Override
      public IntIterator intIterator() {
         return emptyIntIterator;
      }

      @Override
      public void clear() {
         throw new UnsupportedOperationException();
      }

      @Override
      public Iterator<IntCursor> iterator() {
         return Collections.<IntCursor>emptyIterator();
      }

      @Override
      public int hashCode() {
         return 0;
      }

      @Override
      public boolean equals( Object obj ) {
         return this == obj || ((IntCollection) obj).isEmpty();
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
      public int get( int index ) {
         throw new NoSuchElementException();
      }

      @Override
      public int set( int index, int value ) {
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
      public void removeIndex( int index ) {
         throw new NoSuchElementException();
      }

      @Override
      public void sort() {
         throw new UnsupportedOperationException();
      }

      @Override
      public void sort( IntComparator comparator ) {
         throw new UnsupportedOperationException();
      }

      @Override
      public int indexOf( int e ) {
         return -1;
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
         throw new UnsupportedOperationException();
      }

      @Override
      public int get( int key ) {
         throw new NoSuchElementException( "Key " + key + " not found." );
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
         throw new UnsupportedOperationException();
      }
      
      @Override
      public int hashCode() {
         return 0;
      }

      @Override
      public boolean equals( Object obj ) {
         return this == obj || ((IntIntMap) obj).isEmpty();
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
         throw new UnsupportedOperationException();
      }

      @Override
      public double get( int key ) {
         throw new NoSuchElementException( "Key " + key + " not found." );
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
         throw new UnsupportedOperationException();
      }

      @Override
      public int hashCode() {
         return 0;
      }

      @Override
      public boolean equals( Object obj ) {
         return this == obj || ((IntDoubleMap) obj).isEmpty();
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
         throw new UnsupportedOperationException();
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
         throw new UnsupportedOperationException();
      }

      @Override
      public int hashCode() {
         return 0;
      }

      @Override
      public boolean equals( Object obj ) {
         return this == obj || ((IntObjectMap) obj).isEmpty();
      }
   };
   
   private static class UnmodifiableIntIntMap implements IntIntMap {
      
      private final IntIntMap _map;
      
      UnmodifiableIntIntMap( final IntIntMap map ) {
         _map = map;
      }

      @Override
      public Iterator<IntIntEntry> iterator() {
         return new ReadOnlyIterator<>( _map.iterator() );
      }

      @Override
      public boolean containsKey( int key ) {
         return _map.containsKey( key );
      }

      @Override
      public boolean put( int key, int value ) {
         throw new UnsupportedOperationException();
      }

      @Override
      public boolean remove( int key ) {
         throw new UnsupportedOperationException();
      }

      @Override
      public int get( int key ) {
         return _map.get( key );
      }

      @Override
      public int size() {
         return _map.size();
      }

      @Override
      public boolean isEmpty() {
         return _map.isEmpty();
      }

      @Override
      public IntIterator keyIterator() {
         return new ReadOnlyIntIterator( _map.keyIterator() );
      }

      @Override
      public void clear() {
         throw new UnsupportedOperationException();
      }
      
      @Override
      public int hashCode() {
         return _map.hashCode();
      }

      @Override
      public boolean equals( Object obj ) {
         return this == obj || _map.equals( obj );
      }
   }
   
   private static class UnmodifiableIntLongMap implements IntLongMap {
      
      private  final IntLongMap _map;
      
      UnmodifiableIntLongMap( final IntLongMap map ) {
         _map = map;
      }

      @Override
      public Iterator<IntLongEntry> iterator() {
         return new ReadOnlyIterator<>( _map.iterator() );
      }

      @Override
      public boolean containsKey( int key ) {
         return _map.containsKey( key );
      }

      @Override
      public boolean put( int key, long value ) {
         throw new UnsupportedOperationException();
      }

      @Override
      public boolean remove( int key ) {
         throw new UnsupportedOperationException();
      }

      @Override
      public long get( int key ) {
         return _map.get( key );
      }

      @Override
      public int size() {
         return _map.size();
      }

      @Override
      public boolean isEmpty() {
         return _map.isEmpty();
      }

      @Override
      public IntIterator keyIterator() {
         return new ReadOnlyIntIterator( _map.keyIterator() );
      }

      @Override
      public void clear() {
         throw new UnsupportedOperationException();
      }
      
      @Override
      public int hashCode() {
         return _map.hashCode();
      }

      @Override
      public boolean equals( Object obj ) {
         return this == obj || _map.equals( obj );
      }
   }
   
   private static class UnmodifiableIntDoubleMap implements IntDoubleMap {
      
      private  final IntDoubleMap _map;
      
      UnmodifiableIntDoubleMap( final IntDoubleMap map ) {
         _map = map;
      }

      @Override
      public Iterator<IntDoubleEntry> iterator() {
         return new ReadOnlyIterator<>( _map.iterator() );
      }

      @Override
      public boolean containsKey( int key ) {
         return _map.containsKey( key );
      }

      @Override
      public boolean put( int key, double value ) {
         throw new UnsupportedOperationException();
      }

      @Override
      public boolean remove( int key ) {
         throw new UnsupportedOperationException();
      }

      @Override
      public double get( int key ) {
         return _map.get( key );
      }

      @Override
      public int size() {
         return _map.size();
      }

      @Override
      public boolean isEmpty() {
         return _map.isEmpty();
      }

      @Override
      public IntIterator keyIterator() {
         return new ReadOnlyIntIterator( _map.keyIterator() );
      }

      @Override
      public void clear() {
         throw new UnsupportedOperationException();
      }
      
      @Override
      public int hashCode() {
         return _map.hashCode();
      }

      @Override
      public boolean equals( Object obj ) {
         return this == obj || _map.equals( obj );
      }
   }

   private static class UnmodifiableIntObjectMap<T> implements IntObjectMap<T> {

      private final IntObjectMap<T> _map;

      private UnmodifiableIntObjectMap( IntObjectMap<T> map ) {
         _map = map;
      }

      @Override
      public Iterator<IntObjectEntry<T>> iterator() {
         return new ReadOnlyIterator<>( _map.iterator() );
      }

      @Override
      public boolean containsKey( int key ) {
         return _map.containsKey( key );
      }

      @Override
      public T put( int key, T value ) {
         throw new UnsupportedOperationException();
      }

      @Override
      public T remove( int key ) {
         throw new UnsupportedOperationException();
      }

      @Override
      public T get( int key ) {
         return _map.get( key );
      }

      @Override
      public int size() {
         return _map.size();
      }

      @Override
      public boolean isEmpty() {
         return _map.isEmpty();
      }

      @Override
      public IntIterator keyIterator() {
         return new ReadOnlyIntIterator( _map.keyIterator() );
      }

      @Override
      public void clear() {
         throw new UnsupportedOperationException();
      }

      @Override
      public int hashCode() {
         return _map.hashCode();
      }

      @Override
      public boolean equals( Object obj ) {
         return this == obj || _map.equals( obj );
      }

   }

   private static class UnmodifiableIntSet extends UnmodifiableIntCollection implements IntSet {
      
      UnmodifiableIntSet( IntSet c ) {
         super( c );
      }
      
   }
   
   private static class UnmodifiableIntList extends UnmodifiableIntCollection implements IntList {
      
      private final IntList _c;
      
      UnmodifiableIntList( IntList c ) {
         super( c );
         _c = c;
      }

      @Override
      public int getFirst() {
         return _c.getFirst();
      }

      @Override
      public int getLast() {
         return _c.getLast();
      }

      @Override
      public int get( int index ) {
         return _c.get( index );
      }

      @Override
      public int set( int index, int value ) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void removeFirst() {
         throw new UnsupportedOperationException();
      }

      @Override
      public void removeLast() {
         throw new UnsupportedOperationException();
      }
      
      @Override
      public void removeIndex( int index ) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void sort() {
         throw new UnsupportedOperationException();
      }

      @Override
      public void sort( IntComparator comparator ) {
         throw new UnsupportedOperationException();
      }
      
      @Override
      public int indexOf( int e ) {
         return _c.indexOf( e );
      }
   }
   
   private static class UnmodifiableIntCollection implements IntCollection {

      private final IntCollection _c;

      UnmodifiableIntCollection( IntCollection c ) {
         _c = c;
      }

      @Override
      public Iterator<IntCursor> iterator() {
         return new ReadOnlyIterator<>( _c.iterator() );
      }

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
         throw new UnsupportedOperationException();
      }
      
      @Override
      public boolean removeAll( IntCollection elements ) {
         throw new UnsupportedOperationException();
      }
      
      @Override
      public boolean removeIf( IntPredicate filter ) {
         throw new UnsupportedOperationException();
      }
      
      @Override
      public boolean retainAll( IntCollection elements ) {
         throw new UnsupportedOperationException();
      }

      @Override
      public int size() {
         return _c.size();
      }

      @Override
      public boolean isEmpty() {
         return _c.isEmpty();
      }

      @Override
      public boolean contains( int value ) {
         return _c.contains( value );
      }

      @Override
      public boolean containsAll( IntCollection c ) {
         return _c.containsAll( c );
      }

      @Override
      public IntIterator intIterator() {
         return new ReadOnlyIntIterator( _c.intIterator() );
      }

      @Override
      public void clear() {
         throw new UnsupportedOperationException();
      }

      @Override
      public int hashCode() {
         return _c.hashCode();
      }

      @Override
      public boolean equals( Object obj ) {
         return this == obj || _c.equals( obj );
      }
   }

   private static class ReadOnlyIntIterator implements IntIterator {

      private final IntIterator _itr;

      ReadOnlyIntIterator( final IntIterator itr ) {
         _itr = itr;
      }

      @Override
      public int nextInt() {
         return _itr.nextInt();
      }

      @Override
      public boolean hasNext() {
         return _itr.hasNext();
      }

      @Override
      public void remove() {
         throw new UnsupportedOperationException();
      }

   }

   private static class ReadOnlyIterator<T> implements Iterator<T> {

      private final Iterator<T> _itr;

      ReadOnlyIterator( final Iterator<T> itr ) {
         _itr = itr;
      }

      @Override
      public T next() {
         return _itr.next();
      }

      @Override
      public boolean hasNext() {
         return _itr.hasNext();
      }

      @Override
      public void remove() {
         throw new UnsupportedOperationException();
      }

      public void forEachRemaining( Consumer<? super T> action ) {
         _itr.forEachRemaining( action );
      }

   }
   
   public static void main( String[] args ) {
      IntList l = new IntArrayList();
      
      l.add( 1 );
      l.add( 2 );
      l.add( 3 );
      l.add( 4 );
      l.add( 5 );
      l.add( 6 );
      l.add( 7 );
      
      shuffle( l, 7, 6, new Random() );
      
      System.out.println( l );
   }
}
