package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
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
	static int current							= 0;
	static String name							= "";
	
	//TODO get rid of Node objects
	private static int[] visited				= new int[BIG_ENOUGHT_LINES];
	
	public static void main(String[] args) throws IOException
	{
        
		LINES_RED = 0;
		
		slurp();
		Node root = cache.get("all");
		if(cycle(root)) System.out.println("ERROR");
		else
		{
			for(Node n : cache.values()) n.visited=false;
			traverse(root);
			out();
		}
		
		
	}

	private static Node node(String key)
	{
		key=key.trim();
		Node n ;
		if(!cache.containsKey(key))
		{
			//System.err.print("creating "+key);
			n = new Node(key);
			cache.put(key, n);
		}
		else n = cache.get(key);
		
		return n;
	}
	
	
	private static void readLine()
	{
		line_map[LINES_RED++]=s;
		
		System.err.print("RED LINE : "+s);
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
	

    private static void printEachLine() throws IOException {
        int foo = 0;
        char ch = 0;
        StringBuilder sb = new StringBuilder();
        
        boolean isEOL = false;  //err, need to set this or get rid of it
        do {
            foo = bi.read();
            ch = (char) foo;
            sb.append(ch);
            
            if ((ch == 10))
            {    
                System.err.print(sb);

                foo = bi.read();
                ch = (char) foo;
                sb.append(ch);
                if (ch != 13)
                {
                    while ((255 > ch) && (ch >= 0))
                    {
                        sb = new StringBuilder();
                        foo = bi.read();
                        ch = (char) foo;
                        sb.append(ch);
                        System.err.print(sb);
                        
                    }
                }
                sb.append(ch);
            }

        } while (!isEOL);
    }
    
	public static String slurp()
	{
		
		try
		{
			while((current = bi.read()) > -1)
			{
				s+=(char)current;
				
				if(current == '\n')
				{
					readLine();
				}
			}
			System.out.println("");
			//System.err.println("s "+s);
			//readLine();
		}
		catch (UnsupportedEncodingException ex)	{ System.out.println("ERROR");}
		catch (IOException ex)					{ System.out.println("ERROR");}
		return s;
	}

	static void out()
	{
		buffer.setLength(0);
		for(int i=0; i<LINES_RED; i++)
			buffer.append((i==0 || cache.get(relation.get(i)).visited)?(line_map[i]):("#"+line_map[i]));
		System.out.print(buffer);
	}


	static boolean cycle(Node n)
	{
	    return false;
	}
	
	public static void traverse(Node node)
	{
		Queue<Node> q = new LinkedList<Node>();
		q.add(node);
		node.visited=true;
		
		while(!q.isEmpty())
		{
			Node n = (Node)q.poll();

			for(Node adj : getNeighbors(n))
			{
				if(!adj.visited)
				{
					adj.visited=true;
					q.add(adj);
				}
			}
		}
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
