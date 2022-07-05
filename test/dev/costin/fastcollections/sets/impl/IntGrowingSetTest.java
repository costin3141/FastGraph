package dev.costin.fastcollections.sets.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.IntPredicate;
import dev.costin.fastcollections.bridging.collections.IndexedObjectSet;
import dev.costin.fastcollections.bridging.impl.IndexedWrapper;
import dev.costin.fastcollections.bridging.impl.ObjectIndexer;
import dev.costin.fastcollections.sets.IntSet;

public class IntGrowingSetTest {

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
   public void testGrowth() {
      final IntGrowingSet set = new IntGrowingSet(0,0,1);
      
      assertTrue( !set.contains( 0 ) );
      assertTrue( !set.contains( 1 ) );
      assertTrue( !set.contains( -1 ) );
      
      set.add( 2 );
      
      assertTrue( set.contains( 2 ) );
      
      set.add( 0 );
      
      assertTrue( set.contains( 0 ) );
      
      set.add( -2 );
      
      assertTrue( set.contains( -2 ) );
      assertTrue( !set.contains( -1 ) );
      assertTrue( !set.contains( -100 ) );
      
      assertTrue( set.remove( -2 ) );
      assertTrue( !set.contains( -2 ) );
   }
   
   @Test
   public void testGrowthDefault() {
      final IntGrowingSet set = new IntGrowingSet();
      
      assertTrue( !set.contains( 0 ) );
      assertTrue( !set.contains( 1 ) );
      assertTrue( !set.contains( -1 ) );
      
      set.add( 2 );
      
      assertTrue( set.contains( 2 ) );
      
      set.add( 0 );
      
      assertTrue( set.contains( 0 ) );
      
      set.add( -2 );
      
      assertTrue( set.contains( -2 ) );
      assertTrue( !set.contains( -1 ) );
      assertTrue( !set.contains( -100 ) );
      
      assertTrue( set.remove( -2 ) );
      assertTrue( !set.contains( -2 ) );
   }

