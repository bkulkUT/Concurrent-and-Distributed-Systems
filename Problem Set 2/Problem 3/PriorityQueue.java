/* Names: Bharat Kulkarni
 * 		  Chethan Valleru
 * EIDs:  bsk524
 * 		  cv7999
 */

import java.util.concurrent.locks.*;

public class PriorityQueue {
	private LinkedList list;
	final ReentrantLock createLock = new ReentrantLock();
	final ReentrantLock operationLock = new ReentrantLock();
	final Condition isFull = operationLock.newCondition();
	final Condition isEmpty = operationLock.newCondition();
	
	// Creates a Priority queue with maximum allowed size as capacity
	public PriorityQueue(int capacity) {
		createLock.lock();
		try {
			list = new LinkedList(capacity);
		} finally {
			createLock.unlock();
		}
	}
	
	
	/*
	 * Adds the name with its priority to this queue.
	 * Returns the current position in the list where the name was inserted;
	 * otherwise, returns -1 if the name is already present in the list.
	 * This method blocks when the list is full
	 */
	
	public int add(String name, int priority) throws InterruptedException {
		int position = 0;
		
		operationLock.lock();
		while((list.getAmountFilled() == list.getCapacity()) || ((list.getAdders() + list.getAmountFilled()) >= list.getCapacity())) {
			isFull.await();
		}
		list.incrementAdders();
		operationLock.unlock();
		
		position = list.addNode(name, priority);
		
		operationLock.lock();
		list.decrementAdders();
		isEmpty.signal();
		operationLock.unlock();
		
		return position;
	}
	
	
	/*
	 * Returns the position of the name in the list;
	 * otherwise, returns -1 if the name is not found.
	 */
	public int search(String name) {
		int position = -1;
		position = list.findName(name);
		
		return position;
	}
	
	
	/*
	 * Retrieves and removes the name with the highest priority in the list,
	 * or blocks the thread if the list is empty.
	 */
	public String getFirst() throws InterruptedException {
		String name;
		
		operationLock.lock();
		while(list.getAmountFilled() == 0) {
			isEmpty.await();
		}
		operationLock.unlock();
		
		name = list.getFirstName();
		
		operationLock.lock();
		isFull.signal();
		operationLock.unlock();
		
		return name;
	}
}









