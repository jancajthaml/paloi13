package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main
{

    static Queue           path     = new Queue();
    static int             size     = 0;
    static Graph           graph    = null;
    static BufferedReader  in       = new BufferedReader( new InputStreamReader(System.in) );
    static int             first    = -1;
    
    //###############################################################################//
    
    public static void main( String ... args ) throws NumberFormatException, IOException
    {	
        try
        {
            read_data	                                   ();
            find_start_vertex__and__check_euler_existence  ();
            walk_graph                                     ();
            print_path                                     ();
        }
        catch(FatalError r) { System.out.println("-1"); }
        catch(Throwable r)  { r.printStackTrace();}
        finally             { }
    }
    
    //###############################################################################//
    
    static void read_data() throws FatalError
    {
        try                             { size=Integer.valueOf(in.readLine()); }
        catch (NumberFormatException e) { throw new FatalError();              }
        catch (IOException e)           { throw new FatalError();              }

        graph     = new Graph(size);
        int from  = -1;
        int to    = -1;
        
        Reader:while(true)
        {
            StringTokenizer st  =  null;
            try                             { st = new StringTokenizer(in.readLine()," ");  }
            catch (NumberFormatException e) { throw new FatalError();                       }
            catch (IOException e)           { throw new FatalError();                       }
            
            from  =  Integer.valueOf(st.nextToken());
            to    =  Integer.valueOf(st.nextToken());
            
            if( from==0 && to==0 ) break Reader;

            if(first==-1) first=from;
            
            graph . addEdge( from, to );
        }
    }

    static void walk_graph() throws FatalError
    {
    	if(first==-1) return;
    	
    	Queue[] copy      =  new Queue[size];  // copy of graph data
    	Stack   stack     =  new Stack();      // cycle stack
    	Stack   circuit   =  new Stack();      // walked path
    	int     location  = first;
    	
        for (int v = 0; v < size; v++)
        {
        	copy[ v ] = new Queue();
            for( int w : graph.getVertexNeighbours( v ) )
                copy[ v ].enqueue( w );
        }
        
    	stack.push( location );
    	
    	while( !copy[ location ].isEmpty() )
    	{
    		stack.push( location = copy[ location ].dequeue() );	
    		while( copy[location].isEmpty() && !stack.isEmpty() )
    			circuit.push( location = stack.pop() );
    	}
    	
    	while( !circuit.isEmpty() ) path . enqueue( circuit.pop() );
    }

    static void find_start_vertex__and__check_euler_existence() throws FatalError
    {
        ArrayList<Integer> start	= new ArrayList<Integer>();
        ArrayList<Integer> end		= new ArrayList<Integer>();

        for(int i=0; i<size; i++)
        switch( graph.getDegreeIn(i)-graph.getDegreeOut(i) )
        {
            case  0  :                           break;
            case -1  :  start . add( i )       ; break;  //Starting vertex
            case  1  :  end   . add( i )       ; break;  //Ending vertex
            default  :  throw new FatalError() ;
        }

        if( start.size()>1 || end.size()>1 ) throw new FatalError();

        if(!start.isEmpty()) first = start.get(0);
    }

    static void print_path()
    {
        StringBuffer buffer  =  new StringBuffer();
        int last             =  -1;

        buffer . append ( size );  // number_of_vertices
        buffer . append ( '\n' );  // ENDL
        
        for( int v : path )
		{
            if(last==-1)
            {
                last = v;
                continue;
            }
            else
            {
                buffer . append ( last );  // from
                buffer . append ( ' '  );  // SPACE
                buffer . append ( v    );  // to
                buffer . append ( '\n' );  // ENDL

                last = v;
            }
        }

        buffer.append( 0   );
        buffer.append( ' ' );
        buffer.append( 0   );
        
        System.out.println(buffer);
    }
    
    //------------------------------------------------------------------------------------//
    // nested helpers 

    static class FatalError extends RuntimeException
    { private static final long serialVersionUID = 3638938829930139263L; }
    
}