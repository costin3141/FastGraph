package dev.costin.fastcollections.maps.impl;

import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.Random;

import org.junit.Test;

import dev.costin.fastcollections.maps.IntIntMap.IntIntEntry;

public class IntIntRangeMapTest {

   @Test
   public void test() {
      fail( "Not yet implemented" );
   }

   static void testIntIntMap2( final int n, final int repeats, final int[] rnd ) {
      final long start = System.currentTimeMillis();
      final IntIntRangeMap map = new IntIntRangeMap(0, n-1, n);
      
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
