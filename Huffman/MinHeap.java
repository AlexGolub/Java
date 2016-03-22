/* File: MinHeap.java
 * 
 * The class builds a min-heap from an array list of nodes
 * 
 * Name: Alex Golub
 */

import java.util.*;


public class MinHeap 
{
    ArrayList<Node> array;          // Array of nodes
    int size;                       // Size of the heap
    
    // Constructor 1
    // Preconditions: none
    // Postconditions: initializes array
    MinHeap()
    {
        array = new ArrayList<Node>();
    }
    
    // Constructor 2
    // Preconditions: takes in an arraylist of nodes
    // Postconditions: initializes array with the inputtted arraylist
    MinHeap(ArrayList<Node> nodes)
    {
        array = nodes;
        this.size = array.size();
        buildHeap();
    }
    
    // buildHeap
    // Preconditions: none
    // Postconditions: builds a heap by calling the siftDown method
    public void buildHeap()
    {
        int end = array.size() / 2;
        while (end >= 0)
        {
            siftDown(end);
            end --;
        }
    }
    
    // extract
    // Preconditions: none
    // Postconditions: extracts the node with the smallest frequency from the heap
    public Node extract() 
    {
        if ( array.size() <= 0 ) 
        {
            return null;
        }
        Node min = array.get(0);
        array.set(0, array.get(array.size() - 1));
        array.remove(array.size() - 1);
        siftDown(0);
        this.size = size - 1;
        return min;
    }
    
    // insert
    // Preconditions: takes in a new node
    // Postconditions: inserts the new node into the heap
    public void insert(Node n)
    {
        array.add(n);
        this.size = size + 1;
        buildHeap();
    }
    
    // siftDown
    // Preconditions: takes in an array index
    // Postconditions: puts the node in its rightful place
    public void siftDown(int i) 
    {
        int left = leftChild(i);
        int right = rightChild(i);
        int smallest = i;

        if (left < array.size() && array.get(left).compareTo(array.get(smallest)) < 0)
        {
            smallest = left;
        }
        if (right < array.size() && array.get(right).compareTo(array.get(smallest)) < 0)
        {
            smallest = right;
        }
        if (smallest != i) 
        {
            swap(smallest, i);
            siftDown(smallest);
        }
    }
    
    // swap
    // Preconditions: takes in 2 array indeces
    // Postconditions: initializes array
    void swap(int i, int j) 
    {
        Node n = array.get(i);
        array.set(i, array.get(j));
        array.set(j, n);
    }

    // parent
    // Preconditions: takes in an index
    // Postconditions: returns index of the parent node
    public int parent (int i)
    {
        return (int)Math.floor(i/2);
    }
    
    // leftChild
    // Preconditions: takes in an index
    // Postconditions: returns the index of the left child node
    public int leftChild (int i)
    {
        return 2*i;
    }
    
    // rightChild
    // Preconditions: takes in an index
    // Postconditions: returns the right child node
    public int rightChild (int i)
    {
        return 2*i + 1;
    }
    
    // getSize
    // Preconditions: none
    // Postconditions: return the size of the heap
    public int getSize()
    {
    	return this.size;
    }
    
}
