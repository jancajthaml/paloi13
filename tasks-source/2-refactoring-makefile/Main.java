package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Main
{

    //------------------------------------------------------------------------------------//

    private static  HashMap<Integer, SoftReference<Node>> cache     =  new HashMap<Integer, SoftReference<Node>>();
    private static  String                                newline   =  System.getProperty("line.separator");
    private static  BufferedReader                        in        =  new BufferedReader(new InputStreamReader(System.in));
    private static  HashSet<Integer>                      marker    =  new HashSet<Integer>();
    private static  ArrayList<Integer>                    sort      =  new ArrayList<Integer>();
    private static  Node                                  root      =  null;
    private static  int                                   name      =  0;

    //------------------------------------------------------------------------------------//
    
    public static void main(String[] args)
    {
        try
        {
            read_data                                               ();
            check_cyclic_dependedcy___dead_code___non_declaration   ();
            flush_data                                              ();
        }
        catch(FatalError e){ System.out.println("ERROR"); }
    }
    
    //------------------------------------------------------------------------------------//
    
    static void check_cyclic_dependedcy___dead_code___non_declaration() throws FatalError
    {
    	if(check_node(root)) throw new FatalError();
    	marker.clear();
    }

    static boolean check_node(Node node) throws FatalError
    {
    	node.visited = true;

    	if(!node.isDeclared && node.isDependency)  throw new FatalError();

        if(marker.contains(node.id)) return true;
    	
        marker.add(node.id);
    	
        for(Node n : node.link)
        {
            if(!n.marked)
            {
                n.marked = true;
                if(check_node(n)) return true;
            }
        }

        marker.remove(node.id);
        return false;
    }
    
    public static void read_data()
    {
        try
        { 
            String[]  dependencies  =  null;
            Node      n             =  null;
            Node      m             =  null;
            int       index         =  0;
            String    line          =  "";
            
            outer:while(true)
            {
                 line = in.readLine();

                 if(line.toCharArray()[0]=='\t')
                 {
                     node(name).lines.add(line.getBytes());
                 }
                 else
                 {
                     index				= line.indexOf(":");

                     if(index<=0) continue outer;

                     name					= hash(line.substring(0, index));
                     n				        = node(name);
                     n.isDeclared            = true;

                     if(root==null) root=n;

                     dependencies	= line.substring(index).split(" ");

                     inner:for(int i=1; i<dependencies.length; i++)
                     {
                         if(dependencies[i].isEmpty()) continue inner;
                         m = node(hash(dependencies[i]));
                         n.addEdge(m);
                         m.isDependency = true;
                     }
                     
                     n.lines.add(line.getBytes());
                     
                     sort.add(name);
                 }
             }
        }
        catch(Exception e)		{  /*ignore*/ }
    	
        try						{ in.close(); }
        catch (IOException e)	{  /*ignore*/ }
        finally					{ in = null;  }
    }

    static void flush_data() throws FatalError
    {
        StringBuilder buffer  =  new StringBuilder();
        Node n                =  null;

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

            release(a);
        }

      //  System.gc();
        System.out.print(buffer);
    }

    static void release(int key)
    { cache.remove(key).get().destroy(); }
    
    static int hash(String text)
    {
    	int hash = 0;
    	for(int i=0;i<text.length(); hash = (hash << 5) - hash + text.charAt(i++));
    	return hash;    	  
    }
    
    //------------------------------------------------------------------------------------//
    // nested helpers 
    
    static class FatalError extends RuntimeException
    { private static final long serialVersionUID = 3638938829930139263L; }

    static class Node
    {
        boolean            visited       =  false;
        boolean            marked	     =  false;
        boolean            isDependency  =  false;
        boolean            isDeclared    =  false;
        ArrayList<Node>    link          =  new ArrayList<Node>();
        ArrayList<byte[]>  lines         =  new ArrayList<byte[]>();
        int                index         =  0;
        int                id            =  0;

        public Node(int id)
        { this.id = id; }

        public void addEdge(Node b)
        { link.add(b); }

        public int hashCode()
        { return this.id; }

        public boolean equals(Object another)
        {
            Node n = (Node) another;
            return (n.id)==this.id; 
        }

        public void destroy()
        {
            this.lines.clear();
            this.link.clear();

            this.lines = null;
            this.link  = null;
			
            try                 { super.finalize(); }
            catch (Throwable e) { /*ignore*/        }
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
    
    //------------------------------------------------------------------------------------//

}