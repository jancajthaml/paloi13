package pal;

import pal.api.generator.Filter;
import pal.api.struct.GenericGraph;
import pal.api.util.Fio;

public class Main
{

	static int[]			degree		= null;
	static Fio				fio			= new Fio(System.in);
	static Filter<Integer>	decorator	= new Filter<Integer>();

	//###############################################################################//
    
	public static void main(String ... args)
	{
		try
		{
			read_data();
			GenericGraph.generateGraphsFromDegree( degree,decorator );
		}
		catch(Throwable t){ t.printStackTrace(); }
		finally
		{ System.out.println( decorator.getNumberOfGraphs() ); }
	}

	//###############################################################################//
    
	private static void read_data()
	{
		int index   	= 0;
		int val     	= 0;
		String[] line	= fio.nextLine().split(" ");
		degree			= new int[Integer.valueOf(line[0])];
		reader:while( index<degree.length )
		{
			if( ( val=Integer.valueOf(line[index+1]) )==0 )
			{
				int[] a = new int[index];
				System.arraycopy(degree, 0, a, 0, a.length);
				degree = a;
				break reader;
			}
			degree[index++]	= val;
		}
	}

}