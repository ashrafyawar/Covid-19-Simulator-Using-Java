package threads;

import com.company.Controller;
/**
 * this class used to simulate thread and calles  method of it's controller aggregated class.
 */
public class GUIThread extends Thread{
    private Controller controller;
    private final Mutex mutex;

    public GUIThread(Controller controller) {
        this.controller = controller;
        this.mutex = new Mutex(false);
    }

    public Mutex getMutex() {
        return this.mutex;
    }

    public void run(){
        while(!isInterrupted()){
            mutex.step();
            try {// refresh GUI
                controller.setSpreadingFactor(controller.randomFloat(0.5, 1.0, 0.1));
                controller.setMortalityRate(controller.randomFloat(0.1, 0.9, 0.1));

                for (int i = 0; i < this.controller.getIndividuals().size(); i++) {
                    controller.getIndividuals().get(i).setMortalityRate(controller.getMortalityRate());
                }
                controller.getInfectedCountArr().add((double) controller.getInfectedCount());
                controller.getDeadCountArr().add((double) controller.getDeadCount());
                controller.getTimerCountArr().add((double) (int) (controller.getTimer()));

                controller.getEpidemicSimulatorGUI().repaint();
            }catch (NullPointerException e){}
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){
            }
        }
    }
}