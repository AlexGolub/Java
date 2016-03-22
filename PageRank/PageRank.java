/* 
 * PageRank.java
 *
 *
 * Alex Golub
 * 2016
 *
 * ----------------------------------------------------------------------------
 *
 *
 *
 * Arguments:
 * 
 *   inputFileName         a plaintext file encoding the social network
 *   d                     a PageRank parameter (should be between 0 and 1)
 *   theta                 a PageRank parameter (should be greater than 0)
 *   outdir                the directory where various analyses will be written
 *                         
 *
*/

import java.util.LinkedList;
import java.util.HashMap;

public class PageRank 
{
    private static final String slash = "/";
    public static void main( String[] args ) 
    {
        // Read arguments
        if( args.length != 4 ) {    
            System.err.println("Error: Wrong number of arguments");
            System.exit(1);
        }

        String inputFileName = args[0];
        double d             = Double.parseDouble(args[1]);
        double theta         = Double.parseDouble(args[2]);
        String outdir        = args[3];

        // Read graph and get nodes
        System.out.println("Declaring a new SocialNetwork");
        SocialNetwork network = new SocialNetwork(inputFileName,d,theta);
        LinkedList<String> nodes = network.getSortedNodes();
        
        // Print all in and out degrees for each node 
        java.io.PrintStream degreesFile = null;
        try {
            degreesFile = new java.io.PrintStream(new java.io.File(outdir + slash + "nodeDegrees.txt"));
            System.out.println("getting out/in degree of all nodes");
            for( String name : nodes ) 
            {
                degreesFile.printf("Node %s has outdegree %d and indegree %d\n",name,network.outDegreeOfNode(name),network.inDegreeOfNode(name));
            }
        } catch( java.io.FileNotFoundException e ) {
            System.err.println("Error: Unable to open output file for writing" + e);
            System.exit(1);
        }
        
        // Find and print the min/max/avg in/out-degrees
        System.out.println("computing all in out degrees");
        DegreeResult degrees = network.computeAllInOutDegrees();
        System.out.printf("Node %s has the minimum in-degree: %d\n",degrees.argminInDegree,degrees.minInDegree);
        System.out.printf("Node %s has the minimum out-degree: %d\n",degrees.argminOutDegree,degrees.minOutDegree);
        System.out.printf("Node %s has the maximum in-degree: %d\n",degrees.argmaxInDegree,degrees.maxInDegree);
        System.out.printf("Node %s has the maximum out-degree: %d\n",degrees.argmaxOutDegree,degrees.maxOutDegree);
        System.out.printf("The average out degree is %.2f\n",degrees.avgOutDegree);

        // Compute and print PageRank
        HashMap<String,Double> PR = network.pageRank();
        java.io.PrintStream pageRankFile = null;
        try {
            System.out.println("computing pageRank for all nodes");
            pageRankFile = new java.io.PrintStream(new java.io.File(outdir + slash + "pageRank.txt"));
            for( String node : nodes ) {
                pageRankFile.printf("PageRank of node %s is %.5e\n",node,PR.get(node));
            }
        } catch( java.io.FileNotFoundException e ) {
            System.err.println("Error: Unable to open output file for writing" + e);
            System.exit(1);
        }
        
        // Print adjacency list and reverse adjacency lists
        java.io.PrintStream adjListOut    = null;
        java.io.PrintStream revAdjListOut = null;
        try {
            adjListOut = new java.io.PrintStream(new java.io.File(outdir + slash + "adjacencyList.txt"));
            revAdjListOut = new java.io.PrintStream(new java.io.File(outdir + slash + "reverseAdjacencyList.txt"));

            adjListOut.print(network.adjacencyListToString());
            revAdjListOut.print(network.reverseAdjacencyListToString());
        } catch( java.io.FileNotFoundException e ) {
            System.err.println("Error: Unable to open output file for writing" + e);
            System.exit(1);
        }

        // Compute and print degreesOfSeparation
        java.io.PrintStream degreesOfSep = null;
        try {
            degreesOfSep = new java.io.PrintStream(new java.io.File(outdir + slash + "degreesOfSeparation.txt"));
            String node1 = nodes.getFirst();
            HashMap<String,Integer> degreesFromNode = network.degreesOfSeparation(node1);
            for( String node2 : nodes ) {
                if( null != degreesFromNode.get(node2) ) {
                    degreesOfSep.printf("%s --> %s is %d\n",node1,node2,degreesFromNode.get(node2));
                }
            }
        } catch( java.io.FileNotFoundException e ) {
            System.err.println("Error: Unable to open output file for writing" + e);
            System.exit(1);
        }
        
    }
}