package fastcollections.lists;

import fastcollections.IntCollection;

public interface IntList extends IntCollection {

	int getFirst();
	
	int getLast();
	
	void removeFirst();
	
	void removeLast();
}
