
public class Edge {
	public Vertex source;
	public Vertex destination;
	public float weight;
	
	public Edge(Vertex src, Vertex dest, float w) {
		source = src;
		destination = dest;
		weight = w;
	}
	
	public String toString() {
		return "("+source+","+destination+","+weight+")";
	}
}
