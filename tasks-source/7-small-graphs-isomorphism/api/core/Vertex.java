package pal.api.core;

public class Vertex
{
	public int	label      ;
	public int       	degree     ;
	public Vertex[]  	conections ;
	public int       	index       ;
	    	
	public Vertex( int degree , int index )
	{
		this.label		= index;
		this.degree		= degree;
		this.conections	= new Vertex[degree];
		this.index		= 0;
	}
			
	public void delete(Vertex vertex)
	{
		vertex . index--;
		this   . index--;
	}

	public void join(Vertex vertex)
	{	
		conections[index++] = vertex;
		vertex.conections[vertex.index++] = this;
	}

	public boolean isConectedTo( int i )
	{
		for( Vertex v : conections ) if( v.label==i ) return true;
		return false;
	}

	public Vertex copy()
	{
		Vertex v = new Vertex( degree,label );
		System.arraycopy(conections, 0, v.conections, 0, conections.length);
		return v;
	}

}