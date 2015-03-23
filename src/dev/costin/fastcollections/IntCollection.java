package dev.costin.fastcollections;

public interface IntCollection extends Iterable<IntCursor> {

	boolean add( int value );
	
	int addAll( IntCollection elements );
	int addAll( int...elements );
	
	boolean remove( int value );
	
	int size();
	
	boolean isEmpty();
	
	boolean contains( int value );
	
	boolean containsAll( IntCollection c );
	
	IntIterator intIterator();
	
	void clear();
}
