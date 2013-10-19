package pal;

public class Permutation implements Cloneable
{

    public           int[]          data  =  null                ;
    private  static  StringBuilder  sb    =  new StringBuilder() ;
	
	Permutation( int ... data )
	{
        this.data = new int [ data.length ];
        System.arraycopy( data, 0, this.data, 0, data.length );
	}

	public Permutation clone()
	{ return new Permutation(data); }
	
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