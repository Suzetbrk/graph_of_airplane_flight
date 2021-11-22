import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Project {

	public static Graph generateGraph(String fileName) {
		Graph g = new Graph();
		String line;
		try {
			File input=new File(fileName);
			FileReader file_reader=new FileReader(input);
			BufferedReader buf_reader=new BufferedReader(file_reader);
			while ((line=buf_reader.readLine())!=null) {
				String[] data=line.split(",");

				// Search the graph to determine is the vertices are present
				Vertex v1=g.findVertexByName(data[0].toUpperCase());
				Vertex v2=g.findVertexByName(data[1].toUpperCase());
				
				// Create vertices if they are not present
				if (v1==null) {
					v1=new Vertex(data[0].toUpperCase());
					g.addVertex(v1);
				}
				if (v2==null) {
					v2=new Vertex(data[1].toUpperCase());
					g.addVertex(v2);
				}

				// Read the distance between the airports (in km)
				float weight=Float.parseFloat(data[2]);

				// Create two edges between the airports. One represents
				// a flight between v1 and v2 and the other represents a flight between
				// v2 and v1
				g.addEdge(v1,v2,weight);				
			}
			buf_reader.close();
		}
		catch(IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return g;
	}

	public static void main(String[] args) {
		Graph g=generateGraph("distances.txt");
				
		String source = "BDL";
		String destination = "STR";
		
		System.out.println("shortest path from "+source+" to "+destination+":");		
		Graph sp=g.shortestPath(g.findVertexByName("BDL"));
		Path p=sp.getPath(source, destination);
		System.out.println(p);

		System.out.println("\npath from "+source+" to "+destination+" on minimum spanning tree:");		
		Graph mst=g.minimumSpanningTree();
		p=mst.getPath(source, destination);
		System.out.println(p);
		
		System.out.println("\nshortest path with maximum hop count form "+source);
		p=sp.getPathMaxHops(source);
		System.out.println(p);
		
		System.out.println("\nshortest path with maximum hop count in entire graph:");		
		Path maxPath=p; // initialize to something;
		for (int i=0; i<g.activeVertices; i++) {
			p=g.shortestPath(g.vertices[i]).getPathMaxHops(g.vertices[i].name);
			if (p.hopCount()>maxPath.hopCount()) {
				maxPath = p;
			}
		}
		System.out.println(maxPath);
	}

}
