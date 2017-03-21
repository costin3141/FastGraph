package dev.costin.fastcollections.dequeue;

/**
 * Interface for fast int-based queues (FIFO).
 * 
 * @author Stefan C. Ionescu
 *
 */
public interface IntQueue {

   /**
    * Puts an {@code element} into the queue.
    * 
    * @param element The element to put.
    * @return {@code True} if there was enough space and the element was successful inserted.
    */
   boolean offer( int element );
   
   /**
    * Removes and returns the element which is the longest time in the queue.
    * 
    * @return The element removed.
    */
   int take();
   
   /**
    * Returns but does not remove the next element that would be returned by {@link #take()}.
    *
    * @return The next queue element that would be taken.
    */
   int peekNext();
   
   /**
    * The number of elements within the queue.
    * 
    * @return The queue size.
    */
   int size();

   /**
    * Checks whether the queue is empty, {@link #size()}{@code ==0}.
    */
   boolean isEmpty();
   
   /** Remove all elements. */
   void clear();
}
