package fastgraph;

public interface Graph {

	boolean hasEdge( int source, int dest );
	
	boolean addEdge( int source, int dest );
	
	boolean removeEdge( int source, int dest );
}
