package threads;

import com.company.Controller;
/**
 * this class used to simulate thread and calles method of it's controller aggregated class.
 */
public class FindDeadIndividualsThread extends Thread{

    private Controller controller;
    private final Mutex mutex;

    /**
     * @param controller  controller
     *                    constructor.
     */
    public FindDeadIndividualsThread(Controller controller) { this.controller = controller;this.mutex = new Mutex(false); }

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
            long currentTime = System.currentTimeMillis();
            for (int i = 0; i < controller.getIndividuals().size(); i++) {//find if any dead in the society.
                if(controller.getIndividuals().get(i).getInfected()){

                    long infectedTime = controller.getIndividuals().get(i).getInfectedTime();
                    float deadTime = controller.getIndividuals().get(i).getDeadTime()/1000F;
                    float timeLapsed = (currentTime - infectedTime) / 1000F;

                    if(timeLapsed >= deadTime){

                        int y = controller.getIndividuals().get(i).getyCoordinate();
                        int x = controller.getIndividuals().get(i).getxCoordinate();

                        for (int k = y; k < y + 5; k++){// mark dead individuals pixles as available.
                            for (int l = x; l < x + 5; l++) {
                                controller.getCanvas2DArray().get(k).get(l).setOccupied('n');
                            }
                        }
                        controller.getIndividuals().remove(i);
                        controller.setInfectedCount(controller.getInfectedCount() - 1);
                        controller.setDeadCount(controller.getDeadCount() + 1);
                        controller.setIndividualCount(controller.getIndividuals().size());
                    }
                }
            }
            try{
                Thread.sleep(5000);
            } catch (InterruptedException e){}
        }
    }
}
