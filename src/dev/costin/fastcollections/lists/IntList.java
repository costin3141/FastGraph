package dev.costin.fastcollections.lists;

import dev.costin.fastcollections.IntCollection;

public interface IntList extends IntCollection {

	int getFirst();
	
	int getLast();
	
	int get( final int index );
	
	void removeFirst();
	
	void removeLast();
}
