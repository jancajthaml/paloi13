package task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main
{
	static BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws IOException
	{

		int counter			= 1;
		int number_of_nodes = Integer.valueOf(bi.readLine());
		
		double sum			= 0;
		int last_x			= -1;
		int last_y			= -1;
		int first_x			= 0;
		int first_y			= 0;
		int x				= 0;
		int y				= 0;
		
		String line			= bi.readLine();
		int index			= line.indexOf(' ');
		
        first_x = last_x = x = cast(line.substring(0, index));
		first_y = last_y = y = cast(line.substring(index + 1));
		
		while (counter++ < number_of_nodes)
		{
			line	=  bi.readLine();
			index	=  line.indexOf(' ');
	        x		=  cast(line.substring(0, index));
	        y		=  cast(line.substring(index + 1));
			sum		+= sqrt(x-last_x, y-last_y);
			last_x	=  x;
			last_y	=  y;
		}
		
		sum			+= sqrt(first_x-last_x, first_y-last_y);
		sum			*= 5;
		
		System.out.println((sum > 0 && sum != ((int)sum))?(((int)sum) + 1):((int)sum));
	}
	
	private static double sqrt(int x, int y)
	{ return Math.sqrt(square(x)+square(y)); }
	
	private static int square(int x)
	{
		int s = 0;
	    
	    if(( x & (1 << 0)) != 0)	s += x;
	    if(( x & (1 << 1)) != 0)	s += x << 1;
	    if(( x & (1 << 2)) != 0)	s += x << 2;
	    if(( x & (1 << 3)) != 0)	s += x << 3;
	    if(( x & (1 << 4)) != 0)	s += x << 4;
	    if(( x & (1 << 5)) != 0)	s += x << 5;
	    if(( x & (1 << 6)) != 0)	s += x << 6;
	    if(( x & (1 << 7)) != 0)	s += x << 7;
	    if(( x & (1 << 8)) != 0)	s += x << 8;
	    if(( x & (1 << 9)) != 0)	s += x << 9;
	    if(( x & (1 << 10)) != 0)	s += x << 10;
	    if(( x & (1 << 11)) != 0)	s += x << 11;
	    if(( x & (1 << 12)) != 0)	s += x << 12;
	    if(( x & (1 << 13)) != 0)	s += x << 13;
	    if(( x & (1 << 14)) != 0)	s += x << 14;
	    if(( x & (1 << 15)) != 0)	s += x << 15;
	    if(( x & (1 << 16)) != 0)	s += x << 16;
	    if(( x & (1 << 17)) != 0)	s += x << 17;
	    if(( x & (1 << 18)) != 0)	s += x << 18;
	    if(( x & (1 << 19)) != 0)	s += x << 19;
	    if(( x & (1 << 20)) != 0)	s += x << 20;
	    if(( x & (1 << 21)) != 0)	s += x << 21;
	    if(( x & (1 << 22)) != 0)	s += x << 22;
	    if(( x & (1 << 23)) != 0)	s += x << 23;
	    if(( x & (1 << 24)) != 0)	s += x << 24;
	    if(( x & (1 << 25)) != 0)	s += x << 25;
	    if(( x & (1 << 26)) != 0)	s += x << 26;
	    if(( x & (1 << 27)) != 0)	s += x << 27;
	    if(( x & (1 << 28)) != 0)	s += x << 28;
	    if(( x & (1 << 29)) != 0)	s += x << 29;
	    if(( x & (1 << 30)) != 0)	s += x << 30;
	    if(( x & (1 << 31)) != 0)	s += x << 31;
	    
	    return s;
	}
	
	private static int cast(String s)
	{
		int result	= 0;
		int i		= 0;
		int len		= s.length();
		int digit	= 0;
		
		while (i < len)
		{
			digit  =  Character.digit(s.charAt(i++),10);
			
            result *= 10;
            result -= digit;
		}
		return result;
	}

}