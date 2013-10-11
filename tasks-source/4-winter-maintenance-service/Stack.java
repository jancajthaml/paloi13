package pal;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class Stack implements Iterable<Integer>, Iterator<Integer>
{

    private Node first		= null;
    private Node current	= null;

    public boolean isEmpty()
    { return first == null; }

    public void push(int id)
    {
        Node old	= first;
        first		= new Node();
        first.id	= id;
        first.next	= old;
    }

    public int pop()
    {
        int id  = first.id;
        first   = first.next;
        return id;
    }

    public Iterator<Integer> iterator()
    {
    	current = first;
        return this;
    }

    public boolean hasNext() 
    { return current != null; }
    
    public void remove()
    { throw new ConcurrentModificationException();  }

    public Integer next()
    {
    	int id   = current.id;
    	current  = current.next; 
    	return id;
    }
    
    private static class Node
    {
        private int id;
        private Node next;
    }

}