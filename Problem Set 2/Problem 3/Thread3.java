package hw2;

public class Thread3 extends Thread {
	PriorityQueue q;
	
	public Thread3(PriorityQueue q) {
		this.q = q;
	}
	
	public void run() {
		try {
			q.add("My", 0);
			q.add("name is", 8);
			q.add("Bharat", 1);


		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}