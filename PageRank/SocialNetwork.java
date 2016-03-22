/* 
 * SocialNetwork.java
 *
 * Alex Golub
 *
 */

import java.lang.*;
import java.util.*;
import java.io.*;

public class SocialNetwork {
    private HashMap<String,LinkedList<String>> adjacencyList;        // adj. list representation of graph
    private HashMap<String,LinkedList<String>> reverseAdjacencyList; // rev. adj. list representation of graph
    private double d;                                                // parameter for PageRank
    private double theta;                                            // parameter for PageRank
    private LinkedList<String> sortedNodes;                          // a list of the nodes, sorted

    // Constructor 
    public SocialNetwork(String networkFilename, double d, double theta) 
    {
    	adjacencyList = new HashMap<String,LinkedList<String>>();
    	reverseAdjacencyList = new HashMap<String,LinkedList<String>>();
    	sortedNodes = new LinkedList<String>();
    	loadNetworkFile(networkFilename);
    	
    	for(String key : adjacencyList.keySet())
    	{
    		if(!reverseAdjacencyList.containsKey(key))
    		{
    			reverseAdjacencyList.put(key,new LinkedList<String>()); 
    		}
    	}
    	
    	for(String key : reverseAdjacencyList.keySet())
    	{
    		if(!adjacencyList.containsKey(key))
    		{
    			adjacencyList.put(key,new LinkedList<String>()); 
    		}
    	}
    	
    	sortAllLists();
    	
    	this.d = d;
    	this.theta = theta;
    }

    // getSortedNodes
    public LinkedList<String> getSortedNodes() 
    {
    	return this.sortedNodes;
    }

    // outDegreeOfNode
    public int outDegreeOfNode(String name) 
    {
    	if(this.adjacencyList.containsKey(name))
    	{
    		return this.adjacencyList.get(name).size();
    	}
    	return -1;
    }

    // inDegreeOfNode
    public int inDegreeOfNode(String name) 
    {
    	if(this.reverseAdjacencyList.containsKey(name))
    	{
    		return this.reverseAdjacencyList.get(name).size();
    	}
    	return -1;
    }

    // computeAllInOutDegrees
    public DegreeResult computeAllInOutDegrees() 
    {
    	int maxIn = 0;
    	int minIn = adjacencyList.size();
    	String argMinIn = "";
    	String argMaxIn = "";
    	
    	int maxOut = 0;
    	int minOut = adjacencyList.size();
    	String argMinOut = "";
    	String argMaxOut = "";
    	
    	int totalOutDegree = 0;
    	double avgOutDegree = 0;
    	
    	DegreeResult result = new DegreeResult();
    	
    	for(String key : adjacencyList.keySet())
    	{
    		LinkedList<String> adjList = adjacencyList.get(key);
    		LinkedList<String> revList = reverseAdjacencyList.get(key);
    		
    		for(int i = 0; i < adjList.size(); i++)
    		{
    			totalOutDegree += this.outDegreeOfNode(adjList.get(i));
    			
    			if(minOut >= this.outDegreeOfNode(adjList.get(i)))
    			{
					if(minOut == this.outDegreeOfNode(adjList.get(i))) {
						argMinOut = getAlphabeticallyFirst(adjList.get(i), argMinOut);
					}
    				minOut = this.outDegreeOfNode(adjList.get(i));
    			}
    			
    			if(maxOut <= this.outDegreeOfNode(adjList.get(i)))
    			{
					if(maxOut == this.outDegreeOfNode(adjList.get(i))) {
						argMaxOut = getAlphabeticallyFirst(adjList.get(i), argMaxOut);
					}
    				maxOut = this.outDegreeOfNode(adjList.get(i));
    			}
    		}
    		
    		for(int i = 0; i < revList.size(); i++)
    		{	
    			if(minIn >= this.outDegreeOfNode(revList.get(i)))
    			{
					if(minIn == this.outDegreeOfNode(revList.get(i))) {
						argMinIn = getAlphabeticallyFirst(revList.get(i), argMinIn);
					}
    				minIn = this.outDegreeOfNode(revList.get(i));
    			}
    			
    			if(maxIn <= this.outDegreeOfNode(revList.get(i)))
    			{
					if(maxIn == this.outDegreeOfNode(revList.get(i))) {
						argMaxIn = getAlphabeticallyFirst(revList.get(i), argMaxIn);
					}
    				maxIn = this.outDegreeOfNode(revList.get(i));
    			}
    		}
    	}
    	
    	avgOutDegree = (double)totalOutDegree / this.adjacencyList.size();
    	
    	result.argmaxInDegree = argMaxIn;
    	result.argmaxOutDegree = argMaxOut;
    	result.argminInDegree = argMinIn;
    	result.argminOutDegree = argMinOut;
    	result.avgOutDegree = avgOutDegree;
    	result.maxInDegree = maxIn;
    	result.maxOutDegree = maxOut;
    	result.minInDegree = minIn;
    	result.minOutDegree = minOut;
    	
    	return result;
    }

