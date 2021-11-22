import java.util.Iterator;
import java.util.LinkedList;

public class Path {
	public LinkedList<Edge> edgeList;
	public float cost;
	public Vertex source;
	public Vertex destination;

	public Path() {
		edgeList = new LinkedList<Edge>();
		cost=0;
		source=null;
		destination=null;
	}
	
	public void addFirst(Edge e) {
		edgeList.addFirst(e);
		cost+=e.weight;
	}
	
	// Make a copy of the current Path and return it
	public Path copy() {
		Path pathCopy = new Path();
		pathCopy.cost = cost;
		pathCopy.source = source;
		pathCopy.destination = destination;
		Iterator<Edge> i = edgeList.iterator();
		while (i.hasNext()) {
			pathCopy.edgeList.add(i.next());
		}
		return pathCopy;
	}
	
	// Count the number of hops in the Path
	public int hopCount() {
		return edgeList.size();
	}
	
	public String toString() {
		String s="path from "+source+" to "+destination+" (cost "+cost	+", hops "+hopCount()+"):";
		Iterator<Edge> i = edgeList.iterator();
		while (i.hasNext()) {
			s+=" "+i.next();
		}
		return s;
	}

}
