package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main
{
	static  String newline = System.getProperty("line.separator");

    static  int                   BIG_ENOUGHT_LINES       =  10000;
    static  int                   BIG_ENOUGHT_NEIGHBOURS  =  100;
    static  BufferedReader        bi                      =  new BufferedReader(new InputStreamReader(System.in));
    static  StringBuilder         buffer                  =  new StringBuilder();
    static  HashMap<String,Node>  cache                   =  new HashMap<String,Node>();
    static  String[]              line_map                =  new String[BIG_ENOUGHT_LINES];
    static  String[]              relation                =  new String[BIG_ENOUGHT_LINES];
    static  int                   LINES_RED               =  0;
    static  String                name                    =  "";
    static  int                   current                 =  0;	
    static  Queue<Node>           q                       =  new LinkedList<Node>();
    static String FIRST = "";

    public static void main(String[] args) throws IOException
    {
    	
        buffer.setLength(0);
        cache.clear();
        q.clear();
        LINES_RED = 0;
        FIRST     = "";
        name      = "";
        current   = 0;
        bi        = new BufferedReader(new InputStreamReader(System.in));
    	
        try
        {
            slurp();

            Node root = cache.get(FIRST);

//            unlinked();
            traverse(root);
          //  circle(root);

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
        key     = key.trim();
        Node n  = cache.get(key);

        if(n==null)
        {
            n = new Node(key);
            cache.put(key, n);
        }
        return n;
    }
    
    static StringTokenizer tokenizer;
    
    private static void readLine(String line) throws FatalError
	{
    	if(line.trim().isEmpty()) return;
    	
    	line_map[LINES_RED++] = line;
    	
    	if (line.startsWith("\t"))
    	{
            relation[LINES_RED-1] = name;
		}
    	else
    	{
	        tokenizer             = new StringTokenizer(line);
			
            try
            { name = tokenizer.nextToken().replace(":", ""); }
            catch(NoSuchElementException e)
            { return; }
            
            if(FIRST.equalsIgnoreCase("")) FIRST=name;
            
            Node n				    = null;

            if(!name.equals(FIRST) && !cache.containsKey(name))
            {
                n      = node(name);
                n.dead = true;
            }
            else n = node(name);
			
            n.isDeclared=true;

			while(tokenizer.hasMoreTokens())
			{
                Node m = node(tokenizer.nextToken());
                n.addEdge(m);
                m.isDependency = true;
                
			}
            relation[LINES_RED-1]= name;
		}
    }

    public static void slurp() throws FatalError,IOException
    {
    	String line="";
    	while ((line = bi.readLine()) != null)
    	{ readLine(line); }
    }
       

    static void out() throws FatalError
    {
        buffer.setLength(0);

        Node n = null;

        for(int i=0; i<LINES_RED; i++)
        {
			n = cache.get(relation[i]);
			if(n==null) continue;
			if(n.dead && n.visited) throw new FatalError();

			if(n.visited) 	buffer.append(line_map[i]+newline);
			else			buffer.append("#"+line_map[i]+newline);
        }
		
        System.out.print(buffer);
    }

    public static void circle(Node node) throws FatalError
    {
        for(int i=0; i<node.index; i++)
        {
            Node n   = node.link[i];
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

			if(n==null)                   continue;
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

			}
		}
	}

    static class FatalError extends RuntimeException
    { private static final long serialVersionUID = 3638938829930139263L; }

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