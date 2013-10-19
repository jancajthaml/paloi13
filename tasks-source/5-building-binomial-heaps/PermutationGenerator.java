package pal;

import java.util.ConcurrentModificationException;
import java.util.Iterator;


public class PermutationGenerator implements Iterator<Permutation>, Iterable<Permutation>
{

	private  final  Permutation  helper;
	private  final  Permutation  it;
    private  int[]    high   =  null;
    private  boolean  ready  =  false;
    
    public PermutationGenerator( int[] LOW, int[] HIGH, int N )
    {
        this . high    =  HIGH . clone()                 ;
        this . helper  =  new Permutation ( LOW )        ;
        this . it      =  new Permutation ( new int[N] ) ;
        this . ready   =  false                          ;
    }

	@Override public Iterator<Permutation> iterator()
	{ return this; }

	@Override public boolean hasNext()
	{
		boolean end  =  ready;
        boolean h    =  true;
        
        for( int i = 1 ; i < helper.data.length ; i++ )
        {
            end = end && helper.data[i] < helper.data[i-1];
            if( helper.data[i] != high[i] ) h = false;
        }

        return !h && !end;
	}

	@Override public Permutation next()
	{
        if( !ready )
        {
            ready = true;
            return expand();
        }

        int j = helper.data.length - 2;
        int k = helper.data.length - 1;

        for( ; helper.data[ j ] > helper.data[ j+1 ] ; j-- ) ;
        for( ; helper.data[ j ] > helper.data[ k   ] ; k-- ) ;

        
        helper.swap(k, j);
        
        int r = helper.data.length - 1;
        int s = j + 1;
	 
        while( r > s )
        {
        	helper.swap(r, s);
             
             s++;
             r--;
        }
            
        return expand();
	}
	
	private Permutation expand( )
	{
        int    M  =  helper.data.length  ;
        int    i  =  M-1                 ;

        System.arraycopy( helper.data , 0 , it.data , 0 , i+1 );
        
        while( i++ < it.data.length-1 )  it.data[i]  =  M * (i/M) + helper.data [ i%M ];

        return it;
	}

	@Override public void remove()
	{ throw new ConcurrentModificationException(); }
 
}