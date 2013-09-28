package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Main
{

	public static HashMap<Integer, SoftReference<Node>> cache = new HashMap<Integer, SoftReference<Node>>();
	static  String                newline                 =  System.getProperty("line.separator");
	static  int                   BIG_ENOUGHT_NEIGHBOURS  =  2500;
    static  BufferedReader        bi                      =  new BufferedReader(new InputStreamReader(System.in));
    static  StringBuilder         buffer                  =  new StringBuilder();
    static  Queue<Node>           q                       =  new LinkedList<Node>();
    static  int                   FIRST                   =  0;
    static  int                   name                    =  0;
    static  String                line                    =  "";
    static  int                   index                   =  0;
    static  HashSet<Node>         marker                  =  new HashSet<Node>();
	static ArrayList<Integer>     sort                    =  new ArrayList<Integer>();

    public static void main(String[] args)
    {

        try
        {
            slurp();

            Node root = cache.get(FIRST).get();
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

    public static void slurp()
    {
    	try
    	{ 
    		String[]dependencies	= null;
    		Node n					= null;
    		Node m					= null;
    		
    		Loop:while(true)
    		{
    			line=bi.readLine();
    			
    			 if(line.toCharArray()[0]=='\t')
    		     {
    				 n = node(name);
    				 n.lines.add(line.getBytes());
    		     }
    			 else
    			 {
    				 index				= line.indexOf(":");
    		        	
    				 if(index<=0) continue Loop;
    				 
    				 	name					= hash(line.substring(0, index));
    				 	
    				 	if(FIRST==0) FIRST=name;
    				 	
    		        	n				        = node(name);
    		        	n.isDeclared            = true;
    		        	
    		        	dependencies	= line.substring(index).split(" ");
    		        	
    		        	Loop1:for(int i=1; i<dependencies.length; i++)
    		        	{
    		        		if(dependencies[i].isEmpty()) continue Loop1;
    		                    
    		        		m = node(hash(dependencies[i]));
    		        		n.addEdge(m);
    		        		m.isDependency = true;
    		        	}
    		        	n.lines.add(line.getBytes());
    		        	sort.add(name);
    		        }
    		}
    	}
    	catch(Exception e)		{/*ignore*/}
    	
    	try						{ bi.close(); }
    	catch (IOException e)	{/*ignore*/}
    	finally					{ bi = null; }
    	
    	//System.gc();
    }
    
    static void out() throws FatalError
    {
        buffer.setLength(0);

        Node n    =  null;
	
    	for(int a : sort)
    	{
    		n = node(a);

        	if(n.visited)
        	{
        		for(byte[] line : n.lines)
        		{
        			buffer.append(new String(line));
        			buffer.append(newline);
        		}
        	}
        	else
        	{
        		for(byte[] line : n.lines)
        		{
        			buffer.append("#");
        			buffer.append(new String(line));
        			buffer.append(newline);
        		}
        	}
        	n.destroy();
        	release(a);
    	}
    	
        System.gc();
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
		boolean               visited       =  false;
		boolean               marked	    =  false;
		boolean               isDependency  =  false;
		boolean               isDeclared    =  false;
		Node[]                link          =  new Node[BIG_ENOUGHT_NEIGHBOURS];
		ArrayList<byte[]>     lines         =  new ArrayList<byte[]>();
		int                   index         =  0;
		int                   id            =  0;
		
		public Node(int id)
		{ this.id = id; }
	
		public void addEdge(Node b)
		{ link[index++] = b; }
		
		public int hashCode()
		{ return this.id; }
		
		public void destroy()
		{
			this.lines.clear();
			this.link=null;
			try { super.finalize(); }
			catch (Throwable e) {/*ignore*/}
		}

	}
    
    public static Node node(int key)
    {
    	Node node = null;
    	SoftReference<Node> ref   =  cache.get(key);
    	if(ref != null)     node  =  ref.get();
    	if(node == null)    cache.put(key, new SoftReference<Node>(node = new Node(key)));
    	return node;   
    }

    public static void release(int key)
    {
    	cache.remove(key);
    }
    
    static int hash( String text)
    {
    	return text.hashCode();
	}

}