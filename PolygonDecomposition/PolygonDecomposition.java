import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

/**
 * PolygonDecomposition.java
 */

/**
 * @author Alex
 *
 */
public class PolygonDecomposition {

	
	
	public static Double total = 0.0;
	public static Stack<LinkedList<Integer>> chords = new Stack<>();
 
	
	
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Map<Integer, List<Double>> vertices;
        LinkedList<Double> list;
        Integer s[][];
        int i;
        int n;
        Double min;

        
        PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
        System.setOut(out);
        
        

		for (String str : args)
            System.out.println("Graph: " + str);
		
		System.out.println();

        Scanner input = new Scanner(new File(args[0]));
        
        
        /* Build an adjacency list */
        vertices = new HashMap<>();
        i = 0;
        
        while (input.hasNextFloat())
        {
        	list = new LinkedList<>();
        	list.add(input.nextDouble());
        	list.add(input.nextDouble());
        	vertices.put(i++, list);
        }
        
        
        
        /* Polygon Decomposition */
        n = vertices.size();
        

        s = new Integer[n][n];
        
        min = PolygonTriangulation(vertices, s, n);
        
        System.out.println("Minimal sum of triangle perimeters = " + String.format("%.4f", min));
        
        System.out.println((vertices.size()-3) + " chords are:");
        PrintTriangulation(1, n-1, s);
        
        System.out.println("Check: twice sum(chords) + poly perimeter = " + String.format("%.4f", check(vertices)));
        
        input.close ();

	}
	
	public static Double PolygonTriangulation (Map<Integer, List<Double>> vertices, Integer s[][], int n)
	{
		Double m[][];
		Double q;
		int j;

		m = new Double[n][n];
		

		
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y++) {
				m[x][y] = Double.MAX_VALUE;
			}
		}
		
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y++) {
				s[x][y] = 0;
			}
		}
		
		
		for (int d = 0; d < n; d++) {
			for (int i = 0; i < n-d; i++) {
				j = i + d;
				if (j < i+2) {
					m[i][j] = 0.0;
				}
				else {
					for (int k = i+1; k < j; k++) {
						q = m[i][k] + m[k][j] + perimeter(vertices, i, k, j);
						if (q < m[i][j]) {
							m[i][j] = q;
							s[i][j] = k;
						}
					}
				}
			}
		
		}
		
		
		

		return m[0][n-1];
	}
	
	
	
	public static Double perimeter (Map<Integer, List<Double>> vertices, int i, int k, int j)
	{
		Double ij, ik, jk;
		
		ij = distance(i, j, vertices);
		ik = distance(i, k, vertices);;
		jk = distance(j, k, vertices);;
		
		return (ij + ik + jk);
	}
	
	public static void PrintTriangulation (int i, int j, Integer [][]s)
	{
		LinkedList<Integer> list;
		
		if (j > i+1) {
			System.out.println(i + " " + j);
			
			list = new LinkedList<>();
			list.add(i);
			list.add(j);
			chords.push(list);
			
			
			PrintTriangulation(i, s[i][j], s);
			PrintTriangulation(s[i][j], j, s);
		}
	}
	
	public static Double distance (int x, int y, Map<Integer, List<Double>> vertices)
	{
		return Math.sqrt(Math.pow(vertices.get(x).get(0) - vertices.get(y).get(0), 2) + Math.pow(vertices.get(x).get(1) - vertices.get(y).get(1), 2));
	}
	
	
	public static Double check (Map<Integer, List<Double>> vertices)
	{
		LinkedList<Integer> list;
		
		for (int i = 0; i < vertices.size()-1; i++) {
			total += distance(i, i+1, vertices);
		}
		total += distance(vertices.size()-1, 0, vertices);
		
		while (!chords.isEmpty()) {
			list = chords.pop();
			total += 2 * distance(list.get(0), list.get(1), vertices);
		}
		
		return total;
	}

}


