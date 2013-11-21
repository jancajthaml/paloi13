package circus;

import java.util.ArrayList;
import java.util.List;

import circus.algo.Tarjan;
import circus.struct.Component;
import circus.struct.Condensation;
import circus.struct.Vertex;
import circus.util.Fio;
public class Main
{

	private static int						M			= 0;
	private static int						N			= 0;

	private static Vertex<Integer>[]		verticies	= null;
	private static int[]					relations	= null;
	private static List<Component<Integer>>	component	= null;
	private static int						max			= 0;
	private static Fio						fio			= new Fio(System.in);
	
	//###############################################################################//
    
	public static void main(String ... args)
	{
		
		read_data             () ;
		calculate_best_profit () ;
		print_result          () ;
	}
	
	//###############################################################################//
    
	private static void calculate_best_profit()
	{
		int i=-1;
		while( ++i<M ) verticies[fio.nextInt()-1].addEdge(verticies[fio.nextInt()-1]);

		new Tarjan<Integer,Vertex<Integer>>( verticies,relations,component=new ArrayList<Component<Integer>>() );
		
		for( Vertex<Integer> city : verticies                  )
		for( Vertex<Integer> next : city.getEdges()            )
		if( relations[next.position]!=relations[city.position] )
		{
			component . get( relations[next.position] ) . unique_from . add( next );
			component . get( relations[city.position] ) . unique_to   . add( city );
		}
	    
	    int maximum	= Integer.MAX_VALUE;
	    i					= component.size();
	    while( --i >= 0 )
	    {
	    	maximum = new Condensation<Integer>( component.get(i),relations ).condenzation();
			if( max<maximum ) max = maximum;
		}	
	}
	
	private static void print_result()
	{ System.out.println(max); }
	
	@SuppressWarnings("unchecked")
	private static void read_data()
	{
		N					= fio.nextInt();
		M					= fio.nextInt();
		fio.nextLine();
		
		relations			= new int[N];
		verticies			= new Vertex[N];

		int i=-1;
		while( ++i<N )
		{
			String[] line = fio.nextLine().split(" ");

			verticies[i]=
			(
				( line.length>1 )
				?
				new Vertex<Integer>(i, Integer.valueOf(line[0]), Integer.valueOf(line[1]))
				:
				new Vertex<Integer>(i, Integer.valueOf(line[0]))
			);
		}
	}

}