
public class PriorityQueue {
	private Edge[] data;
	private int head;
	private int size;
	
	public PriorityQueue() {
		size = 2;
		data = new Edge[size];
		head = -1;
	}
	
	private void increaseSize() {
		Edge[] newData = new Edge[size*2];
		
		for (int i=0; i<size; i++) {
			newData[i]=data[i];
		}
		data = newData;
		size = size*2;
	}
	
	public void enqueue(Edge item) {
		int i = head;
		if (head==data.length-1) {
			increaseSize();
		}
		while (i>=0 && item.weight > data[i].weight) {
			data[i+1]=data[i];
			i--;
		}
		data[i+1] = item;
		head++;
	}
	
	public Edge dequeue() {
		return data[head--];
	}
	
	public boolean isEmpty() {
		return (head==-1);
	}
	
	public Edge peek() {
		return data[head];
	}
	
	public String toString() {
		String out;
		
		out = "(";
		for (int i=head; i>=0; i--) {
			if (i<head) {
				out+=",";
			}
			out+=data[i];
		}
		out+=")";
		return out;
	}
}
