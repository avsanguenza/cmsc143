package graphtheory;

import java.awt.Color;
import java.util.Date;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

class graphcolor{
    public static Vector<Vertex> vertexList;
    private static Vector<Edge> edgeList;
    private static Vector<Vertex> visitedNodes = new Vector<Vertex>();
	public static void main(String[] args) {
		Vertex v0 = new Vertex("0",0,5);
		Vertex v1 = new Vertex("1",0,1);
		Vertex v2 = new Vertex("2",3,3);
		Vertex v3 = new Vertex("3",6,3);
		
		
		Edge e0 = new Edge(v0,v1,1);
		Edge e1 = new Edge(v0,v2,1);
		Edge e2 = new Edge(v1,v2,1);
		Edge e3 = new Edge(v2,v3,1);
		v0.addVertex(v1);
		v1.addVertex(v0);
		
		v0.addVertex(v2);
		v2.addVertex(v0);
		
		v1.addVertex(v2);
		v2.addVertex(v1);
		
		v2.addVertex(v3);
		v3.addVertex(v2);
		
	
		
		//coloring graph
		Hashtable<Vertex, Integer>cgraph = colorGraph(v0,v3);
		Set<Vertex> key = cgraph.keySet();
		System.out.println("Vertex - Color");
		for(Vertex v : key) {
			System.out.println(v.name+"="+cgraph.get(v));
		}
	}
	
	/**
	 * Graph coloring using BFS.
	 * @param vertex1 - Initial node
	 * @param vertex2 - Terminal Node
	 * @return
	 */
	  public static Hashtable<Vertex,Integer> colorGraph(Vertex vertex1, Vertex vertex2) {
		 Hashtable<Vertex,Integer> cgraph = new Hashtable<Vertex,Integer>();
	    	visitedNodes.add(vertex1);
	    	int label = 0;
	    	int count=0;
	    	cgraph.put(vertex1,label);
	    	while(!visitedNodes.contains(vertex2)) {
	    		int workingSize = visitedNodes.size();
	    		for(int i=count;i<workingSize;i++) {
	    			//retrieve adjacent vertices
	    			for(Vertex x: visitedNodes.get(i).connectedVertices) {
	    				//if not yet visited
	    				if(!visitedNodes.contains(x)) {
	    					visitedNodes.add(x);
			//color visited node
	    					label++;
	    					cgraph.put(x,label);
	    				}
	    			}
	    		}
	    		label=0;
	    		count++;
	    	}
	    	return cgraph;
	    }
}
