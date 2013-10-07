package pal;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

class PowerSet implements Iterator<Set<Integer>>, Iterable<Set<Integer>>
{
	private  int        size   =  0;
	private  Integer[]  arr    =  null;
	private  boolean[]  bset   =  null;
	
	public PowerSet(List<Integer> set, int size)
	{
		this.size  = size;
		this.arr   = set.toArray(new Integer[set.size()]);
		this.bset  = new boolean[arr.length + 1];

		for (int i = 0; i < size; i++) bset[i]=true;
	}

	@Override public boolean hasNext()
	{ return !bset[arr.length]; }

	@Override public Set<Integer> next()
	{
		Set<Integer> set = new TreeSet<Integer>();
	        
		for (int i = 0; i < arr.length; i++) if (bset[i])
			set.add(arr[i]);

		do { increment(); }
		while ((count() != size));

		return set;
	}

	protected void increment()
	{
		for (int i = 0; i < bset.length; i++)
		{
			if (bset[i])
				bset[i] = false;
			else
			{
				bset[i] = true;
				break;
			}
		}
	}

	protected int count()
	{
		int count = 0;
		for (int i = 0; i < bset.length; i++) if (bset[i]) count++;
		return count;
	}

	@Override public void remove(){}
	@Override public Iterator<Set<Integer>> iterator() { return this; }
}