package pal;

import java.util.PriorityQueue;

public class Main
{
	static Fio fio				= new Fio(System.in);
	
	static Node states[]		= null;

	static Sequence shortest	= new Sequence();
	static Sequence longest		= new Sequence();

	static int		N			= 0;
	static int		M			= 0;
	static boolean	Q			= false;
	
	static boolean	FINITE		= true;
	
	static Node FIRST			= null;
	
    //###############################################################################//
    
	public static void main(String ... args)
	{
		read_data      () ;
		solve_automata () ;
		print_result   () ;
	}

    //###############################################################################//
    
	static void read_data()
	{
		shortest . length	= Integer.MAX_VALUE;
		longest  . length	= Integer.MIN_VALUE;
		
		N					= fio.nextInt();
		M					= fio.nextInt();
		
		states				= new Node[N];
		
		FIRST				= states[0] = new Node(0);
		
		for( int i=1; i<N ; i++ )
			states[i] = new Node(i);
		
		Q				= fio.nextInt()==1;
		
		fio.nextLine();

		if( Q )	read_automata_defined   () ;
		else	read_automata_generated () ;
				read_terminals          () ;	
	}

	static void read_automata_defined()
	{
		for( int i=0 ; i<N ; i++ )
		{
			int sj = fio.nextInt();
			
			for( int j=0 ; j<M; j++ )
			{
				int h = fio.nextInt();
				for( int k=0 ; k<h ; k++ )
					states[fio.nextInt()] . addEdge( j,sj );
			}
			fio.nextLine();
		}
	}
	
	static void read_automata_generated()
	{
		int B		= fio.nextInt();
		int C		= fio.nextInt();
		int T		= fio.nextInt();
		int U		= fio.nextInt();
		int V		= fio.nextInt();
		int W		= fio.nextInt();
		int floor	= N/B;
		for( int sj = 0 ; sj<N                      ; sj++ )
		for( int h  = 0 ; h<M                       ; h++  )
		for( int k  = 1 ; k<=T + ((U*sj + V*h) % W) ; k++  )
		{
			if( sj<floor )	states[1 + sj + ((B*sj*k + C*h) % (N - floor - 1))] . addEdge( h,sj );
			else			states[floor  + ((B*sj*k + C*h) % (N - floor))]     . addEdge( h,sj );
		}
	}

	static void read_terminals()
	{
		int n	= fio.nextInt();
		int i	= -1;
		
		while( ++i<n ) states[fio.nextInt()].isTerminal = true;		
	}
	
	static void solve_automata()
	{	
		for( Node state : states )
		{
			if( !state.isTerminal ) continue;
			
			for( Node k : states )	k.visited=false;

			solve_automata_to(state);
			
			if( FIRST.hasCycle ) FINITE = false;
			
			//post-solve for FINITE AUTOMATA
			if( FINITE )
			{
				if( FIRST.max.data==null ) continue;
				if( longest.length<FIRST.max.length )
				{
					longest.length	= FIRST.max.length;
					longest.data	= FIRST.max.data;
				}
				else if( longest.length==FIRST.max.length && longest.data!=null && FIRST.max.data.compareTo(longest.data)>0 )
				{
					longest.data	= FIRST.max.data;
				}
			}
			
			//post-solve for INFINITE AUTOMATA
			else
			{
				if( FIRST.min.data == null ) continue;
				if( shortest.length>FIRST.min.length )
				{
					shortest.length	= FIRST.min.length;
					shortest.data	= FIRST.min.data;
				}
				else if( shortest.length==FIRST.min.length && shortest.data!=null && FIRST.min.data.compareTo(shortest.data)<0 )
				{
					shortest.data	= FIRST.min.data;
				}	
			}
		}
	}

	static void solve_automata_to( Node state )
	{
		PriorityQueue<Node> queue = new PriorityQueue<Node>( N );
		
		queue.add(state);
		state.clear();

		state.visited	= false;
		Node current	= null;
		String min		= null;
		String max		= null;
		boolean found	= false;
		
		while( !queue.isEmpty() )
		{
			current = queue.poll();
			
			if( !FINITE && current==FIRST ) return;
			
			current.visited  = true;
			current.iterator = 0;

			for( int i=0; i<M; i++ )
			{
				char c	= (char)( 'a'+i );
				min		= c + current.min.data;
				max		= c + current.max.data;
				
				for( Integer j : current )
				{
					state	= states[j];
					found	= false;

					if( FINITE )
					{
						if( current.hasCycle )
						{
							if( !state.hasCycle )
							{
								state.hasCycle	= true;
								found			= true;
							}
						}
						else if(state.max.length<=current.max.length+1)
						{
							if(state.max.length==current.max.length+1)
							{
								if( max.compareTo(state.max.data)>0 )
								{	
									found				= true;
									state.max.length	= current.max.length+1;
									state.max.data		= max;
									state.predecessor	= current.index;
								}
							}
							else
							{
								state.predecessor	= current.index;
								found				= true;
								
								if( state.visited )
								{
									Node v			= states[state.predecessor];
									int curLength	= current.max.length;
										
									while( state!=v && curLength!=state.max.length )
									{
										v = states[v.predecessor];
										curLength--;
									}
										
									if( state==v )
									{
										state.hasCycle		= true;
										current.hasCycle	= true;
										queue.add(current);
									}
								}

								state.max.length	= current.max.length+1;
								state.max.data		= max;
							}
						}
					}

					if( state.visited )
					{
						if( found ) queue.add( state );						
						continue;
					}
					
					if( state.min.length>=current.min.length+1 )
					{
						if( state.min.length==current.min.length+1 )
						{
							if( min.compareTo(state.min.data)<0 )
							{
								state.min.length	= current.min.length+1;
								state.min.data		= min;
								found				= true;
							}
						}
						else
						{
							state.min.length	= current.min.length+1;
							state.min.data		= min;
							found				= true;
						}						
					}
					
					if( found ) queue.add( state );
				}
			}
		}
	}

	static void print_result()
	{
		if( FINITE )	System.out.println("FINITE " 	+ (longest.length >0 ? longest.data  : "EPSILON"));
		else			System.out.println("INFINITE "	+ (shortest.length>0 ? shortest.data : "EPSILON"));
	}
	
}
