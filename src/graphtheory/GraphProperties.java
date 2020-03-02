/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphColoring;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author mk
 */
public class GraphProperties {

    private int[][] adjacencyMatrix;
    private int[][] distanceMatrix;
    private int[] degreeCentrality;
    private float[] closenessCentrality;
    public Vector<VertexPair> vpList;
    private Vector<Vertex> vertexList;
    private Vector<Edge> edgeList;
    public int[] degreeCentrality(Vector<Vertex> vList, Vector<Edge> eList){
        Vector<Integer> degreeCentralityIndex = new Vector<Integer>();
        int[] adjacentCount = new int[vList.size()];
        for (int i = 0; i < adjacentCount.length; i++){
            adjacentCount[i] = 0;
        }
        for (int i = 0; i < eList.size(); i++){
            adjacentCount[vList.indexOf(eList.get(i).vertex1)]++;
            adjacentCount[vList.indexOf(eList.get(i).vertex2)]++;
        }


        int max = adjacentCount[0];
        degreeCentralityIndex.add(0);
        for (int i = 1; i < adjacentCount.length; i++){
            if (max < adjacentCount[i]){
                degreeCentralityIndex = new Vector<Integer>();
                degreeCentralityIndex.add(i);
            } else if (max == adjacentCount[i]) {
                degreeCentralityIndex.add(i);
            }
        }
        int[] result = new int[degreeCentralityIndex.size()];
        for (int i = 0; i < degreeCentralityIndex.size(); i++){
            result[i] = degreeCentralityIndex.get(i);
        }

        return result;
    }
    
    //closeness centrality 
    //Average shortest path between a node and all other nodes

    public float[] closenessCentrality(int[][]distanceMatrix) {
    	float[]result = new float[distanceMatrix.length];
    		for(int i=0;i<distanceMatrix.length;i++) {
    			float temp=0;
    			for(int j=0;j<distanceMatrix.length;j++) {
    				temp+=distanceMatrix[i][j];
    			}
    			result[i] = 1/(temp/(distanceMatrix.length-1));
    		}
    	return result;
    }
    
    //get centrality of one node
    //from j to k, what is the centrality of node i (is node i involved in the shortest paths)
    public float getBetweennessCentrality(Vector<Vertex>vList,Vertex v) {
    	float ans=0;
    	for(int i=vList.size()-1;i>=1;i--) {
    		ans +=betweennessCentrality(vList.get(0),vList.get(i),v);
    	}
    	return ans;
    }
    private float betweennessCentrality(Vertex j,Vertex k, Vertex i) {
        VertexPair vp;
		int count=0;
		float ans = 0;
    	vp = new VertexPair(j,k);
		int numShortPath = vp.getShortestDistance();
    	if(numShortPath> 0) {
    		Vector<Vertex>path = vp.getPaths();
			ArrayList<Vertex>vPath = new ArrayList<>(path.subList(1, path.size()-1));
			for(int a =0; a<vPath.size();a++) {
				if(vPath.contains(i)) {
					count++;
				}
			}
    	}
    	ans = (float)count/(float)numShortPath;
    	return ans;
    }
    public int[][] generateAdjacencyMatrix(Vector<Vertex> vList, Vector<Edge> eList) {
        adjacencyMatrix = new int[vList.size()][vList.size()];

        for (int a = 0; a < vList.size(); a++)//initialize
        {
            for (int b = 0; b < vList.size(); b++) {
                adjacencyMatrix[a][b] = 0;
            }
        }

        for (int i = 0; i < eList.size(); i++) {
            adjacencyMatrix[vList.indexOf(eList.get(i).vertex1)][vList.indexOf(eList.get(i).vertex2)] = eList.get(i).weight;
            adjacencyMatrix[vList.indexOf(eList.get(i).vertex2)][vList.indexOf(eList.get(i).vertex1)] = eList.get(i).weight;
        }

        return adjacencyMatrix;
    }

    public int[] generateDegreeCentrality(int[][] adjacencyMatrix)
    {
    	degreeCentrality = new int[adjacencyMatrix.length];
    	for(int a = 0; a < adjacencyMatrix.length; a++)
    	{
    		int degreeCount = 0;
    		for (int b = 0; b < adjacencyMatrix.length; b++)
    		{
    			if(adjacencyMatrix[a][b] == 1)
    			{
    				degreeCount++;
    			}
    		}
    		degreeCentrality[a] = degreeCount;
    	}
		return degreeCentrality;
    	
    }
    
