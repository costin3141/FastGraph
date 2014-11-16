package fastcollections;

public interface IntCollection extends Iterable<IntCursor> {

	boolean add( int value );
	boolean remove( int value );
	int size();
	boolean contains( int value );
	IntIterator intIterator();
	
	void clear();
}
