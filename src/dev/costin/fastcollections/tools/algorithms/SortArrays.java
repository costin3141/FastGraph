package dev.costin.fastcollections.tools.algorithms;

import dev.costin.fastcollections.IntComparator;

public class SortArrays {

   private SortArrays() {}

   /** Sorts the array list from position {@code from} (inclusive) to position {@code to} (exclusive) using {@link IntComparator} {@code cmp}. */
   public static void sort( final int list[], final int from, final int to, final IntComparator cmp ) {
      timSort( list, from, to, cmp );
   }
   
//   public static void sort( final int list[], final int from, final int to, final IntComparator cmp ) {
//      final IntMinHeap heap = new IntMinHeap( cmp, to - from + 1 );
////      heap.invalidate();
//      
//      for( int i=from; i<to; i++ ) {
//         heap.offer( list[i] );
//      }
//      
//      for( int i=from; i<to; i++ ) {
//         list[i] = heap.take();
//      }
//   }

   public static boolean isSorted(  final int list[], final int from, final int to, IntComparator cmp ) {
      for( int i = from; i < to - 1; i++ ) {
         if( cmp.compare( list[from], list[ i + 1 ] ) > 0 ) {
            return false;
         }
      }
      
      return true;
   }
   
   
///////////////////////////////////////////////////////////////
   static final int MIN_MERGE = 32;
   
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
      final int minRun = minRunLength( MIN_MERGE );
      final int MIN_MERGE_MINUS_1 = MIN_MERGE - 1;

      for( int i = from; i < to; i += minRun ) {
         insertionSort( list, i, Math.min( ( i + MIN_MERGE_MINUS_1 ), to - 1 ), cmp );
      }

      final int length = to - from;
      final int[] buffer = new int[length];

      for( int size = minRun; size < length; size = ( size << 1 ) ) {

         for( int left = from; left < to; left += ( size << 1 ) ) {

            int mid = left + size - 1;
            int right = Math.min( ( left + ( size << 1 ) - 1 ), to - 1 );

            if( mid < right ) {
               merge( list, left, mid, right, cmp, buffer );
            }
         }
      }
   }

   private static void merge( final int list[], final int l, final int m, final int r, final IntComparator cmp, final int buffer[] ) {
      final int n1 = m - l + 1;
      final int n2 = r - m;
      final int endR = n1 + n2;

      // I optimized the first for-loop with a second loop variable k
      // but for some strange reason doing so for the second loop is worse.
      // Nobody understands java!

      for( int i = 0, k = l; i < n1; i++, k++ ) {
         buffer[i] = list[k];
      }

      for( int i = n1; i < endR; i++ ) {
         buffer[i] = list[m + 1 - n1 + i];
      }
//      for( int i = n1, k = m + 1; i < endR; i++, k++ ) {
//         buffer[i] = list[k];
//      }

      int i = 0, j = n1, k = l;

      while( i < n1 && j < endR ) {
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

      while( i < n1 ) {
         list[k++] = buffer[i++];
      }

      while( j < endR ) {
         list[k++] = buffer[j++];
      }
   }

   /*
    * Merge the sorted ranges [low, mid1), [mid1, mid2) and [mid2, high) mid1 is
    * first midpoint index in overall range to merge mid2 is second midpoint index
    * in overall range to merge
    */
   public static void merge( int[] gArray, int low, int mid1, int mid2, int high, int[] buffer, IntComparator cmp ) {
      int i = low, j = mid1, k = mid2, l = low;

      // choose smaller of the smallest in the three ranges
      while( ( i < mid1 ) && ( j < mid2 ) && ( k < high ) ) {
         if( cmp.compare( gArray[i], gArray[j] ) < 0 ) {
            if( cmp.compare( gArray[i], gArray[k] ) < 0 )
               buffer[l++] = gArray[i++];

            else
               buffer[l++] = gArray[k++];
         }
         else {
            if( cmp.compare( gArray[j], gArray[k] ) < 0 )
               buffer[l++] = gArray[j++];
            else
               buffer[l++] = gArray[k++];
         }
      }

      // case where first and second ranges have
      // remaining values
      while( ( i < mid1 ) && ( j < mid2 ) ) {
         if( cmp.compare( gArray[i], gArray[j] ) < 0 )
            buffer[l++] = gArray[i++];
         else
            buffer[l++] = gArray[j++];
      }

      // case where second and third ranges have
      // remaining values
      while( ( j < mid2 ) && ( k < high ) ) {
         if( cmp.compare( gArray[j], gArray[k] ) < 0 )
            buffer[l++] = gArray[j++];

         else
            buffer[l++] = gArray[k++];
      }

      // case where first and third ranges have
      // remaining values
      while( ( i < mid1 ) && ( k < high ) ) {
         if( cmp.compare( gArray[i], gArray[k] ) < 0 )
            buffer[l++] = gArray[i++];
         else
            buffer[l++] = gArray[k++];
      }

      // copy remaining values from the first range
      while( i < mid1 )
         buffer[l++] = gArray[i++];

      // copy remaining values from the second range
      while( j < mid2 )
         buffer[l++] = gArray[j++];

      // copy remaining values from the third range
      while( k < high )
         buffer[l++] = gArray[k++];
   }
   
//   static ExecutorService exec = Executors.newFixedThreadPool( 8 );
//
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
//      final int r = 1000;
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
//
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
//         assert isSorted( l2._list, 0, l2.size(), cmp );
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
//         assert isSorted( l2._list, 0, l2.size(), cmp );
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
//         assert isSorted( l2._list, 0, l2.size(), cmp );
//         if( l2.size() < 20 ) {
//            System.out.println( l2 );
//         }
//      }
//      System.out.println( System.currentTimeMillis() - t );
//      
//      exec.shutdown();
//   }
}