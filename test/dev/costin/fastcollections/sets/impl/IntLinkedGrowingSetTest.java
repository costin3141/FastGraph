package dev.costin.fastcollections.sets.impl;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.IntPredicate;

import static dev.costin.fastcollections.tools.CollectionUtils.*;

public class IntLinkedGrowingSetTest {

   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
   }

   @AfterClass
   public static void tearDownAfterClass() throws Exception {
   }

   @Test
   public void testLongLoHiOperations() {
      System.out.println( toBin( Integer.MIN_VALUE) );
      long l;
      l = combineToLong( Integer.MIN_VALUE, Integer.MIN_VALUE );
      System.out.println( toBin( l ) );
      System.out.println( toBin( getUpperInt( l ) ) + " " + toBin( getLowerInt( l ) ));
      assertEquals( Integer.MIN_VALUE, getUpperInt( l ) );
      assertEquals( Integer.MIN_VALUE, getLowerInt( l ) );

      l = setUpperInt( l, -1 );
      System.out.println( toBin( l ) );
      System.out.println( toBin( getUpperInt( l ) ) + " " + toBin( getLowerInt( l ) ));
      assertEquals( -1, getUpperInt( l ) );
      assertEquals( Integer.MIN_VALUE, getLowerInt( l ) );
      
      l = setLowerInt( l, -1 );
      System.out.println( toBin( l ) );
      System.out.println( toBin( getUpperInt( l ) ) + " " + toBin( getLowerInt( l ) ));
      assertEquals( -1, getUpperInt( l ) );
      assertEquals( -1, getLowerInt( l ) );
      
      l = setUpperInt( l, Integer.MIN_VALUE );
      System.out.println( toBin( l ) );
      System.out.println( toBin( getUpperInt( l ) ) + " " + toBin( getLowerInt( l ) ));
      assertEquals( Integer.MIN_VALUE, getUpperInt( l ) );
      assertEquals( -1, getLowerInt( l ) );
      
      l = setLowerInt( l,  Integer.MIN_VALUE );
      System.out.println( toBin( l ) );
      System.out.println( toBin( getUpperInt( l ) ) + " " + toBin( getLowerInt( l ) ));
      assertEquals( Integer.MIN_VALUE, getUpperInt( l ) );
      assertEquals( Integer.MIN_VALUE, getLowerInt( l ) );
      
      l = combineToLong( 1, Integer.MIN_VALUE );
      System.out.println( toBin( l ) );
      System.out.println( toBin( getUpperInt( l ) ) + " " + toBin( getLowerInt( l ) ));
      assertEquals( 1, getUpperInt( l ) );
      assertEquals( Integer.MIN_VALUE, getLowerInt( l ) );
      
   }
   
   @Test
   public void testPositves() {
      IntLinkedGrowingSet s = new IntLinkedGrowingSet();
      
      s.add( 1 );
      s.add( 2 );
      
      IntIterator i = s.intIterator();
      
      assertEquals( 1, i.nextInt() );
      assertEquals( 2, i.nextInt() );
      assertFalse( i.hasNext() );
      
      Iterator<IntCursor> itr = s.iterator();
      
      assertEquals( 1, itr.next().value() );
      assertEquals( 2, itr.next().value() );
      assertFalse( itr.hasNext() );
   }
   
   @Test
   public void testNegatives() {
      IntLinkedGrowingSet s = new IntLinkedGrowingSet();
      
      s.add( Integer.MIN_VALUE + 10 );
      s.add( Integer.MIN_VALUE );
      
      IntIterator i = s.intIterator();
      
      assertEquals( Integer.MIN_VALUE + 10, i.nextInt() );
      assertEquals( Integer.MIN_VALUE, i.nextInt() );
      assertFalse( i.hasNext() );
      
      Iterator<IntCursor> itr = s.iterator();
      
      assertEquals( Integer.MIN_VALUE + 10, itr.next().value() );
      assertEquals( Integer.MIN_VALUE, itr.next().value() );
      assertFalse( itr.hasNext() );
   }
   
   @Test
   public void testRemoveFirstElement() {
      IntLinkedGrowingSet s = new IntLinkedGrowingSet();
      
      s.add( Integer.MIN_VALUE + 100 );
      s.add( Integer.MIN_VALUE + 7 );
      s.add( Integer.MIN_VALUE );
      
      assertTrue( s.contains( Integer.MIN_VALUE + 100 ) );
      assertTrue( s.contains( Integer.MIN_VALUE + 7 ) );
      assertTrue( s.contains( Integer.MIN_VALUE ) );
      
      IntIterator i = s.intIterator();
      
      assertEquals( Integer.MIN_VALUE + 100, i.nextInt() );
      i.remove();
      assertFalse( s.contains( Integer.MIN_VALUE + 100 ) );
      assertEquals( Integer.MIN_VALUE + 7, i.nextInt() );
      assertEquals( Integer.MIN_VALUE, i.nextInt() );
      assertFalse( i.hasNext() );
      
      assertEquals(  2, s.size() );
      
      Iterator<IntCursor> itr = s.iterator();
      
      assertEquals( Integer.MIN_VALUE + 7, itr.next().value() );
      itr.remove();
      assertFalse( s.contains( Integer.MIN_VALUE + 7 ) );
      assertEquals( Integer.MIN_VALUE, itr.next().value() );
      assertEquals( 1, s.size() );
      
      s.add( Integer.MIN_VALUE + 1 );
      assertEquals( 2, s.size() );
      
      i = s.intIterator();
      
      assertEquals( Integer.MIN_VALUE, i.nextInt() );
      assertEquals( Integer.MIN_VALUE + 1, i.nextInt() );
      
      itr = s.iterator();
      
      assertEquals( Integer.MIN_VALUE, itr.next().value() );
      assertEquals( Integer.MIN_VALUE + 1, itr.next().value() );
      
      assertTrue( s.contains( Integer.MIN_VALUE + 1 ) );
   }
   
   @Test
   public void testRemoveLastElement() {
      IntLinkedGrowingSet s = new IntLinkedGrowingSet();
      
      s.add( Integer.MIN_VALUE + 100 );
      s.add( Integer.MIN_VALUE + 7 );
      s.add( Integer.MIN_VALUE );
      
      assertTrue( s.contains( Integer.MIN_VALUE + 100 ) );
      assertTrue( s.contains( Integer.MIN_VALUE + 7 ) );
      assertTrue( s.contains( Integer.MIN_VALUE ) );
      
      IntIterator i = s.intIterator();
      
      assertEquals( Integer.MIN_VALUE + 100, i.nextInt() );
      assertEquals( Integer.MIN_VALUE + 7, i.nextInt() );
      assertEquals( Integer.MIN_VALUE, i.nextInt() );
      assertFalse( i.hasNext() );
      
      i.remove();
      assertFalse( s.contains( Integer.MIN_VALUE ) );
      
      assertEquals( 2, s.size() );
      
      Iterator<IntCursor> itr = s.iterator();
      
      assertEquals( Integer.MIN_VALUE + 100, itr.next().value() );
      assertEquals( Integer.MIN_VALUE + 7, itr.next().value() );
      itr.remove();
      assertFalse( s.contains( Integer.MIN_VALUE + 7 ) );
      assertEquals( 1, s.size() );
      
      s.add( Integer.MIN_VALUE + 1 );
      assertEquals( 2, s.size() );
      
      i = s.intIterator();
      
      assertEquals( Integer.MIN_VALUE + 100, i.nextInt() );
      assertEquals( Integer.MIN_VALUE + 1, i.nextInt() );
      
      itr = s.iterator();
      
      assertEquals( Integer.MIN_VALUE + 100, itr.next().value() );
      assertEquals( Integer.MIN_VALUE + 1, itr.next().value() );
      
      assertTrue( s.contains( Integer.MIN_VALUE + 1 ) );
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
   
   @Test
   public void testCmpWithIntGrowingSet() {
      Random r = new Random();
      final long seed = r.nextLong();
      r = new Random( seed );
      System.out.println( "rnd seed " + seed );
      
      LinkedHashSet<Integer> s = new LinkedHashSet<>();
      IntGrowingSet s1 = new IntGrowingSet();
      IntLinkedGrowingSet s2 = new IntLinkedGrowingSet();
      
      final int n = 1000;
      
      for( int i=n; i>0; i-- ) {
         final int x = r.nextInt( 1000 );
         
         boolean isNew = s.add( x );
         
         assertEquals( isNew, s1.add( x ) );
         assertEquals( isNew, s2.add( x ) );
      }
      
      assertEquals( s1.size(), s2.size() );
      
      for( IntCursor i : s1 ) {
         assertTrue( s2.contains( i.value() ) );
      }
      for( IntCursor i : s2 ) {
         assertTrue( s1.contains( i.value() ) );
      }
      
      Iterator<Integer> itr = s.iterator();
      IntIterator i2 = s2.intIterator();
      while( itr.hasNext() ) {
         assertEquals( itr.next().intValue(), i2.nextInt() );
      }
      
      for( int i=n; i>0; i-- ) {
         final int x = r.nextInt( 2000 );
         
         boolean removed = s.remove( x );
         assertEquals( removed, s1.remove( x ) );
         assertEquals( removed, s2.remove( x ) );
         
         itr = s.iterator();
         i2 = s2.intIterator();
         while( itr.hasNext() ) {
            assertEquals( itr.next().intValue(), i2.nextInt() );
         }
         
         for( int j=2; j>0; j-- ) {
            final int y = r.nextInt( 1000 );
            
            boolean isNew = s.add( y );
            
            assertEquals( isNew, s1.add( y ) );
            assertEquals( isNew, s2.add( y ) );
         }
      }
      
      assertEquals( s1.size(), s2.size() );
   }

   public static String toBin( int i ) {
      int z = 32 + Integer.numberOfLeadingZeros( i );
      return z + "," + Integer.toBinaryString( i );
   }
   
   public static String toBin( long l ) {
      return Long.numberOfLeadingZeros( l ) + "," + Long.toBinaryString( l );
   }
}
