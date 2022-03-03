package dev.costin.fastcollections.maps.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IntObjectGrowingMapTest {

   @Test
   public void testGrowing() {
      final IntObjectGrowingMap<Integer> map = new IntObjectGrowingMap<Integer>(0, 0);
      
      assertTrue( !map.containsKey( 0 ) );
      assertTrue( !map.containsKey( -1 ) );
      assertTrue( !map.containsKey( 1 ) );
      
      map.put( 2, 20 );
      
      assertTrue( map.containsKey( 2 ) );
      assertTrue( map.get( 2 ) == 20 );
      
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
      
      assertTrue( map.remove( -100 ) == null );
      assertTrue( map.remove( 100 ) == null );
      assertTrue( map.remove( -2 ).intValue() == -20 );
      assertTrue( !map.containsKey( -2 ) );
      
      assertTrue( map.get( -2 ) == null );
      assertTrue( map.get( -100 ) == null );
      assertTrue( map.get( 100 ) == null );
      assertTrue( map.get( 2 ) == 20 );
   }
   
   @Test
   public void testGrowingDefault() {
      final IntObjectGrowingMap<Integer> map = new IntObjectGrowingMap<Integer>();
      
      assertTrue( !map.containsKey( 0 ) );
      assertTrue( !map.containsKey( -1 ) );
      assertTrue( !map.containsKey( 1 ) );
      
      map.put( 2, 20 );
      
      assertTrue( map.containsKey( 2 ) );
      assertTrue( map.get( 2 ) == 20 );
      
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
      
      assertTrue( map.remove( -100 ) == null );
      assertTrue( map.remove( 100 ) == null );
      assertTrue( map.remove( -2 ).intValue() == -20 );
      assertTrue( !map.containsKey( -2 ) );
      
      assertTrue( map.get( -2 ) == null );
      assertTrue( map.get( -100 ) == null );
      assertTrue( map.get( 100 ) == null );
      assertTrue( map.get( 2 ) == 20 );
   }

   @Test
   public void testCopyConstructor() {
      final int n = 12;
      
      final IntObjectGrowingMap<Integer> map = new IntObjectGrowingMap<Integer>();
      for( int i = 0; i < n; i++ ) {
         map.put( i, i );
      }
      
      final IntObjectGrowingMap<Integer> copy1 = new IntObjectGrowingMap<Integer>( map );
      assertTrue( copy1.equals( map ) );
      
      map.remove( 0 );
      map.remove( 11 );
      
      final IntObjectGrowingMap<Integer> copy2 = new IntObjectGrowingMap<Integer>( map );
      assertTrue( copy2.equals( map ) );
      
      map.put( -1, -1 );
      map.put( 20, 20 );
      
      final IntObjectGrowingMap<Integer> copy3 = new IntObjectGrowingMap<Integer>( map );
      assertTrue( copy3.equals( map ) );
   }
   
   @Test
   public void testEquals() {
      final IntObjectGrowingMap<Integer> map1 = new IntObjectGrowingMap<>();
      
      map1.put( 1, 1 );
      map1.put( 2, 2 );
      map1.put( 3, 3 );
      map1.put( 4, 4 );
      
      final IntObjectGrowingMap<Integer> map2 = new IntObjectGrowingMap<>();
      
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
}
