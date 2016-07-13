package dev.costin.fastcollections.tools;

import static org.junit.Assert.*;

import org.junit.Test;

import dev.costin.fastcollections.maps.IntDoubleMap;
import dev.costin.fastcollections.maps.IntIntMap;
import dev.costin.fastcollections.maps.IntObjectMap;
import dev.costin.fastcollections.sets.IntSet;

public class FastCollectionsTest {

   @Test
   public void testEmptyIntSet() {
      IntSet empty = FastCollections.emptyIntSet();
      
      assertTrue( empty.isEmpty() );
      assertEquals( 0, empty.size() );

      assertFalse( empty.contains( 0 ) );
      assertFalse( empty.remove( 0 ) );
      
      boolean ok = false;
      try {
         empty.add( 0 );
      }
      catch( UnsupportedOperationException e ) {
         ok = true;
      }
      if( !ok ) {
         fail( "Missing UnsupportedOperationException on add( int )" );
      }
      
   }

   @Test
   public void testEmptyIntIntMap() {
      IntIntMap empty = FastCollections.emptyIntIntMap();
      
      assertTrue( empty.isEmpty() );
      assertEquals( 0, empty.size() );
      
      assertFalse( empty.containsKey( 0 ) );
      assertFalse( empty.remove( 0 ) );
      
      boolean ok = false;
      try {
         empty.put( 0, 0 );
      }
      catch( UnsupportedOperationException e ) {
         ok = true;
      }
      if( !ok ) {
         fail( "Missing UnsupportedOperationException on add( int )" );
      }
      
   }
   
   @Test
   public void testEmptyIntDoubleMap() {
      IntDoubleMap empty = FastCollections.emptyIntDoubleMap();
      
      assertTrue( empty.isEmpty() );
      assertEquals( 0, empty.size() );
      
      assertFalse( empty.containsKey( 0 ) );
      assertFalse( empty.remove( 0 ) );
      
      boolean ok = false;
      try {
         empty.put( 0, 0 );
      }
      catch( UnsupportedOperationException e ) {
         ok = true;
      }
      if( !ok ) {
         fail( "Missing UnsupportedOperationException on add( int )" );
      }
      
   }
   
   @Test
   public void testEmptyIntObjectMap() {
      IntObjectMap<Integer> empty = FastCollections.emptyIntObjectMap();
      
      assertTrue( empty.isEmpty() );
      assertEquals( 0, empty.size() );
      
      assertFalse( empty.containsKey( 0 ) );
      assertEquals( null, empty.remove( 0 ) );
      
      boolean ok = false;
      try {
         empty.put( 0, null );
      }
      catch( UnsupportedOperationException e ) {
         ok = true;
      }
      if( !ok ) {
         fail( "Missing UnsupportedOperationException on add( int )" );
      }
      
   }
   
}
