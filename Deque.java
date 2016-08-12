package mwong.myprojects.utilities;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/****************************************************************************
 *  @author   Meisze Wong
 *            www.linkedin.com/pub/macy-wong/46/550/37b/
 *
 *  Compilation:  javac Deque.java
 *  Execution:    java Deque
 *
 * A generic data type for a dequeue implement elementary data structures using 
 * linked lists, generic and iterators.
 * Dequeue - A double-ended queue or dequeue (pronounced "deck") is a generalization
 * of a stack and a queue that supports adding and removing items from either the
 * front or the back of the data structure.
 *
 ****************************************************************************/

public class Deque<Item> implements Iterable<Item> {
    private static Scanner scanner;
    private Node head, tail;
    private int N; 
    
    //  data tyoe Node class for deque.
    //  it has the item, the pointer to next Node, and the pointer to previous Node
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }
    
    /**
     * Initializes an empty deque.
     */
    public Deque() {
        head = null;
        tail = null;
        N = 0;
    }
    
    /**
     * Return the empty status of the deque.
     * 
     * @return true if deque is empty, false otherwise.
     */
    public boolean isEmpty() {
        return (N == 0);
    }
    
    /**
     * Return the number of items in this  deque.
     * 
     * @return an integer, the number of items in this  deque.
     */
    public int size() {
        return N;
    }
    
    /**
     * Adds the item at the front of the deque.
     * 
     * @param item the item to add
     */
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (N == 0) {
            head = new Node();
            head.item = item;
            tail = head;
        } else {
            Node oldH = head;
            head = new Node();
            head.item = item;
            oldH.prev = head;
            head.next = oldH;
            oldH = null;
        }
        N++;
    }
    
    /**
     * Adds the item at the end of the deque.
     * 
     * @param item the item to add
     */
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (N == 0) {
            head = new Node();
            head.item = item;
            tail = head;
        } else {
            Node oldT = tail;
            tail = new Node();
            tail.item = item;
            oldT.next = tail;
            tail.prev = oldT;
            oldT = null;
        }
        N++;
    }
    
    /**
     * Removes and returns the first item on this deque.
     * 
     * @return the first item on this deque
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = head.item;
        if (N > 1) {
            head = head.next;
            head.prev = null;
        } else {
            head = null;
            tail = null;
        }
        N--;
        return item;
    }
    
    /**
     * Removes and returns the last item on this deque.
     * 
     * @return the last item on this deque
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = tail.item;
        if (N > 1) {
            tail = tail.prev;
            tail.next = null;
        } else {
            head = null;
            tail = null;
        }
        N--;
        return item;
    }
    
    /**
     * Returns an iterator that iterates over the items in this queue in FIFO order.
     * 
     * @return an iterator that iterates over the items in this queue in FIFO order
     */
    public Iterator<Item> iterator() {
        return new DoubleLinkedListIterator();
    }
    
    // an iterator, doesn't implement remove() since it's optional
    private class DoubleLinkedListIterator implements Iterator<Item> {
        private int n = N;
        private Node current = head;
        
        public boolean hasNext() { 
            return (n > 0);
        }
        
        public void remove() { 
            throw new UnsupportedOperationException();
        }
        
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            n--;
            return item;
        }
    }
    
    /**
     * Unit tests the <tt>Deque</tt> data type.
     * 
     * @param args main function standard arguments
     */
    public static void main(String[] args) {
        scanner = new Scanner(System.in, "UTF-8");
        int idx = 1;
        Deque<Integer> q = new Deque<Integer>();
        while (true) {
            System.out.println("Enter 0 - exit, 1 - addFirst, 2 - addLast,"
                    + " 3 - removeFirst, 4 - removeLast:");
            int option = scanner.nextInt();
            if (option == 0) {
                break;
            } else if (option == 1) {
                q.addFirst(idx++);
            } else if (option == 2) {
                q.addLast(idx++);
            } else if (option == 3) {
                if (q.isEmpty()) {
                    System.out.println("Empty queue.");
                } else {
                    q.removeFirst();
                }
            } else if (option == 4) {
                if (q.isEmpty()) {
                    System.out.println("Empty queue.");
                } else {
                    q.removeLast();
                } 
            } else {
                System.out.println("Invalid input");
            }
            
            if (!q.isEmpty()) {
                Iterator<Integer> itr = q.iterator();
                while (itr.hasNext()) {
                    System.out.print(itr.next() + " ");
                }
                System.out.println();
            }
            if (option == 0) {
                System.exit(0);
            }
        }
    }
}
