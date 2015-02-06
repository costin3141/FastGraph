package dev.costin.fastgraph.properties;

import dev.costin.fastgraph.impl.DiGraphWithProperties;

/** Base class for properties of vertices or edges.
 *
 * <p>
 * Adding new properties is done by deriving from this class and using
 * the subclass as generic property in {@link DiGraphWithProperties}.
 * </p>
 */
public class BasicProperties implements Cloneable {

   public BasicProperties() {
      // This is the constructor used to create a properties
      // container for a new vertex or edge.
   }

   /** Edge color. Used for coloring graph algorithms. */
   public short color;
   
   /** Edge weight. Used for various graph algorithm. */
   public int   weight;

   @Override
   public BasicProperties clone() {
      try {
         return (BasicProperties) super.clone();
      }
      catch( CloneNotSupportedException e ) {
         // shouldn't be possible to happen
         throw new RuntimeException(e);
      }
   }
}