    public int[][] generateDistanceMatrix(Vector<Vertex> vList) {
      /*  distanceMatrix = new int[vList.size()][vList.size()];

        for (int a = 0; a < vList.size(); a++)//initialize
        {
            for (int b = 0; b < vList.size(); b++) {
                distanceMatrix[a][b] = 0;
            }
        }*/
        distanceMatrix = initializeDistanceMatrix(vList.size());
        VertexPair vp;
        int shortestDistance;
        for (int i = 0; i < vList.size(); i++) {
            for (int j = i + 1; j < vList.size(); j++) {
                vp = new VertexPair(vList.get(i), vList.get(j));
                shortestDistance = vp.getShortestDistance();
                distanceMatrix[vList.indexOf(vp.vertex1)][vList.indexOf(vp.vertex2)] = shortestDistance;
                distanceMatrix[vList.indexOf(vp.vertex2)][vList.indexOf(vp.vertex1)] = shortestDistance;
            }
        }
        return distanceMatrix;
    }

    
    //closeness centrality: Average shortest path between a node and all other nodes
    public float[] generateClosenessCentrality(int[][]distanceMatrix) {
    	closenessCentrality = new float[distanceMatrix.length];
    		for(int i=0;i<distanceMatrix.length;i++) {
    			float temp=0;
    			for(int j=0;j<distanceMatrix.length;j++) {
    				temp+=distanceMatrix[i][j];
    			}
    			closenessCentrality[i] = 1/(temp/(distanceMatrix.length-1));
    		}
    	return closenessCentrality;
    }
    
    public void displayContainers(Vector<Vertex> vList) {
        vpList = new Vector<VertexPair>();
        int[] kWideGraph = new int[10];
        for (int i = 0; i < kWideGraph.length; i++) {
            kWideGraph[i] = -1;
        }

        VertexPair vp;

        for (int a = 0; a < vList.size(); a++) {    // assign vertex pairs
            for (int b = a + 1; b < vList.size(); b++) {
                vp = new VertexPair(vList.get(a), vList.get(b));
                vpList.add(vp);
                int longestWidth = 0;
                System.out.println(">Vertex Pair " + vList.get(a).name + "-" + vList.get(b).name + "\n All Paths:");
                vp.generateVertexDisjointPaths();
                for (int i = 0; i < vp.VertexDisjointContainer.size(); i++) {//for every container of the vertex pair
                    int width = vp.VertexDisjointContainer.get(i).size();
                    Collections.sort(vp.VertexDisjointContainer.get(i), new descendingWidthComparator());
                    int longestLength = vp.VertexDisjointContainer.get(i).firstElement().size();
                    longestWidth = Math.max(longestWidth, width);
                    System.out.println("\tContainer " + i + " - " + "Width=" + width + " - Length=" + longestLength);

                    for (int j = 0; j < vp.VertexDisjointContainer.get(i).size(); j++) //for every path in the container
                    {
                        System.out.print("\t\tPath " + j + "\n\t\t\t");
                        for (int k = 0; k < vp.VertexDisjointContainer.get(i).get(j).size(); k++) {
                            System.out.print("-" + vp.VertexDisjointContainer.get(i).get(j).get(k).name);
                        }
                        System.out.println();
                    }

                }
                //d-wide for vertexPair
                for (int k = 1; k <= longestWidth; k++) { // 1-wide, 2-wide, 3-wide...
                    int minLength = 999;
                    for (int m = 0; m < vp.VertexDisjointContainer.size(); m++) // for each container with k-wide select shortest length
                    {
                        minLength = Math.min(minLength, vp.VertexDisjointContainer.get(m).size());
                    }
                    if (minLength != 999) {
                        System.out.println(k + "-wide for vertexpair(" + vp.vertex1.name + "-" + vp.vertex2.name + ")=" + minLength);
                        kWideGraph[k] = Math.max(kWideGraph[k], minLength);
                    }
                }
            }
        }

        for (int i = 0; i < kWideGraph.length; i++) {
            if (kWideGraph[i] != -1) {
                System.out.println("D" + i + "(G)=" + kWideGraph[i]);
            }
        }


    }
    
