package dev.costin.fastcollections.bridging.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import dev.costin.fastcollections.bridging.IndexedObject;
import dev.costin.fastcollections.bridging.IndexedObjectBridge;

public class IndexedObjectAccessor<T extends IndexedObject> implements IndexedObjectBridge<T> {
   
   private final Object[] _toObject;
   private final int _minIndex;
   private final int _maxIndex;
   
   public IndexedObjectAccessor( final Collection<T> objects ) {
      if( objects.isEmpty() ) {
         _minIndex = _maxIndex = 0;
         _toObject = new Object[0];
      }
      else {
         final Iterator<T> iter = objects.iterator();
         int min = iter.next().getIndex();
         int max = min;
         
         for( ; iter.hasNext(); ) {
            final int idx = iter.next().getIndex();
            
            if( idx < min ) {
               min = idx;
            }
            else if( idx > max ) {
               max = idx;
            }
         }
         
         _minIndex = min;
         _maxIndex = max;
         
         _toObject = new Object[ _maxIndex - _minIndex + 1 ];
         for( final T obj : objects ) {
            _toObject[obj.getIndex()] = obj;
         }
      }
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
      @SuppressWarnings( "unchecked" )
      final T obj = (T) _toObject[index];
      if( obj == null ) {
         throw new NoSuchElementException();
      }
      
      return obj;
   }

   @Override
   public int getIndex( T obj ) {
      return obj.getIndex();
   }

}
