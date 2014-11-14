package fastgraph;

public interface Graph {
   
   int verticesCount();

   Adjacency adjacencyOf( int vertex );

   boolean hasEdge( int source, int dest );

}
