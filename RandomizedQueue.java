package mwong.myprojects.utilities;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/****************************************************************************
 *  @author   Meisze Wong
 *            www.linkedin.com/pub/macy-wong/46/550/37b/
 *
 *  Compilation:  javac RandomizeQueue.java
 *  Execution:    java RandomizeQueue
 *
 * A generic data type for a randomized queue implement elementary data structures 
 * using re-sizable circular array, generic and iterators.
 * Randomized queue - A randomized queue is similar to a stack or queue, except that 
 * the item removed is chosen uniformly at random from items in the data structure.
 *
 ****************************************************************************/

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;
    private int N;
    private Random random;
    
    /**
     * Initializes an empty randomized queue and a random seed.
     */
    public RandomizedQueue() {
        random = new Random();
        a = (Item[]) new Object[8];
        N = 0;
    }  
    
    /**
     * Is this queue empty?
     * 
     * @return true if this queue is empty; false otherwise
     */
    public boolean isEmpty() { 
        return N == 0; 
    }
    
    // resize the underlying array
    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        System.arraycopy(a, 0, temp, 0, N);
        a = temp;
    }
    
    /**
     * Returns the number of items in this queue.
     * 
     * @return the number of items in this queue
     */
    public int size() { 
        return N; 
    }
    
    /**
     * Adds the item to this queue.
     * 
     * @param item the item to add
     */
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (N == a.length) {
            resize(2*a.length);
        }
        a[N++] = item;
    }
    
    /**
     * Removes and returns the item on this queue randomly.
     * 
     * @return the item on this queue randomly
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    public Item dequeue() {
        if (N == 0) {
            throw new NoSuchElementException();
        }
        int r = random.nextInt(N);
        Item item = a[r];
        a[r] = a[N-1];
        a[--N] = null;
        if (N > 2 && N == a.length/4) {
            resize(a.length/2);
        }
        return item;    
    } 
    
    /**
     * Returns the item on this queue randomly.
     * 
     * @return the item on this queue randomly
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    public Item sample() {
        if (N == 0) {
            throw new NoSuchElementException();
        }
        return a[random.nextInt(N)];
    }
    
    /**
     * Returns an iterator that iterates over the items in this queue in random order.
     * @return an iterator that iterates over the items in this queue in random order
     */
    public Iterator<Item> iterator() {
        return new RandomizedArrayIterator();
    }
    
    // an iterator, doesn't implement remove() since it's optional
    private class RandomizedArrayIterator implements Iterator<Item> {
        private int n = N;
        private Item[] copy = (Item[]) new Object[N];

        RandomizedArrayIterator() {
            System.arraycopy(a, 0, copy, 0, N);           
        }
        public boolean hasNext() { 
            return n > 0; 
        }
        
        public void remove() { 
            throw new UnsupportedOperationException(); 
        }
        
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            
            int r = random.nextInt(n);
            Item item = copy[r];
            copy[r] = copy[n-1];
            copy[--n] = null;
            return item;
        }
    }

}

