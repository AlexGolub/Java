/**
 * EdmondsKarp.java
 * CS 405
 * November 12, 2016
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;


/**
 * @author Alex Golub
 *
 */
public class EdmondsKarp {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		Map<Integer, List<Integer>> adjacencyList;
        LinkedList<Integer> list;
        int [][] capacityMatrix;
        int [][] flow;
        int [][] residualCapacity;
        int [] parent;
        int [] x;
        String line;
        int i, k;
        int n;
        int v, u;
        int s, t, maxFlow;
        
        PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
        System.setOut(out);
        
        

		for (String str : args)
            System.out.println("Graph: " + str);
		
		System.out.println();

        Scanner input = new Scanner(new File(args[0]));
        
        n = Integer.parseInt(input.nextLine());
        
        

        /* Build an adjacency list */
        adjacencyList = new HashMap<>();
        i = 0;
        k = 0;
        while (input.hasNextLine())
        {
            list = new LinkedList<>();
            line = input.nextLine().trim().concat("n");
            if (line.length() <= 1)
                continue;
            for (int j = 0; j < line.length(); j++) {
                while (!Character.isDigit(line.charAt(j))) {
                        j++;
                    }
                if (Character.isDigit(line.charAt(j))) {
                    while (Character.isDigit(line.charAt(j))) {
                        k++;
                        j++;
                    }
                }
                if (!Character.isDigit(line.charAt(j))) {
                    list.add(Integer.parseInt(line.substring(j-k, j).trim()));
                    k = 0;
                }
            }
            adjacencyList.put(i, list);
            i++;
        }


        
        /* Build an capacity matrix */
        capacityMatrix = new int[n][n];
        for (int j = 0; j < n; j++) {
        	for (int l = 0; l < n; l++) {
        		capacityMatrix[j][l] = 0;
        	}
        }
        
        for (int j = 0; j < n-1; j++) {
        	v = adjacencyList.get(j).get(0);
        	for (int l = 1; l < adjacencyList.get(j).size(); l++) {
        		capacityMatrix[v][adjacencyList.get(j).get(l)] = adjacencyList.get(j).get(++l);
        	}
        }
        
        

        /* Edmonds-Karp Algorithm */
        
        // Establish G'
        flow = new int[n][n];
        s = 0;
        t = n-1;
        maxFlow = 0;	// Initial flow
        for (int j = 0; j < n; j++) {
        	for (int l = 0; l < n; l++) {
        		flow[j][l] = 0;
        	}
        }
        
        residualCapacity = new int[n][n];
        
        
        x = new int[n];     // Capacity of path to vertex

        while (true) {
        	parent = BFS (capacityMatrix, residualCapacity, flow, x, s, t, n);
        	
        	if (x[t] == 0)
        		break;
        	
        	maxFlow = maxFlow + x[t];
        	
        	v = t;
        	
        	// Update flow and residual
        	while (v != s) {
        		u = parent[v];
        		flow[u][v] = flow[u][v] + x[t];
        		flow[v][u] = flow[v][u] - x[t];
        		residualCapacity[u][v] = flow[u][v] - x[t];
        		residualCapacity[v][u] = flow[v][u] + x[t];
        		v = u;
        	}
        }

        /* Print the results */
        System.out.print("Max flow = " + maxFlow);
        System.out.println();
        System.out.println();
        
        
        
        
        /* Compute the residual matrix */
        for (int j = 0; j < n; j++) {
        	for (int l = 0; l < n; l++) {
        		residualCapacity[j][l] = capacityMatrix[j][l] - flow[j][l];
        	}
        }
        
        
        
        System.out.println("Min cut (S, T) is");
        getMinCut (residualCapacity, capacityMatrix, s, n);
        
        
        input.close ();


	}
	
	
	/*
	 * Breadth-First-Search
	 * Input: capacity matrix, residual capacity matrix, flow matrix, capacity array, source vertex, sink vertex, number of vertices
	 * Output: integer array
	 */
	public static int [] BFS (int capacityMatrix[][], int residualCapacity[][], int flow[][], int x[], int s, int t, int n)
	{
		Queue<Integer> q = new ArrayDeque<Integer>();
		int current;
		
		int [] parent = new int [n];	// Parent array
        Arrays.fill(parent, -1);
        parent[s] = s;
        
        x[s] = Integer.MAX_VALUE;

		q.add(s);		
		
		while (!q.isEmpty()) {
			
			current = q.remove();

			for (int i = 0; i < n; i++) {
				if (capacityMatrix[current][i] - flow[current][i] > 0 && parent[i] == -1) {
					parent[i] = current;
					x[i] = Math.min(x[current], capacityMatrix[current][i] - flow[current][i]);
					
					if (i != t) {
						q.add(i);
					}
					else
						return parent;
				}
			}

		}
		x[t] = 0;
		return parent;
		
	} 
	
	
	/*
	 * getMinCut
	 * Input: adjacency matrix, start vertex, number of vertices
	 * Output: array of vertices
	 */
	public static void getMinCut (int adjacencyMatrix[][], int capacityMatrix[][], int s, int n)
	{
		Queue<Integer> q = new ArrayDeque<Integer>();
		Queue<Integer> S = new ArrayDeque<Integer>();
		Queue<Integer> T = new ArrayDeque<Integer>();
		int minCut;
		int current;
		int size;
		Object[] array;

		q.add(s);
		S.add(s);
		
		while (!q.isEmpty()) {
			
			current = q.remove();

			for (int i = 0; i < n; i++) {
				if (adjacencyMatrix[current][i] > 0) {
					if (!S.contains(i)) {
						q.add(i);
						S.add(i);
					}
				}
			}

		}
		
		System.out.print("S: ");
		size = S.size();
		array = new Object[size];
		array = S.toArray();
		for (int i = 0; i < size; i++) {
			if (i % 10 == 0)
				System.out.println();
			System.out.print(array[i] + " ");
			
		}

		System.out.println();
		System.out.println();
		
		System.out.print("T: ");
		for (int i = 0; i < n; i++) {
			if (!S.contains(i))
				T.add(i);
		}
			
		
		size = T.size();
		array = new Object[size];
		array = T.toArray();
		for (int i = 0; i < size; i++) {
			if (i % 10 == 0)
				System.out.println();
			System.out.print(array[i] + " ");
			
		}
		System.out.println();
		
		minCut = 0;	
		size = S.size();
		
		for (int j = 0; j < size; j++) {
			current = S.remove();
			for (int l = 0; l < n; l++) {
				if (capacityMatrix[current][l] > 0 && T.contains(l))
					minCut += capacityMatrix[current][l];
			}
		}
		
		
		System.out.println();
        System.out.println("Min cut capacity = " + minCut);
		
	}
	

}


