package task2;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.StringTokenizer;

public class Main
{

    //------------------------------------------------------------------------------------//

    private static  HashMap<Integer, SoftReference<Node>> cache     =  new HashMap<Integer, SoftReference<Node>>();
    private static  BufferedReader                        in        =  new BufferedReader(new InputStreamReader(System.in));
    private static  ByteArrayOutputStream                 out       =  new ByteArrayOutputStream();
    private static  ArrayList<Integer>                    sort      =  new ArrayList<Integer>();
    private static  byte[]                                newline   =  System.getProperty("line.separator").getBytes();
    private static  Node                                  root      =  null;
    private static  int                                   name      =  0;

    //------------------------------------------------------------------------------------//
    
    public static void main(String ... args)
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
    	try                         { check_node(root); }
    	catch(Exception e)          { check_node(last); }
    	catch(StackOverflowError e) { check_node(last); }
    	catch(Throwable e)          { check_node(last); }
    }

    static Node last = null;
    
    static void check_node(Node node) throws FatalError //non-recursive approach is slower
    {
    	node . visited = true;

    	if( (!node.isDeclared && node.isDependency) || node.state==Node.GRAY )  throw new FatalError();
    	
    	node.state     = Node.GRAY;
    	
    	last           = node;
        for(Node n : node.link)
        {
            if(n.state==Node.BLACK) continue;
            check_node(n);
        }
        
        
        
        node.state=Node.BLACK;
    }
    
    public static void read_data()
    {
        try
        {
            Node      n             =  null;
            Node      m             =  null;
            String    line          =  "";
            
            outer:while(true)
            {
                 line = in.readLine();

                 if(line.getBytes()[0] == 9)
                 { node(name).lines.add(line.getBytes()); }
                 else
                 {
                     StringTokenizer st = new StringTokenizer(line," ");

                     if(!st.hasMoreTokens()) continue outer;

                     String token         = st.nextToken();
                     name                 = hash(token.substring(0, token.length()-1));
                     n                    = node(name);
                     n.isDeclared         = true;

                     if(root==null) root  = n;

                     while (st.hasMoreTokens())
                     {
                         m               = node(hash(st.nextToken()));
                         m.isDependency  = true;
                         n.addEdge(m);
                     }

                     n.lines . add(line.getBytes());
                     sort    . add(name);
                     
                     st = null;
                 }
             }
        }
        catch(Exception e)      {  /*ignore*/ }
    	
        try                     { in.close(); }
        catch (IOException e)   {  /*ignore*/ }
        finally                 { in = null;  }
    }

    static void flush_data() throws FatalError
    {
    	try
    	{
            Node n  =  null;

            for(int index : sort)
            {
                n = node(index);

                if(n.visited)  for( byte[] line : n.lines )
                {
                    out . write(line);
                    out . write(newline);
                }
                else           for( byte[] line : n.lines )
                {
                    out . write(0x23);
                    out . write(line);
                    out . write(newline);
                }

                release(index);
            }

            System.gc();
            System.out.print(out);
    	}
    	catch(IOException e){ /*ignore*/ }
    }

    static void release(int key)
    { cache.remove(key).get().destroy(); }
    
    static int hash(String text)
    {
    	int hash = 0;
    	for( int i=0 ; i < text.length() ; hash = (hash << 5) - hash + text.charAt(i++) );
    	return hash;    	  
    }
    
    //------------------------------------------------------------------------------------//
    // nested helpers 
    
    static class FatalError extends RuntimeException
    { private static final long serialVersionUID = 3638938829930139263L; }

    static class Node
    {
        public  static  int  WHITE         = 0x01;
        public  static  int  GRAY          = 0x02;
        public  static  int  BLACK         = 0x03;

    	int                  state         =  WHITE;
        boolean              visited       =  false;
        boolean              marked        =  false;
        boolean              isDependency  =  false;
        boolean              isDeclared    =  false;
        ArrayList<Node>      link          =  new ArrayList<Node>();
        ArrayList<byte[]>    lines         =  new ArrayList<byte[]>();
        int                  index         =  0;
        int                  id            =  0;

        public Node(int id)
        { this.id = id; }

        public void addEdge(Node b)
        { link.add(b); }

        public int hashCode()
        { return this.id; }

        public boolean equals(Object another)
        { return (((Node) another).id)==this.id; }
        
        public void destroy()
        {
            this.lines . clear();
            this.link  . clear();

            this.lines = null;
            this.link  = null;
			
            try                 { super.finalize(); }
            catch (Throwable e) { /*ignore*/        }
        }
    }
    
    public static Node node(int key)
    {
    	Node node = null;
    	
    	SoftReference<Node> ref   =  cache . get(key);
    	if(ref != null)     node  =  ref   . get();
    	
    	if(node == null)             cache . put( key, new SoftReference<Node>(node = new Node(key)) );
    	
    	return node;   
    }
    
    //------------------------------------------------------------------------------------//

}