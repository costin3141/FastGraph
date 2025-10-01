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
   
   /** Returns the value associated with the key if it exists or returns the default value. The default value will not be stored in the map! */
   int getOrDefault( int key, int defaultValue );
   
   int size();
   
   boolean isEmpty();
   
   IntIterator keyIterator();

   void clear();
}
