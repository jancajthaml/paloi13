package pal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Node implements  Iterable<Integer>, Comparable<Node>
{

	int index								= -1;
	Sequence min							= new Sequence();
	Sequence max							= new Sequence();
	HashMap<Integer,ArrayList<Integer>> adj	= new HashMap<Integer,ArrayList<Integer>>();
	
	int predecessor							= 0;
	
	boolean visited							= false;
	boolean isTerminal						= false;
	boolean hasCycle						= false;
	int iterator							= 0;

	public Node( int index )
	{
		min.length	= Integer.MAX_VALUE;
		max.length	= Integer.MIN_VALUE;
		this.index	= index;
		predecessor	= max.length;

		for( int i=0; i<Main.M ; i++ ) adj.put(i, new ArrayList<Integer>());		
	}

	public void addEdge(int i, int j)
	{ adj.get(i).add(j); }

	public void clear()
	{
		max.length	= 0;
		min.length	= 0;
		max.data	= "";
		min.data	= "";
	}

	public String toString()
	{ return "max["+this.max.data+"] / min["+this.min.data+"]  --  "+this.adj; }

	@Override public Iterator<Integer> iterator()
	{ return adj.get(iterator++).iterator(); }

	@Override public int compareTo(Node o)
	{ return min.length-o.min.length; }

}