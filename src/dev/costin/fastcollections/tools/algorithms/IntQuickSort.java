package dev.costin.fastcollections.tools.algorithms;

import dev.costin.fastcollections.IntComparator;
import dev.costin.fastcollections.LongComparator;

public class IntQuickSort {

   private IntQuickSort() {
   }

   private static final int MAX_RUN_COUNT = 67;

   private static final int MAX_RUN_LENGTH = 33;

   private static final int QUICKSORT_THRESHOLD = 286;

   private static final int INSERTION_SORT_THRESHOLD = 47;

   public static void sort( int[] a, IntComparator cmp ) {
      sort( a, 0, a.length, cmp );
   }

   /**
    * # Sort the elements in a at index {@code from} to index {@code to}
    * (exclusive) using the {@link IntComparator} {@code cmp}.
    */
   public static void sort( int[] a, final int from, final int to, IntComparator cmp ) {
      assert to > from;

      int left = from;
      int right = to - 1;
      // Use Quicksort on small arrays
      if( right - left < QUICKSORT_THRESHOLD ) {
         sort( a, left, right, true, cmp );
         return;
      }

      int[] run = new int[MAX_RUN_COUNT + 1];
      int count = 0;
      run[0] = left;

      // Check if the array is nearly sorted
      for( int k = left; k < right; run[count] = k ) {
         if( cmp.compare( a[k], a[k + 1] ) < 0 ) { // ascending
            while( ++k <= right && cmp.compare( a[k - 1], a[k] ) <= 0 )
               ;
         }
         else if( cmp.compare( a[k], a[k + 1] ) > 0 ) { // descending
            while( ++k <= right && cmp.compare( a[k - 1], a[k] ) >= 0 )
               ;
            for( int lo = run[count] - 1, hi = k; ++lo < --hi; ) {
               int t = a[lo];
               a[lo] = a[hi];
               a[hi] = t;
            }
         }
         else { // equal
            for( int m = MAX_RUN_LENGTH; ++k <= right && cmp.compare( a[k - 1], a[k] ) == 0; ) {
               if( --m == 0 ) {
                  sort( a, left, right, true, cmp );
                  return;
               }
            }
         }

         if( ++count == MAX_RUN_COUNT ) {
            sort( a, left, right, true, cmp );
            return;
         }
      }

      // Check special cases
      if( run[count] == right++ ) { // The last run contains one element
         run[++count] = right;
      }
      else if( count == 1 ) { // The array is already sorted
         return;
      }

      int[] b;
      byte odd = 0;
      for( int n = 1; ( n <<= 1 ) < count; odd ^= 1 )
         ;

      if( odd == 0 ) {
         b = a;
         a = new int[b.length];
         for( int i = left - 1; ++i < right; a[i] = b[i] )
            ;
      }
      else {
         b = new int[a.length];
      }

      // Merging
      for( int last; count > 1; count = last ) {
         for( int k = ( last = 0 ) + 2; k <= count; k += 2 ) {
            int hi = run[k], mi = run[k - 1];
            for( int i = run[k - 2], p = i, q = mi; i < hi; ++i ) {
               if( q >= hi || p < mi && cmp.compare( a[p], a[q] ) >= 0 ) {
                  b[i] = a[p++];
               }
               else {
                  b[i] = a[q++];
               }
            }
            run[++last] = hi;
         }
         if( ( count & 1 ) != 0 ) {
            for( int i = right, lo = run[count - 1]; --i >= lo; b[i] = a[i] )
               ;
            run[++last] = right;
         }
         int[] t = a;
         a = b;
         b = t;
      }
   }

