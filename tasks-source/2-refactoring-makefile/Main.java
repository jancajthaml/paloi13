package task2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

public class Main
{
	static int BIG_ENOUGHT_LINES				= 1000000;
	static BufferedReader bi					= new BufferedReader(new InputStreamReader(System.in));
	static StringBuilder buffer					= new StringBuilder();
	public static HashMap<String,Node> cache	= new HashMap<String,Node>();
	static String[] line_map					= new String[BIG_ENOUGHT_LINES];
	static int LINES_RED						= 0;
	static HashMap<Integer,String> relation		= new HashMap<Integer,String>();
	protected static Vector<Edge> edges			= new Vector<Edge>();
	static String s								= "";
	static String name							= "";
	static int current							= 0;
	
	//TODO get rid of Node objects
	private static int[] visited				= new int[BIG_ENOUGHT_LINES];
	
	public static void main(String[] args) throws IOException
	{
        
		LINES_RED = 0;
		
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
			catch(FatalError e){System.out.println("ERROR "+e.getLocalizedMessage());}
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
		
		//System.err.print("RED LINE : "+s);
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
					edges.add(new Edge(n,m));
				}
				relation.put(LINES_RED-1, name);
			}
			catch(StringIndexOutOfBoundsException e){}
		}
		else relation.put(LINES_RED-1, name);
		
		s="";
	}
	
	public static String slurp()
	{
		
		try
		{
			while((current = bi.read()) > -1)
			{
				s+=(char)current;
				
				if(current == '\n')
					readLine();
			}
			//System.err.println("s "+s);
			//readLine();
		}
		catch (UnsupportedEncodingException ex)	{ System.out.println("ERROR"); }
		catch (IOException ex)					{ System.out.println("ERROR"); }
		return s;
	}

	static void out()
	{
		buffer.setLength(0);
		
		//FIXME set "#" in NODE and get rid of O(n) here
		for(int i=0; i<LINES_RED; i++)
			buffer.append((i==0 || cache.get(relation.get(i)).visited)?(line_map[i]):("#"+line_map[i]));
		
		System.out.print(buffer);
	}

	
	public static boolean circle(Node node)
	{
		for(Node n : getNeighbors(node))
		{
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
		
		for(Node n : getNeighbors(node))
		{
			System.out.println(" checking "+origin+" –> "+node+" -> "+n);
			if(origin.visited && n.visited) return true;
			if(node.visited)
			{
				n.visited=true;
				if(circle(n,node)){
					n.visited=false;
					return true;
				}
				n.visited=false;
			}
		}
		return false;
	}
	
	public static void traverse(Node node) throws FatalError
	{
		Queue<Node> q	= new LinkedList<Node>();
		q.add(node);
		node.visited	= true;
		
		while(!q.isEmpty())
		{
			Node n = (Node)q.poll();

			for(Node adj : getNeighbors(n))
			{
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
}

class Node
{
	String data;
	boolean visited;


	public Node(String data)
	{ this.data = data; }
	
	public java.lang.String toString()
	{ return data.toString()+"["+(visited?"X":" ")+"]"; }

	public boolean equals(Object another)
	{
		System.out.println((((Node)another).data == this.data)+" "+this+" x "+another);
		return ((Node)another).data == this.data;
	}
}

class Edge
{
	protected Node a, b;
	
	public Edge(Node a, Node b)
	{
		this.a = a;
		this.b = b;
	}
}
