package dev.costin.fastcollections.lists.impl;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Test;

import dev.costin.fastcollections.IntIterator;

public class IntArrayListTest {

   @Test
   public void testGrowDefault() {
      final IntArrayList list = new IntArrayList();
      
      assertFalse( list.contains( 0 ) );
      
      list.add( 0 );
      
      assertTrue( list.contains( 0 ) );
      assertTrue( list.indexOf( 0 ) == 0 );
      
      list.add( 1 );
      list.add( 2 );
      list.add( 3 );
      list.add( 4 );
      list.add( 5 );
      list.add( 6 );
      list.add( 7 );
      list.add( 8 );
      list.add( 9 );
      list.add( 10 );
      
      assertTrue( list.contains( 10 ) );
      assertTrue( list.indexOf( 10 ) == 10 );
   }
   
   @Test
   public void testGrow() {
      final IntArrayList list = new IntArrayList( 3 );
      
      assertFalse( list.contains( 0 ) );
      
      list.add( 0 );
      
      assertTrue( list.contains( 0 ) );
      assertTrue( list.indexOf( 0 ) == 0 );
      
      list.add( 1 );
      list.add( 2 );
      list.add( 3 );
      list.add( 4 );
      list.add( 5 );
      list.add( 6 );
      list.add( 7 );
      list.add( 8 );
      list.add( 9 );
      list.add( 10 );
      
      assertTrue( list.contains( 10 ) );
      assertTrue( list.indexOf( 10 ) == 10 );
   }
   
   @Test
   public void testRemove() {
      final IntArrayList list = new IntArrayList();
      
      assertFalse( list.contains( 0 ) );
      
      list.add( 0 );
      
      assertTrue( list.contains( 0 ) );
      assertTrue( list.indexOf( 0 ) == 0 );
      
      list.remove( 0 );
      
      assertFalse( list.contains( 0 ) );
      assertTrue( list.indexOf( 0 ) == -1 );
      
      list.add( 1 );
      list.add( 2 );
      list.add( 3 );
      list.add( 4 );
      list.add( 5 );
      list.add( 6 );
      list.add( 7 );
      list.add( 8 );
      list.add( 9 );
      list.add( 10 );
      
      assertTrue( list.contains( 10 ) );
      assertTrue( list.indexOf( 10 ) == 9 );
      
      assertTrue( list.remove( 7 ) );
      assertTrue( list.indexOf( 7 ) == -1 );
      assertFalse( list.contains( 7 ) );
      assertTrue( list.indexOf( 6 ) == 5 );
      assertTrue( list.indexOf( 8 ) == 6 );
   }

   @Test
   public void testIntIterator() {
      final IntArrayList list = new IntArrayList();
      
      list.add( 1 );
      list.add( 2 );
      list.add( 3 );
      list.add( 4 );
      list.add( 5 );
      list.add( 6 );
      list.add( 7 );
      list.add( 8 );
      list.add( 9 );
      list.add( 10 );
      
      final IntIterator itr = list.intIterator();
      
      assertTrue( itr.hasNext() );
      assertTrue( itr.nextInt() == 1 );
      
      itr.remove();
      assertFalse( list.contains( 1 ) );
      assertTrue( list.getFirst() == 2 );
      assertTrue( list.get( 0 ) == 2 );
      assertTrue( list.size() == 9 );
      
      assertTrue( itr.hasNext() );
      assertTrue( itr.nextInt() == 2 );
      assertTrue( itr.hasNext() );
      assertTrue( itr.nextInt() == 3 );
      assertTrue( itr.hasNext() );
      assertTrue( itr.nextInt() == 4 );
      
      itr.remove();
      assertFalse( list.contains( 4 ) );
      assertTrue( list.getFirst() == 2 );
      assertTrue( list.indexOf( 4 ) < 0 );
      assertTrue( list.indexOf( 3 ) == 1 );
      assertTrue( list.indexOf( 5 ) == 2 );
      assertTrue( list.size() == 8 );
      
      assertTrue( itr.hasNext() );
      assertTrue( itr.nextInt() == 5 );
      assertTrue( itr.hasNext() );
      assertTrue( itr.nextInt() == 6 );
      assertTrue( itr.hasNext() );
      assertTrue( itr.nextInt() == 7 );
      assertTrue( itr.hasNext() );
      assertTrue( itr.nextInt() == 8 );
      assertTrue( itr.hasNext() );
      assertTrue( itr.nextInt() == 9 );
      assertTrue( itr.hasNext() );
      assertTrue( itr.nextInt() == 10 );
      
      assertTrue( list.getLast() == 10 );
      assertTrue( list.indexOf( 10 ) == 7 );
      assertTrue( list.get( 7 ) == 10 );
      
      assertFalse( itr.hasNext() );

      itr.remove();
      
      assertFalse( itr.hasNext() );
      assertTrue( list.size() == 7 );
      assertTrue( list.getLast() == 9 );
      assertFalse( list.contains( 10 ) );
      assertTrue( list.indexOf( 10 ) < 0 );
      
      try{
         itr.remove();
         fail( "Expected NoSuchElementException." );
      }
      catch( NoSuchElementException e ) {
         // expected
      }
   }

}
