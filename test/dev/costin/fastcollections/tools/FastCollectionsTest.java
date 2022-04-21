package dev.costin.fastcollections.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Iterator;

import org.junit.Test;

import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.lists.IntList;
import dev.costin.fastcollections.lists.impl.IntArrayList;
import dev.costin.fastcollections.maps.IntDoubleMap;
import dev.costin.fastcollections.maps.IntDoubleMap.IntDoubleEntry;
import dev.costin.fastcollections.maps.IntIntMap;
import dev.costin.fastcollections.maps.IntIntMap.IntIntEntry;
import dev.costin.fastcollections.maps.IntLongMap;
import dev.costin.fastcollections.maps.IntLongMap.IntLongEntry;
import dev.costin.fastcollections.maps.IntObjectMap;
import dev.costin.fastcollections.maps.IntObjectMap.IntObjectEntry;
import dev.costin.fastcollections.maps.impl.IntDoubleGrowingMap;
import dev.costin.fastcollections.maps.impl.IntIntGrowingMap;
import dev.costin.fastcollections.maps.impl.IntLongGrowingMap;
import dev.costin.fastcollections.maps.impl.IntObjectGrowingMap;
import dev.costin.fastcollections.sets.IntSet;
import dev.costin.fastcollections.sets.impl.IntGrowingSet;

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
   
   @Test
   public void testUnmodifiableIntObjectMap() {
      IntObjectMap<BigDecimal> m = new IntObjectGrowingMap<>();
      
      m.put( 1, BigDecimal.valueOf( 1 ) );
      
      IntObjectMap<BigDecimal> u = FastCollections.unmodifiableMap( m );
      
      assertEquals( BigDecimal.ONE, u.get( 1 ) );
      
      m.put( 2, BigDecimal.valueOf( 2 ) );
      
      assertEquals( BigDecimal.valueOf( 2 ), u.get( 2 ) );
      
      try {
         u.remove( 1 );
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      try {
         u.put( 3, BigDecimal.valueOf( 3 ) );
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      try {
         u.clear();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      IntIterator itr1 = u.keyIterator();
      itr1.nextInt();
      
      try {
         itr1.remove();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      Iterator<IntObjectEntry<BigDecimal>> itr2 = u.iterator();
      itr2.next();
      
      try {
         itr2.remove();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
   }
   
   @Test
   public void testUnmodifiableIntIntMap() {
      IntIntMap m = new IntIntGrowingMap();
      
      m.put( 1, 1  );
      
      IntIntMap u = FastCollections.unmodifiableMap( m );
      
      assertEquals( 1, u.get( 1 ) );
      
      m.put( 2, 2 );
      
      assertEquals( 2, u.get( 2 ) );
      
      try {
         u.remove( 1 );
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      try {
         u.put( 3, 3 );
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      try {
         u.clear();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      IntIterator itr1 = u.keyIterator();
      itr1.nextInt();
      
      try {
         itr1.remove();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      Iterator<IntIntEntry> itr2 = u.iterator();
      itr2.next();
      
      try {
         itr2.remove();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
   }
   
   @Test
   public void testUnmodifiableIntLongMap() {
      IntLongMap m = new IntLongGrowingMap();
      
      m.put( 1, 1  );
      
      IntLongMap u = FastCollections.unmodifiableMap( m );
      
      assertEquals( 1, u.get( 1 ) );
      
      m.put( 2, 2 );
      
      assertEquals( 2, u.get( 2 ) );
      
      try {
         u.remove( 1 );
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      try {
         u.put( 3, 3 );
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      try {
         u.clear();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      IntIterator itr1 = u.keyIterator();
      itr1.nextInt();
      
      try {
         itr1.remove();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      Iterator<IntLongEntry> itr2 = u.iterator();
      itr2.next();
      
      try {
         itr2.remove();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
   }
   
   @Test
   public void testUnmodifiableIntDoubleMap() {
      IntDoubleMap m = new IntDoubleGrowingMap();
      
      m.put( 1, 1  );
      
      IntDoubleMap u = FastCollections.unmodifiableMap( m );
      
      assertEquals( Double.valueOf( 1 ), Double.valueOf( u.get( 1 ) ) );
      
      m.put( 2, 2 );
      
      assertEquals( Double.valueOf( 2 ), Double.valueOf( u.get( 2 ) ) );
      
      try {
         u.remove( 1 );
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      try {
         u.put( 3, 3 );
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      try {
         u.clear();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      IntIterator itr1 = u.keyIterator();
      itr1.nextInt();
      
      try {
         itr1.remove();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      Iterator<IntDoubleEntry> itr2 = u.iterator();
      itr2.next();
      
      try {
         itr2.remove();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
   }
   
   @Test
   public void testUnmodifiableIntList() {
      IntList m = new IntArrayList();
      
      m.add( 1 );
      
      IntList u = FastCollections.unmodifiableIntList( m );
      
      assertEquals( 1, u.get( 0 ) );
      
      m.add( 2 );
      
      assertEquals( 2, u.get( 1 ) );
      
      try {
         u.remove( 1 );
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      try {
         u.add( 3 );
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      try {
         u.clear();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      IntIterator itr1 = u.intIterator();
      itr1.nextInt();
      
      try {
         itr1.remove();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      Iterator<IntCursor> itr2 = u.iterator();
      itr2.next();
      
      try {
         itr2.remove();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
   }
   
   @Test
   public void testUnmodifiableIntSet() {
      IntSet m = new IntGrowingSet();
      
      m.add( 1 );
      
      IntSet u = FastCollections.unmodifiableIntSet( m );
      
      assertTrue( u.contains( 1 ) );
      
      m.add( 2 );
      
      assertTrue( u.contains( 2 ) );
      
      try {
         u.remove( 1 );
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      try {
         u.add( 3 );
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      try {
         u.clear();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      IntIterator itr1 = u.intIterator();
      itr1.nextInt();
      
      try {
         itr1.remove();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
      
      Iterator<IntCursor> itr2 = u.iterator();
      itr2.next();
      
      try {
         itr2.remove();
         fail();
      }
      catch( UnsupportedOperationException e ) {
         
      }
   }
   
}
