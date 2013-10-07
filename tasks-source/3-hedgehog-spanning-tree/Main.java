
package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public final class Main
{  
	static HashMap<Integer,ArrayList<Edge>> list	= new HashMap<Integer,ArrayList<Edge>>();
	static  BufferedReader   bi    	 				= new BufferedReader(new InputStreamReader(System.in));
	
	static int size									= 0;
	static List<Integer> A							= null;
	static int K									= 0;
	static int result								= 0;
	
    public static void main (String[] args) throws NumberFormatException, IOException
    {	
		try
		{
			read_data	();
			find_mst	();
		}
		catch(FatalError r)	{ result = -1;					}
		finally				{ System.out.println(result);	}
    }
	
    static void read_data() throws FatalError
    {
    	try								{ size	= Integer.valueOf(bi.readLine());	}
    	catch (NumberFormatException e)	{ throw new FatalError();					}
    	catch (IOException e)			{ throw new FatalError();					}
    	
		for (int i = 1; i <= size; i++) list.put(i,new ArrayList<Edge>());
		
		int number	= 0;
		int p		= 0;
		
		for (int i = 1; i <= size; i++)
		{
			StringTokenizer st  =  null;
			try								{ st = new StringTokenizer(bi.readLine()," ");	}
	    	catch (NumberFormatException e)	{ throw new FatalError();						}
	    	catch (IOException e)			{ throw new FatalError();						}
	    	
			
			for (int j = 1; j <= i; j++)
			{
				number = Integer.valueOf(st.nextToken());
				if (number != 0)
				{
					DataStorage.edges.add(new Edge(p, i, j, number));
					list.get(i).add(new Edge(p, i, j, number));
					list.get(j).add(new Edge(p, j, i, number));
					p++;
				}
			}
		}
		
		for (int i = 1; i <= list.size(); i++)
			Collections.sort(list.get(i));
	
		try
		{
			String[] split = bi.readLine().split("  ");
		
			A = new ArrayList<Integer>();

			for(String s : split[1].split(" "))
				A.add(Integer.valueOf(s));
		
			K = Integer.valueOf(bi.readLine());        
		}
    	catch (NumberFormatException e)	{ throw new FatalError();									}
    	catch (IOException e)			{ throw new FatalError();									}
    	finally							{ if(DataStorage.edges.size()==0) throw new FatalError();	}
    }
    
	public static void find_mst() throws FatalError
	{
		int current	= 0;
		int min		= Integer.MAX_VALUE;
		int minimum	= 0;
		
		Collections.sort(DataStorage.edges);

		for (int i = 0; i < DataStorage.edges.size(); i++)
			DataStorage.edges.get(i).id = i;

        PowerSet			subsets_of_a	= new PowerSet(A, K);
		Kruskal				krustkal		= new Kruskal(size - K);

		
		ArrayList<Edge> help;

		for(Set<Integer> I : subsets_of_a)
		{
			DataStorage.marker.clear();
			DataStorage.marker.addAll(I);
			
			krustkal.clear();

			if ((current = krustkal.analyse()) != -1)
			{
				minimum = -1;

				for(Integer pointer : DataStorage.marker)
				{
					help = list.get(pointer);
					for (int i = 0; i < help.size(); i++)
					{
						if(!DataStorage.marker.contains(help.get(i).to))
						{
							minimum = help.get(i).weight;
							break;
						}
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
		}
		
		if(min == Integer.MAX_VALUE) throw new FatalError();
		result=min;
	}

	static class FatalError extends RuntimeException
	{ private static final long serialVersionUID = 3638938829930139263L; }
	
}	