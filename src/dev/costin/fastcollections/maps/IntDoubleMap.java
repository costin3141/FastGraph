package dev.costin.fastcollections.maps;

import dev.costin.fastcollections.IntIterator;

public interface IntDoubleMap extends Iterable<IntDoubleMap.IntDoubleEntry> {
   public static interface IntDoubleEntry {
      public int getKey();
      public double getValue();
      public void setValue( double value );
   }

   boolean containsKey( int key );

   boolean put( int key, double value );
   
   boolean remove( int key );
   
   double get( int key );
   
   /** Returns the value associated with the key if it exists or returns the default value. The default value will not be stored in the map! */
   double getOrDefault( int key, double defaultValue );
   
   int size();
   
   boolean isEmpty();
   
   IntIterator keyIterator();

   void clear();
}
