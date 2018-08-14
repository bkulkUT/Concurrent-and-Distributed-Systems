/* Names: Bharat Kulkarni
 * 		  Chethan Valleru
 * EIDs:  bsk524
 * 		  cv7999
 */


import java.util.concurrent.locks.*;

public class Node {
	ReentrantLock myLock = new ReentrantLock();
	String name;
	int priority;
	Node next;
	Node prev;
	
	public Node() {
		this.name = null;
		this.next = null;
		this.prev = null;
	}
	
	public Node(String name, int priority) {
		this.name = name;
		this.priority = priority;
	}
}