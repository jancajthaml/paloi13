package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Main
{
    static int BIG_ENOUGHT_LINES               = 100000;
    static int BIG_ENOUGHT_NEIGHBOURS          = 1000;
    static BufferedReader bi                   = new BufferedReader(new InputStreamReader(System.in));
    static StringBuilder buffer                = new StringBuilder();
    public static HashMap<String,Node> cache   = new HashMap<String,Node>();
    static String[] line_map                   = new String[BIG_ENOUGHT_LINES];
	static String[] relation                   = new String[BIG_ENOUGHT_LINES];
	static int LINES_RED                       = 0;
	static String s                            = "";
	static String name                         = "";
	static int current                         = 0;	
	static Queue<Node> q                       = new LinkedList<Node>();

    public static void main(String[] args) throws IOException
    {
    	
    	bi = new BufferedReader(new InputStreamReader(System.in));
    	
        buffer.setLength(0);
        cache.clear();
        q.clear();
        
        LINES_RED = 0;
        s         = "";
        name      = "";
        current   = 0;

        try
        {
        	slurp();
		
        	Node root = cache.get("all");
        
        	unlinked();
        	traverse(root);
        	circle(root);
        	
        	out();
        }
        catch(FatalError e){ System.out.println("ERROR"); }
        
    }

    private static void unlinked()
    {
    	for(Node n : cache.values())
		if(n.isDependency && !n.isDeclared) throw new FatalError();
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
	
	private static void readLine() throws FatalError
	{
		line_map[LINES_RED++] = s;
		
		if(s.charAt(0) != 9)
		{
			try
			{
				int index				= s.indexOf(":");
				name					= s.substring(0, index);
				String[] dependencies	= s.substring(index).split(" ");
				Node n				    = null;
				
				if(!name.equals("all") && !cache.containsKey(name))
				{
					n = node(name);
					n.dead=true;
				}
				else n = node(name);
				
				n.isDeclared=true;

				for(int i=1; i<dependencies.length; i++)
				{
					if(dependencies[i].isEmpty()) continue;
					Node m = node(dependencies[i]);
					n.addEdge(m);
					m.isDependency=true;
					//edges.add(new Edge(n,m));
				}
				relation[LINES_RED-1]= name;
			}
			catch(StringIndexOutOfBoundsException e){}
		}
		else
		{
			relation[LINES_RED-1]=name;
		}
		
		s="";
	}
	
	
	public static void slurp() throws FatalError,IOException
	{
		while(((s=bi.readLine()) != null) && s.length()>0)
			readLine();
	}

	static void out() throws FatalError
	{
		buffer.setLength(0);
		
		Node n = null;
		for(int i=0; i<LINES_RED; i++)
		{
			n = cache.get(relation[i]);
			
			if(n.dead && n.visited)             throw new FatalError();

			
			if(n.visited) 	buffer.append(line_map[i]+"\n");
			else			buffer.append("#"+line_map[i]+"\n");
		}
		
		System.out.print(buffer);
	}
	
	private static void dumpLines()
	{
		for(int i=0; i<LINES_RED; i++)
			System.out.println(line_map[i]);
		
	}
	
	public static void circle(Node node) throws FatalError
	{
		for(int i=0; i<node.index; i++)
		{
			Node n    = node.link[i];
			n.marked = true;
			
			if(circle(n,node))
			{
				n.marked = false;
				throw new FatalError();
			}
			n.marked = false;
		}
	}
	
	public static boolean circle(Node node, Node origin)
	{
		
		for(int i=0; i<node.index; i++)
		{
			Node n = node.link[i];
			if(n==null) continue;
			if(origin.marked && n.marked) return true;
			if(node.marked && node.visited)
			{
				n.marked=true;
				if(circle(n,node))
				{
					n.marked=false;
					return true;
				}
				n.marked = false;
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
		
	}

	static class Node
	{
		String data       = "";
		boolean visited   = false;
		boolean marked	  = false;
		boolean dead      = false;
		
		boolean isDependency = false;
		boolean isDeclared   = false;
		
		private int index = 0;
		Node[] link       = new Node[BIG_ENOUGHT_NEIGHBOURS];
	
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