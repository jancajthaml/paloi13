package pal;

public class Kruskal
{
	private int	edges_discovered;
	private int	vertices_discovered;
	private int	size;

	public Kruskal( int size)
	{		
		this.size					= 0;
		this.edges_discovered		= 0;
		this.vertices_discovered	= size;
	}
	
	public void clear()
	{
		this.size				= 0;
		this.edges_discovered	= 0;
		
		for(Edge e : DataStorage.edges) e.parent = null;
	}

	public int analyse()
	{	
		for (Edge pointer : DataStorage.edges)
		{
			if (!DataStorage.union.same(pointer))
			{
				size				+= pointer.weight;
				edges_discovered	++;
			}
			
			if (vertices_discovered == edges_discovered+1) return size;
		}
		return -1;
	}

}