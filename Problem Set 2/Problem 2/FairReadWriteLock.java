//UT-EID= CV7999, bsk524

public class FairReadWriteLock {

    private int write = 0;
    private int read = 0;
    private int next = 0;
    private int count = 0;


    public synchronized void beginRead() {
        int ID = count;
        count++;
        while(ID != next || write > 0){
            try{
                wait();
            }
            catch(Exception e){
                System.out.println("An Error Occurred in " + (count - 1) + "while doing beginRead");
            }
        }
        read++;
        next++;
    }

    public synchronized void endRead() {
        read--;
        notifyAll();
    }

    public synchronized void beginWrite() {
        int ID = count;
        count++;
        while(ID != next || write > 0 || read > 0){
            try{
                wait();
            }
            catch(Exception e){
                System.out.println("An Error Occurred in " + (count - 1) + "while doing beginWrite");
            }
        }
        write++;
        next++;
    }
    public synchronized void endWrite() {
        write--;
        notifyAll();
    }
}