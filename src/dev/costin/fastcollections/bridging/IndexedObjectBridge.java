package dev.costin.fastcollections.bridging;


public interface IndexedObjectBridge<T> {

   int getMinIndex();
   
   int getMaxIndex();
   
   T getObject( final int index );
   
   int getIndex( final T obj );
   
}
