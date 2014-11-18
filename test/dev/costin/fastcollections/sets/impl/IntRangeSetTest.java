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
		   final int v = iter.nextInt();
		   iter.remove();
		   c++;
	   }
	   
	   assertTrue( c==n );
	   assertTrue( intSet.size()==0 );
	   
   }
}
