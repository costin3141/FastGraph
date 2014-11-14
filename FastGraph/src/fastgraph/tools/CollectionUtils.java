package fastgraph.tools;

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
}
