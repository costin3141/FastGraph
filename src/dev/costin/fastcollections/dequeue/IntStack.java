package dev.costin.fastcollections.dequeue;

import java.util.NoSuchElementException;

/**
 * Interface for fast int-based stacks (LIFO).
 * 
 * @author Stefan C. Ionescu
 *
 */
public interface IntStack {

   /**
    * Pushes an {@code element} onto the stack.
    * 
    * @return {@code True} if there was space for the new element and the push succeed.
    */
   boolean push( int element );
   
   /**
    * Removes and returns the top-most element from the stack (the element most recently pushed onto).
    * 
    * @return The element removed.
    * @throws NoSuchElementException If the stack is empty.
    */
   int pop();
   
   /**
    * Returns the number of elements currently on the stack.
    * 
    * @return The stack size.
    */
   int size();
   
   /**
    * Checks whether the stack is empty, {@link #size()}{@code ==0}.
    */
   boolean isEmpty();
   
   /** Remove all elements. */
   void clear();

}
