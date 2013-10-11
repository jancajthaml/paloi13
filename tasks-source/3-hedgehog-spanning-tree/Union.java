package pal;

class Union
{	
	int start					= 0;
	int end						= 0;

	public boolean same(Edge e)
	{
		start	= e.from;
		end		= e.to;

		if (DataStorage.marker.contains(start) || DataStorage.marker.contains(end) || (find(start) == find(end)))	return true;
		
		union(start, end);
		return false;
	}

	private int union(int a, int b)
	{
		Edge e = DataStorage.edges.get(find(a));

		DataStorage.edges.get(find(b)).parent = e;

		return e.id;
	}

	private int find(int a)
	{
		Edge n		= DataStorage.edges.get(a);
		int jumps	= 0;
	
		while (n.parent != null)
		{
			n = n.parent;
			jumps++;
		}
		
		if (jumps > 1) repair(a, n.id);
		
		return n.id;
	}

	private void repair(int a, int id)
	{
		Edge curr = DataStorage.edges.get(a);

		while (curr.id != id)
		{
			Edge tmp	= curr.parent;
			curr.parent	= DataStorage.edges.get(id);
			curr		= tmp;
		}
	}

}