   @Test
   public void testRandom() {
      final int n = 1000;
      final Random rnd = new Random( new Date().getTime() );

      final IntGrowingSet intSet = new IntGrowingSet( n );
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
	   
	   final IntGrowingSet intSet = new IntGrowingSet( n );
	   
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
   
   @Test
   public void testCopyConstructor() {
      IntGrowingSet set = new IntGrowingSet(1);
      
      set.add( 1 );
      set.add( 2 );
      set.add( 0 );
      set.remove( 0 );
      
      IntGrowingSet set2 = new IntGrowingSet( set );
      
      set2.remove( 1 );
      
      assertTrue( set2.size() == 1 );
      assertTrue( set2.contains( 2 ) );
      assertFalse( set2.contains( 1 ) );
      assertFalse( set2.contains( 0 ) );
      assertFalse( set2.contains( 3 ) );
   }
   
   @Test
   public void testEquals() {
      IntGrowingSet set1 = new IntGrowingSet();
      
      set1.add( 1 );
      set1.add( 2 );
      set1.add( 3 );
      
      IntGrowingSet set2 = new IntGrowingSet();
      
      set2.add( 1 );
      set2.add( 3 );
      set2.add( 2 );
      
      assertEquals( set1, set2 );
      
      set2.remove( 3 );
      assertNotEquals( set1, set2 );
      
      set1.remove( 3 );
      assertEquals( set1, set2 );
   }
   
   @Test
   public void testRemoveIf() {
      IntLinkedGrowingSet s = new IntLinkedGrowingSet();
      
      s.add( 100 );
      s.add( 7 );
      s.add( 0 );
      
      System.out.println( s.removeIf( i -> i > 1 ) );

      assertEquals( 1, s.size() );
      assertTrue( s.contains( 0 ) );
      
      s.add( 7 );
      System.out.println( s.removeIf( IntPredicate.not( i -> i == 7 ) ) );
      
      assertEquals( 1, s.size() );
      assertTrue( s.contains( 7 ) );
   }
   
   static IntSet testIntSet( final int n, final int repeats, final int[] randomInts ) {
      final long start = System.currentTimeMillis();
//      final IntSet set = new IntGrowingSet( 0, n-1, n*3/2 );
      final IntSet set = new IntLinkedGrowingSet( 0, n-1 );
      int c = 0;
      
      for( int i=0; i<repeats; i++ ) {
         for( int j=0; j<n; j++ ) {
            set.add( randomInts[j] );
         }
         
//         for( int j=0; j<n; j++ ) {
//            if( set.contains( j ) ) {
//               c++;
//            }
//         }
      }
      
      System.out.println("time: "+(System.currentTimeMillis()-start)+"   "+c);
      return set;
   }
   
   static void testIntSetIteration( final int repeats, final IntSet set ) {
      final long start = System.currentTimeMillis();
//      final IntSet set = new IntGrowingSet( 0, n-1, n*3/2 );
      long x = 0;
      for( int r = 0; r<repeats; r++ ) {
         for( IntIterator i = set.intIterator(); i.hasNext(); ) {
            x += i.nextInt();
         }
      }
      
      System.out.println("time: "+(System.currentTimeMillis()-start)+"   "+x);
   }
   
   static void testHashSet( final int n, final int repeats, final int[] randomInts ) {
      final ArrayList<Integer> objects = new ArrayList<>(randomInts.length);
      for( int i=0; i<randomInts.length; i++ ) {
         objects.add( Integer.valueOf( randomInts[i] ) );
      }
      final long start = System.currentTimeMillis();
      final Set<Integer> set = new HashSet<>( n*3/2 );
      int c = 0;
      
      for( int i=0; i<repeats; i++ ) {
         for( int j=0; j<n; j++ ) {
            set.add( objects.get( j ) );
         }
         
         for( int j=0; j<n; j++ ) {
            if( set.contains( objects.get( j ) ) ) {
               c++;
            }
         }
      }
      
      System.out.println("time: "+(System.currentTimeMillis()-start)+"   "+c);
   }
   
   static void testWrappedSet( final int n, final int repeats, final int[] randomInts ) {
      final ArrayList<Integer> objects = new ArrayList<>(randomInts.length);
      for( int i=0; i<randomInts.length; i++ ) {
         objects.add( Integer.valueOf( randomInts[i] ) );
      }
      final ObjectIndexer<Integer> ow = new ObjectIndexer<>( objects, 0 );
      final List<IndexedWrapper<Integer>> wrappedObjects = ow.getWrapperList();
      final long start = System.currentTimeMillis();
      final Set<IndexedWrapper<Integer>> set = new IndexedObjectSet<>( ow );
      int c = 0;
      
      for( int i=0; i<repeats; i++ ) {
         for( int j=0; j<n; j++ ) {
            set.add( wrappedObjects.get( j ) );
         }
         
         for( int j=0; j<n; j++ ) {
            if( set.contains( wrappedObjects.get( j ) ) ) {
               c++;
            }
         }
      }
      
      System.out.println("time: "+(System.currentTimeMillis()-start)+"   "+c);
   }
   
   public static void main( String[] args ) {
      IntGrowingSet set = new IntGrowingSet(1);
      
      set.add( 1 );
      set.add( 2 );
      set.add( 0 );
      set.remove( 0 );
      
      IntGrowingSet set2 = new IntGrowingSet( set );
      
      set2.remove( 1 );
      
      System.out.println( "contains -1" + set.contains( -1 ) );
      System.out.println( "contains 0" + set.contains( 0 ) );
      System.out.println( "contains 1" + set.contains( 1 ) );
      
      set.remove( -1 );
      set.remove( 0 );
      
      
      final int n = 2000;
      final int repeats = 1000000;
      
      final Random rnd = new Random( 50991 );
      final int[] randomInts = new int[n];
      for( int j=0; j<n; j++ ) {
         randomInts[j] = Integer.valueOf( rnd.nextInt(n) );
      }
      
      IntSet s = testIntSet( n, repeats, randomInts );
      testIntSetIteration( repeats, s );
      s = testIntSet( n, repeats, randomInts );
      testIntSetIteration( repeats, s );
      s = testIntSet( n, repeats, randomInts );
      testIntSetIteration( repeats, s );
      s = testIntSet( n, repeats, randomInts );
      testIntSetIteration( repeats, s );
      s = testIntSet( n, repeats, randomInts );
      testIntSetIteration( repeats, s );
      
      
//      testHashSet( n, repeats, randomInts );
//      testHashSet( n, repeats, randomInts );
//      testHashSet( n, repeats, randomInts );
//      testHashSet( n, repeats, randomInts );
      
//      testWrappedSet( n, repeats, randomInts);
//      testWrappedSet( n, repeats, randomInts);
//      testWrappedSet( n, repeats, randomInts);
//      testWrappedSet( n, repeats, randomInts);
   }
}
