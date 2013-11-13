package pal;

import java.util.ArrayList;
import java.util.List;

public class Graph
{
	private List<Vertex>	verticies;
	private List<Edge>		edges;
	
	public Graph()
	{
		verticies	= new ArrayList<Vertex>();
		edges		= new ArrayList<Edge>();
	}

	public boolean addVertex(Vertex v)
	{ return verticies.add(v); }

	public Vertex getVertex( int n )
	{ return verticies.get(n); }

	public boolean addEdge( Vertex from, Vertex to )
	{
		Edge e = new Edge	( from , to );
		
		from	. addEdge	( e );
		to		. addEdge	( e );
		edges	. add		( e );
		
		return true;
	}

	public Matrix getSparseLaplacianMatrix()
    {
    	int n			= verticies.size();
    	Matrix matrix	= new Matrix(n);

    	for( Edge edge : edges )
    	{
    		int i	= edge.to.id;
    		int j	= edge.from.id;

    		matrix.set( i , j , -1 );
    		matrix.set( j , i , -1 );
    		
    		matrix.increment( i , i );
    		matrix.increment( j , j );
    	}
            
    	return matrix;
    }

	public int getNumberOfSpanningTrees()
	{ return getSparseLaplacianMatrix().determinant( this.verticies.size()-1 ); }

	public static class Edge
	{
		public Vertex from	= null;
		public Vertex to	= null;

		public Edge( Vertex from , Vertex to )
		{
			this.from	= from;
			this.to		= to;
		}
	}

	public static class Vertex
	{
		private List<Edge> in	= null;
		private List<Edge> out	= null;
		public int id			= 0;

		public Vertex( int i )
		{
			in	= new ArrayList<Edge>();
			out	= new ArrayList<Edge>();
			id	= i;
		}

		public boolean addEdge(Edge e)
		{
			     if (e.from == this)	out.add(e);
			else if (e.to == this)		in.add(e);
			else						return false;
			     						return true;
		}
	}

}