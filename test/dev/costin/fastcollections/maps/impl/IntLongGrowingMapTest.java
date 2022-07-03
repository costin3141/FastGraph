package dev.costin.fastcollections.maps.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;

import org.junit.Test;

import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.tools.FastCollections;

public class IntLongGrowingMapTest {

   @Test
   public void test() {
      final IntLongGrowingMap map = new IntLongGrowingMap(0, 0);
      
      assertTrue( !map.containsKey( 0 ) );
      assertTrue( !map.containsKey( -1 ) );
      assertTrue( !map.containsKey( 1 ) );
      
      map.put( 2, 20 );
      
      assertTrue( map.containsKey( 2 ) );
      assertTrue( map.get( 2 ) == 20.0 );
      
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
      
      assertTrue( map.get( 2 ) == 20.0 );
   }
   
   @Test
   public void testInitialZeroCapacity() {
      final IntLongGrowingMap map = new IntLongGrowingMap(0);
      
      assertTrue( !map.containsKey( 0 ) );
      assertTrue( !map.containsKey( -1 ) );
      assertTrue( !map.containsKey( 1 ) );
      
      map.put( 2, 20 );
      
      assertTrue( map.containsKey( 2 ) );
      assertTrue( map.get( 2 ) == 20.0 );
      
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
      
      assertTrue( map.get( 2 ) == 20.0 );
   }
   
   @Test
   public void testEquals() {
      final IntLongGrowingMap map1 = new IntLongGrowingMap();
      
      map1.put( 1, 1 );
      map1.put( 2, 2 );
      map1.put( 3, 3 );
      map1.put( 4, 4 );
      
      final IntLongGrowingMap map2 = new IntLongGrowingMap();
      
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
      IntLongGrowingMap map = new IntLongGrowingMap();
      
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
      
      map = new IntLongGrowingMap();
      
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

}
