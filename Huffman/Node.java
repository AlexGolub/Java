/* 
 * File: Node.java
 * 
 * Builds a node that contains a character and it's frequency
 * 
 * Name: Alex Golub
 */

import java.util.*;


public class Node {
	int frequency;          // Frequency of the character
	Node left;              // The node's left child
	Node right;             // The node's right child
        Character c;            // the character
	
        // Constructor 1
        // Node
        // Preconditions: Takes in character and its frequency along with left and right child nodes
        // Postconditions: initializes all the members
	Node(Character c, Integer i, Node left, Node right){
		this.c = c;
		this.frequency = i;
		this.left = left;
		this.right = right;
	}
        
        // Constructor 2
        // Node
        // Preconditions: takes in a character and its frequency
        // Postconditions: initialies character and frequency
        Node(Character c, int i)
        {
            this.c = c;
            this.frequency = i;
        }
        
        // getfrequency
        // Preconditions: none
        // Postconditions: returns the frequency
        public int getFrequency()
        {
            return frequency;
        }
        
        // getCharacter
        // Preconditions: none
        // Postconditions: returns character
        public Character getCharacter()
        {
            return c;
        }
        
        // compareTo
        // Preconditions: takes in a node
        // Postconditions: compares the node with previous node and returns a positive or negative integer, depending on which larger
        public int compareTo(Node n)
        {
            return this.frequency - n.frequency;
        }
	
        // getLeft
        // Preconditions: none
        // Postconditions: return the left child node
	public Node getLeft()
	{
		return left;
	}
        
        // getRight
        // Preconditions: none
        // Postconditions: returns the right child node
        public Node getRight()
	{
		return right;
	}
        
        // setRight
        // Preconditions: takes in a node
        // Postconditions: sets the given node as the right child
        public void setRight(Node right)
        {
            this.right = right;
        }
        
        // setLeft
        // Preconditions: takes in a node
        // Postconditions: sets the given node as the left child
        public void setLeft(Node left)
        {
            this.left = left;
        }
}

