/*
 * Heap.java
 *
 * Loads a text file and puts each individual character and frequency of occurance
 * into individual nodes
 *
 * Name: Alex Golub
 */

import java.lang.*;
import java.util.*;
import java.io.*;


public class Heap {
    String str = "";                            // Stores the inputed text file
    HashMap<Character, Integer> hm;		// Maps characters to their frequencies
    
    // loadFile
    // Preconditions: The method takes in a filename
    // Postcondition: Fills in the str string with the entire inputed text file
    public void loadFile (String filename)
	{
            Scanner input = null;
        
            try {
                  input = new Scanner(new File(filename));
            } catch (FileNotFoundException fe) {
                  System.out.println("Error: Unable to open file " + filename);
                  System.exit(1);
            }
            
            str = input.next();         // In case the text contains only one word
            while(input.hasNext())
            {
                str = str + " " + input.next();
            }
            input.close();
	}
    
    // ComputeCharFrequency
    // Preconditions: The method doesn't take anything in but uses the global variable str
    // Postconditions: Creates and returns a HashMap with each character mapped to it's frequency of occurance
    public void computeCharFrequency ()
    {
	hm = new HashMap<Character, Integer>();
	char [] chars = str.toCharArray();
    	
    	for(char char1 : chars )
    	{
            if (hm.containsKey(char1))
            {
                hm.put(char1, hm.get(char1) + 1);
            }
            else
                hm.put(char1, 1);
  	}
    }
    
    // createNodes
    // Preconditions: none
    // Postconditions: takes array and fills it with nodes
    public ArrayList<Node> createNodes ()
    {
        ArrayList<Node> array = new ArrayList<Node>();
        
        for ( Character c : hm.keySet() ) 
        {
            array.add(new Node(c, hm.get(c)));
        }
        
        return array;
    }
	
} // END CLASS
