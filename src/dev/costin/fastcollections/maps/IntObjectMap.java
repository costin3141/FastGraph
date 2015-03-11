package dev.costin.fastcollections.maps;

import dev.costin.fastcollections.IntIterator;

public interface IntObjectMap<V> extends Iterable<IntObjectMap.IntObjectEntry<V>> {
   
   public static interface IntObjectEntry<V> {
      public int getKey();
      public V getValue();
      public void setValue( V value );
   }

   boolean containsKey( int key );

   /**
    * Puts the {@code value} for the given {@code key} into the map.
    * 
    * @return Returns the previously stored value or {@code null} if none was stored.
    */
   V put( int key, V value );
   
   V remove( int key );
   
   V get( int key );
   
   int size();
   
   boolean isEmpty();
   
   IntIterator keyIterator();

   void clear();
   
}
