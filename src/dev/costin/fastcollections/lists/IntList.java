package dev.costin.fastcollections.lists;

import dev.costin.fastcollections.IntCollection;
import dev.costin.fastcollections.IntComparator;

public interface IntList extends IntCollection {

   int getFirst();
   int getLast();

   int get( final int index );

   /**
    * Substitutes the value at position {@code index} with the given {@code value}
    * and returns the previous one.
    */
   int set( final int index, final int value );

   void removeFirst();
   void removeLast();
   void removeIndex( final int index );

   void sort();
   void sort( final IntComparator comparator );

   /**
    * Returns the index/position in the list of the first occurrence of e or -1 if
    * e is not in the list.
    */
   int indexOf( int e );

   IntListIterator intListIterator();

}
