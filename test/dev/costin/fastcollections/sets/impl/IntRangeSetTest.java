package dev.costin.fastcollections.sets.impl;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.sets.IntSet;

public class IntRangeSetTest {

   boolean equals( IntSet intSet, Set<Integer> javaSet ) {
      boolean equals = true;

      for( IntCursor cursor : intSet ) {
         if( !javaSet.contains( Integer.valueOf( cursor.value() ) ) ) {
            System.err.println( "javaSet does not contain " + cursor.value() );
            equals = false;
            break;
         }
      }

      if( equals ) {
         for( Integer i : javaSet ) {
            if( !intSet.contains( i.intValue() ) ) {
               System.err.println( "intSet does not contain " + i.intValue() );
               equals = false;
               break;
            }
         }
      }

      return equals;
   }

   @Test
   public void testRandom() {
      final int n = 1000;
      final Random rnd = new Random( new Date().getTime() );

      final IntRangeSet intSet = new IntRangeSet( n );
      final Set<Integer> javaSet = new HashSet<>( n );

      intSet._modCounter++;

      for( int i = 0; i < n; i++ ) {
         final int val = rnd.nextInt( n );

         intSet.add( val );
         javaSet.add( val );
      }

      assertTrue( equals( intSet, javaSet ) );

      for( int i = 0; i < n / 3; i++ ) {
         final int val = rnd.nextInt( n );

         intSet.remove( val );
         javaSet.remove( val );
      }

      assertTrue( equals( intSet, javaSet ) );

      for( int i = 0; i < n / 3; i++ ) {
         final int val = rnd.nextInt( n );

         intSet.add( val );
         javaSet.add( val );
      }

      assertTrue( equals( intSet, javaSet ) );

      int j = 0;
      for( IntIterator iter = intSet.intIterator(); j < n / 4 && iter.hasNext(); j++ ) {
         final int val = iter.nextInt();

         iter.remove();
         javaSet.remove( val );
      }

      assertTrue( equals( intSet, javaSet ) );
   }

   @Test
   public void testRemoveAllByIterator() {
	   final int n = 1000;
	   
	   final IntRangeSet intSet = new IntRangeSet( n );
	   
	   for( int i=0; i<n; i++ ) {
		   intSet.add(i);
	   }
	   
	   assertTrue( intSet.size()==n );
	   
	   int c = 0;
	   for( IntIterator iter = intSet.intIterator(); iter.hasNext(); ) {
		   iter.nextInt();
		   iter.remove();
		   c++;
	   }
	   
	   assertTrue( c==n );
	   assertTrue( intSet.size()==0 );
	   
   }
   
   static void testIntSet( final int n, final int repeats, final int[] randomInts ) {
      final long start = System.currentTimeMillis();
      final IntSet set = new IntRangeSet( 0, n-1, n*3/2 );
      int c = 0;
      
      for( int i=0; i<repeats; i++ ) {
         for( int j=0; j<n; j++ ) {
            set.add( randomInts[j] );
         }
         
         for( int j=0; j<n; j++ ) {
            if( set.contains( j ) ) {
               c++;
            }
         }
      }
      
      System.out.println("time: "+(System.currentTimeMillis()-start)+"   "+c);
   }
   
   static void testHashSet( final int n, final int repeats, final int[] randomInts ) {
      final long start = System.currentTimeMillis();
      final Set<Integer> set = new HashSet<>( n*3/2 );
      int c = 0;
      
      for( int i=0; i<repeats; i++ ) {
         for( int j=0; j<n; j++ ) {
            set.add( Integer.valueOf( randomInts[j] ) );
         }
         
         for( int j=0; j<n; j++ ) {
            if( set.contains( Integer.valueOf( j ) ) ) {
               c++;
            }
         }
      }
      
      System.out.println("time: "+(System.currentTimeMillis()-start)+"   "+c);
   }
   
   public static void main( String[] args ) {
      final int n = 500;
      final int repeats = 100000;
      
      final Random rnd = new Random();
      final int[] randomInts = new int[n];
      for( int j=0; j<n; j++ ) {
         randomInts[j] = Integer.valueOf( rnd.nextInt(n) );
      }
      
      testIntSet( n, repeats, randomInts );
      testIntSet( n, repeats, randomInts );
      testIntSet( n, repeats, randomInts );
      testIntSet( n, repeats, randomInts );
      
//      testHashSet( n, repeats, randomInts );
//      testHashSet( n, repeats, randomInts );
//      testHashSet( n, repeats, randomInts );
//      testHashSet( n, repeats, randomInts );
   }
}
