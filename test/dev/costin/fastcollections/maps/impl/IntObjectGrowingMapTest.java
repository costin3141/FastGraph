package dev.costin.fastcollections.maps.impl;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Test;

public class IntObjectGrowingMapTest {

   @Test
   public void testGrowing() {
      final IntObjectGrowingMap<Integer> map = new IntObjectGrowingMap<Integer>(0, 0);
      
      assert !map.containsKey( 0 );
      assert !map.containsKey( -1 );
      assert !map.containsKey( 1 );
      
      map.put( 2, 20 );
      
      assert map.containsKey( 2 );
      assert map.get( 2 ) == 20;
      
      map.put( -2, -20 );
      
      assert map.containsKey( 2 );
      assert map.get( 2 ) == 20;
      assert map.containsKey( -2 );
      assert map.get( -2 ) == -20;
      assert !map.containsKey( 0 );
      assert !map.containsKey( 1 );
      assert !map.containsKey( -1 );
      assert !map.containsKey( 3 );
      assert !map.containsKey( -3 );
      
      assert map.remove( -100 ) == null;
      assert map.remove( 100 ) == null;
      assert map.remove( -2 ).intValue() == -20;
      assert !map.containsKey( -2 );
      
      assert map.get( -2 ) == null;
      assert map.get( -100 ) == null;
      assert map.get( 100 ) == null;
      assert map.get( 2 ) == 20;
   }

}
