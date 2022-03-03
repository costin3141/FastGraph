package dev.costin.fastcollections.maps.impl;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Test;

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
}
