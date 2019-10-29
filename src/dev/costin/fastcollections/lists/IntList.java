package dev.costin.fastcollections.lists;

import dev.costin.fastcollections.IntCollection;

public interface IntList extends IntCollection {

	int getFirst();
	
	int getLast();
	
	int get( final int index );
	/** Substitutes the value at position {@code index} with the given {@code value} and returns the previous one. */
	int set( final int index, final int value );
	
	void removeFirst();
	
	void removeLast();
}