   private static void sort( int[] a, int left, int right, boolean leftmost, IntComparator cmp ) {
      int length = right - left + 1;

      // Use insertion sort on tiny arrays
      if( length < INSERTION_SORT_THRESHOLD ) {
         if( leftmost ) {

            for( int i = left, j = i; i < right; j = ++i ) {
               int ai = a[i + 1];
               while( cmp.compare( ai, a[j] ) < 0 ) {
                  a[j + 1] = a[j];
                  if( j-- == left ) {
                     break;
                  }
               }
               a[j + 1] = ai;
            }
         }
         else {

            do {
               if( left >= right ) {
                  return;
               }
            } while( cmp.compare( a[++left], a[left - 1] ) >= 0 );

            for( int k = left; ++left <= right; k = ++left ) {
               int a1 = a[k], a2 = a[left];

               if( cmp.compare( a1, a2 ) < 0 ) {
                  a2 = a1;
                  a1 = a[left];
               }
               while( cmp.compare( a1, a[--k] ) < 0 ) {
                  a[k + 2] = a[k];
               }
               a[++k + 1] = a1;

               while( cmp.compare( a2, a[--k] ) < 0 ) {
                  a[k + 1] = a[k];
               }
               a[k + 1] = a2;
            }
            int last = a[right];

            while( cmp.compare( last, a[--right] ) < 0 ) {
               a[right + 1] = a[right];
            }
            a[right + 1] = last;
         }
         return;
      }

      // Inexpensive approximation of length / 7
      int seventh = ( length >> 3 ) + ( length >> 6 ) + 1;

      int e3 = ( left + right ) >>> 1; // The midpoint
      int e2 = e3 - seventh;
      int e1 = e2 - seventh;
      int e4 = e3 + seventh;
      int e5 = e4 + seventh;

      // Sort these elements using insertion sort
      if( cmp.compare( a[e2], a[e1] ) < 0 ) {
         int t = a[e2];
         a[e2] = a[e1];
         a[e1] = t;
      }

      if( cmp.compare( a[e3], a[e2] ) < 0 ) {
         int t = a[e3];
         a[e3] = a[e2];
         a[e2] = t;
         if( cmp.compare( t, a[e1] ) < 0 ) {
            a[e2] = a[e1];
            a[e1] = t;
         }
      }
      if( cmp.compare( a[e4], a[e3] ) < 0 ) {
         int t = a[e4];
         a[e4] = a[e3];
         a[e3] = t;
         if( cmp.compare( t, a[e2] ) < 0 ) {
            a[e3] = a[e2];
            a[e2] = t;
            if( cmp.compare( t, a[e1] ) < 0 ) {
               a[e2] = a[e1];
               a[e1] = t;
            }
         }
      }
      if( cmp.compare( a[e5], a[e4] ) < 0 ) {
         int t = a[e5];
         a[e5] = a[e4];
         a[e4] = t;
         if( cmp.compare( t, a[e3] ) < 0 ) {
            a[e4] = a[e3];
            a[e3] = t;
            if( cmp.compare( t, a[e2] ) < 0 ) {
               a[e3] = a[e2];
               a[e2] = t;
               if( cmp.compare( t, a[e1] ) < 0 ) {
                  a[e2] = a[e1];
                  a[e1] = t;
               }
            }
         }
      }

      // Pointers
      int less = left; // The index of the first element of center part
      int great = right; // The index before the first element of right part

      if( cmp.compare( a[e1], a[e2] ) != 0 && cmp.compare( a[e2], a[e3] ) != 0 && cmp.compare( a[e3], a[e4] ) != 0
            && cmp.compare( a[e4], a[e5] ) != 0 ) {

         int pivot1 = a[e2];
         int pivot2 = a[e4];

         a[e2] = a[left];
         a[e4] = a[right];

         while( cmp.compare( a[++less], pivot1 ) < 0 )
            ;
         while( cmp.compare( a[--great], pivot2 ) > 0 )
            ;

         outer: for( int k = less - 1; ++k <= great; ) {
            int ak = a[k];
            if( cmp.compare( ak, pivot1 ) < 0 ) { // Move a[k] to left part
               a[k] = a[less];

               a[less] = ak;
               ++less;
            }
            else if( cmp.compare( ak, pivot2 ) > 0 ) { // Move a[k] to right part
               while( cmp.compare( a[great], pivot2 ) > 0 ) {
                  if( great-- == k ) {
                     break outer;
                  }
               }
               if( cmp.compare( a[great], pivot1 ) < 0 ) { // a[great] <= pivot2
                  a[k] = a[less];
                  a[less] = a[great];
                  ++less;
               }
               else { // pivot1 <= a[great] <= pivot2
                  a[k] = a[great];
               }

               a[great] = ak;
               --great;
            }
         }

         // Swap pivots into their final positions
         a[left] = a[less - 1];
         a[less - 1] = pivot1;
         a[right] = a[great + 1];
         a[great + 1] = pivot2;

         // Sort left and right parts recursively, excluding known pivots
         sort( a, left, less - 2, leftmost, cmp );
         sort( a, great + 2, right, false, cmp );

         if( less < e1 && e5 < great ) {

            while( cmp.compare( a[less], pivot1 ) == 0 ) {
               ++less;
            }

            while( cmp.compare( a[great], pivot2 ) == 0 ) {
               --great;
            }

            outer: for( int k = less - 1; ++k <= great; ) {
               int ak = a[k];
               if( cmp.compare( ak, pivot1 ) == 0 ) { // Move a[k] to left part
                  a[k] = a[less];
                  a[less] = ak;
                  ++less;
               }
               else if( cmp.compare( ak, pivot2 ) == 0 ) { // Move a[k] to right part
                  while( a[great] == pivot2 ) {
                     if( great-- == k ) {
                        break outer;
                     }
                  }
                  if( cmp.compare( a[great], pivot1 ) == 0 ) { // a[great] < pivot2
                     a[k] = a[less];

                     a[less] = pivot1;
                     ++less;
                  }
                  else { // pivot1 < a[great] < pivot2
                     a[k] = a[great];
                  }
                  a[great] = ak;
                  --great;
               }
            }
         }

         // Sort center part recursively
         sort( a, less, great, false, cmp );

      }
      else { // Partitioning with one pivot

         int pivot = a[e3];

         for( int k = less; k <= great; ++k ) {
            if( cmp.compare( a[k], pivot ) == 0 ) {
               continue;
            }
            int ak = a[k];
            if( cmp.compare( ak, pivot ) < 0 ) { // Move a[k] to left part
               a[k] = a[less];
               a[less] = ak;
               ++less;
            }
            else { // a[k] > pivot - Move a[k] to right part
               while( cmp.compare( a[great], pivot ) > 0 ) {
                  --great;
               }
               if( cmp.compare( a[great], pivot ) < 0 ) { // a[great] <= pivot
                  a[k] = a[less];
                  a[less] = a[great];
                  ++less;
               }
               else { // a[great] == pivot

                  a[k] = pivot;
               }
               a[great] = ak;
               --great;
            }
         }

         sort( a, left, less - 1, leftmost, cmp );
         sort( a, great + 1, right, false, cmp );
      }
   }

