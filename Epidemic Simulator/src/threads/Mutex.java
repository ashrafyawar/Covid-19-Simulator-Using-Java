package threads;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * this class is used to represent mutex concept
 */
public class Mutex {
    private final AtomicBoolean lock;
    private final Object mutex;

    /**
     * @param lock lock
     *             constructor
     */
    public Mutex(boolean lock) {
        this.lock = new AtomicBoolean(lock);
        this.mutex = new Object();
    }

    /**
     * run step in thread
     */
    public void step() {
        if (lock.get()) synchronized(mutex) {
            try {
                mutex.wait();
            } catch (InterruptedException ex) {}
        }
    }

    /**
     * lock method
     */
    public void lock() {
        lock.set(true);
    }

    /**
     * unlock method
     */
    public void unlock() {
        lock.set(false);

        synchronized(mutex) {
            mutex.notify();
        }
    }
}