    public void drawAdjacencyMatrix(Graphics g, Vector<Vertex> vList, int x, int y) {
        int cSize = 20;
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y-30, vList.size() * cSize+cSize, vList.size() * cSize+cSize);
        g.setColor(Color.black);
        g.drawString("AdjacencyMatrix", x, y - cSize);
        for (int i = 0; i < vList.size(); i++) 
        {
            g.setColor(Color.RED);
            g.drawString(vList.get(i).name, x + cSize + i * cSize, y);
            g.drawString(vList.get(i).name, x, cSize + i * cSize + y);
            g.setColor(Color.black);
            for (int j = 0; j < vList.size(); j++)
            {
                g.drawString("" + adjacencyMatrix[i][j], x + cSize * (j + 1), y + cSize * (i + 1));
            }
        }
    }

    public void drawDegreeCentrality(Graphics g, int[] degreeCentrality, int x, int y) {
        int cSize = 20;
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y-30, degreeCentrality.length * cSize+cSize, degreeCentrality.length * cSize+cSize);
        g.setColor(Color.black);
        g.drawString("DegreeCentrality", x, y - cSize);
        for (int i = 0; i < degreeCentrality.length; i++) {
            g.setColor(Color.RED);
            g.drawString(Integer.toString(i), x + cSize + i * cSize, y);
            g.setColor(Color.black);
            g.drawString("" + degreeCentrality[i], x + cSize * (i + 1), y + cSize );
        }
    }
    
    public void drawDistanceMatrix(Graphics g, Vector<Vertex> vList, int x, int y) {
        int cSize = 20;
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y-30, vList.size() * cSize+cSize, vList.size() * cSize+cSize);
        g.setColor(Color.black);
        g.drawString("ShortestPathMatrix", x, y - cSize);
        for (int i = 0; i < vList.size(); i++) {
            g.setColor(Color.RED);
            g.drawString(vList.get(i).name, x + cSize + i * cSize, y);
            g.drawString(vList.get(i).name, x, cSize + i * cSize + y);
            g.setColor(Color.black);
            for (int j = 0; j < vList.size(); j++) {
                g.drawString("" + distanceMatrix[i][j], x + cSize * (j + 1), y + cSize * (i + 1));
            }
        }
    }
    
    public void drawClosenessCentrality(Graphics g, float[] closenessCentrality, int x, int y) {
        int cSize = 20;
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y-30, closenessCentrality.length * cSize+cSize, closenessCentrality.length * cSize+cSize);
        g.setColor(Color.black);
        g.drawString("ClosenessCentrality", x, y - cSize);
        for (int i = 0; i < closenessCentrality.length; i++) {
            g.setColor(Color.RED);
            g.drawString(Integer.toString(i), x, i * cSize + y);
            g.setColor(Color.black);
            g.drawString("" + closenessCentrality[i], x + cSize, cSize * (i) + y);
        }
    }

    public void drawBetweennessCentrality(Graphics g, Vector<Vertex> vList, int x, int y){
        int cSize = 20;
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y-30, closenessCentrality.length * cSize+cSize, closenessCentrality.length * cSize+cSize);
        g.setColor(Color.black);
        g.drawString("BetweennessCentrality", x, y - cSize);
        for (int i = 0; i < vList.size(); i++){
            g.setColor(Color.RED);
            g.drawString(Integer.toString(i), x, i * cSize + y);
            g.setColor(Color.black);
            g.drawString("" + getBetweennessCentrality(vList, vList.get(i)), x + cSize, cSize * (i) + y);
        }
    }

    public Vector<Vertex> vertexConnectivity(Vector<Vertex> vList) {
        Vector<Vertex> origList = new Vector<Vertex>();
        Vector<Vertex> tempList = new Vector<Vertex>();
        Vector<Vertex> toBeRemoved = new Vector<Vertex>();
        Vertex victim;


        origList.setSize(vList.size());
        Collections.copy(origList, vList);

        int maxPossibleRemove = 0;
        while (graphConnectivity(origList)) {
            Collections.sort(origList, new ascendingDegreeComparator());
            maxPossibleRemove = origList.firstElement().getDegree();

            for (Vertex v : origList) {
                if (v.getDegree() == maxPossibleRemove) {
                    for (Vertex z : v.connectedVertices) {
                        if (!tempList.contains(z)) {
                            tempList.add(z);
                        }
                    }
                }
            }

            while (graphConnectivity(origList) && tempList.size() > 0) {
                Collections.sort(tempList, new descendingDegreeComparator());
                victim = tempList.firstElement();
                tempList.removeElementAt(0);
                origList.remove(victim);
                for (Vertex x : origList) {
                    x.connectedVertices.remove(victim);
                }
                toBeRemoved.add(victim);
            }
            tempList.removeAllElements();
        }

        return toBeRemoved;
    }

    public int[][]initializeDistanceMatrix(int v){
    	  distanceMatrix = new int[v][v];

          for (int a = 0; a < v; a++)//initialize
          {
              for (int b = 0; b < v; b++) {
                  distanceMatrix[a][b] = 0;
              }
          }
          return distanceMatrix;
    }
    
    private boolean graphConnectivity(Vector<Vertex> vList) {

        Vector<Vertex> visitedList = new Vector<Vertex>();

        recurseGraphConnectivity(vList.firstElement().connectedVertices, visitedList); //recursive function
        if (visitedList.size() != vList.size()) {
            return false;
        } else {
            return true;
        }
    }

    private void recurseGraphConnectivity(Vector<Vertex> vList, Vector<Vertex> visitedList) {
        for (Vertex v : vList) {
            {
                if (!visitedList.contains(v)) {
                    visitedList.add(v);
                    recurseGraphConnectivity(v.connectedVertices, visitedList);
                }
            }
        }
    }

    private class ascendingDegreeComparator implements Comparator {

        public int compare(Object v1, Object v2) {

            if (((Vertex) v1).getDegree() > ((Vertex) v2).getDegree()) {
                return 1;
            } else if (((Vertex) v1).getDegree() > ((Vertex) v2).getDegree()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private class descendingDegreeComparator implements Comparator {

        public int compare(Object v1, Object v2) {

            if (((Vertex) v1).getDegree() > ((Vertex) v2).getDegree()) {
                return -1;
            } else if (((Vertex) v1).getDegree() > ((Vertex) v2).getDegree()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private class descendingWidthComparator implements Comparator {

        public int compare(Object v1, Object v2) {

            if (((Vector<Vertex>) v1).size() > (((Vector<Vertex>) v2).size())) {
                return -1;
            } else if (((Vector<Vertex>) v1).size() < (((Vector<Vertex>) v2).size())) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
