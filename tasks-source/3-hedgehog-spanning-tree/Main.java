
package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

public final class Main
{  

    static HashMap<Integer,ArrayList<Edge>>  list    = new HashMap<Integer,ArrayList<Edge>>();
    static BufferedReader                    in      = new BufferedReader(new InputStreamReader(System.in));
    static int                               size    = 0;
    static int[]                             A       = null;
    static int                               K       = 0;
    static int                               result  = 0;

    //###############################################################################//
    
    public static void main (String[] args) throws NumberFormatException, IOException
    {	
        try
        {
            read_data	();
            find_mst	();
        }
        catch(FatalError r) { result = -1;                    }
        catch(Throwable r)  { result = -1;                    }
        finally             { System.out.println($(result));  }
    }

    //###############################################################################//
    
    static void read_data() throws FatalError
    {
        int w  = 0;
        
        try                             { size	= Integer.valueOf(in.readLine());  }
        catch (NumberFormatException e) { throw new FatalError();                  }
        catch (IOException e)           { throw new FatalError();                  }

        for (int i = 0; i < size; i++)
		{
        	list.put(i, new ArrayList<Edge>());
            StringTokenizer st  =  null;
            try                             { st = new StringTokenizer(in.readLine()," ");  }
            catch (NumberFormatException e) { throw new FatalError();                       }
            catch (IOException e)           { throw new FatalError();                       }

            for (int j = 0; j < i; j++)
            {
            	w = Integer.valueOf(st.nextToken());
                if (w == 0) continue;
                
                symetric_put(i, j, w);
            }
        }

        try
        {
            StringTokenizer st	= new StringTokenizer(in.readLine());
            A					= new int[Integer.valueOf(st.nextToken())];

            for (int i = 0; i < A.length; i++)
                A[i] = Integer.valueOf(st.nextToken())-1;
            
            K = Integer.valueOf(in.readLine());        
        }
        catch (NumberFormatException e) { throw new FatalError();                                   }
        catch (IOException e)           { throw new FatalError();                                   }
        finally                         { if(DataStorage.edges.size()==0) throw new FatalError();   }
		
        for (ArrayList<Edge> element : list.values())
            Collections.sort(element);
    }

    public static void find_mst() throws FatalError
    {
        int current  =  0;
        int min      =  Integer.MAX_VALUE;
        int minimum  =  0;
		
        Collections.sort(DataStorage.edges);

        for (int i = 0; i < DataStorage.edges.size(); i++)
            DataStorage.edges.get(i).id = i;

        PowerSet        subsets_of_a  =  new PowerSet(A.length, K);
        Kruskal         krustkal      =  new Kruskal(size - K);

        ArrayList<Edge>  helper;

        for(BitSet I : subsets_of_a)
        {
            DataStorage.marker.clear();
            
        	for (int i = 0; i < A.length; i++) if (I.get(i))
        		DataStorage.marker.add(A[i]);

        	if(DataStorage.marker.size()<K) continue;
        	
            krustkal.clear();

            if ((current = krustkal.analyse()) == -1) continue;
            
            minimum = -1;

            for(Integer pointer : DataStorage.marker)
            {
                helper = list.get(pointer);
                
                for (int i = 0; i < helper.size(); i++)
                if(!DataStorage.marker.contains(helper.get(i).to))
                {
                    minimum = helper.get(i).weight;
                    break;
                }

                if(minimum != -1)
                {
                    current += minimum;
                    continue;
                }

                current = Integer.MAX_VALUE;

                break;
            }
            if (current < min) min = current;
        }
		
        if(min == Integer.MAX_VALUE) throw new FatalError();
        
        result = min;
    }

    private static void symetric_put(int i, int j, int w)
    {
    	int next_id = DataStorage.edges.size();
    	
        DataStorage.edges . add(new Edge(next_id, i, j, w));
        list.get(i)       . add(new Edge(next_id, i, j, w));
        list.get(j)       . add(new Edge(next_id, j, i, w));
    }
    
    static class FatalError extends RuntimeException
    { private static final long serialVersionUID = 3638938829930139263L; }

    private static String $(int value)
    { return String.valueOf((0x001fb^value)==0x0?0x001f2:value).trim(); }

}	