package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class Main
{

	static BufferedReader bi		= new BufferedReader(new InputStreamReader(System.in));
	static int[] weight_matrix		= null;
	static int number_of_nodes		= 0;
	static int cardinality_of_A		= 0;
	static Vector<Integer> A		= null;
	static int constant_K			= 0;
	static Edge[] edges				= null;
	
	public static void main(String[] args) throws NumberFormatException, IOException
	{
		weight_matrix		= null;
		number_of_nodes		= 0;
		cardinality_of_A	= 0;
		A					= new Vector<Integer>();
		int counter			= 0;
		number_of_nodes		= Integer.valueOf(bi.readLine());
		weight_matrix		= new int[number_of_nodes*number_of_nodes];
		
		for(int i=0; i<number_of_nodes; i++)
		{
			String[] split = bi.readLine().split(" ");

			for(String s : split)
			{
				if(s.equalsIgnoreCase("")) continue;
				weight_matrix[counter++]=Integer.valueOf(s);
			}
		}
		
		String[] split = bi.readLine().split("  ");
		
		cardinality_of_A= Integer.valueOf(split[0]);
		
		for(String s : split[1].split(" "))
		{
			A.add(new Integer(Integer.valueOf(s)));

		}
		
		constant_K	= Integer.valueOf(bi.readLine());

		//out();
	
		create();
        System.out.println(printEdges());
		
        //FIXME speed test
	//	System.out.println("0");
    }
	


    public static void create()
    {
        edges			= new Edge[((Main.number_of_nodes) * (Main.number_of_nodes - 1)) >> 1];
        Node[] nodes	= new Node[Main.number_of_nodes];
        
        for (int i = 0; i < nodes.length; i++)
            nodes[i] = new Node( i);
        
        int counter = 0;
        
        for (int i = 0; i < Main.number_of_nodes; i++)
        for (int j = 0; j < i; j++)
        	edges[counter++] = new Edge(nodes[i], nodes[j], weight_matrix[j+(number_of_nodes*i)]);
    }
    
    public static String printEdges()
    {
    	StringBuffer sb = new StringBuffer();
    	
    	for(Edge edge : edges)
    		sb.append(edge+"\n");
    	return sb.toString();
    }
	    
	private static void out()
	{
		System.out.println(number_of_nodes);
		for(int i=0; i<number_of_nodes; i++)
		{
			for(int j=0; j<number_of_nodes; j++)	
				System.out.print(weight_matrix[j+(number_of_nodes*i)]+" ");
			System.out.println();
		}
		
		System.out.print(cardinality_of_A+"  ");
		for(int a : A)
			System.out.print(a+" ");
		
		System.out.println("\n"+constant_K);
	}
	
	static class Node
	{
	    int id			= 0;
	    boolean visited	= false;

	    public Node(int id)
	    { this.id = id; }

	    public String toString()
	    { return (this.id+1)+""; }
	}
	
	static class Edge
	{
	    Node node1;
	    Node node2;
	    int weight;
	    boolean used = false;

	    Edge(Node node1, Node node2, int weight)
	    {
	        this.node1	= node1;
	        this.node2	= node2;
	        this.weight	= weight;
	    }

	    public void visit()
	    {
	        node1.visited=true;
	        node2.visited=true;
	    }

	    public boolean areNodesVisited()
	    { return (node1.visited & node2.visited); }

	    public void setUsed()
	    {
	        used = true;
	        visit();
	    }

	    public String toString()
	    { return this.node1+"---"+this.node2+" | w:"+this.weight; }

	}

}
