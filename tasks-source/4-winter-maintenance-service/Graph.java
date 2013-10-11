package pal;

public class Graph
{
    private List[] vertices;

    public Graph( int n )
    {
        this.vertices = new List[n];
        
        for( int i = 0; i < n; i++ )
          vertices[i] = new List();
    }

    public void addEdge( int v, int w )
    { vertices[v].add(w); }

    public List getVertexNeighbours( int v )
    { return vertices[v]; }

}