package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Main
{
	static int BIG_ENOUGHT_LINES				= 100000;
	static int BIG_ENOUGHT_NEIGHBOURS			= 100;
	static BufferedReader bi					= new BufferedReader(new InputStreamReader(System.in));
	static StringBuilder buffer					= new StringBuilder();
	public static HashMap<String,Node> cache	= new HashMap<String,Node>();
	static String[] line_map					= new String[BIG_ENOUGHT_LINES];
	static String[] relation					= new String[BIG_ENOUGHT_LINES];
	static int LINES_RED						= 0;
	static String s								= "";
	static String name							= "";
	static int current							= 0;	
	static Queue<Node> q						= new LinkedList<Node>();

	//TODO get rid of Node objects
	//private static int[] visited				= new int[BIG_ENOUGHT_LINES];
	
	public static void main(String[] args) throws IOException
	{
		buffer.setLength(0);
		cache.clear();
		//edges.clear();
		LINES_RED=0;
		
		s		= "";
		name	= "";
		current	= 0;
		
		slurp();
		Node root = cache.get("all");
		if(circle(root)) System.out.println("ERROR");
		else
		{
			try
			{
				traverse(root);
				out();
			}
			catch(FatalError e){ System.out.println("ERROR"); }
		}
	}

	private static Node node(String key)
	{
		key		= key.trim();
		Node n	= cache.get(key);
		
		if(n==null)
		{
			n = new Node(key);
			cache.put(key, n);
		}
		
		return n;
	}
	
	private static void readLine()
	{
		line_map[LINES_RED++] = s;
		
		if(s.charAt(0) != 9)
		{
			try
			{
				int index				= s.indexOf(":");
				name					= s.substring(0, index);
				String[] dependencies	= s.substring(index).split(" ");
				Node n					= node(name);

				for(int i=1; i<dependencies.length; i++)
				{
					if(dependencies[i].isEmpty()) continue;
					Node m = node(dependencies[i]);
					n.addEdge(m);
					//edges.add(new Edge(n,m));
				}
				relation[LINES_RED-1]= name;
			}
			catch(StringIndexOutOfBoundsException e){}
		}
		else relation[LINES_RED-1]=name;
		
		s="";
	}
	
	public static String slurp() throws UnsupportedEncodingException,IOException
	{
		while((current = bi.read()) > -1)
		{
			s+=(char)current;
				
			if(current == '\n')
				readLine();
		}
		
		//System.err.println("s "+s);
		//readLine();
			
		return s;
	}

	static void out()
	{
		buffer.setLength(0);
		
		//FIXME set "#" in NODE and get rid of O(n) here
		boolean ok	= true;
		
		
		for(int i=0; i<LINES_RED; i++)
		{
			ok = cache.get(relation[i]).visited;
			if(ok) 	buffer.append(line_map[i]);
			else	buffer.append("#"+line_map[i]);
		}
		//	buffer.append((i==0 || cache.get(relation.get(i)).visited)?(line_map[i]):("#"+line_map[i]));
		
		System.out.print(buffer);
	}

	
	public static boolean circle(Node node)
	{
		for(int i=0; i<node.index; i++)
		{
			Node n = node.link[i];
			n.visited=true;
			if(circle(n,node))
			{
				n.visited = false;
				return true;
			}
			n.visited = false;
		}
		return false;
	}
	
	public static boolean circle(Node node, Node origin)
	{
		
		for(int i=0; i<node.index; i++)
		{
			Node n = node.link[i];
			if(n==null) continue;
			if(origin.visited && n.visited) return true;
			if(node.visited)
			{
				n.visited=true;
				if(circle(n,node))
				{
					n.visited=false;
					return true;
				}
				n.visited = false;
			}
		}
		return false;
	}

	public static void traverse(Node node) throws FatalError
	{
		q.clear();
		q.add(node);
		node.visited	= true;
		
		while(!q.isEmpty())
		{
			Node n = (Node)q.poll();

			for(int i=0; i<n.index; i++)
			{
				Node adj = n.link[i];
			
				if(!adj.visited)
				{
					adj.visited = true;
					q.add(adj);
				}
				//else throw new FatalError(adj.toString());
			}
		}
	}

	static class FatalError extends RuntimeException
	{
		FatalError(String msg){super(msg);}
	}
	/*
	public static Vector<Node> getNeighbors(Node a)
	{
		Vector<Node> neighbors = new Vector<Node>();

		for(int i = 0; i < edges.size(); i++)
		{
			Edge edge = edges.elementAt(i);

			if(edge.a == a)
				neighbors.add(edge.b);
		}
		
		return neighbors;
	}
	*/

	static class Node
	{
		String data;
		boolean visited;
		private int index = 0;
		Node[] link = new Node[BIG_ENOUGHT_NEIGHBOURS];
		//List<Node> link = new LinkedList<Node>();
	
		public Node(String data)
		{ this.data = data; }
	
		public void addEdge(Node b)
		{ link[index++]=b; }
	
		public java.lang.String toString()
		{ return data.toString()+"["+(visited?"X":" ")+"]"; }

		public boolean equals(Object another)
		{ return ((Node)another).data == this.data; }
		
	}

}
