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

public class IntDoubleGrowingMapTest {

   @Test
   public void test() {
      final IntDoubleGrowingMap map = new IntDoubleGrowingMap(0, 0);
      
      assertTrue( !map.containsKey( 0 ) );
      assertTrue( !map.containsKey( -1 ) );
      assertTrue( !map.containsKey( 1 ) );
      
      map.put( 2, 20.0 );
      
      assertTrue( map.containsKey( 2 ) );
      assertTrue( map.get( 2 ) == 20.0 );
      
      map.put( -2, -20.0 );
      
      assertTrue( map.containsKey( 2 ) );
      assertTrue( map.get( 2 ) == 20.0 );
      assertTrue( map.containsKey( -2 ) );
      assertTrue( map.get( -2 ) == -20.0 );
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
   public void testZeroCapacity1() {
      final IntDoubleGrowingMap map = new IntDoubleGrowingMap();
      
      assertTrue( !map.containsKey( 0 ) );
      assertTrue( !map.containsKey( -1 ) );
      assertTrue( !map.containsKey( 1 ) );
      
      map.put( 2, 20.0 );
      
      assertTrue( map.containsKey( 2 ) );
      assertTrue( map.get( 2 ) == 20.0 );
      
      map.put( -2, -20.0 );
      
      assertTrue( map.containsKey( 2 ) );
      assertTrue( map.get( 2 ) == 20.0 );
      assertTrue( map.containsKey( -2 ) );
      assertTrue( map.get( -2 ) == -20.0 );
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
   public void testZeroCapacity2() {
      final IntDoubleGrowingMap map = new IntDoubleGrowingMap(0);
      
      assertTrue( !map.containsKey( 0 ) );
      assertTrue( !map.containsKey( -1 ) );
      assertTrue( !map.containsKey( 1 ) );
      
      map.put( 2, 20.0 );
      
      assertTrue( map.containsKey( 2 ) );
      assertTrue( map.get( 2 ) == 20.0 );
      
      map.put( -2, -20.0 );
      
      assertTrue( map.containsKey( 2 ) );
      assertTrue( map.get( 2 ) == 20.0 );
      assertTrue( map.containsKey( -2 ) );
      assertTrue( map.get( -2 ) == -20.0 );
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
      final IntDoubleGrowingMap map1 = new IntDoubleGrowingMap();
      
      map1.put( 1, 1.1 );
      map1.put( 2, 2.1 );
      map1.put( 3, 3.1 );
      map1.put( 4, 4.1 );
      
      final IntDoubleGrowingMap map2 = new IntDoubleGrowingMap();
      
      map2.put( 1, 1.1 );
      map2.put( 3, 3.1 );
      map2.put( 4, 4.1 );
      map2.put( 2, 2.1 );
      
      assertEquals( map1, map2 );
      
      map2.put( 2,  2.2 );
      
      assertNotEquals( map1, map2 );
      
      map2.remove( 2 );
      assertNotEquals( map1, map2 );
      
      map1.remove( 2 );
      assertEquals( map1, map2 );
   }
   
   @Test
   public void testExtremePositiveValues() {
      IntDoubleGrowingMap map = new IntDoubleGrowingMap();
      
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
      
      map = new IntDoubleGrowingMap();
      
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
