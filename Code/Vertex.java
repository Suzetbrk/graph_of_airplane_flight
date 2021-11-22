import java.util.LinkedList;

public class Vertex {
		public String name;
		public int graphIndex; // index of adjacency matrix position of node in graph
		LinkedList<Edge> edgeList;
		
		
		public Vertex (String s) {
			name = s;
			graphIndex = -1; // invalid position by default 
			edgeList = new LinkedList<Edge>();
		}
		
		public void addEdge(Edge e) {
			edgeList.add(e);
		}

		public String toString() {
			return name;
		}

}
