package dev.costin.fastcollections.sets.impl;

public class IntHashSet {
   
//   private long[] _occpupiedOrDeleted;
//   private int[] _set;
   private int _size;
   
   /**
    *  higher 32 bit: 0 -> empty, 1.. Integer.MIN_VALUE -> next pointer to slot (not to value!), -1 -> deleted
    *  lower 32 bit: the int value in use.
    */
   private long[] _set;
   private int[] _prev;

   public IntHashSet( final int initialCapacity ) {
//      _occpupiedOrDeleted = createDoubleBitSet( initialCapacity );
      _set = new long[ initialCapacity ];
      _prev = new int[ initialCapacity ];
      _size = 0;
   }
   
   public boolean add( int value ) {
      
   }
   
//   private static long[] createDoubleBitSet( int n ) {
//      return new long[((2*n - 1) >> 6) + 1];
//   }
//
//   private static void setFirstBit( long[] bits, int i ) {
//      bits[i >> 5] |= 1L << (i << 1 );
//   }
//   
//   private static void setSecondBit( long[] bits, int i ) {
//      bits[i >> 5] |= 1L << ((i << 1 ) + 1);
//   }
//   
//   private static void clearFirstBit( long[] bits, int i ) {
//      bits[i >> 5] &= ~( 1L << (i << 1 ) );
//   }
//   
//   private static void clearSecondBit( long[] bits, int i ) {
//      bits[i >> 5] &= ~( 1L << ((i << 1 ) + 1) );
//   }
//
//   private static boolean isFirstClear( long[] bits, int i ) {
//      return ( bits[i >> 5] & (1L << (i << 1 )) ) == 0;
//   }
//   
//   private static boolean isSecondClear( long[] bits, int i ) {
//      return ( bits[i >> 5] & (1L << ((i << 1 ) + 1)) ) == 0;
//   }
   
}
