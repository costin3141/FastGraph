package dev.costin.fastcollections.tools.algorithms;

import dev.costin.fastcollections.IntComparator;

public class SortArrays {

   private SortArrays() {}

   /** Sorts the array list from position {@code from} (inclusive) to position {@code to} (exclusive) using {@link IntComparator} {@code cmp}. */
   public static void sort( final int list[], final int from, final int to, final IntComparator cmp ) {
      timSort( list, from, to, cmp );
   }
   
   public static boolean isSorted(  final int list[], final int from, final int to, IntComparator cmp ) {
      for( int i = from; i < to - 1; i++ ) {
         if( cmp.compare( list[from], list[ i + 1 ] ) > 0 ) {
            return false;
         }
      }
      
      return true;
   }
   
   
///////////////////////////////////////////////////////////////
   static final int MIN_MERGE = 64;
   static final int MIN_RUN = minRunLength( MIN_MERGE );
   static final int MIN_MERGE_MINUS_1 = MIN_MERGE - 1;
   
   private static int minRunLength( int n ) {
      assert n >= 0;

      // Becomes 1 if any 1 bits are shifted off
      int r = 0;
      while( n >= MIN_MERGE ) {
         r |= ( n & 1 );
         n >>= 1;
      }
      return n + r;
   }

   private static void insertionSort( final int[] list, final int from, final int to, final IntComparator cmp ) {
      for( int i = from + 1; i <= to; i++ ) {
         int temp = list[i];
         int j = i - 1;

         while( j >= from && cmp.compare( list[j], temp ) > 0 ) {
            list[j + 1] = list[j];
            j--;
         }
         list[j + 1] = temp;
      }
   }

   private static void timSort( final int[] list, final int from, final int to, final IntComparator cmp ) {
      for( int i = from; i < to; i += MIN_RUN ) {
         insertionSort( list, i, Math.min( ( i + MIN_MERGE_MINUS_1 ), to - 1 ), cmp );
      }

      final int length = to - from;
      final int[] buffer = new int[length];

      for( int size = MIN_RUN; size < length; size = ( size << 1 ) ) {

         for( int left = from; left < to; left += ( size << 1 ) ) {

            final int mid = left + size - 1;
            final int right = Math.min( ( left + ( size << 1 ) - 1 ), to - 1 );

            if( mid < right ) {
               merge( list, left, mid, right, cmp, buffer );
            }
         }
      }
   }

