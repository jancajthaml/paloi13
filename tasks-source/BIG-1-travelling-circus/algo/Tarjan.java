package circus.algo;

import java.util.List;
import java.util.Stack;

import circus.struct.Component;
import circus.struct.Vertex;

public class Tarjan<E,T extends Vertex<E>>
{
	
	private			int[]	neighbours  = null;
	private			int		index       = 0;
	public static	int		counter     = 0;

	public Tarjan( T[] g, int[] neighbours, List<Component<E>> storage )
	{
		this . neighbours	= neighbours;
		
		Stack<Vertex<E>> s      =  new Stack<Vertex<E>>()        ;

		for( T c : g ) if( c.index==0 ) tarjan(c, storage, s);
	}

	public void tarjan( Vertex<E> v, List<Component<E>> scc, Stack<Vertex<E>> s )
	{
		v.index = v.lowlink = ++ index;

		s.push( v );

		for( Vertex<E> w : v.next() )
		{
			if( w.index==0 )
			{
				tarjan( w,scc,s );
				v.lowlink = v.lowlink<w.lowlink ? v.lowlink : w.lowlink;
			}
			else if( s.contains(w) )
			{
				v.lowlink = v.lowlink<w.index ? v.lowlink : w.index;
			}
		}
		if( v.lowlink==v.index )
		{
			Vertex<E>     back       =  null;
			int           position   =  0;
			Component<E>  component  =  new Component<E>(counter);

			do
			{
				back						= s.pop();
				back.origin					= position;
				neighbours[back.position]	= counter;

				if( back.city )	component . cities   . add( back );
				else			component . villages . add( back );

				position++;
				
			}while( back.position!=v.position );

			scc.add( component ); 
			
			counter++;
		}
	}

}