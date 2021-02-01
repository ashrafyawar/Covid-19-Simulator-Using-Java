package threads;
import com.company.Hospital;
/**
 * this class used to simulate thread and calles produce() method of it's hospital aggregated class.
 */
public class HospitalThread extends Thread{
    private Hospital hospital;
    private final Mutex mutex;

    /**
     * @param hospital hospital
     *                 constructor
     */
    public HospitalThread(Hospital hospital) { this.hospital = hospital;this.mutex = new Mutex(false); }

    /**
     * @return mutex
     */
    public Mutex getMutex() { return this.mutex; }

    /**
     * run method of hospital thread.
     */
    public void run(){
        while(!isInterrupted()) {
            mutex.step();
            hospital.produce();
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){}
        }
    }
}