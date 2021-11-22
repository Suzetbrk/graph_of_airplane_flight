In this project we will build a graph of all the airplane flight routes currently used in the world.
Using this graph, we  will be able to determine information about flight paths from source airports
to destination airports. We need to determine
1. The shortest path (in terms of distance) between any two airports
2. The shortest path between the same airports along the minimum spanning tree (which is
much longer)
3. The shortest path from one airport to any other airport such that hop count is maximized.
This traversal should be made using the original graph, not the MST
4. The shortest path between any two airports in the original graph such that hop count is
maximized
The database information is stored in the file distances.txt. This data was generated
from the website: http://openflights.org/data.html and it includes all current flight routes in the
world. Each line in the file has the following format:

BDL,ORD,1256.984732

The two three-letter codes represent airports (e.g. BDL = Bradley International in
Hartford/Springfield and ORD = Chicago – O’Hare). The floating point number represents the
distance between the airports in kilometers. For tasks which involve specific airports (e.g. Task 1
above) the user should be prompted to input the 2 three-letter airport codes. Task 2 can use the
same airports as Task 1 and Task 3 can use the first airport entered for Task 1. We will
build an undirected graph. For example, if there is a flight from BDL to ORD, it is assumed that
there is also a flight from ORD to BDL of the same distance.
Instead of using an adjacency matrix to represent connections between vertices, we use an adjacency list. 