package fastgraph;

public interface DeactivatableVerticesGraph extends Graph {

	  void disable( int vertex );
	   
	  void enable( int vertex );
	  
	  

}
