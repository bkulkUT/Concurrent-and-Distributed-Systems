package hw2;

public class Main {
	public static void main(String[] args) throws InterruptedException {
	PriorityQueue myQ = new PriorityQueue(9);
	Thread1 t1 = new Thread1(myQ);
	Thread2 t2 = new Thread2(myQ);
	Thread3 t3 = new Thread3(myQ);
	
	
	t1.start();
	t2.start();
	t3.start();

	t1.join();
	t2.join();
	t3.join();

		
	}
}









