package com.company;

import java.util.ArrayList;

/**
 * this class represents hospital which accepts patients form society.
 *
 */
public class Hospital implements Producer {
    private int ventilators;
    private ArrayList<Individual> patients;
    private Controller controller;

    /**
     * constructor
     */
    public Hospital() {
        this.patients = new ArrayList();
        this.ventilators = 0;
    }

    /**
     * @param o consumer of individuals in this case
     *          adds inds into hospital
     */
    @Override
    public void registerConsumer(Consumer o) {
        patients.add((Individual) o);
    }

    /**
     * @param o
     */
    @Override
    public void removeConsumer(Consumer o) {
        int i = patients.indexOf(o);
        if (i >= 0) {
            patients.remove(i);
        }
    }

    /**
     * notify consumers
     */
    public void notifyConsumers() {
        for (int i = 0; i < patients.size(); i++){
            Consumer consumer =  patients.get(i);
            consumer.update();
        }
    }

    /**
     * this method is called by a thread and it recieves new patients and releases cured patients.
     */
    public void produce(){
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < this.patients.size(); i++) {//find if any cured patients.
            long infectedTime = this.patients.get(i).getInfectedTime();
            float timeLapsed = (currentTime - infectedTime) / 1000F;

            if(timeLapsed >= 10){//if patient is curried and ready for return back to society.
                Individual individual = this.patients.get(i);
                this.patients.remove(i);
                this.controller.setAtRandom(individual);
            }
        }

        for(int i = 0; i < this.controller.getIndividuals().size(); i++){//receives new patients
            if (this.patients.size() >= this.ventilators){// hospital is full.
                break;
            }else{// if any vacancy in hospital then receive new patients form society
                long infectedTime = this.controller.getIndividuals().get(i).getInfectedTime();
                float timeLapsed = (currentTime - infectedTime) / 1000F;
                if (this.controller.getIndividuals().get(i).getInfected() && timeLapsed >= 25){// if infected
                    Individual individual = this.controller.getIndividuals().get(i);
                    individual.setInfectedTime(System.currentTimeMillis());
                    this.controller.getIndividuals().remove(i);
                    this.controller.setInfectedCount(this.controller.getInfectedCount() - 1);
                    this.patients.add(individual);

                    int y = individual.getyCoordinate();
                    int x = individual.getxCoordinate();
                    for (int k = y; k < y + 5; k++){
                        for (int l = x; l < x + 5; l++) {
                            this.controller.getCanvas2DArray().get(k).get(l).setOccupied('n');
                        }
                    }
                }
            }
        }
    }

    /**
     * @return ventilators count
     */
    public int getVentilators() {
        return ventilators;
    }

    /**
     * @param ventilators ventilators
     */
    public void setVentilators(int ventilators) {
        this.ventilators = ventilators;
    }

    /**
     * @return controller obj
     */
    public Controller getController() {
        return controller;
    }

    /**
     * @param controller cont
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * @return patients count
     */
    public ArrayList<Individual> getPatients() {
        return patients;
    }

    /**
     * @param patients patients
     */
    public void setPatients(ArrayList<Individual> patients) {
        this.patients = patients;
    }
}