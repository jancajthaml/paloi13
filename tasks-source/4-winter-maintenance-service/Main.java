package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main
{

    static Queue           path     = new Queue();
    static int             size     = 0;
    static Graph           graph    = null;
    static BufferedReader  in       = new BufferedReader(new InputStreamReader(System.in));
    
    //###############################################################################//
    
    public static void main (String ... args) throws NumberFormatException, IOException
    {	
        try
        {
            read_data	();
            walk_graph  ();
            print_path  ();
        }
        catch(FatalError r) { }
        catch(Throwable r)  { }
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

            graph . addEdge( from, to );
        }
    }

    static void walk_graph()
    {
		Queue[] copy     =  new Queue[size];
		Stack   stack    =  new Stack();
    	Stack   circuit  =  new Stack();

        for (int v = 0; v < size; v++)
        {
        	copy[ v ] = new Queue();
            for( int w : graph.getVertexNeighbours( v ) )
                copy[ v ].enqueue( w );
        }
        
    	int location = 0;
    	
    	stack.push( location );
    	
    	while( !copy[ location ].isEmpty() )
    	{
    		stack.push( location = copy[ location ].dequeue() );	
    		while( copy[location].isEmpty() && !stack.isEmpty() )
    			circuit.push( location = stack.pop() );
    	}
    	
    	while( !circuit.isEmpty() ) path . enqueue( circuit.pop() );
    }

    static void print_path()
    {
        StringBuffer buffer  =  new StringBuffer();
        int last             =  -1;

        buffer . append ( size );
        buffer . append ( '\n' );
        
        for( int v : path )
		{
            if(last==-1)
            {
                last = v;
                continue;
            }
			
            else
            {
                buffer . append ( last );
                buffer . append ( ' '  );
                buffer . append ( v    );
                buffer . append ( '\n' );

                last = v;
            }
        }

        System.out.println(buffer);
    }

    static class FatalError extends RuntimeException
    { private static final long serialVersionUID = 3638938829930139263L; }
    
}