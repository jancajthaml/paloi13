package circus.struct;

import java.util.PriorityQueue;


public class Condensation<T>
{

	private int				id					= 0;
	private int				BEST				= 0;
	private Component<T>	component			= null;
	private int[]			neighbours			= null;
	private int[][]			bridge				= null;
	private int[][]			to_city				= null;
	private int[][]			from_city			= null;
	private int[][]			city_city			= null;
	private int[]			cost_to				= null;
	private int[]			cost_from			= null;
	

	public Condensation( Component<T> component, int[] neighbours )
	{
		this.component	= component                                                    ;
		this.neighbours	= neighbours                                                   ;
		
		component.shrink();
		
		int size_of_from			= component.getNumberOfOrigins()                   ;
		int size_of_to				= component.getNumberOfDestinations()              ;
		int number_of_cities		= component.getNumberOfCities()                    ;
		this.to_city				= new int[   size_of_from   ] [ number_of_cities ] ;
		this.bridge					= new int[   size_of_from   ] [    size_of_to    ] ;
		this.city_city				= new int[ number_of_cities ] [ number_of_cities ] ;
		this.from_city				= new int[ number_of_cities ] [    size_of_to    ] ;
		this.cost_to				= new int[   size_of_from   ]                      ;
		this.cost_from				= new int[    size_of_to    ]                      ;
		this.id						= component.hashCode()                             ;
	}

	public int condenzation()
	{	
		Vertex<T> to	= null;
		Vertex<T> from	= null;
		Vertex<T> in	= null;
		Vertex<T> out	= null;
		int i			= -1;
		int j			= -1;
		
		component.unlinkUnproffitableDestinations();
		
		while( ++i<component.getNumberOfOrigins() )
		{
			edge( in = component.getOrigin(i) );
			
			j=-1;
			while( ++j<component.getNumberOfCities() )
			{
				to = component.getCity(j);
				to_city[i][j] = to.cost - to.real_cost - in.income;
			}
			j=-1;
			while( ++j<component.getNumberOfDestinations() )
			{
				out = component.getDestination(j);
				bridge[i][j] = -out.cost + in.income;
			}
			
			for( Vertex<T> city    : component.cities   )  city    . cost  =  Integer.MAX_VALUE ;	
			for( Vertex<T> village : component.villages )  village . cost  =  Integer.MAX_VALUE ;
		}
		
		i=-1;
		while( ++i<component.getNumberOfCities() )
		{
			edge( to=component.getCity(i) );
			
			j=-1;
			while( ++j<component.getNumberOfCities() )
			{
				from = component.getCity(j);
				city_city[i][j] = ( i==j ) ? from.real_profit - from.real_cost : from.real_profit + to.real_profit - from.cost;
			}

			j=-1;
			while( ++j<component.getNumberOfDestinations() )
			{
				from			= component.getDestination(j);
				from_city[i][j]	= from.cost - to.cost;
			}
			
			for( Vertex<T> city    : component.cities   )  city    . cost  =  Integer.MAX_VALUE ;	
			for( Vertex<T> village : component.villages )  village . cost  =  Integer.MAX_VALUE ;
		}

		i = -1;
		j = -1;
		int k = -1;
		int l = -1;
		int a = 0;
		
		while( ++i < to_city.length )
		{
			j=-1;
			while( ++j<to_city[i].length )
			{
				k=-1;
				while( ++k<city_city[j].length )
				{
					a = city_city[j][k] - to_city[i][j];
					if( cost_to[i]<a ) cost_to[i] = a;
				}
			}
		}

		if( from_city.length!=0 )
		{
			i=-1;
			while( ++i<city_city.length )
			{
				j=-1;
				while( ++j<city_city[i].length )
				{
					k=-1;
					while( ++k < from_city[j].length )
					{
						a = city_city[i][j] - from_city[j][k];
						if( cost_from[k]<a  ) cost_from[k] = a;
					}
				}
			}
		}

		if( component.getNumberOfOrigins()!=0 && component.getNumberOfDestinations()!=0 && component.getNumberOfCities()!=0 )
		{
			i=-1;
			while( ++i<to_city.length )
			{
				j=-1;
				while( ++j<to_city[i].length )
				{
					k=-1;
					while( ++k<city_city[j].length )
					{
						l=-1;
						while( ++l<from_city[k].length )
						{
							a = city_city[j][k] - to_city[i][j] - from_city[k][l];
							if( cost_from[l]<a ) cost_from[l] = a;
						}
					}
				}
			}
		}
		
		i=-1;
		while( ++i<city_city.length )
		{
			j=-1;
			while( ++j<city_city[i].length ) if( BEST<city_city[i][j] ) BEST = city_city[i][j];
		}
		
		i=-1;
		while( ++i<cost_to.length ) if( cost_to[i]>BEST ) BEST = cost_to[i];
		
		i=-1;
		while( ++i<component.getNumberOfDestinations() ) if( cost_from[i]>-1 )
		{
			out = component.getDestination(i);
			for( Vertex<T> next : out.next() ) if( next.income<cost_from[i] ) next.income = cost_from[i];
		}
		
		return BEST;
	}

	public void edge( Vertex<T> from )
	{
		int a						= 0;
		from.cost					= from.real_cost;
		Vertex<T> current			= null;
		PriorityQueue<Vertex<T>> qu	= new PriorityQueue<Vertex<T>>(10);
		
		qu.add( from );

		while( !qu.isEmpty() )
		{
			current		= qu.poll();
			int entry	= current.cost;

			for( Vertex<T> next : current.next() )
			if( neighbours[next.position]==id )
			{
				a = entry + next.real_cost;
				if( next.cost>a )
				{
					next.cost = a;
					qu.add(next);
				}
			}
		}
	}


}
