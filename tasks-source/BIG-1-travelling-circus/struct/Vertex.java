package circus.struct;

import java.util.ArrayList;

public class Vertex<T> implements Comparable<Vertex<T>>
{

	public   int                   index        =  0;
	public   int                   lowlink      =  0;
	public   int                   position     =  0;
	public   boolean               visited      =  false;
	public   int                   real_cost    =  0;
	public   int                   real_profit  =  0;
	public   int                   cost         =  Integer.MAX_VALUE;
	public   int                   income       =  -1;
	public   int                   origin       =  0;
	private  ArrayList<Vertex<T>>  next         =  null;
	public   boolean               city         =  false;
	
	public Vertex( int pos, int real_cost )
	{
		this.position  = pos                         ;
		this.real_cost    = real_cost                      ;
		this.next      = new ArrayList<Vertex<T>>()  ;
	}

	public Vertex( int pos, int real_cost, int real_profit )
	{
		this.position     =  pos                        ;
		this.real_cost    =  real_cost                  ;
		this.real_profit  =  real_profit                ;
		this.city         =  true                       ;
		this.next         =  new ArrayList<Vertex<T>>() ;
	}

	public void addEdge( Vertex<T> v )
	{ this.next.add( v ); }

	public ArrayList<Vertex<T>> getEdges()
	{ return this.next; }

	public int getPosition()
	{ return this.position; }

	public ArrayList<Vertex<T>> next()
	{ return this.next; }

	@Override public int hashCode()
	{ return this.position; }

	@SuppressWarnings("unchecked")
	@Override public boolean equals(Object obj)
	{
		if( obj==this )					return true;
		if( obj==null )					return false;
		if(!( obj instanceof Vertex ))	return false;

		return ((Vertex<T>) obj).position==this.position;
	}

	@Override public int compareTo(Vertex<T> other)
	{ return ( cost<=other.cost ) ? -1 : 1; }
	
}
