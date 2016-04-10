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
   
   @Test
   public void testGrowingDefault() {
      final IntObjectGrowingMap<Integer> map = new IntObjectGrowingMap<Integer>();
      
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

   @Test
   public void testCopyConstructor() {
      final int n = 12;
      
      final IntObjectGrowingMap<Integer> map = new IntObjectGrowingMap<Integer>();
      for( int i = 0; i < n; i++ ) {
         map.put( i, i );
      }
      
      final IntObjectGrowingMap<Integer> copy1 = new IntObjectGrowingMap<Integer>( map );
      assert copy1.equals( map );
      
      map.remove( 0 );
      map.remove( 11 );
      
      final IntObjectGrowingMap<Integer> copy2 = new IntObjectGrowingMap<Integer>( map );
      assert copy2.equals( map );
      
      map.put( -1, -1 );
      map.put( 20, 20 );
      
      final IntObjectGrowingMap<Integer> copy3 = new IntObjectGrowingMap<Integer>( map );
      assert copy3.equals( map );
   }
}