   public static void sort( long[] a, LongComparator cmp ) {
      sort( a, 0, a.length - 1, cmp );
   }

   public static void sort( long[] a, int left, int right, LongComparator cmp ) {
      // Use Quicksort on small arrays
      if( right - left < QUICKSORT_THRESHOLD ) {
         sort( a, left, right, true, cmp );
         return;
      }

      int[] run = new int[MAX_RUN_COUNT + 1];
      int count = 0;
      run[0] = left;

      // Check if the array is nearly sorted
      for( int k = left; k < right; run[count] = k ) {
         if( cmp.compare( a[k], a[k + 1] ) < 0 ) { // ascending
            while( ++k <= right && cmp.compare( a[k - 1], a[k] ) <= 0 )
               ;
         }
         else if( cmp.compare( a[k], a[k + 1] ) > 0 ) { // descending
            while( ++k <= right && cmp.compare( a[k - 1], a[k] ) >= 0 )
               ;
            for( int lo = run[count] - 1, hi = k; ++lo < --hi; ) {
               long t = a[lo];
               a[lo] = a[hi];
               a[hi] = t;
            }
         }
         else { // equal
            for( int m = MAX_RUN_LENGTH; ++k <= right && cmp.compare( a[k - 1], a[k] ) == 0; ) {
               if( --m == 0 ) {
                  sort( a, left, right, true, cmp );
                  return;
               }
            }
         }

         if( ++count == MAX_RUN_COUNT ) {
            sort( a, left, right, true, cmp );
            return;
         }
      }

      // Check special cases
      if( run[count] == right++ ) { // The last run contains one element
         run[++count] = right;
      }
      else if( count == 1 ) { // The array is already sorted
         return;
      }

      long[] b;
      byte odd = 0;
      for( int n = 1; ( n <<= 1 ) < count; odd ^= 1 )
         ;

      if( odd == 0 ) {
         b = a;
         a = new long[b.length];
         for( int i = left - 1; ++i < right; a[i] = b[i] )
            ;
      }
      else {
         b = new long[a.length];
      }

      // Merging
      for( int last; count > 1; count = last ) {
         for( int k = ( last = 0 ) + 2; k <= count; k += 2 ) {
            int hi = run[k], mi = run[k - 1];
            for( int i = run[k - 2], p = i, q = mi; i < hi; ++i ) {
               if( q >= hi || p < mi && cmp.compare( a[p], a[q] ) >= 0 ) {
                  b[i] = a[p++];
               }
               else {
                  b[i] = a[q++];
               }
            }
            run[++last] = hi;
         }
         if( ( count & 1 ) != 0 ) {
            for( int i = right, lo = run[count - 1]; --i >= lo; b[i] = a[i] )
               ;
            run[++last] = right;
         }
         long[] t = a;
         a = b;
         b = t;
      }
   }

