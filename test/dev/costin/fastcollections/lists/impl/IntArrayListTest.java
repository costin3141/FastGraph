package dev.costin.fastcollections.lists.impl;

import static org.junit.Assert.*;

import org.junit.Test;

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

}
