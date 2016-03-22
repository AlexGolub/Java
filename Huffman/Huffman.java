/*
 * Huffman.java
 *
 * Prompts the user to enter the name of the textfile that
 * is going to get Huffman codes. Uses Heap, MinHeap, Htree, Node classes
 * To create create a .huf file.
 *
 * Name: Alex Golub
 */

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Huffman 
{

    /**
     * main
     * @param args
     */
    public static void main(String[] args) 
    {
        // Read arguments
        if( args.length != 1 ) 
        {    
            System.err.println("Error: Wrong number of arguments");
            System.exit(1);
        }
        
        String inputFileName = args[0];
        
        Heap obj = new Heap();
        
        // Loadt the text file
	obj.loadFile(inputFileName);
	System.out.println(obj.str);
	obj.computeCharFrequency();
        System.out.println(obj.hm);

        
        // create min heap
        MinHeap heap = new MinHeap(obj.createNodes());
       
        
        System.out.println("**********************************************");
        
        // Create and print the Huffman tree
        Htree tree = new Htree();
        Node root = tree.buildHTree(heap);
        tree.printTree(root, 0, new int [10000]);
        
        // Calculate the total number of  Huffman code bits
        int bitCounter = 0;
        for ( Character c : obj.hm.keySet() ) 
        {
            bitCounter += obj.hm.get(c) * tree.bitCounter.get(c);
        }
        
        // Calculate the total number of bits in the original uncompressed file
        int originalBitCounter = 0;
        for ( Character c : obj.hm.keySet() ) 
        {
            originalBitCounter += obj.hm.get(c) * 8;
        }
        System.out.println("The total number of Huffman code bits is: " + bitCounter);
        System.out.println("The total number of bits in the original file is: " + originalBitCounter);
        System.out.println("The amount of bits that we saved using Huffman coding is: " + (originalBitCounter - bitCounter));
        
        
        
        // Print to file
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("output/" + inputFileName.substring(0, inputFileName.length() - 4) + ".huf", "UTF-8");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Program3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Program3.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Collections.sort(tree.text);        // Sort lexicographicaly
        
        tree.text.add("The total number of Huffman code bits is: " + bitCounter);
        tree.text.add("The total number of bits in the original file is: " + originalBitCounter);
        tree.text.add("The amount of bits that we saved using Huffman coding is: " + (originalBitCounter - bitCounter));
        for ( int i = 0; i < tree.text.size(); i++ )
        {
            writer.println(tree.text.get(i));
        }
        
        writer.close();
        
        
    }
    
}
