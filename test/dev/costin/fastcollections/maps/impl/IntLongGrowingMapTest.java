package dev.costin.fastcollections.maps.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;

import org.junit.Test;

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

}
