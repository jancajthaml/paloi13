package circus.struct;

import java.util.ArrayList;
import java.util.HashSet;


public class Component<T>
{
	private		int                   id           ;
	private		ArrayList<Vertex<T>>  to           ;
	private		ArrayList<Vertex<T>>  from         ;
	public		ArrayList<Vertex<T>>  cities       ;
	public		HashSet<Vertex<T>>    unique_to    ;
	public		HashSet<Vertex<T>>    unique_from  ;
	public		ArrayList<Vertex<T>>  villages     ;
	
	public Component( int id )
	{
		this . id           =  id                         ;
		this . unique_to    =  new HashSet<Vertex<T>>()   ;
		this . unique_from  =  new HashSet<Vertex<T>>()   ;
		this . cities       =  new ArrayList<Vertex<T>>() ;
		this . villages     =  new ArrayList<Vertex<T>>() ;
		this . to           =  new ArrayList<Vertex<T>>() ;
		this . from         =  new ArrayList<Vertex<T>>() ;	
	}
	
	public int hashCode()
	{ return this.id; }
	
	public Vertex<T> getDestination( int v )
	{ return this.to.get(v); }
	
	public int getNumberOfDestinations()
	{ return this.to.size(); }

	public Vertex<T> getOrigin( int v )
	{ return this.from.get(v); }
	
	public int getNumberOfOrigins()
	{ return this.from.size(); }

	public int getNumberOfCities()
	{ return cities.size(); }

	public Vertex<T> getCity( int v )
	{ return cities.get(v); }

	public int getNumberOfBVillages()
	{ return villages.size(); }

	public Vertex<T> getVillage( int v )
	{ return villages.get(v); }

	public void shrink()
	{
		this . from . addAll( unique_from ) ;
		this . to   . addAll( unique_to   ) ;
		
		this . unique_from	= null			;
		this . unique_to	= null			;
	}
	
	public void unlinkUnproffitableDestinations()
	{
		HashSet<Vertex<T>> delete = new HashSet<Vertex<T>>();
		
		for( Vertex<T> in : from ) if( in.income<=0 ) delete.add(in);
		
		from.removeAll(delete);
	}

}