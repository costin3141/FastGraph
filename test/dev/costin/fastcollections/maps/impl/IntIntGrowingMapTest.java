package dev.costin.fastcollections.maps.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.Test;

import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.maps.IntIntMap.IntIntEntry;
import dev.costin.fastcollections.tools.FastCollections;

public class IntIntGrowingMapTest {

   @Test
   public void testGrowth() {
      final IntIntGrowingMap map = new IntIntGrowingMap(0, 0);
      
      assertTrue( !map.containsKey( 0 ) );
      assertTrue( !map.containsKey( -1 ) );
      assertTrue( !map.containsKey( 1 ) );
      
      map.put( 2, 20 );
      
      assertTrue( map.containsKey( 2 ) );
      assertTrue( map.get( 2 ) == 20 );
      
      map.put( -2, -20 );
      
      assertTrue( map.containsKey( 2 ) );
      assertTrue( map.get( 2 ) == 20 );
      assertTrue( map.containsKey( -2 ) );
      assertTrue( map.get( -2 ) == -20 );
      assertTrue( !map.containsKey( 0 ) );
      assertTrue( !map.containsKey( 1 ) );
      assertTrue( !map.containsKey( -1 ) );
      assertTrue( !map.containsKey( 3 ) );
      assertTrue( !map.containsKey( -3 ) );

      assertTrue( !map.remove( -100 ) );
      assertTrue( !map.remove( 100 ) );
      assertTrue( map.remove( -2 ) );
      assertTrue( !map.containsKey( -2 ) );
      
      try{
         map.get( -2 );
         fail();
      }
      catch( NoSuchElementException e ) {
      }
      try{
         map.get( -100 );
         fail();
      }
      catch( NoSuchElementException e ) {
      }
      try{
         map.get( 100 );
         fail();
      }
      catch( NoSuchElementException e ) {
      }
      
      assertTrue( map.get( 2 ) == 20 );
   }
   
   @Test
   public void testGrowthDefault() {
      final IntIntGrowingMap map = new IntIntGrowingMap();
      
      assertTrue( !map.containsKey( 0 ) );
      assertTrue( !map.containsKey( -1 ) );
      assertTrue( !map.containsKey( 1 ) );
      
      map.put( 2, 20 );
      
      assertTrue( map.containsKey( 2 ) );
      assertTrue( map.get( 2 ) == 20 );
      
      map.put( -2, -20 );
      
      assertTrue( map.containsKey( 2 ) );
      assertTrue( map.get( 2 ) == 20 );
      assertTrue( map.containsKey( -2 ) );
      assertTrue( map.get( -2 ) == -20 );
      assertTrue( !map.containsKey( 0 ) );
      assertTrue( !map.containsKey( 1 ) );
      assertTrue( !map.containsKey( -1 ) );
      assertTrue( !map.containsKey( 3 ) );
      assertTrue( !map.containsKey( -3 ) );
      
      assertTrue( !map.remove( -100 ) );
      assertTrue( !map.remove( 100 ) );
      assertTrue( map.remove( -2 ) );
      assertTrue( !map.containsKey( -2 ) );
      
      try{
         map.get( -2 );
         fail();
      }
      catch( NoSuchElementException e ) {
      }
      try{
         map.get( -100 );
         fail();
      }
      catch( NoSuchElementException e ) {
      }
      try{
         map.get( 100 );
         fail();
      }
      catch( NoSuchElementException e ) {
      }
      
      assertTrue( map.get( 2 ) == 20 );
   }
   
   @Test
   public void testCopyConstructor() {
      final int n = 12;
      
      final IntIntGrowingMap map = new IntIntGrowingMap();
      for( int i = 0; i < n; i++ ) {
         map.put( i, i );
      }
      
      final IntIntGrowingMap copy1 = new IntIntGrowingMap( map );
      assertTrue( copy1.equals( map ) );
      
      map.remove( 0 );
      map.remove( 11 );
      
      final IntIntGrowingMap copy2 = new IntIntGrowingMap( map );
      assertTrue( copy2.equals( map ) );
      
      map.put( -1, -1 );
      map.put( 20, 20 );
      
      final IntIntGrowingMap copy3 = new IntIntGrowingMap( map );
      assertTrue( copy3.equals( map ) );
   }
   
   @Test
   public void testEquals() {
      final IntIntGrowingMap map1 = new IntIntGrowingMap();
      
      map1.put( 1, 1 );
      map1.put( 2, 2 );
      map1.put( 3, 3 );
      map1.put( 4, 4 );
      
      final IntIntGrowingMap map2 = new IntIntGrowingMap();
      
      map2.put( 1, 1 );
      map2.put( 3, 3 );
      map2.put( 4, 4 );
      map2.put( 2, 2 );
      
      assertEquals( map1, map2 );
      
      map2.put( 2,  20 );
      
      assertNotEquals( map1, map2 );
      
      map2.remove( 2 );
      assertNotEquals( map1, map2 );
      
      map1.remove( 2 );
      assertEquals( map1, map2 );
   }
   
