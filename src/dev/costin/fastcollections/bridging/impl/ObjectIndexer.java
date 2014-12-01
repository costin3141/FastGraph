package dev.costin.fastcollections.bridging.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import dev.costin.fastcollections.bridging.IndexedObjectBridge;

public class ObjectIndexer<T> implements IndexedObjectBridge<IndexedWrapper<T>> {
   
   private final Object[] _toObject;
   private final Map<T, Integer> _toIndex;
   private final ArrayList<IndexedWrapper<T>> _wrappers;
   private final List<IndexedWrapper<T>> _wrapperList;
   private final int _indexOffset;

   @SuppressWarnings( "unchecked" )
   public ObjectIndexer( final Collection<T> objects, final int indexOffset ) {
      _indexOffset = indexOffset;
      _toObject = objects.toArray();
      _toIndex = new HashMap<T, Integer>( objects.size()*3/2 );
      for( int i=0; i<_toObject.length; i++ ) {
         _toIndex.put( (T)_toObject[i], Integer.valueOf( i ) );
      }
      
      _wrappers = new ArrayList<>( _toObject.length );
      for( int i=0; i<_toObject.length; i++ ) {
         _wrappers.add( new IndexedWrapper<T>( (T)_toObject[i], i+_indexOffset ) );
      }
      
      _wrapperList = Collections.unmodifiableList( _wrappers );
   }
   
   @Override
   public int getMinIndex() {
      return _indexOffset;
   }
   
   @Override
   public int getMaxIndex() {
      return _indexOffset + _toObject.length - 1;
   }
   
   public IndexedWrapper<T> getObject( final int index ) {
      if( index >= _indexOffset ) {
         final int idx = index - _indexOffset;
         if( idx < _toObject.length ) {
            return _wrappers.get( index - _indexOffset );
         }
      }
      
      throw new NoSuchElementException( "No object with index "+index+" available." );
   }
   
   @SuppressWarnings( "unchecked" )
   public T getOriginalObject( final int index ) {
      if( index >= _indexOffset ) {
         final int idx = index - _indexOffset;
         if( idx < _toObject.length ) {
            return (T) _toObject[ idx ];
         }
      }
      
      throw new NoSuchElementException( "No object with index "+index+" available." );
   }
   
   @Override
   public int getIndex( final IndexedWrapper<T> obj ) {
      return obj.getIndex();
   }
   
   public int findIndex( final T obj ) {
      final Integer index = _toIndex.get( obj );
      if( index != null ) {
         return index.intValue() + _indexOffset;
      }
      
      throw new NoSuchElementException( "Object "+obj+" has not been indexed.");
   }
   
   public List<IndexedWrapper<T>> getWrapperList() {
      return _wrapperList;
   }
   
}
