package dev.costin.fastcollections.bridging.collections;

import java.util.Iterator;

import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.bridging.IndexedObjectBridge;

public class IndexedObjectIterator<T> implements Iterator<T> {
   
   private final IndexedObjectBridge<T> _indexer;
   private final IntIterator _iterator;
   
   public IndexedObjectIterator( final IndexedObjectBridge<T> indexer, final IntIterator iterator ) {
      _indexer = indexer;
      _iterator = iterator;
   }

   @Override
   public boolean hasNext() {
      return _iterator.hasNext();
   }

   @Override
   public T next() {
      final int idx = _iterator.nextInt();
      return _indexer.getObject( idx );
   }

   @Override
   public void remove() {
      _iterator.remove();
   }

}
