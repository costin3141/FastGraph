package fastcollections;

public interface IntCollection extends Iterable<IntCursor> {

	boolean add( int value );
	
	int addAll( IntCollection elements );
	int addAll( int...elements );
	
	boolean remove( int value );
	
	int size();
	
	boolean contains( int value );
	
	IntIterator intIterator();
	
	void clear();
}
