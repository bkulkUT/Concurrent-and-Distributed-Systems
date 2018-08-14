/* Names: Bharat Kulkarni
 * 		  Chethan Valleru
 * EIDs:  bsk524
 * 		  cv7999
 */
public class MonitorThreadSynch {
	private int parties;
	private int reload;
	
	public MonitorThreadSynch(int parties) {
		this.parties = parties;
		this.reload = this.parties;
	}
	
	public synchronized int await() throws InterruptedException {
		int index = --parties;
		
		if(parties != 0) {
			this.wait();
		}
	
		else {
			notifyAll();
			resetCount(index);
		}
		return index;
	}
	
	private void resetCount(int i) {
		this.parties = this.reload;
	}
}