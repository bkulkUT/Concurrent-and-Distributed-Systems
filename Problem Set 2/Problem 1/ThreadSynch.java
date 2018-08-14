/* Names: Bharat Kulkarni
 * 		  Chethan Valleru
 * EIDs:  bsk524
 * 		  cv7999
 */
import java.util.concurrent.Semaphore;

public class ThreadSynch {
	private int parties;
	private int count;
	private Semaphore lock;
	private Semaphore sync;


	public ThreadSynch(int parties) {
		this.parties = parties;
		this.count = 0;
		lock = new Semaphore(1);
		sync = new Semaphore(parties);
	}
	
	public int await() throws InterruptedException {
		lock.acquire();
		sync.acquire();
		int ID = count;
		count++;
		lock.release();
		if(count == parties){
			sync.notifyAll();
			count = 0;
		}
		else{
			sync.wait();
		}
		sync.release();
		return parties - ID - 1;
	}
}
