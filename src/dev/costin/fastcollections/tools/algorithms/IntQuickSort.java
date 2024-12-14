package dev.costin.fastcollections.tools.algorithms;

import dev.costin.fastcollections.IntComparator;
import dev.costin.fastcollections.LongComparator;

public class IntQuickSort {

   private IntQuickSort() {
   }

   public static void sort( final int list[], final int from, final int length, final IntComparator cmp ) {
//      mergeSort( list, from, , cmp );
      timSort( list, from, from + length - 1, cmp );
   }
   
   
   static int MIN_MERGE = 32;

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

   public static void insertionSort( final int[] list, final int left, final int right, final IntComparator cmp ) {
      for( int i = left + 1; i <= right; i++ ) {
         int temp = list[i];
         int j = i - 1;
         
         while( j >= left && cmp.compare( list[j], temp ) > 0 ) {
            list[j + 1] = list[j];
            j--;
         }
         list[j + 1] = temp;
      }
   }

   private static void timSort( final int[] list, final int from, final int to, final IntComparator cmp ) {
      int minRun = minRunLength( MIN_MERGE );

      // Sort individual subarrays of size RUN
      for( int i = from; i <= to; i += minRun ) {
         insertionSort( list, i, Math.min( ( i + MIN_MERGE - 1 ), to ), cmp );
      }

      final int length = to - from + 1;
      
      for( int size = minRun; size < length; size = 2 * size ) {

         for( int left = from; left <= to; left += 2 * size ) {

            int mid = left + size - 1;
            int right = Math.min( ( left + 2 * size - 1 ), to );

            if( mid < right ) {
               merge( list, left, mid, right, cmp );
            }
         }
      }
   }
   
   //////////// merge sort

   private static void mergeSort( int list[], int l, int r, final IntComparator cmp ) {
      if( l < r ) {
         int m = l + ( r - l ) / 2;
         mergeSort( list, l, m, cmp );
         mergeSort( list, m + 1, r, cmp );
         merge( list, l, m, r, cmp );
      }
   }

   private static void merge( int list[], int l, int m, int r, final IntComparator cmp ) {
      int n1 = m - l + 1;
      int n2 = r - m;
      int[] L = new int[n1];
      int[] R = new int[n2];

      for( int i = 0; i < n1; i++ ) {
         L[i] = list[l + i];
      }

      for( int i = 0; i < n2; i++ ) {
         R[i] = list[m + 1 + i];
      }

      int i = 0, j = 0, k = l;

      while( i < n1 && j < n2 ) {
         if( cmp.compare( L[i], R[j] ) <= 0 ) {
            list[k++] = L[i++];
         }
         else {
            list[k++] = R[j++];
         }
      }

      while( i < n1 ) {
         list[k++] = L[i++];
      }

      while( j < n2 ) {
         list[k++] = R[j++];
      }
   }
}