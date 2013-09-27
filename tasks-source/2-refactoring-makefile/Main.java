package pal;

import java.io.BufferedReader;
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
    static  int                   BIG_ENOUGHT_NEIGHBOURS  =  100;
    static  BufferedReader        bi                      =  new BufferedReader(new InputStreamReader(System.in));
    static  StringBuilder         buffer                  =  new StringBuilder();
    static  HashMap<String,Node>  cache                   =  new HashMap<String,Node>();
    static  ArrayList<String>     line_map                =  new ArrayList<String>();
    static  ArrayList<String>     relation                =  new ArrayList<String>();
    static  Queue<Node>           q                       =  new LinkedList<Node>();
    
    static  String                FIRST;
    static  int                   LINES_RED;
    static  String                name;
    static  int                   current;
    
    static  HashSet<String> marker = new HashSet<String>();

    public static void main(String[] args)
    {
    	line_map . clear();
    	marker   . clear();
    	relation . clear();
        buffer   . setLength(0);
        cache    . clear();
        q        . clear();

        name      = "";
        FIRST     = "";
        LINES_RED = 0;
        current   = 0;
    	
        try
        {
            slurp();

            Node root = cache.get(FIRST);

            if(root==null) throw new FatalError();
            
            non_declared();
            traverse(root);
            
            if(cycle(root)) throw new FatalError();

            out();
        }
        catch(FatalError e){ System.out.println("ERROR"); }
    }

    static boolean cycle(Node node)
    {
    	if(node==null) return false;
    	
    	if(marker.contains(node.data)) return true;
    	
    	marker.add(node.data);
    	
    	for(int i=0; i<node.index; i++)
    	{
    		if(!cache.containsKey(node.link[i].data)) return true;
    		
    		if(!node.link[i].marked)
    		{
    			node.link[i].marked = true;
    			if(cycle(node.link[i]))
    				return true;
    		}
    	}
	
    	marker.remove(node.data);
    	return false;
    }
    
    //OK
    private static void non_declared()
    {
    	for(Node n : cache.values())
    		if(n.visited && !n.isDeclared && n.isDependency) throw new FatalError();
    }

    //OK
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

    //FIXME breaks at ~50ms sooooo it should be somewhere there
    private static void readLine(String line)
	{
    	
    	LINES_RED++;
        line_map.add(line);

        if(line.startsWith("\t"))
        {
        	relation.add(name);
        }
        else
        {
        	int index				= line.indexOf(":");
        	
        	if(index<=0) return;
        	
        	name					= line.substring(0, index);
                
        	if(FIRST.equals("")) FIRST=name;
            
        	//FIXME there could be a exception
        	String[] dependencies	= line.substring(index).split(" ");
        	Node n				    = node(name);
				
        	n.isDeclared = true;

        	//OK ?
        	//if(dependencies.length>1)
        	for(int i=1; i<dependencies.length; i++)
        	{
        		//OK
        		if(dependencies[i]==null || dependencies[i].isEmpty()) continue;
                    
        		Node m = node(dependencies[i]);
        		n.addEdge(m);
        		m.isDependency = true;
        	}
        	
        	relation.add(name);
        }
    }

    //OK
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
    
    
    //OK
    static void out()
    {
        buffer.setLength(0);

        Node n = null;

        for(int i=0; i<LINES_RED; i++)
        {
        	//FIXME there could be a exception
			n = cache.get(relation.get(i));
			
			if(n==null) continue;

			if(n.visited) 	buffer . append(line_map.get(i)     + newline);
			else			buffer . append("#"+line_map.get(i) + newline);
        }
        
        System.out.print(buffer);
    }

    
	public static void traverse(Node node) throws FatalError
	{
		//FIXME need?
		if(node==null) return;
		
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