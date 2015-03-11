package dev.costin.fastcollections.bridging.impl;

import dev.costin.fastcollections.bridging.IndexedObject;

public class IndexedWrapper<T> implements IndexedObject {
   
   private final T _obj;
   private final int _index;
   
   IndexedWrapper( final T object, final int index ) {
      _obj = object;
      _index = index;
   }
   
   public T getObject() {
      return _obj;
   }

   @Override
   public int getIndex() {
      return _index;
   }

   @Override
   public String toString() {
      return _index+":"+_obj.toString();
   }
}
