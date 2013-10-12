package pal;

import java.util.HashMap;

public class Graph
{
    private List[]  vertices    =  null;
    private int[]   degree_map_in  =  null;
    private int[]   degree_map_out  =  null;
    
    public Graph( int n )
    {
        this.vertices   = new List[ n ];
        this.degree_map_in = new int[ n ];
        this.degree_map_out = new int[ n ];
        
        for( int i = 0 ; i<n ; i++ )
          vertices[i] = new List();
    }

    public void addEdge( int v, int w )
    {
    	degree_map_in[ w ]++;
    	degree_map_out[ v ]++;
    	
    	vertices[ v ] . add( w );
    }
    
    public int getDegreeIn( int v )
    { return degree_map_in[ v ]; }
    
    public int getDegreeOut( int v )
    { return degree_map_out[ v ]; }

    public List getVertexNeighbours( int v )
    { return vertices[ v ]; }

}