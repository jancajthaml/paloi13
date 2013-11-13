package pal.api.generator;

import java.util.ArrayList;
import java.util.BitSet;

import pal.api.core.Vertex;
import pal.api.struct.GenericGraph;

public class NonIsomorphicGenerator<T>
{
	
	private Filter<T> decorator				=  null;
	static int degrees[][]					=  null;
	static BitSet degree_map				=  new BitSet();
	static ArrayList<Vertex[]> storage		=  new ArrayList<Vertex[]>();
	boolean first							=  false;

    public NonIsomorphicGenerator( Filter<T> processor )
    { this.decorator = processor; }
    
    public void generate( int ... sequence )
    {
    	storage.clear();
    	
    	this.first					= true;
    	int n						= sequence.length;
    	Vertex[] vertices			= new Vertex[n];
		int j						= 0;
		int i						= -1;
		int degree					= sequence[0];
		int swaps					= 1;
		int degree_index			= 0;
		int number_of_el_in_degree	= 0;
		int offset					= 0;
		int a						= 0;
		
		while( ++i<n )
		{
			if( ( j=sequence[i] )!=degree )
			{
				degree = j;
				swaps++;
			}
		}
		
		degree_map.clear();
		
		degrees		= new int[swaps][];
		degree		= sequence[0];
		i			= -1;

		while( ++i<n )
		{
			vertices[i] = new Vertex( sequence[i],i );

			if( sequence[i]!=degree )
			{
				degrees[degree_index]	= new int[number_of_el_in_degree];
				offset 					= i - number_of_el_in_degree;
				
				
		    	while( a<degrees[degree_index].length )
		    		degrees[degree_index][a] = a+++offset;
		    	
		    	degree					= sequence[i];
		    	number_of_el_in_degree	= 0;
				a						= 0;

				degree_index++;
			}
			
			number_of_el_in_degree++;
		}
		
		degrees[degree_index]	= new int[number_of_el_in_degree];
		offset					= i - number_of_el_in_degree;
		a						= 0;

    	while ( a<degrees[degree_index].length )
    		degrees[degree_index][a] = a+++offset;

    	ArrayList<int[]> maps		= new ArrayList<int[]>();

    	fillMaps                        ( 0,0,0,maps,new int[n] );
		generateAllNonIsomorphicGraphs  ( vertices,0,maps       );
    }
		
    private void generateAllNonIsomorphicGraphs( Vertex[] vertices,int index,ArrayList<int[]> maps )
    {
    	int n = vertices.length;
    	if( index==n )
    	{
    		Vertex[] temp = new Vertex[n];
    		
    		for( int i=0; i<n; i++ )
    			temp[i] = vertices[i].copy();
				
    		if( first )
    		{
    			first = false;
    			process( temp );
    		}
    		else
    		{
    			boolean isometric = false;
    			
    			outer:for( Vertex[] graph : storage )
    			{
    			 	inner:for( int[] map : maps )
    		    	{
    		    		for( int i = 0; i < graph.length; i++ )
    		    		for( Vertex neighbour : graph[i].conections )
    		    			if( !temp[map[i]].isConectedTo(map[neighbour.label]) ) continue inner;
    		    		
    		    		isometric = true;
    		    		
    		    		break outer;
    		    	}
    			}

    			if( !isometric ) process( temp );
    		}
    		return;
    	}
			
    	generateVerticies( index,vertices,index+1,maps );
    }
		
    private void process( Vertex ... verticies )
    {
    	storage.add( verticies );
    	
    	GenericGraph<T> graph = new GenericGraph<T>();
			
    	int i = -1;
    	int j = -1;
    	
    	while( ++i<verticies.length                      )
    	while( ++j<verticies[i].conections.length && i>j )
    		graph.addEdge(verticies[i].label, verticies[i].conections[j].label, 1);
		
    	decorator.process( graph );
    }

    private void generateVerticies( int index, Vertex[] vertices, int count,ArrayList<int[]> maps )
    {	
    	Vertex vertex = vertices[index];
		
    	if( vertex.degree-vertex.index>vertices.length-count ) return;
			
    	if( vertex.index==vertex.degree )
    	{
    		generateAllNonIsomorphicGraphs( vertices,index+1,maps );
    		return;
    	}

    	count-=1;
    	
		while( ++count<vertices.length )
    	{
    		if( vertices[count].degree==vertices[count].index ) continue;
    			
    		vertex . join      ( vertices[count]             );
    		generateVerticies  ( index,vertices,count+1,maps );
    		vertex . delete    ( vertices[count]             );
    	}
    }
		
    static void fillMaps( int index , int offset , int counter,ArrayList<int[]> maps,int[] map )
    {
    	if( counter==map.length )
    	{
    		int temp[] = new int[counter];
    		System.arraycopy(map, 0, temp, 0, map.length);
    		maps.add(temp);
    		return;
    	}
		
    	int n = degrees[index].length;
    	if( offset==n )
    	{
    		fillMaps( index+1,0,counter,maps,map );
    		return;
    	}
		
    	for( int i=0; i<n; i++ )
    	{
    		int o = index<<8;
    		if( degree_map.get(o+i) ) continue;
				
    		degree_map.set(o+i,true);
    		map[counter] = degrees[index][i];
    		fillMaps( index, offset+1, counter+1,maps,map );
    		degree_map.set(o+i,false);
    	}
    }

}