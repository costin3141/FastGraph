package dev.costin.fastcollections.tools;

public class CollectionUtils {

   /**
    * A fast fill for int arrays.
    * 
    * @param array
    *           The array to be filled.
    * @param start
    *           The index at which to start filling.
    * @param end
    *           The index at which to stop filling. {@code array[end]} will
    *           not be changed.
    * @param val
    *           The value to set.
    */
   public static void fill( final int[] array, final int start, final int end, int val ) {
      final int len = end - start;
      if( len > 0 ) {
         array[start] = val;
      }
      for( int i = 1; i < len; i += i ) {
         final int delta = (len - i);
         System.arraycopy( array, start, array, start + i,
                  (delta < i) ? delta : i );
      }
   }
   
   /**
    * A fast fill for long arrays.
    * 
    * @param array
    *           The array to be filled.
    * @param start
    *           The index at which to start filling.
    * @param end
    *           The index at which to stop filling. {@code array[end]} will
    *           not be changed.
    * @param val
    *           The value to set.
    */
   public static void fill( final long[] array, final int start, final int end, long val ) {
      final int len = end - start;
      if( len > 0 ) {
         array[start] = val;
      }
      for( int i = 1; i < len; i += i ) {
         final int delta = (len - i);
         System.arraycopy( array, start, array, start + i,
                  (delta < i) ? delta : i );
      }
   }
   
   public static int getUpperInt( long l ) {
      return (int)( l >>> 32);
   }
   
   public static int getLowerInt( long l ) {
      return (int) l;
   }
   
   public static long combineToLong( int upper, int lower ) {
      return ((long)upper << 32) | ((long)lower & 0xffffffffl );
//      long l;
//      if (lower < 0) {
//         l = upper + 1;
//      }
//      else {
//         l = upper;
//      }
//      l <<= 32;
//      l += lower;
//      return l;
   }
   
   public static long setLowerInt( long l, int lower ) {
      return (l & 0xffffffff00000000l) | ((long)lower & 0xffffffffl );
   }
   
   public static long setUpperInt( long l, int upper ) {
      return ((long)l & 0xffffffffl ) | ((long)upper << 32);
   }
}
