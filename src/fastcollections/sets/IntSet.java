package fastcollections.sets;

import fastcollections.IntCursor;
import fastcollections.IntIterator;

public interface IntSet extends Iterable<IntCursor> {
	
	int size();

	boolean contains( final int value );
	
	boolean add( final int value );
	
	boolean remove( final int value );
	
	IntIterator intIterator();
	
}
