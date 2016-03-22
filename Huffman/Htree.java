/* 
 * File: Htree.java
 * 
 * Build and treverse the Huffman tree
 *
 * Name: Alex Golub
 */

import java.util.*;


public class Htree {
  
    public static HashMap<Character, Integer> bitCounter = new HashMap<>();     // Holds the number of Huffman code bits per character
    public static String str = "";                                              // Helper variable
    public static LinkedList<String> text = new LinkedList<String>();           // Holds the data that is going to be written to a file

    // Constructor
    // Htree
    // Preconditions: none
    // Postconditions: build a huffman tree
    Htree()
    {
        //buildHTree(heap);
    }
    
    // buildHTree
    // Preconditions: takes in a heap
    // Postconditions: combines the two nodes extracted from the heap and builds a tree
    public Node buildHTree(MinHeap heap) 
    {

        while (heap.getSize() > 1) {
            Node node1 = heap.extract();
            Node node2 = heap.extract();
            Node newNode = new Node(null, node1.frequency + node2.frequency);
            newNode.setLeft(node1);
            newNode.setRight(node2);

            heap.insert(newNode);
        }
        return heap.extract();
    }

    // isLeaf
    // Preconditions: takes in a node
    // Postconditions: returns true if the node is a leaf
    private static boolean isLeaf(Node root) 
    {
        if ( root.getLeft() == null && root.getRight() == null)
        {
            return true;
        }
        else
            return false;
    }

    // printArr
    // Preconditions: takes in an array and it's size
    // Postconditions: prints the bit codes for a given character
    static void printArr(int array[], int size) 
    {
        int i;
        for (i = 0; i < size; ++i)
        {
            str += array[i];
            System.out.print(array[i]);
        }
        System.out.println();
    }

    // printTree
    // Preconditions: takes in a node, index, and an array
    // Postconditions: traverses the array and prints out Huffman codes
    public static void printTree(Node root, int i, int[] array) 
    {
        if (root.getLeft() != null) 
        {
            array[i] = 0;
            printTree(root.getLeft(), i + 1, array);
        }
        if (root.getRight() != null) 
        {
            array[i] = 1;
            printTree(root.getRight(), i + 1, array);
        }
        if (isLeaf(root))
        {
            str += root.getCharacter() + " : ";
            System.out.print(root.getCharacter() + " : ");
            bitCounter.put(root.getCharacter(), i);                    // Count the bits for each character
            printArr(array, i);
            text.add(str);
            str = "";
        }
    }
} // END CLASS
