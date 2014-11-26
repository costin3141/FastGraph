package dev.costin.fastcollections.maps;

import dev.costin.fastcollections.IntIterator;

public interface IntObjectMap<V> extends Iterable<IntObjectMap.IntObjectEntry<V>> {
   
   public static interface IntObjectEntry<V> {
      public int getKey();
      public V getValue();
      public void setValue( V value );
   }

   boolean contains( int key );

   boolean put( int key, V value );
   
   boolean remove( int key );
   
   V get( int key );
   
   int size();
   
   IntIterator keyIterator();

   void clear();
   
}
