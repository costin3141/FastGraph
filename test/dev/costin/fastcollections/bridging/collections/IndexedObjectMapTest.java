package dev.costin.fastcollections.bridging.collections;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

import dev.costin.fastcollections.bridging.impl.IndexedWrapper;
import dev.costin.fastcollections.bridging.impl.ObjectIndexer;

public class IndexedObjectMapTest {

   @Test
   public void testKeySet() {
      final int n = 10;
      final List<Integer> list = new ArrayList<>();
      for( int i=0; i<n; i++ ) {
         list.add( i );
      }
      
      ObjectIndexer<Integer> indexer = new ObjectIndexer<Integer>( list );
      
      IndexedObjectMap<IndexedWrapper<Integer>, Integer> map = new IndexedObjectMap<>( indexer );
      
      map.put( indexer.getObject( 0 ), 0 );
      map.put( indexer.getObject( 1 ), 1 );
      map.put( indexer.getObject( 2 ), 2 );
      map.put( indexer.getObject( 3 ), 3 );
      
      Iterator<IndexedWrapper<Integer>> iter = map.keySet().iterator();
      int i=0;
      while( iter.hasNext() ) {
         final IndexedWrapper<Integer> key = iter.next();
         
         assertTrue( i==key.getObject() );
         
//         System.out.println(key);
         if( key.getObject() == 2 ) {
            iter.remove();
         }
         
         ++i;
      }
      
      assertTrue( map.containsKey( indexer.getObject( 0 ) ) );
      assertTrue( map.containsKey( indexer.getObject( 1 ) ) );
      assertTrue( !map.containsKey( indexer.getObject( 2 ) ) );
      assertTrue( map.containsKey( indexer.getObject( 3 ) ) );
      
      map.put( indexer.getObject( 4 ), 4 );
      map.put( indexer.getObject( 5 ), 5 );
      map.put( indexer.getObject( 6 ), 6 );
      map.put( indexer.getObject( 7 ), 7 );
      
      ArrayList<IndexedWrapper<Integer>> set = new ArrayList<>();
      set.add( indexer.getObject( 1 ) );
      set.add( indexer.getObject( 2 ) );
      set.add( indexer.getObject( 6 ) );
      set.add( indexer.getObject( 7 ) );
      
      map.keySet().retainAll( set );
      assertTrue( map.size()==3 );
      assertTrue( map.containsKey( indexer.getObject( 1 ) ) );
      assertTrue( map.containsKey( indexer.getObject( 6 ) ) );
      assertTrue( map.containsKey( indexer.getObject( 7 ) ) );
      
      map.keySet().removeAll( set );
      assertTrue( map.size() == 0 );
   }
   
   @Test
   public void testEntrySet() {
      final int n = 10;
      final List<Integer> list = new ArrayList<>();
      for( int i=0; i<n; i++ ) {
         list.add( i );
      }
      
      ObjectIndexer<Integer> indexer = new ObjectIndexer<Integer>( list );
      
      IndexedObjectMap<IndexedWrapper<Integer>, Integer> map = new IndexedObjectMap<>( indexer );
      
      map.put( indexer.getObject( 0 ), 0 );
      map.put( indexer.getObject( 1 ), 1 );
      map.put( indexer.getObject( 2 ), 2 );
      map.put( indexer.getObject( 3 ), 3 );
      
      Iterator<Entry<IndexedWrapper<Integer>, Integer>> iter = map.entrySet().iterator();
      int i=0;
      while( iter.hasNext() ) {
         final Entry<IndexedWrapper<Integer>, Integer> entry = iter.next();
         
         assertTrue( i==entry.getKey().getObject() );
         assertTrue( i==entry.getValue() );
         
//         System.out.println(entry.getKey()+":"+entry.getValue());
         if( entry.getKey().getObject() == 2 ) {
            iter.remove();
         }
         
         ++i;
      }
      
      assertTrue( map.containsKey( indexer.getObject( 0 ) ) );
      assertTrue( map.containsKey( indexer.getObject( 1 ) ) );
      assertTrue( !map.containsKey( indexer.getObject( 2 ) ) );
      assertTrue( map.containsKey( indexer.getObject( 3 ) ) );
      
      map.put( indexer.getObject( 4 ), 4 );
      map.put( indexer.getObject( 5 ), 5 );
      map.put( indexer.getObject( 6 ), 6 );
      map.put( indexer.getObject( 7 ), 7 );
      
      ArrayList<Entry<IndexedWrapper<Integer>, Integer>> set = new ArrayList<>();
      
      for( Entry<IndexedWrapper<Integer>, Integer> entry : map.entrySet() ) {
         if( entry.getKey().getObject() == 1 || entry.getKey().getObject() == 2 ||
                  entry.getKey().getObject() == 6 || entry.getKey().getObject() == 7 ) {
            set.add( entry );
         }
      }
      
      map.entrySet().retainAll( set );
      assertTrue( map.size()==3 );
      assertTrue( map.containsKey( indexer.getObject( 1 ) ) );
      assertTrue( map.containsKey( indexer.getObject( 6 ) ) );
      assertTrue( map.containsKey( indexer.getObject( 7 ) ) );
      
      map.entrySet().removeAll( set );
      assertTrue( map.size() == 0 );
   }

}
