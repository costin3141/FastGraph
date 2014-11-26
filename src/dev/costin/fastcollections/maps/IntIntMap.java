package dev.costin.fastcollections.maps;

import dev.costin.fastcollections.IntCursor;
import dev.costin.fastcollections.IntIterator;


public interface IntIntMap extends Iterable<IntCursor> {

   boolean contains( int key );

   boolean put( int key, int value );
   
   boolean remove( int key );
   
   int get( int key );
   
   int size();
   
   IntIterator keyIterator();
   
}
