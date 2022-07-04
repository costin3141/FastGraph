package dev.costin.fastcollections.sets.impl;

import static org.junit.Assert.*;

import java.util.Iterator;

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
   public void test() {
      System.out.println( toBin( Integer.MIN_VALUE) );
      long l;
      l = combineToLong( Integer.MIN_VALUE, Integer.MIN_VALUE );
      System.out.println( toBin( l ) );
      System.out.println( toBin( getUpperInt( l ) ) + " " + toBin( getLowerInt( l ) ));
//      
//      l = setUpperInt( l, -1 );
//      System.out.println( toBin( l ) );
//      System.out.println( toBin( getUpperInt( l ) ) + " " + toBin( getLowerInt( l ) ));
//      
//      l = setLowerInt( l, -1 );
//      System.out.println( toBin( l ) );
//      System.out.println( toBin( getUpperInt( l ) ) + " " + toBin( getLowerInt( l ) ));
//      
//      l = setUpperInt( l, Integer.MIN_VALUE );
//      System.out.println( toBin( l ) );
//      System.out.println( toBin( getUpperInt( l ) ) + " " + toBin( getLowerInt( l ) ));
//      
//      l = setLowerInt( l,  Integer.MIN_VALUE );
//      System.out.println( toBin( l ) );
//      System.out.println( toBin( getUpperInt( l ) ) + " " + toBin( getLowerInt( l ) ));
//      
//      System.out.println(  );
      
      l = combineToLong( 1, Integer.MIN_VALUE );
      System.out.println( toBin( l ) );
      System.out.println( toBin( getUpperInt( l ) ) + " " + toBin( getLowerInt( l ) ));
      
      long lf = c( 1, Integer.MIN_VALUE );
      System.out.println( toBin( lf ) );
      System.out.println( toBin( getUpperInt( lf ) ) + " " + toBin( getLowerInt( lf ) ) );
   }
   
   public static long c( int upper, int lower ) {
      long l;
      if (lower < 0) {
         l = upper + 1;
      }
      else {
         l = upper;
      }
      l <<= 32;
      l += lower;
      return l;
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

   public static String toBin( int i ) {
      int z = 32 + Integer.numberOfLeadingZeros( i );
      return z + "," + Integer.toBinaryString( i );
   }
   
   public static String toBin( long l ) {
      return Long.numberOfLeadingZeros( l ) + "," + Long.toBinaryString( l );
   }
}
