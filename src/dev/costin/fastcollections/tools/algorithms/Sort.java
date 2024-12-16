package dev.costin.fastcollections.tools.algorithms;

import dev.costin.fastcollections.IntComparator;
import dev.costin.fastcollections.lists.IntList;

public class Sort {

   private Sort() {
   }

   public static void sort( final int list[], final int from, final int to, final IntComparator cmp ) {
      timSort( list, from, to - 1, cmp );
   }

   public static boolean isSorted( IntList l, IntComparator cmp ) {
      for( int i = 0; i < l.size() - 1; i++ ) {
         if( cmp.compare( l.get( i ), l.get( i + 1 ) ) > 0 ) {
            return false;
         }
      }
      
      return true;
   }
   
   
///////////////////////////////////////////////////////////////
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
      int minRun = minRunLength( MIN_MERGE );

      for( int i = from; i <= to; i += minRun ) {
         insertionSort( list, i, Math.min( ( i + MIN_MERGE - 1 ), to ), cmp );
      }

      final int length = to - from + 1;
      final int[] buffer = new int[length];

      for( int size = minRun; size < length; size = ( size << 1 ) ) {

         for( int left = from; left <= to; left += ( size << 1 ) ) {

            int mid = left + size - 1;
            int right = Math.min( ( left + ( size << 1 ) - 1 ), to );

            if( mid < right ) {
               merge( list, left, mid, right, cmp, buffer );
            }
         }
      }
   }

   private static void merge( int list[], int l, int m, int r, final IntComparator cmp, final int buffer[] ) {
      final int n1 = m - l + 1;
      final int n2 = r - m;
      final int endR = n1 + n2;

      for( int i = 0; i < n1; i++ ) {
         buffer[i] = list[l + i];
      }

      for( int i = n1; i < endR; i++ ) {
         buffer[i] = list[m + 1 - n1 + i];
      }

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

}