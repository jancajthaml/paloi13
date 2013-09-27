package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;

public class Main
{
	static  String                newline                 =  System.getProperty("line.separator");
    static  int                   BIG_ENOUGHT_NEIGHBOURS  =  3000;
    static  BufferedReader        bi                      =  new BufferedReader(new InputStreamReader(System.in));
    static  StringBuilder         buffer                  =  new StringBuilder();
    static  Cache                 cache                   =  new Cache();
    static  ArrayList<String>     line_map                =  new ArrayList<String>();
    static  ArrayList<Integer>    relation                =  new ArrayList<Integer>();
    static  Queue<Node>           q                       =  new LinkedList<Node>();
    static  int                   FIRST                   =  0;
    static  String                name                    =  "";
    
    static  HashSet<Node> marker = new HashSet<Node>();

    public static void main(String[] args)
    {
    	line_map  .  clear();
    	marker    .  clear();
    	relation  .  clear();
        buffer    .  setLength(0);
        //cache     .  clear();
        q         .  clear();
        
        name      =  "";
        FIRST     =  0;

        try
        {
            slurp();

            Node root = cache.get(FIRST);

            non_declared();
            
            traverse(root);
    		q.clear();
            
            if(cycle(root)) throw new FatalError();
            marker.clear();
            
            out();
        }
        catch(FatalError e){ System.out.println("ERROR"); }
    }

    static boolean cycle(Node node)
    {
    	if(marker.contains(node)) return true;
    	
    	marker.add(node);
    	
    	for(int i=0; i<node.index; i++)
    	{
    		if(!cache.containsKey(node.link[i].id)) return true;
    		
    		if(!node.link[i].marked)
    		{
    			node.link[i].marked = true;
    			if(cycle(node.link[i])) return true;
    		}
    	}
	
    	//marker.remove(node.data.hashCode());
    	marker.remove(node);
    	return false;
    }
    
    private static void non_declared()
    {
    	Collection<SoftReference<Node>> c = cache.values();
    	
    	for(SoftReference<Node> node : c)
    	{
    		Node n = node.get();
    		if(!n.isDeclared && n.isDependency)  throw new FatalError();
    	}
    	
    	c = null;
    }

    private static Node node(String key)
    { return cache.get(key.trim().hashCode()); }

    private static void readLine(String line)
	{
        line_map.add(line);
        
        if(line.startsWith("\t"))
        {
        	relation.add(name.hashCode());
        }
        else
        {
        	
        	int index				= line.indexOf(":");
        	if(index<=0) return;
        	name					= line.substring(0, index);
        	
        	if(FIRST==0) FIRST = name.hashCode();
        	
        	String[] dependencies	= line.substring(index).split(" ");
        	Node n				    = node(name);
        	n.isDeclared            = true;

        	for(int i=1; i<dependencies.length; i++)
        	{
        		if(dependencies[i].isEmpty()) continue;
                    
        		Node m = node(dependencies[i]);
        		n.addEdge(m);
        		m.isDependency = true;
        	}
        	
        	relation.add(name.hashCode());
        }
    }

    public static void slurp()
    {
    	try						{ while(true) readLine(bi.readLine()); }
    	catch(Exception e)		{/*ignore*/}
    	
    	try						{ bi.close(); }
    	catch (IOException e)	{/*ignore*/}
    	finally					{ bi = null; }
    	
    	System.runFinalization();
    	System.gc();
    }
    
    static void out() throws FatalError
    {
        buffer.setLength(0);

        Node n    = null;
        int size  = relation.size();
        
        for(int i=0; i<size; i++)
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
		boolean  visited       =  false;
		boolean  marked	       =  false;
		boolean  isDependency  =  false;
		boolean  isDeclared    =  false;
		Node[]   link          =  new Node[BIG_ENOUGHT_NEIGHBOURS];
		int      index         =  0;
		int      id            =  0;
		
		public Node(int id)
		{ this.id = id; }
	
		public void addEdge(Node b)
		{ link[index++] = b; }
		
		public boolean equals(Object other)
		{ return ((Node)other).id==this.id; }
		
		public int hashCode()
		{ return this.id; }

	}

    static class Cache
    { 
        private TreeMap<Integer, SoftReference<Node>> cache = new TreeMap<Integer, SoftReference<Node>>();

        public Node get(int key)
        {
        	Node node = null;
        	SoftReference<Node> ref = cache.get(key);
            if(ref != null)		node = ref.get();
            if(node == null)	cache.put(key, new SoftReference<Node>(node = new Node(key)));
            return node;   
        }

		public Collection<SoftReference<Node>> values()
		{ return cache.values(); }

		public boolean containsKey(int id)
		{ return cache.containsKey(id); }

    }
}