package dev.costin.fastcollections.maps;

import dev.costin.fastcollections.IntIterator;


public interface IntIntMap extends Iterable<IntIntMap.IntIntEntry> {
   
   public static interface IntIntEntry {
      public int getKey();
      public int getValue();
      public void setValue( int value );
   }

   boolean containsKey( int key );

   boolean put( int key, int value );
   
   boolean remove( int key );
   
   int get( int key );
   
   int size();
   
   IntIterator keyIterator();

   void clear();
   
}
