package dev.costin.fastcollections.maps;

public interface IntObjectSortedMap<V> extends IntObjectMap<V> {

   IntObjectSortedMap<V> subMap( final int start, final int end );
   IntObjectSortedMap<V> subMap( final int start, final boolean includeStart, final int end, boolean includeEnd );
   
   // TODO: add inverse iterator ability
}
