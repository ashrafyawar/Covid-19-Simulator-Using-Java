package threads;

import com.company.Controller;

/**
 * this class used to simulate thread and calles runCeck method of it's controller aggregated class.
 */
public class BackEndThread extends Thread{
    private Controller controller;
    private final Mutex mutex;

    /**
     * @param controller controller
     *                   constructor
     */
    public BackEndThread(Controller controller) { this.controller = controller;this.mutex = new Mutex(false); }

    /**
     * @return mutex
     */
    public Mutex getMutex() { return this.mutex; }

    /**
     * run thread
     */
    public void run(){
        while(!isInterrupted()) {
            mutex.step();
            controller.runCheck();
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){}
        }
    }
}