package dev.costin.fastcollections.maps.impl;

import java.util.NoSuchElementException;

import org.junit.Test;

public class IntDoubleGrowingMapTest {

   @Test
   public void test() {
      final IntDoubleGrowingMap map = new IntDoubleGrowingMap(0, 0);
      
      assert !map.containsKey( 0 );
      assert !map.containsKey( -1 );
      assert !map.containsKey( 1 );
      
      map.put( 2, 20.0 );
      
      assert map.containsKey( 2 );
      assert map.get( 2 ) == 20.0;
      
      map.put( -2, -20.0 );
      
      assert map.containsKey( 2 );
      assert map.get( 2 ) == 20.0;
      assert map.containsKey( -2 );
      assert map.get( -2 ) == -20.0;
      assert !map.containsKey( 0 );
      assert !map.containsKey( 1 );
      assert !map.containsKey( -1 );
      assert !map.containsKey( 3 );
      assert !map.containsKey( -3 );

      assert !map.remove( -100 );
      assert !map.remove( 100 );
      assert map.remove( -2 );
      assert !map.containsKey( -2 );
      
      try{
         map.get( -2 );
         assert false;
      }
      catch( NoSuchElementException e ) {
      }
      try{
         map.get( -100 );
         assert false;
      }
      catch( NoSuchElementException e ) {
      }
      try{
         map.get( 100 );
         assert false;
      }
      catch( NoSuchElementException e ) {
      }
      
      assert map.get( 2 ) == 20.0;
   }

}
