package pal;

class Edge implements Comparable<Edge>
{
	int id;
	int from;
	int to;
    int weight;
	Edge parent;
	
    public Edge(int id, int from, int to, int weight)
    {
    	this.id		= id;
        this.from	= from;
        this.to		= to;
        this.weight	= weight;
    }
	    
    public int compareTo(Edge o)
    { return (this.weight <= o.weight)?-1:1;}
	  /*  
    public boolean equals(Object another)
    {
    	if(!(another instanceof Edge)) return false;
    	return ((Edge)another).id == this.id;
    //	return ((Edge)another).from == this.from && ((Edge)another).to == this.to && ((Edge)another).weight == this.weight;
    }
*/
}