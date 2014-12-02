package dev.costin.fastcollections.bridging.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import dev.costin.fastcollections.bridging.IndexedObject;
import dev.costin.fastcollections.bridging.IndexedObjectBridge;
import dev.costin.fastcollections.sets.IntSet;
import dev.costin.fastcollections.sets.impl.IntRangeSet;

public class IndexedObjectSet<T extends IndexedObject> implements Set<T> {
   
   private final IndexedObjectBridge<T> _indexer;
   private final IntSet _set;
   
   public IndexedObjectSet( IndexedObjectBridge<T> indexer ) {
      _indexer = indexer;
      _set = new IntRangeSet( indexer.getMinIndex(), _indexer.getMaxIndex() );
   }

   @Override
   public int size() {
      return _set.size();
   }

   @Override
   public boolean isEmpty() {
      return _set.isEmpty();
   }

   @SuppressWarnings( "unchecked" )
   @Override
   public boolean contains( Object o ) {
      return _set.contains( ((T)o).getIndex() );
   }

   @Override
   public Iterator<T> iterator() {
      return new IndexedObjectIterator<T>( _indexer, _set.intIterator() );
   }

   @Override
   public Object[] toArray() {
      // TODO ......
      //return _set.toArray();
      return null;
   }

   @Override
   public <E> E[] toArray( E[] a ) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public boolean add( T e ) {
      return _set.add( e.getIndex() );
   }

   @SuppressWarnings( "unchecked" )
   @Override
   public boolean remove( Object o ) {
      return _set.remove( ((T)o).getIndex() );
   }

   @Override
   public boolean containsAll( Collection<?> c ) {
      for( final Object o : c ) {
         if( !contains(o) ) {
            return false;
         }
      }
      return true;
   }

   @Override
   public boolean addAll( Collection<? extends T> c ) {
      boolean changed = false;

      for( final T obj : c ) {
         changed = add( obj ) || changed;
      }

      return changed;
   }

   @Override
   public boolean retainAll( Collection<?> c ) {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean removeAll( Collection<?> c ) {
      boolean changed = false;
      
      for( final Object o : c ) {
         changed = remove( o ) || changed;
      }
      
      return changed;
   }

   @Override
   public void clear() {
      _set.clear();
   }

}
