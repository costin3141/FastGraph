package dev.costin.fastgraph.properties;

public class BasicProperties implements Cloneable {

   public BasicProperties() {
      // This is the constructor used to create a properties
      // container for a new edge.
   }

   /** Edge color. Used for coloring graph algorithms. */
   short color;
   
   /** Edge weight. Used for various graph algorithm. */
   int   weight;

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
