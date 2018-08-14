/* Names: Bharat Kulkarni
 * 		  Chethan Valleru
 * EIDs:  bsk524
 * 		  cv7999
 */


import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.*;

public class LinkedList {
	private ReentrantLock addLock = new ReentrantLock();
	private Node header;
	private int listCapacity;
	private int listFilled;
	private int amountOfAdders;
	private Set<String> allNames;
	
	public LinkedList(int size) {
		addLock.lock();
		header = null;
		amountOfAdders = 0;
		listFilled = 0;
		listCapacity = size;
		allNames = new HashSet<String>();
		addLock.unlock();
	}
	
	public int addNode(String name, int priority) {
		int insertPosition = 0;
		
		addLock.lock();
		
		if(allNames.contains(name)) {
			addLock.unlock();
			insertPosition = -1;
			
			return insertPosition;
		}
		allNames.add(name);
		
		Node temp = header;
		if(temp ==  null) {
			incrementListFilled();
			Node newNode = new Node(name, priority);
			newNode.next = null;
			newNode.prev = null;
			decrementAdders();
			header = newNode;
			addLock.unlock();
			
			return insertPosition;
		}
		addLock.unlock();

		
		while(true) {
			
			/*
			 * CASE 1: The queue contains only one node.
			 * 
			 * If (my priority) > (existing node's priority)
			 * Then update list.
			 */
			temp.myLock.lock();
			if((priority > temp.priority) && (temp.prev == null)) {				
				incrementListFilled();
				insertPosition = 0;
				Node newNode = new Node(name, priority);
				newNode.next = temp;
				newNode.prev = null;
				temp.prev = newNode;
				
				addLock.lock();
				header = newNode;
				addLock.unlock();
				
				decrementAdders();
				temp.myLock.unlock();
				
				return insertPosition;
			}
			temp.myLock.unlock();

			
			/*
			 * CASE 2: The queue may contain several nodes. Examine a node, N.
			 * 
			 * If (my priority) > (priority of node N) ****AND**** (my priority) <= (priority of node N's previous node)
			 * Then update list.
			 */
			temp.myLock.lock();
			if((priority > temp.priority) && ((temp.prev != null) && (temp.prev.priority >= priority))) {
				temp.prev.myLock.lock();				
				incrementListFilled();
				Node newNode = new Node(name, priority);
				newNode.next = temp;
				newNode.prev = temp.prev;
				temp.prev.next = newNode;
				temp.prev = newNode;
				decrementAdders();
				
				temp.prev.prev.myLock.unlock();
				temp.myLock.unlock();

				return insertPosition;
			}
			temp.myLock.unlock();

			
			/*
			 * CASE 3: The queue may contain several nodes. Examine a node, N.
			 * 
			 * If (my priority) > (priority of node N) ****BUT**** (current priority) > (priority of node N's previous node)
			 * Restart while loop -- another node with greater a smaller priority has added before you.
			 */
			temp.myLock.lock();
			if((priority > temp.priority) && ((temp.prev != null) && (temp.prev.priority < priority))) {
				temp.prev.myLock.lock();				
				insertPosition = 0;
				temp.prev.myLock.unlock();
				temp.myLock.unlock();
				addLock.lock();
				temp = header;
				addLock.unlock();
				
				continue;
			}
			temp.myLock.unlock();
			
			
			/*
			 * CASE 4: End of the queue. Examine last node, N.
			 * 
			 * If (my priority) <= (priority of node N) ****AND**** N is the last node
			 * Then update list.
			 */
			temp.myLock.lock();
			if((priority <= temp.priority) && (temp.next == null)) {								
				insertPosition++;
				incrementListFilled();
				Node newNode = new Node(name, priority);
				newNode.next = null;
				newNode.prev = temp;
				temp.next = newNode;
				decrementAdders();
				temp.myLock.unlock();
				
				return insertPosition;
			}
			insertPosition++;
			temp = temp.next;	
			temp.prev.myLock.unlock();
		}			
	}
	
	public int findName(String name) {
		int position = -1;
		Node temp;
		
		addLock.lock();
		temp = header;
		addLock.unlock();
		
		while(temp != null) {
			if(temp.name == name) {
				return (++position);
			}
			temp = temp.next;
			++position;
		}
		position = -1;
		
		return position;
	}
	
	public String getFirstName() {
		String name;
		Node temp;
		
		addLock.lock();
		temp = header;
		temp.myLock.lock();
		
		if(temp.next != null) {
			temp.next.myLock.lock();
			temp.next.prev = null;
			header = temp.next;
			temp.next.myLock.unlock();
		}
		
		else if(temp.next == null)
			header = null;
		
		decrementListFilled();
		name = temp.name;
		temp.next = null;
		allNames.remove(name);
		
		temp.myLock.unlock();
		addLock.unlock();
		
		return name;
	}
		
	public int getCapacity() {
		int capacity;
		
		addLock.lock();
		capacity = listCapacity;
		addLock.unlock();
		
		return capacity;
	}
	
	public int getAmountFilled() {
		int filled;
		
		addLock.lock();
		filled = listFilled;
		addLock.unlock();
		
		return filled;
	}
	
	public int getAdders() {
		int adders;
		
		addLock.lock();
		adders = amountOfAdders;
		addLock.unlock();
		
		return adders;
	}
	
	public void incrementAdders() {
		addLock.lock();
		amountOfAdders++;
		addLock.unlock();
	}
	
	public void decrementAdders() {
		addLock.lock();
		amountOfAdders--;
		addLock.unlock();
	}
	
	public void incrementListFilled() {
		addLock.lock();
		listFilled++;
		addLock.unlock();
	}
	
	public void decrementListFilled() {
		addLock.lock();
		listFilled--;
		addLock.unlock();
	}
}