   private static void merge( final int list[], final int l, final int m, final int r, final IntComparator cmp, final int buffer[] ) {
      final int sizeL = m - l + 1;
      final int endR = sizeL + r - m;

      // I optimized the first for-loop with a second loop variable k
      // but for some strange reason doing so for the second loop is worse.
      // Nobody understands java!

      for( int i = 0, k = l; i < sizeL; i++, k++ ) {
         buffer[i] = list[k];
      }

      for( int i = sizeL; i < endR; i++ ) {
         buffer[i] = list[l + i];
      }

      int i = 0, j = sizeL, k = l;

      while( i < sizeL && j < endR ) {
         final int lValue = buffer[i];
         final int rValue = buffer[j];

         if( cmp.compare( lValue, rValue ) <= 0 ) {
            list[k++] = lValue;
            ++i;
         }
         else {
            list[k++] = rValue;
            ++j;
         }
      }

      while( i < sizeL ) {
         list[k++] = buffer[i++];
      }

      while( j < endR ) {
         list[k++] = buffer[j++];
      }
   }

//   public static void main( String[] args ) {
//      final IntComparator cmp = new IntComparator() {
//
//         @Override
//         public int compare( int i1, int i2 ) {
//            return Integer.compare( i2, i1 );
//         }
//      };
//
//      Random rnd = new Random( new Date().getTime() );
//      final int n = 100000;
//      final int r = 10000;
//
//      IntArrayList l[] = new IntArrayList[4];
//      for( int k=0; k<l.length; k++ ) {
//         l[k] = new IntArrayList(n);
//         
//         for( int i = 0; i < n; i++ ) {
//            l[k].add( i );
//         }
//      }
//      
//      long t = System.currentTimeMillis();
//      
//      Future<Integer>[] f = new Future[l.length];
//      
//      t = System.currentTimeMillis();
//      for( int i = 0; i < r; i++ ) {
//         for( int k=0; k<l.length; k++ ) {
//            final IntArrayList list = l[k];
//            final int lk = k;
//            final int round = i;
//            
//           f[k] = exec.submit( new Callable<Integer>() {
//
//               @Override
//               public Integer call() throws Exception {
////                  System.out.println( Thread.currentThread().getId() + " " + round + " " + lk + " -> shuffle..." );
//                  FastCollections.shuffle( list, rnd );
////                  System.out.println( Thread.currentThread().getId() + " " + round + " " + lk + " -> sort..." );
//                  list.sort( cmp );
//                  return list.get(0);
//               }
//               
//            } );
//         }
//         
//         for( int k=0; k<l.length; k++ ) {
//            try {
//               System.out.println( "result " + k + " " + f[k].get() );
//            }
//            catch( InterruptedException | ExecutionException e ) {
//               // TODO Auto-generated catch block
//               e.printStackTrace();
//            }
//         }
////         );
//      }
//
//      t = System.currentTimeMillis();
//      for( int i = 0; i < r; i++ ) {
//         final int round = i;
//         Arrays.stream( l ).parallel().forEach( list -> {
////         for( int k=0; k<l.length; k++ ) {
////            final IntArrayList list = l[k];
//            final int lk = -1;//k;
//            
////            System.out.println( Thread.currentThread().getId() + " " + round + " " + lk + " -> shuffle..." );
//            FastCollections.shuffle( list, rnd );
////            System.out.println( Thread.currentThread().getId() + " " + round + " " + lk + " -> sort..." );
//            list.sort( cmp );
////            System.out.println( "result " + lk + " " + list.get(0) );
//         }
//         );
//      }
//      System.out.println( System.currentTimeMillis() - t );

//      IntArrayList l2 = new IntArrayList();
//      final int k =  rnd.nextInt( n ) + 1;
//      for( int j = 0; j < k; j++ ) {
//         l2.add( j );
//      }
//
//      t = System.currentTimeMillis();
//      for( int i = 0; i < r; i++ ) {
//         FastCollections.shuffle( l2, rnd );
//         if( l2.size() < 50 ) {
//            System.out.println( l2 );
//         }
//         
//         l2.sort( cmp );
////         assert isSorted( l2._list, 0, l2.size(), cmp );
//         if( l2.size() < 20 ) {
//            System.out.println( l2 );
//         }
//      }
//      System.out.println( System.currentTimeMillis() - t );
//      
//      t = System.currentTimeMillis();
//      for( int i = 0; i < r; i++ ) {
//         FastCollections.shuffle( l2, rnd );
//         if( l2.size() < 50 ) {
//            System.out.println( l2 );
//         }
//         
//         l2.sort( cmp );
////         assert isSorted( l2._list, 0, l2.size(), cmp );
//         if( l2.size() < 20 ) {
//            System.out.println( l2 );
//         }
//      }
//      System.out.println( System.currentTimeMillis() - t );
//      
//      t = System.currentTimeMillis();
//      for( int i = 0; i < r; i++ ) {
//         FastCollections.shuffle( l2, rnd );
//         if( l2.size() < 50 ) {
//            System.out.println( l2 );
//         }
//         
//         l2.sort( cmp );
////         assert isSorted( l2._list, 0, l2.size(), cmp );
//         if( l2.size() < 20 ) {
//            System.out.println( l2 );
//         }
//      }
//      System.out.println( System.currentTimeMillis() - t );
//      
//   }
}