package dev.costin.fastcollections.bridging.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.bridging.IndexedObject;
import dev.costin.fastcollections.bridging.IndexedObjectBridge;
import dev.costin.fastcollections.sets.IntSet;
import dev.costin.fastcollections.sets.impl.IntGrowingSet;

public class IndexedObjectSet<T extends IndexedObject> implements Set<T> {
   
   private final IndexedObjectBridge<T> _indexer;
   private final IntSet _set;
   
   public IndexedObjectSet( IndexedObjectBridge<T> indexer ) {
      _indexer = indexer;
      final int min = _indexer.getMinIndex();
      final int max = _indexer.getMaxIndex();
      if( min <= max ) {
         _set = new IntGrowingSet( min, max );
      }
      else {
         _set = new IntGrowingSet();
      }
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
      final Object[] array = new Object[size()];
      int i=0;
      for( IntIterator iter=_set.intIterator(); iter.hasNext(); i++ ) {
         final int idx = iter.nextInt();
         array[i] = _indexer.getObject( idx );
      }
      return array;
   }

   @SuppressWarnings( "unchecked" )
   @Override
   public <E> E[] toArray( E[] a ) {
      final int size = size();
      final E[] array;
      
      if( a.length < size ) {
         array = a;
      }
      else {
         array = (E[])java.lang.reflect.Array
                  .newInstance(a.getClass().getComponentType(), size);
      }
      int i=0;
      for( IntIterator iter=_set.intIterator(); iter.hasNext(); i++ ) {
         final int idx = iter.nextInt();
         array[i] = (E)_indexer.getObject( idx );
      }
      return array;
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
      boolean modified = false;
      
      for( final T obj : this ) {
         if( !c.contains(obj) ) {
            remove( obj );
            modified = true;
         }
      }
      return modified;
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
