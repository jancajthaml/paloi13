package pal;

public class BinomialHeap
{
    private Node head;
    
    public BinomialHeap()
    { head = null; }
    
    private static class Node
    {
        int   value    = -1;
        Node  child    = null;
        Node  sibling  = null;
        int   degree   = 0;
    	
        public Node( int e )
        { value  =  e; }
    	
        public int max()
        {
            int  max  =  this . value;
            Node x    =  this . child;
    		
            while( x != null )
            {
                int        v    =  x . max()   ;
                if(v>max)  max  =  v           ;
                           x    =  x . sibling ;
            }

            return max;
        }
    }

    public void insert( int e )
    {
    	BinomialHeap  prime  =  new BinomialHeap (       );
    	prime.head           =  new Node         (   e   );
    	this.head            =  this.union       ( prime ).head;
    }
	
    public BinomialHeap union( BinomialHeap h2 )
    {
    	BinomialHeap h	=  new BinomialHeap();

    	h    . head     =  binomialHeapMerge(this, h2) ;
    	this . head     =  null                        ;
    	h2   . head     =  null                        ;

    	if (h.head == null) return h ;

    	Node prevX  =  null          ;
    	Node x      =  h . head      ;
    	Node next   =  x . sibling   ;

    	while( next != null )
    	{
    		if( x.degree != next.degree || (next.sibling != null && next.sibling.degree == x.degree) )
    		{
    			prevX	= x;
    			x		= next;
    		}
    		else
    		{
    			if( x.value < next.value )
    			{
    				x.sibling = next.sibling;
    				binomialLink(next, x);
    			}
    			else
    			{
    				if( prevX == null )  h     . head     =  next;
    				else                 prevX . sibling  =  next;

    				binomialLink( x, next );
    				x = next;
    			}
    		}
    		next = x.sibling;
    	}
    	return h;	
    }

    private void binomialLink( Node y, Node z )
    {
        y . sibling  =  z.child;
        z . child    =  y;
        z . degree++;
    }

    private static Node binomialHeapMerge( BinomialHeap h1, BinomialHeap h2 )
    {
             if ( h1.head == null )  return h2 . head;
        else if ( h2.head == null )  return h1 . head;
        else
        {
    		Node head	= null;
    		Node tail	= null;
    		Node h1Next	= h1.head,
    		h2Next		= h2.head;

    		if (h1.head.degree <= h2.head.degree)
    		{
    			head	= h1.head;
    			h1Next	= h1Next.sibling;
    		}
    		else
    		{
    			head	= h2.head;
    			h2Next	= h2Next.sibling;
    		}

    		tail = head;

    		while (h1Next != null && h2Next != null)
    		{
    			if (h1Next.degree <= h2Next.degree)
    			{
    				tail.sibling	= h1Next;
    				h1Next			= h1Next.sibling;
    			}
    			else
    			{
    				tail.sibling	= h2Next;
    				h2Next			= h2Next.sibling;
    			}
    			tail = tail.sibling;
    		}

    		tail.sibling = (h1Next != null) ?h1Next:h2Next;

    		return head;
    	}
    }

	public int diff()
	{
        int   difference  =  0    ;
        Node  tree        =  head ;

        while( tree!=null )
        {
            int  max         =   tree.max()   ;
            int  root        =   tree.value   ;
                 difference  +=  max - root   ;
                 tree        =   tree.sibling ;
        }

        return difference;
    }

	public void recycle( Permutation data )
	{
        head = null;
        for( int i : data.data ) insert( i );
	}

}