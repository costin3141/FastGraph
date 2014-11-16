package fastgraph;

public interface Graph {
   
   int verticesCount();

   void disable( int vertex );
   
   void enable( int vertex );

   Adjacency adjacencyOf( int vertex );

   boolean hasEdge( int source, int dest );

   boolean addEdge( int source, int dest );
   
   boolean removeEdge( int source, int dest );
   
   int getOutDegree( int vertex );
   
   int getInDegree( int vertex );
   
}
