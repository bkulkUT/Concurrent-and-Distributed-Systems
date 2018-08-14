package hw2;

public class Thread2 extends Thread {
	PriorityQueue q;
	
	public Thread2(PriorityQueue q) {
		this.q = q;
	}
	
	public void run() {
		try {
			q.add("HELLO", 2);
			q.add("WORLD!", 0);
			q.add("Concurrent", 4);
			q.add("Distributed!", 6);
//			q.add("Random", 2);
//			q.add("Words", 0);
//			q.add("Happy", 2);
//			q.add("Grumpy", 3);
//			q.add("Funny", 4);
//			q.add("Lumpy", 9);
			q.add("Stumpy", 5);
			q.add("My", 9);

			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}