import java.util.Iterator;


public class Graph {
	int size;
	Vertex[] vertices; // array of nodes
	int activeVertices;

	// Initially create a graph with two vertices. The graph 
	// size is dynamically enlarged as needed
	public Graph() {
		size = 2;
		vertices = new Vertex[size];
		activeVertices = 0;
	}
	
	// Double the storage size for Vertices
	private void increaseSize() {
		Vertex[] newVertices = new Vertex[size*2];
		for (int i=0; i<activeVertices; i++) {
			newVertices[i]=vertices[i];
		}
		vertices=newVertices;
		size=size*2;
	}
	
	public void addVertex(Vertex v) {
		if (activeVertices >= size) {
			increaseSize();
		}
		vertices[activeVertices] = v; // add vertex to list of vertices
		v.graphIndex = activeVertices; // record index of vertex in graph
		activeVertices++;  // increment vertex count
	}
	
	// Graph is undirected so edges both to and from edges
	public void addEdge(Vertex v1, Vertex v2, float w) {
		v1.addEdge(new Edge(v1,v2,w));
		v2.addEdge(new Edge(v2,v1,w));
	}
	
	public Vertex findVertexByName(String s) {
		for (int i=0; i<activeVertices; i++) {
			if (vertices[i].name.equals(s)) {
				return vertices[i];
			}
		}
		return null;
	}
	
	// Use Kruskal's method to build an MST
	public Graph minimumSpanningTree() {
		Graph mst = new Graph(); // create new graph
		int[] set = new int[activeVertices];
		for (int i=0; i<activeVertices; i++) { // copy nodes to graph
			mst.addVertex(new Vertex(vertices[i].name));
			set[i]=i; // assign each node to its own set
		}
		PriorityQueue q = new PriorityQueue(); // create priority queue
		for (int i=0; i<activeVertices; i++) { // copy edges to queue
			Iterator<Edge> j = vertices[i].edgeList.iterator();
			while (j.hasNext()) {
				q.enqueue(j.next());
			}
		}
		
		while (!q.isEmpty()) { // iterate over all edges in priority order
			Edge e = q.dequeue(); // consider next edge
			if (set[e.source.graphIndex]!=set[e.destination.graphIndex]) { // skip edges not connecting different sets
				mst.addEdge(mst.vertices[e.source.graphIndex], mst.vertices[e.destination.graphIndex], e.weight); // add edge to MST
				int setToMerge=set[e.destination.graphIndex]; // rename nodes from "other" set
				for (int i=0; i<activeVertices; i++) {
					if (set[i]==setToMerge) { // find nodes from "other" set
						set[i]=set[e.source.graphIndex]; // reassign nodes
					}
				}
			}
		}
		return mst;
	}
	
