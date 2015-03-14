package dev.costin.fastcollections.bridging.impl;

import dev.costin.fastcollections.bridging.IndexedObject;
import dev.costin.fastcollections.bridging.IndexedObjectBridge;
import dev.costin.fastcollections.tools.FastCollections;


public class PassThroughBridge<T extends IndexedObject> implements IndexedObjectBridge<T> {
   
   private int _minIndex;
   private int _maxIndex;
   
   public PassThroughBridge() {
      this( 0, FastCollections.DEFAULT_LIST_CAPACITY );
   }
   
   public PassThroughBridge( final int initialMinIndex, final int initialMaxIndex ) {
      _minIndex = initialMinIndex;
      _maxIndex = initialMaxIndex;
   }

   @Override
   public int getMinIndex() {
      return _minIndex;
   }

   @Override
   public int getMaxIndex() {
      return _maxIndex;
   }

   @Override
   public T getObject( int index ) {
      throw new UnsupportedOperationException();
   }

   @Override
   public int getIndex( T obj ) {
      final int idx = obj.getIndex();
      if( idx < _minIndex ) {
         _minIndex = idx;
      }
      else if( idx > _maxIndex ) {
         _maxIndex = idx;
      }
      return idx;
   }

}