   private static void sort( long[] a, int left, int right, boolean leftmost, LongComparator cmp ) {
      int length = right - left + 1;

      // Use insertion sort on tiny arrays
      if( length < INSERTION_SORT_THRESHOLD ) {
         if( leftmost ) {

            for( int i = left, j = i; i < right; j = ++i ) {
               long ai = a[i + 1];
               while( cmp.compare( ai, a[j] ) < 0 ) {
                  a[j + 1] = a[j];
                  if( j-- == left ) {
                     break;
                  }
               }
               a[j + 1] = ai;
            }
         }
         else {

            do {
               if( left >= right ) {
                  return;
               }
            } while( cmp.compare( a[++left], a[left - 1] ) >= 0 );

            for( int k = left; ++left <= right; k = ++left ) {
               long a1 = a[k], a2 = a[left];

               if( cmp.compare( a1, a2 ) < 0 ) {
                  a2 = a1;
                  a1 = a[left];
               }
               while( cmp.compare( a1, a[--k] ) < 0 ) {
                  a[k + 2] = a[k];
               }
               a[++k + 1] = a1;

               while( cmp.compare( a2, a[--k] ) < 0 ) {
                  a[k + 1] = a[k];
               }
               a[k + 1] = a2;
            }
            long last = a[right];

            while( cmp.compare( last, a[--right] ) < 0 ) {
               a[right + 1] = a[right];
            }
            a[right + 1] = last;
         }
         return;
      }

      // Inexpensive approximation of length / 7
      int seventh = ( length >> 3 ) + ( length >> 6 ) + 1;

      int e3 = ( left + right ) >>> 1; // The midpoint
      int e2 = e3 - seventh;
      int e1 = e2 - seventh;
      int e4 = e3 + seventh;
      int e5 = e4 + seventh;

      // Sort these elements using insertion sort
      if( cmp.compare( a[e2], a[e1] ) < 0 ) {
         long t = a[e2];
         a[e2] = a[e1];
         a[e1] = t;
      }

      if( cmp.compare( a[e3], a[e2] ) < 0 ) {
         long t = a[e3];
         a[e3] = a[e2];
         a[e2] = t;
         if( cmp.compare( t, a[e1] ) < 0 ) {
            a[e2] = a[e1];
            a[e1] = t;
         }
      }
      if( cmp.compare( a[e4], a[e3] ) < 0 ) {
         long t = a[e4];
         a[e4] = a[e3];
         a[e3] = t;
         if( cmp.compare( t, a[e2] ) < 0 ) {
            a[e3] = a[e2];
            a[e2] = t;
            if( cmp.compare( t, a[e1] ) < 0 ) {
               a[e2] = a[e1];
               a[e1] = t;
            }
         }
      }
      if( cmp.compare( a[e5], a[e4] ) < 0 ) {
         long t = a[e5];
         a[e5] = a[e4];
         a[e4] = t;
         if( cmp.compare( t, a[e3] ) < 0 ) {
            a[e4] = a[e3];
            a[e3] = t;
            if( cmp.compare( t, a[e2] ) < 0 ) {
               a[e3] = a[e2];
               a[e2] = t;
               if( cmp.compare( t, a[e1] ) < 0 ) {
                  a[e2] = a[e1];
                  a[e1] = t;
               }
            }
         }
      }

      // Pointers
      int less = left; // The index of the first element of center part
      int great = right; // The index before the first element of right part

      if( cmp.compare( a[e1], a[e2] ) != 0 && cmp.compare( a[e2], a[e3] ) != 0 && cmp.compare( a[e3], a[e4] ) != 0
            && cmp.compare( a[e4], a[e5] ) != 0 ) {

         long pivot1 = a[e2];
         long pivot2 = a[e4];

         a[e2] = a[left];
         a[e4] = a[right];

         while( cmp.compare( a[++less], pivot1 ) < 0 )
            ;
         while( cmp.compare( a[--great], pivot2 ) > 0 )
            ;

         outer: for( int k = less - 1; ++k <= great; ) {
            long ak = a[k];
            if( cmp.compare( ak, pivot1 ) < 0 ) { // Move a[k] to left part
               a[k] = a[less];

               a[less] = ak;
               ++less;
            }
            else if( cmp.compare( ak, pivot2 ) > 0 ) { // Move a[k] to right part
               while( cmp.compare( a[great], pivot2 ) > 0 ) {
                  if( great-- == k ) {
                     break outer;
                  }
               }
               if( cmp.compare( a[great], pivot1 ) < 0 ) {
                  a[k] = a[less];
                  a[less] = a[great];
                  ++less;
               }
               else {
                  a[k] = a[great];
               }
               a[great] = ak;
               --great;
            }
         }

         a[left] = a[less - 1];
         a[less - 1] = pivot1;
         a[right] = a[great + 1];
         a[great + 1] = pivot2;

         sort( a, left, less - 2, leftmost, cmp );
         sort( a, great + 2, right, false, cmp );

         if( less < e1 && e5 < great ) {

            while( cmp.compare( a[less], pivot1 ) == 0 ) {
               ++less;
            }

            while( cmp.compare( a[great], pivot2 ) == 0 ) {
               --great;
            }

            outer: for( int k = less - 1; ++k <= great; ) {
               long ak = a[k];
               if( cmp.compare( ak, pivot1 ) == 0 ) {
                  a[k] = a[less];
                  a[less] = ak;
                  ++less;
               }
               else if( cmp.compare( ak, pivot2 ) == 0 ) {
                  while( a[great] == pivot2 ) {
                     if( great-- == k ) {
                        break outer;
                     }
                  }
                  if( cmp.compare( a[great], pivot1 ) == 0 ) {
                     a[k] = a[less];
                     a[less] = pivot1;
                     ++less;
                  }
                  else {
                     a[k] = a[great];
                  }
                  a[great] = ak;
                  --great;
               }
            }
         }

         sort( a, less, great, false, cmp );
      }
      else {
         long pivot = a[e3];

         for( int k = less; k <= great; ++k ) {
            if( cmp.compare( a[k], pivot ) == 0 ) {
               continue;
            }
            long ak = a[k];
            if( cmp.compare( ak, pivot ) < 0 ) {
               a[k] = a[less];
               a[less] = ak;
               ++less;
            }
            else {
               while( cmp.compare( a[great], pivot ) > 0 ) {
                  --great;
               }
               if( cmp.compare( a[great], pivot ) < 0 ) {
                  a[k] = a[less];
                  a[less] = a[great];
                  ++less;
               }
               else { // a[great] == pivot
                  a[k] = pivot;
               }
               a[great] = ak;
               --great;
            }
         }

         sort( a, left, less - 1, leftmost, cmp );
         sort( a, great + 1, right, false, cmp );
      }
   }
}