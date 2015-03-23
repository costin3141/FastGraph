package dev.costin.fastcollections.maps.impl;

import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.Test;

import dev.costin.fastcollections.maps.IntIntMap.IntIntEntry;

public class IntIntGrowingMapTest {

   @Test
   public void testGrowth() {
      final IntIntGrowingMap map = new IntIntGrowingMap(0, 0);
      
      assert !map.containsKey( 0 );
      assert !map.containsKey( -1 );
      assert !map.containsKey( 1 );
      
      map.put( 2, 20 );
      
      assert map.containsKey( 2 );
      assert map.get( 2 ) == 20;
      
      map.put( -2, -20 );
      
      assert map.containsKey( 2 );
      assert map.get( 2 ) == 20;
      assert map.containsKey( -2 );
      assert map.get( -2 ) == -20;
      assert !map.containsKey( 0 );
      assert !map.containsKey( 1 );
      assert !map.containsKey( -1 );
      assert !map.containsKey( 3 );
      assert !map.containsKey( -3 );

      assert !map.remove( -100 );
      assert !map.remove( 100 );
      assert map.remove( -2 );
      assert !map.containsKey( -2 );
      
      try{
         map.get( -2 );
         assert false;
      }
      catch( NoSuchElementException e ) {
      }
      try{
         map.get( -100 );
         assert false;
      }
      catch( NoSuchElementException e ) {
      }
      try{
         map.get( 100 );
         assert false;
      }
      catch( NoSuchElementException e ) {
      }
      
      assert map.get( 2 ) == 20;
   }
   
   @Test
   public void testCopyConstructor() {
      final int n = 12;
      
      final IntIntGrowingMap map = new IntIntGrowingMap();
      for( int i = 0; i < n; i++ ) {
         map.put( i, i );
      }
      
      final IntIntGrowingMap copy1 = new IntIntGrowingMap( map );
      assert copy1.equals( map );
      
      map.remove( 0 );
      map.remove( 11 );
      
      final IntIntGrowingMap copy2 = new IntIntGrowingMap( map );
      assert copy2.equals( map );
      
      map.put( -1, -1 );
      map.put( 20, 20 );
      
      final IntIntGrowingMap copy3 = new IntIntGrowingMap( map );
      assert copy3.equals( map );
   }

   static void testIntIntMap2( final int n, final int repeats, final int[] rnd ) {
      final long start = System.currentTimeMillis();
      final IntIntGrowingMap map = new IntIntGrowingMap(0, n-1, n);
      
      long c = 0;
      
      for( int i=0; i<repeats; i++ ) {
         
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
