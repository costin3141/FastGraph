package dev.costin.fastcollections.tools.algorithms;

import java.util.Date;
import java.util.Random;

import dev.costin.fastcollections.IntComparator;
import dev.costin.fastcollections.LongComparator;
import dev.costin.fastcollections.lists.impl.IntArrayList;
import dev.costin.fastcollections.tools.FastCollections;

public class Sort {

   private Sort() {
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

      final int[] L = new int[length - (length >> 2)];
      final int[] R = new int[length >> 1];

      for( int size = minRun; size < length; size = (size << 1) ) {
         for( int left = from; left <= to; left += (size << 1) ) {

            final int mid = left + size - 1;
            final int right = Math.min( ( left + (size << 1) - 1 ), to );

            if( mid < right ) {
               merge( list, left, mid, right, cmp, L, R );
            }
         }
      }
   }

   //////////// merge sort

//   private static void mergeSort( int list[], int l, int r, final IntComparator cmp ) {
//      if( l < r ) {
//         int m = l + ( r - l ) / 2;
//         mergeSort( list, l, m, cmp );
//         mergeSort( list, m + 1, r, cmp );
//         merge( list, l, m, r, cmp );
//      }
//   }

   private static void merge( final int list[], final int l, final int m, final int r, final IntComparator cmp,
         final int[] L, final int[] R ) {
      final int leftLength = m - l + 1;
      final int rightLength = r - m;

      for( int i = 0; i < leftLength; i++ ) {
         L[i] = list[l + i];
      }

      for( int i = 0; i < rightLength; i++ ) {
         R[i] = list[m + 1 + i];
      }

      int i = 0, j = 0, k = l;

      while( i < leftLength && j < rightLength ) {
         if( cmp.compare( L[i], R[j] ) <= 0 ) {
            list[k++] = L[i++];
         }
         else {
            list[k++] = R[j++];
         }
      }

      while( i < leftLength ) {
         list[k++] = L[i++];
      }

      while( j < rightLength ) {
         list[k++] = R[j++];
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
//      final int r = 1000;
//      
//      IntArrayList l = new IntArrayList(n);
//      for( int i=0; i<n; i++ ) {
//         l.add( i );
//      }
//      
//      FastCollections.shuffle( l, rnd );
//      
//      long t = System.currentTimeMillis();
//      for( int i=0; i<r; i++ ) {
////         System.out.println( i );
//         FastCollections.shuffle( l, rnd );
//         sort( l._list, 0, n-1, cmp );
////         mergeSort( l._list, 0, n-1, cmp );
//      }
//      System.out.println( System.currentTimeMillis() - t );
//      
//      t = System.currentTimeMillis();
//      for( int i=0; i<r; i++ ) {
////         System.out.println( i );
//         FastCollections.shuffle( l, rnd );
//         sort( l._list, 0, n-1, cmp );
////         mergeSort( l._list, 0, n-1, cmp );
//      }
//      System.out.println( System.currentTimeMillis() - t );
//      
//      t = System.currentTimeMillis();
//      for( int i=0; i<r; i++ ) {
//         IntArrayList l2 = new IntArrayList( rnd.nextInt(n) + 1 );
//         for( int j=0; j<n; j++ ) {
//            l2.add( i );
//         }
////         System.out.println( i );
//         FastCollections.shuffle( l2, rnd );
//         sort( l2._list, 0, l2.size() - 1, cmp );
////         mergeSort( l._list, 0, n-1, cmp );
//      }
//      System.out.println( System.currentTimeMillis() - t );
//   }
}