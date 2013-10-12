package pal;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class Queue implements Iterable<Integer>, Iterator<Integer>
{

    private Node  head     =  null;
    private Node  tail     =  null;
    private Node  current  =  null;

    public boolean isEmpty()
    { return head == null; }

    public void enqueue( int id )
    {
        Node old   =  tail;
        tail       =  new Node();
        tail.id    =  id;
        tail.next  =  null;
        
        if (isEmpty()) head      =  tail;
        else           old.next  =  tail;
    }

    public Integer dequeue()
    {
        int id  =  head.id;
        head    =  head.next;
        if (isEmpty()) tail = null;
        return id;
    }

    public Iterator<Integer> iterator()
    {
    	current  =  head;
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
        private int   id;
        private Node  next;
    }

}