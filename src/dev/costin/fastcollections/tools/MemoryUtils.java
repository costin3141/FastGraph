package dev.costin.fastcollections.tools;

public class MemoryUtils {

   public static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
   
   public static int capacity( final int minCapacity, final int oldCapacity ) {
      return capacity( minCapacity, oldCapacity, 1 );
   }
   
   public static int capacity( final int minCapacity, final int oldCapacity, final int growShifter ) {
      if( minCapacity < 0 ) { // overflow
         throw new OutOfMemoryError();
      }
      int newCapacity = oldCapacity + (oldCapacity >>> growShifter);
      if( newCapacity - minCapacity < 0 ) {
         newCapacity = minCapacity;
      }
      if( newCapacity - MAX_ARRAY_SIZE > 0 ) {
         newCapacity = hugeCapacity(minCapacity);
      }
      
      return newCapacity;
   }
   
   public static int hugeCapacity(final int minCapacity) {
      if( minCapacity < 0 ) { // overflow
         throw new OutOfMemoryError();
      }
      return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
   }

}
