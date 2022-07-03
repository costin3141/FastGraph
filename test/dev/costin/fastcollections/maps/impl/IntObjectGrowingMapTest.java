package dev.costin.fastcollections.maps.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.tools.FastCollections;

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
   
   @Test
   public void testExtremePositiveValues() {
      IntObjectGrowingMap<Integer> map = new IntObjectGrowingMap<>();
      
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
      
      map = new IntObjectGrowingMap<>();
      
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
