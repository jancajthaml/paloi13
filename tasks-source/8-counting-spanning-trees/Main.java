package pal;

public class Main
{
	static  Fio    io  =  new Fio(System.in);
	static  Graph  g   =  new Graph();
	
	public static void main( String ... args )
	{
		int number_of_vertices	= io.nextInt();
		int from				= 0;
		int to					= 0;
		
		Graph.Vertex[] v = new Graph.Vertex[number_of_vertices];
		
		for( int i=0 ; i<number_of_vertices ; i++ )
			g.addVertex( v[i] = new Graph.Vertex(i) );
		
		reader: while( true )
		{
			from	= io.nextInt();
			to		= io.nextInt();
			
			if( from==0 && to==0 ) break reader;
			
			g.addEdge( v[from] , v[to] );
		}
		
		int result = g.getNumberOfSpanningTrees();
		
		System.out.println( result );

		io.close();
	}

}