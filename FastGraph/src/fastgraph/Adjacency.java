package fastgraph;

public interface Adjacency extends Iterable<Integer> {
   
   Graph ownerGraph();
   
   int owner();
   
   int size();

	boolean contains( final int v );
	
	boolean add( final int v );
	boolean remove( final int v );
	
	IntIterator intIterator();
}
