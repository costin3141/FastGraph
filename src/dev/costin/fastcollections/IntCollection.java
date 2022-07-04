package dev.costin.fastcollections;

public interface IntCollection extends Iterable<IntCursor> {

	boolean add( int value );
	
	boolean addAll( IntCollection elements );
	boolean addAll( int...elements );
	
	boolean remove( int value );
	boolean removeAll( IntCollection elements );
	
	boolean retainAll( IntCollection elements );
	
	int size();
	
	boolean isEmpty();
	
	boolean contains( int value );
	
	boolean containsAll( IntCollection c );
	
	IntIterator intIterator();
	
	void clear();
}