   @Test
   public void testExtremePositiveValues() {
      IntIntGrowingMap map = new IntIntGrowingMap();
      
      map.put( Integer.MAX_VALUE, 0 );
      map.put( Integer.MAX_VALUE-1, 1 );
      map.put( Integer.MAX_VALUE-100, 2 );
      
      IntIterator i = map.keyIterator();
      assertEquals( Integer.MAX_VALUE, i.nextInt() );
      i.remove();
      assertFalse( map.containsKey( Integer.MAX_VALUE ) );
      
      int nextKey1 = i.nextInt();
      assertTrue( nextKey1 == Integer.MAX_VALUE-1 || nextKey1 == Integer.MAX_VALUE-100 );
      int nextKey2 = i.nextInt();
      assertTrue( nextKey1 != nextKey2 );
      assertTrue( nextKey2 == Integer.MAX_VALUE-1 || nextKey2 == Integer.MAX_VALUE-100 );
      assertFalse( i.hasNext() );
      assertEquals( 2, map.size() );
      
      map = new IntIntGrowingMap();
      
      map.put( Integer.MAX_VALUE - FastCollections.DEFAULT_LIST_CAPACITY, 0 );
      map.put( Integer.MAX_VALUE, 1 );
      map.put( Integer.MAX_VALUE-100, 2 );
      
      i = map.keyIterator();
      assertEquals( Integer.MAX_VALUE - FastCollections.DEFAULT_LIST_CAPACITY, i.nextInt() );
      i.remove();
      assertFalse( map.containsKey( Integer.MAX_VALUE - FastCollections.DEFAULT_LIST_CAPACITY ) );
      
      nextKey1 = i.nextInt();
      assertTrue( nextKey1 == Integer.MAX_VALUE || nextKey1 == Integer.MAX_VALUE-100 );
      nextKey2 = i.nextInt();
      assertTrue( nextKey1 != nextKey2 );
      assertTrue( nextKey2 == Integer.MAX_VALUE || nextKey2 == Integer.MAX_VALUE-100 );
      assertFalse( i.hasNext() );
      assertEquals( 2, map.size() );
      
   }

   static void testIntIntMap2( final int n, final int repeats, final int[] rnd ) {
      final long start = System.currentTimeMillis();
//      final IntIntGrowingMap map = new IntIntGrowingMap(0, n-1, n);
      
      long c = 0;
      
      for( int i=0; i<repeats; i++ ) {
         final IntIntGrowingMap map = new IntIntGrowingMap();
         
         for( int j=0; j<n; j++ ) {
            if(map.put( rnd[j], j )) {
               c++;
            }
         }
         
//         for( int j=0; j<n; j++ ) {
//            c += map.get( j );
//         }
//         for( int j=0; j<n; j++ ) {
//            c += map.get( j );
//         }
//         for( int j=0; j<n; j++ ) {
//            c -= 2*map.get( j );
//         }
         for( IntIntEntry entry : map ) {
            c += entry.getValue();
         }
         for( IntIntEntry entry : map ) {
            c += entry.getValue();
         }
         for( IntIntEntry entry : map ) {
            c -= 2*entry.getValue();
         }
         
         for( int j=0; j<n; j++ ) {
            if( map.remove( j ) ) {
               c--;
            }
         }
//         for( Iterator<IntIntEntry> iter=map.iterator(); iter.hasNext(); ) {
//            iter.next();
//            iter.remove();
//            c --;
//         }
//         c-=map.size();
//         map.clear();
         
         c += map.size();
      }
      
      System.out.println("time: "+(System.currentTimeMillis()-start)+"    "+c);
   }
   
   public static void main( String[] args ) {
      final int n = 2000;
      final int repeats = 100000;
      
      final Random rnd = new Random();
      final int[] randomInts = new int[n];
      for( int j=0; j<n; j++ ) {
         randomInts[j] = Integer.valueOf( rnd.nextInt(n) );
      }
      
//      testIntIntMap( n, repeats, randomInts );
//      testIntIntMap( n, repeats, randomInts );
//      testIntIntMap( n, repeats, randomInts );
//      testIntIntMap( n, repeats, randomInts );
//      testIntIntMap( n, repeats, randomInts );
      
      testIntIntMap2( n, repeats, randomInts );
      testIntIntMap2( n, repeats, randomInts );
      testIntIntMap2( n, repeats, randomInts );
      testIntIntMap2( n, repeats, randomInts );
      testIntIntMap2( n, repeats, randomInts );
      
   }
}
