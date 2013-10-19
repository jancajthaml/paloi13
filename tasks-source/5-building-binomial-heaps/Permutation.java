package pal;

public class Permutation implements Comparable<Permutation>
{
    public           int[]          data  =  null                ;
    private  static  StringBuilder  sb    =  new StringBuilder() ;
	
	Permutation( int ... data )
	{
        this.data = new int [ data.length ];
        System.arraycopy( data, 0, this.data, 0, data.length );
	}
	
	@Override public int compareTo( Permutation e )
	{
        for( int i=0; i<e.data.length; i++ )
        {
            if( e.data[i] == data[i] )  continue   ;
			
            if(  e.data[i]<data[i]   )  return   1 ;
            if(  e.data[i]>data[i]   )  return  -1 ;
        }

        return 0;
	}

	public void swap( int i, int j )
	{
        int temp        =  data [ i ] ;
            data [ i ]  =  data [ j ] ;
            data [ j ]  =  temp       ;		
	}

	public String toString()
	{
        sb.setLength(0);

        for( int i = 0; i<data.length; i++ )
           sb.append(i==data.length-1?data[i]:data[i]+" ");

        return sb.toString();
	}

	public void clip( int n )
	{
        int[] clip  =  new int [ n ];

        System.arraycopy( data , 0 , clip , 0 , n );

        this.data   =  clip;
	}

}