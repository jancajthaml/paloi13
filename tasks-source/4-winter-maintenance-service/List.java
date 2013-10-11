package pal;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class List implements Iterable<Integer>, Iterator<Integer>
{

    private Node first   = null;
    private Node current = null;
    
    public boolean isEmpty()
    { return first == null; }

    public void add(int id)
    {
        Node old   = first;
        first      = new Node();
        first.id   = id;
        first.next = old;
    }

    public Iterator<Integer> iterator() 
    {
    	current = first;
        return this;  
    }

    public boolean hasNext()
    { return current != null; }
    
    public void remove()
    { throw new ConcurrentModificationException(); }

    public Integer next()
    {
    	int id   = current.id;
    	current  = current.next; 
    	return id;
    }
    
    private class Node
    {
        private int id;
        private Node next;
    }

}