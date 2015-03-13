package dev.costin.fastcollections.bridging.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import dev.costin.fastcollections.bridging.IndexedObject;
import dev.costin.fastcollections.bridging.IndexedObjectBridge;
import dev.costin.fastcollections.maps.IntObjectMap;
import dev.costin.fastcollections.maps.impl.IntObjectGrowingMap;

public class IndexedObjectAccessor<T extends IndexedObject> implements IndexedObjectBridge<T> {
   
   private final IntObjectMap<T> _toObject;
   private final int _minIndex;
   private final int _maxIndex;
   
   public IndexedObjectAccessor( final Collection<T> objects ) {
      if( objects.isEmpty() ) {
         _minIndex = _maxIndex = 0;
         _toObject = new IntObjectGrowingMap<T>();
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
         
         _toObject = new IntObjectGrowingMap( _minIndex, _maxIndex, objects.size() );
         for( final T obj : objects ) {
            _toObject.put( obj.getIndex(), obj );
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
      final T obj = _toObject.get( index );
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