   // getAlphabeticallyFirst
 	private String getAlphabeticallyFirst(String str1, String str2)
 	{
 		if(str1.equals(""))
 		{
 			return str2;
 		}

 		if(str2.equals(""))
 		{
 			return str1;
 		}

 		if(str1.compareToIgnoreCase(str2) < 0)
 		{
 			return str1;
 		}
 		return str2;
 	}
 	
    // degreesOfSeparation
    public HashMap<String,Integer> degreesOfSeparation( String srcName ) 
    {
    	if(!this.adjacencyList.containsKey(srcName))
    	{
    		return null;
    	}
    	
    	Queue<String> q = new LinkedList<String>();
    	HashMap<String,Integer> degrees = new HashMap<String,Integer>();
    	degrees.put(srcName,0);
    	q.add(srcName);
    	
    	while(!q.isEmpty())
    	{
    		String currentNode = q.remove();
    		for(String connectedNode : adjacencyList.get(currentNode))
    		{
    			if(!degrees.containsKey(connectedNode))
    			{
    				q.add(connectedNode);
    				degrees.put(connectedNode,degrees.get(currentNode)+1);
    			}
    		}
    	}
    	
    	return degrees;
    }
    
    // pageRank 
    public HashMap<String,Double> pageRank() 
    {
    	HashMap<String, Double> prOld = new HashMap<String, Double>();
        HashMap<String, Double> prNew = new HashMap<String, Double>();

        // Initialize
        for (String key : adjacencyList.keySet()) {
            prOld.put(key, (double) 1 / adjacencyList.size());
            prNew.put(key, 0.0);
        }

        boolean converge = false;
        double xNew = 0;
        double xOld = 0;

        while (!converge) {
            for (String x : adjacencyList.keySet()) {
                prNew.put(x, ((1 - d) / adjacencyList.size()));
                for (String y : reverseAdjacencyList.get(x)) {
                    int outDegreeOfCurrentNode = adjacencyList.get(y).size();
                    if (outDegreeOfCurrentNode == 0) {
                        outDegreeOfCurrentNode = adjacencyList.size();
                    }
                    prNew.put(x, prNew.get(x) + d * (prOld.get(y) / outDegreeOfCurrentNode));
                }
                xOld = prOld.get(x);
                prOld.put(x, prNew.get(x));
                xOld = prOld.get(x);
                xNew = prNew.get(x);
            }
            converge = (Math.abs(xNew - xOld) / xOld) <= theta;
        }

        return prNew;
    }
    
    // adjacencyListToString
    public String adjacencyListToString() 
    {
    	return listToString(adjacencyList);
    }


    public String reverseAdjacencyListToString() 
    {
    	return listToString(reverseAdjacencyList);
    }
    
    // listToString
    private String listToString( HashMap<String,LinkedList<String>> map ) 
    {
    	StringBuilder str = new StringBuilder();
    	
    	for(String key : map.keySet())
    	{
    		LinkedList<String> list = map.get(key);
    		str.append(key + ": ");
    		for(String item : list)
    		{
    			str.append(item + " ");
    		}
    		str.append("\n");
    	}
    	
    	return str.toString();
    }


    // loadNetworkFile 
    private void loadNetworkFile( String networkFilename ) 
    {
    	Scanner input = null;
        
    	try {
              input = new Scanner(new File(networkFilename));
        } catch (FileNotFoundException fe) {
              System.out.println("Error: Unable to open file " + networkFilename);
              System.exit(1);
        }
        
      	while(input.hasNext())
      	{
      		String person1 = input.next();
      		String person2 = input.next();
      		addToAdjList(adjacencyList,person1,person2);
      		addToAdjList(reverseAdjacencyList,person2,person1);
      	}
  		input.close();
    }

    // sortAllLists
    private void sortAllLists() 
    {
    	for(String key : adjacencyList.keySet())
    	{
    		LinkedList<String> list1 = adjacencyList.get(key);
    		LinkedList<String> list2 = reverseAdjacencyList.get(key);
    		Collections.sort(list1);
    		Collections.sort(list2);
    		sortedNodes.add(key);
    	}
    	
    	Collections.sort(sortedNodes);
    }
    
    // addToAdjList
    private void addToAdjList(HashMap<String, LinkedList<String>> adjList, String key, String value)
    {
    	if(adjList.get(key) == null)
    	{
    		adjList.put(key, new LinkedList<String>());
    	}

    	addToList(value,adjList.get(key));
    }

    // addToList
    private static void addToList(String nodeName, LinkedList<String> list) 
    {
    	for (String node : list)
    	{
	    	if ( node.equals( nodeName) )
	    	{
	    		return;
	    	}
    	}
    	list.add(nodeName);
    }
} // END SocialNetwork