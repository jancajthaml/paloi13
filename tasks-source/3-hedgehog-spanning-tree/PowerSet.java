package pal;

import java.util.BitSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class PowerSet implements Iterator<BitSet>, Iterable<BitSet>
{
    private int      size   = 0;
    private int      index  = 0;
    private BitSet   bit    = new BitSet();
    private boolean  last   = false;
    private boolean  end    = false;
    private int      count  = 0;

	public PowerSet(int size, int k)
	{
		this.size = size;
		bit.set(0, k, true);
		index = k - 1;
	}

	public BitSet next()
	{ return bit; }

	@Override public Iterator<BitSet> iterator()
	{ return this; }

	
	@Override public boolean hasNext()
	{
		if(end) return false;
		if (size > index++)
		{
			bit.set(index-1, false);
			bit.set(index,true);
			return true;
		}
		
		last	= true;
		count	= 1;
		
		for (int i = size - 2; i >= 0; i--)
		{
			if (bit.get(i))
			{
				count++;
				if (!last)
				{
					count--;
					bit.set(i++,false);
					bit.set(i,true);
					bit.set(i+1,i+1+count,true);
					index = i + count;
					bit.set(i+1+count,size,false);
					return true;
				}
			}
			last = bit.get(i);
		}
		
		end = true;
		return true;
	}

	@Override public void remove() { throw new ConcurrentModificationException(); }

}