	public Graph shortestPath(Vertex start) {
		Graph sp = new Graph();
		float[] distance = new float[activeVertices];
		final float infinity = Float.MAX_VALUE; // define "infinity"
		boolean[] inSet = new boolean[activeVertices];
		Vertex[] predecessor = new Vertex[activeVertices];
		for (int i=0; i<activeVertices; i++) { // copy vertices to graph
			sp.addVertex(new Vertex(vertices[i].name));
			distance[i] = infinity;
			inSet[i] = false;
			predecessor[i] = null;
		}
		distance[start.graphIndex]=0; // initialize starting vertex
		inSet[start.graphIndex]=true; // make part of set
		Vertex min=start; // min tracks last added node
		for (int i=1; i<activeVertices; i++) { // run once for every non-start vertex
			Iterator<Edge> j = min.edgeList.iterator();
			while (j.hasNext()) { // update costs of min's neighbors
				Edge e = j.next();
				if (!inSet[e.destination.graphIndex]) { // if edge to non-set vertex exists
					if (distance[min.graphIndex]+e.weight<distance[e.destination.graphIndex]) { // check if path via min is shorter
						distance[e.destination.graphIndex]=distance[min.graphIndex]+e.weight; // if so, update distance
						predecessor[e.destination.graphIndex]=min; // and predecessor
					}
				}
			}
			min=null;
			for (int k=0; k<activeVertices; k++) { // find non-set vertex with least distance
				if (!inSet[k] && (min==null || (min!=null && distance[k]<distance[min.graphIndex]))) {
					min=vertices[k];
				}
			}
			if (distance[min.graphIndex]<infinity) {
				j = predecessor[min.graphIndex].edgeList.iterator(); // find edge between min's predecessor and min
				Edge e = null;
				while (j.hasNext()) { // update costs of min's neighbors
					e=j.next();
					if (e.destination==min) {
						break;
					}
				}
				sp.addEdge(sp.vertices[predecessor[min.graphIndex].graphIndex], sp.vertices[min.graphIndex], e.weight); // add edge with same weight as in original graph
				inSet[min.graphIndex]=true; // mark vertex as part of set
			} else { // vertex with infinite distance
				System.out.println ("disconnected component (skipping): "+min);
				inSet[min.graphIndex]=true;
			}
		}
		return sp;
	}
	
	// Recursively search the tree to find if the destination vertex 
	// is present. Update the path to the visited vertex
	private boolean treeSearch(Vertex current, Vertex dest, Path path, boolean[] visited) {
		if (current==null || dest==null || path==null) {
			return false;
		} else if (current==dest) {
			return true;
		} else {
			Iterator<Edge> i=current.edgeList.iterator();
			while (i.hasNext()) {
				Edge e=i.next();
				if (!visited[e.destination.graphIndex]) {
					visited[e.destination.graphIndex]=true;
					if (treeSearch(e.destination, dest, path, visited)) {
						path.addFirst(e);
						return true;
					}
				}
			}
			return false;
		}
	}
	
	// Locate a Path in the graph between two named Vertices
	public Path getPath(String srcName, String destName) {
		Path path = new Path();
		path.source=findVertexByName(srcName);
		path.destination=findVertexByName(destName);
		boolean[] visited = new boolean[activeVertices];
		visited[path.source.graphIndex]=true;
		treeSearch(path.source, path.destination, path, visited);
		return path;
	}

	// Find the Path from the current Vertex which has the most hops
	// to a destination
	private Path treeSearchMaxHops(Vertex current, boolean[] visited) {
		if (current==null) {
			return null;
		} else {
			Iterator<Edge> i=current.edgeList.iterator();
			Path maxPath = new Path();
			maxPath.destination = current;
			while (i.hasNext()) {
				Edge e=i.next();
				Path path;
				if (!visited[e.destination.graphIndex]) {
					visited[e.destination.graphIndex]=true;
					path=treeSearchMaxHops(e.destination, visited);
					path.addFirst(e);
					// Keep track of which Path is the longest in 
					// terms of hop count
					if (path.hopCount()>maxPath.hopCount()) {
						maxPath = path;
					}
				}
			}
			return maxPath;
		}
	}
	
	// Driver for determining the Path with the maximum number of 
	// hops. The method finds the path and sets its source vertex
	public Path getPathMaxHops(String srcName) {
		boolean[] visited = new boolean[activeVertices];
		Vertex start = findVertexByName(srcName);
		visited[start.graphIndex]=true;
		Path path = treeSearchMaxHops(start, visited);
		path.source=start;
		return path;		
	}
	
	public void print() {
		System.out.println("Graph with "+activeVertices+" nodes");
	}

	public void printVerbose() {
		for (int i=0; i<activeVertices; i++) {
			System.out.print(vertices[i].name);
			Iterator<Edge> j = vertices[i].edgeList.iterator();
			while (j.hasNext()) {
				Edge e = j.next();
				System.out.print(" ("+e.destination+","+e.weight+")");
			}
			System.out.println();
		}
	}
}
