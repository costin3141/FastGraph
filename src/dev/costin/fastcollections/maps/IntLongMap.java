package dev.costin.fastcollections.maps;

import dev.costin.fastcollections.IntIterator;

public interface IntLongMap extends Iterable<IntLongMap.IntLongEntry> {
   public static interface IntLongEntry {
      public int getKey();
      public long getValue();
      public void setValue( long value );
   }

   boolean containsKey( int key );

   boolean put( int key, long value );
   
   boolean remove( int key );
   
   long get( int key );
   
   int size();
   
   boolean isEmpty();
   
   IntIterator keyIterator();

   void clear();
}
