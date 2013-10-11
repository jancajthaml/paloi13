package pal;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class Queue implements Iterable<Integer>, Iterator<Integer>
{

    private Node first    =  null;
    private Node last     =  null;
    private Node current  =  null;

    public boolean isEmpty()
    { return first == null; }

    public void enqueue( int id )
    {
        Node old   =  last;
        last       =  new Node();
        last.id    =  id;
        last.next  =  null;
        
        if (isEmpty()) first     =  last;
        else           old.next  =  last;
    }

    public Integer dequeue()
    {
        int id = first.id;
        first = first.next;
        if (isEmpty()) last = null;
        return id;
    }

    public Iterator<Integer> iterator()
    {
    	current  =  first;
    	return this;
    }

    public boolean hasNext()
    { return current != null; }
    
    public void remove()
    { throw new ConcurrentModificationException();  }

    public Integer next()
    {
    	int id   =  current.id;
    	current  =  current.next; 
    	return id;
    }

    private static class Node
    {
        private int id;
        private Node next;
    }

}