package pal;

import java.util.ArrayList;

public class Main
{

	static int M                                =  0;
	static int N                                =  0;
	static Fio in                               =  null;
	static ArrayList<Permutation> permutations  =  new ArrayList<Permutation>();
	static Permutation  LOW                     =  null;
	static Permutation  CLONE                   =  null;
	static Permutation  HI                      =  null;
	static int          best_eval               =  0;
	static Permutation  best                    =  null;
	
	//###############################################################################//
    
	public static void main( String ... args )
	{
        try
        {
            read_data             (          ) ;
            generate_permutations ( CLONE, 0 ) ;
            find_best_candidate   (          ) ;
            print_output          (          ) ;
		}
        catch(Throwable t) { /*ignore*/  }
		finally            { in.close(); }
	}
	
	//###############################################################################//
    
	static void print_output()
	{
		System.out.println ( best_eval );
        System.out.println (   best    );
	}
	
	static void find_best_candidate()
	{
		for( Permutation i : permutations )
		{
			BinomialHeap heap  =  new BinomialHeap ( i ) ;
			int diff           =  heap.diff        (   ) ;
			
			if( diff>best_eval )
			{
				best_eval  =  diff;
				best       =  i;
			}
		}

		best . clip ( M );
	}

	static void read_data() throws Throwable
	{
		in     =  new Fio         (   System.in   );
		N      =  in.nextInt      (               );
		M      =  in.nextInt      (               );
		LOW    =  new Permutation ( new int [ M ] );
		CLONE  =  new Permutation ( new int [ M ] );
		HI     =  new Permutation ( new int [ M ] );
	
		for( int i=0; i<M; i++ ) CLONE . data [ i ] =
                                 LOW   . data [ i ] = in.nextInt();
		for( int i=0; i<M; i++ ) HI    . data [ i ] = in.nextInt();
	}
	
	static void generate_permutations( Permutation input, int start )
	{
		if( input.compareTo( LOW ) == -1 ) return;
		if( input.compareTo( HI  ) ==  1 ) return;

		int size = input.data.length;
		
		if( size==start+1 )
		{
			int[]  n  =  new int [ N ]       ;			
			int    i  =  input.data.length-1 ;
			
			System.arraycopy( input.data , 0 , n , 0 , i+1 );

			while( i++ < n.length-1 )  n[i] = M * (i/M) + input.data [ i%M ];

			permutations.add( new Permutation( n ) );
		}
		else for( int i=start; i < size; i++ )
		{	
			 input . swap           ( i   , start     );
			 generate_permutations  ( input , start+1 );
			 input . swap           ( i   , start     );
		 }
	 }

}