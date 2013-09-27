package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Main
{
	static  String newline = System.getProperty("line.separator");

    //static  int                   BIG_ENOUGHT_LINES       =  100000;
    static  int                   BIG_ENOUGHT_NEIGHBOURS  =  1000;
    static  BufferedReader        bi                      =  new BufferedReader(new InputStreamReader(System.in));
    static  StringBuilder         buffer                  =  new StringBuilder();
    static  HashMap<String,Node>  cache                   =  new HashMap<String,Node>();
    static  ArrayList<String>     line_map                =  new ArrayList<String>();
    static  ArrayList<String>     relation                =  new ArrayList<String>();
    static  int                   LINES_RED               =  0;
    static  String                name                    =  "";
    static  int                   current                 =  0;	
    static  Queue<Node>           q                       =  new LinkedList<Node>();
    static  String                FIRST                   =  "";
    
    static  HashSet<Node>  dependences                    =  new HashSet<Node>();
    static  HashSet<Node>  declarations                   =  new HashSet<Node>();

    static  HashSet<String> marker = new HashSet<String>();

    public static void main(String[] args)
    {
    	declarations.clear();
    	dependences.clear();
    	line_map.clear();
    	marker.clear();
    	relation.clear();
        buffer.setLength(0);
        cache.clear();
        q.clear();
        LINES_RED = 0;
        name      = "";
        FIRST     = "";
        current   = 0;
        bi        = new BufferedReader(new InputStreamReader(System.in));
    	
        try
        {
            slurp();

            Node root = cache.get(FIRST);

            non_declared();
            traverse(root);
            
            if(cycle(root)) throw new FatalError();

            out();
        }
        catch(FatalError e){ System.out.println("ERROR"); }
    }

    static boolean cycle(Node node)
    {
    	if(marker.contains(node.data)) return true;
    	
    	marker.add(node.data);
    	
    	for(int i=0; i<node.index; i++)
    	{
    		String name = node.link[i].data;
    		
    		if(!cache.containsKey(name)) return true;
    		
    		if(!node.link[i].marked)
    		{
    			node.link[i].marked = true;
    			if(cycle(node.link[i]))
    				return true;
    		}
    	}
	
    	marker.remove(node.data.hashCode());
    	return false;
    }
    
    private static void non_declared()
    {
    	for(Node n : Main.dependences)
    		if(!Main.declarations.contains(n)) throw new FatalError();
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

    private static void readLine(String line)
	{
    	
    	LINES_RED++;
        line_map.add(line);

        	if(line.startsWith("\t"))
        	{
        		relation.add(name);
        		//relation.set(LINES_RED-1, name);
        	}
        	else
        	{
        		if(line.indexOf(":")<=0) return;	
                int index				= line.indexOf(":");
                name					= line.substring(0, index);
                
                if(FIRST.equals("")) FIRST=name;
                
                String[] dependencies	= line.substring(index).split(" ");
                Node n				    = node(name);
				
                Main.declarations.add(n);
                
                n.isDeclared = true;

                for(int i=1; i<dependencies.length; i++)
                {
                    if(dependencies[i].isEmpty()) continue;
                    
                    Node m = node(dependencies[i]);
                    n.addEdge(m);
                    m.isDependency = true;
                    Main.dependences.add(m);
                }
                relation.add(name);
        }
       
       
       

    }

    public static void slurp()
    {
    	String line="";
    	try
    	{
    		while((line=bi.readLine()) != null)
    			readLine(line);
    	}
    	catch(Exception e){/*ignore*/}
    }
    
    static void out() throws FatalError
    {
        buffer.setLength(0);

        Node n = null;

        for(int i=0; i<LINES_RED; i++)
        {
			n = cache.get(relation.get(i));
			if(n==null) continue;

			if(n.visited) 	buffer . append(line_map.get(i)     + newline);
			else			buffer . append("#"+line_map.get(i) + newline);
        }
        System.out.print(buffer);
    }

	public static void traverse(Node node) throws FatalError
	{
		q.clear();
		q.add(node);
		node.visited	= true;
		
		while( !q.isEmpty() )
		{
			Node n = q.poll();

			for(int i=0; i< n.index; i++)
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
		String   data          =  "";
		boolean  visited       =  false;
		boolean  marked	       =  false;
		boolean  isDependency  =  false;
		boolean  isDeclared    =  false;
		Node[]   link          =  new Node[BIG_ENOUGHT_NEIGHBOURS];
		int      index         =  0;
		
		public Node(String data)
		{ this.data = data; }
	
		public void addEdge(Node b)
		{ link[index++] = b; }
	
		public java.lang.String toString()
		{ return data . toString(); }

		public boolean equals(Object another)
		{ return (this.hashCode() == another.hashCode()) || (((Node)another) . data.equals(this . data)); }
		
		public int hashCode()
		{ return this.data.hashCode(); }

	}

}