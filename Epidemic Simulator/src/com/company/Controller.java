package com.company;

import gui.EpidemicSimulatorGUI;
import gui.EpidemicStatisticsGUI;
import threads.BackEndThread;
import threads.FindDeadIndividualsThread;
import threads.GUIThread;
import threads.HospitalThread;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

/**
 * controller is acts like mediator design pattern
 */
public class Controller implements Mediator{

    private int width = 1000;
    private int height = 600;
    private int infectedCount;
    private int healthyCount;
    private int hospitalizedCount;
    private int deadCount;
    private int individualCount;

    private double spreadingFactor;
    private double mortalityRate;

    private long timer;

    private ArrayList<Individual> individuals;
    private ArrayList<Double> infectedCountArr;
    private ArrayList<Double> deadCountArr;
    private ArrayList<Double> timerCountArr;
    private ArrayList<ArrayList<OnePixelInfo>> canvas2DArray;

    private EpidemicSimulatorGUI epidemicSimulatorGUI;
    private EpidemicStatisticsGUI epidemicStatisticsGUI;
    private BackEndThread backEndThread;
    private GUIThread guiThread;
    private FindDeadIndividualsThread findDeadIndividualsThread;
    private HospitalThread hospitalThread;
    private Hospital hospital;

    /**
     * controller constructor
     */
    public Controller(){
        this.timer = 0;
        this.individuals  = new ArrayList<>(0);
        this.canvas2DArray= new ArrayList<>(0);
        this.infectedCountArr= new ArrayList<>(0);
        this.deadCountArr = new ArrayList<>(0);
        this.timerCountArr=  new ArrayList<>(0);
        this.spreadingFactor = randomFloat(0.5, 1.0, 0.1);
        this.infectedCount = 1;

        for (int i = 0; i < this.height; i++) {// create 2d array of canvas
            ArrayList<OnePixelInfo> temp= new ArrayList<>(0);
            for (int j = 0; j < this.width; j++){temp.add(new OnePixelInfo());}
            this.canvas2DArray.add(temp);
        }
    }

    /**
     * this method initializes canvas at back end  where when user enters inds count it will create canvas array accordingly and
     * place the inds at random pos
     */
    public void initializeCanvas(){

        double wearMask = 0.2;
        int indCount = 0,ranIntX = 0,ranIntY = 0;
        ranIntX = randomNumber(this.width - 5);
        ranIntY = randomNumber(this.height - 5);

        //add one infected at random pos
        Individual ind = new Individual(wearMask,100,randomNumberStartFrom1(9),randomNumberStartFrom1(5),true,ranIntX,ranIntY);
        ind.setInfectedTime(System.currentTimeMillis());
        this.individuals.add(ind);
        for (int i = ranIntY; i < ranIntY + 5; i++) {//mark inds place as occupied
            for (int j = ranIntX; j < ranIntX + 5; j++) {
                this.canvas2DArray.get(i).get(j).setOccupied('y');
                this.canvas2DArray.get(i).get(j).setIndividual(ind);
            }
        }

        while (indCount < this.individualCount+1){//for each ind
            Boolean randFits = false;

            while(randFits == false){// find if any empty place exist or not
                ranIntX = randomNumber(this.width - 5);
                ranIntY = randomNumber(this.height - 5);
                Boolean yesGo = false;
                for (int i = ranIntY; i < ranIntY + 5; i++) {
                    for (int j = ranIntX; j < ranIntX + 5; j++) {
                        if(this.canvas2DArray.get(i).get(j).getOccupied() == 'y'){ yesGo = true; }
                    }
                }
                if (!yesGo){ randFits = true; }
            }

            //place the new pos for ind
            Individual individual = new Individual(wearMask,this.randomNumberStartFrom1(500),randomNumberStartFrom1(9),randomNumberStartFrom1(5),false,ranIntX,ranIntY);
            this.individuals.add(individual);
            for (int i = ranIntY; i < ranIntY + 5; i++) {
                for (int j = ranIntX; j < ranIntX + 5; j++) {
                    this.canvas2DArray.get(i).get(j).setOccupied('y');
                    this.canvas2DArray.get(i).get(j).setIndividual(individual);
                }
            }
            indCount++;
        }
    }

    /**
     *  this method initializes from where we add ind one by one form GUI
     */
    public void initializeFromOneByOne(){
        int indCount = 0,ranIntX = 0,ranIntY = 0;
        ranIntX = randomNumber(this.width - 5);
        ranIntY = randomNumber(this.height - 5);

        //add one infected ind into society.
        Individual ind = new Individual(0.2,100,5,5,true,ranIntX,ranIntY);
        this.individuals.add(ind);
        this.canvas2DArray.get(ranIntY).get(ranIntX).setOccupied('y');
        this.canvas2DArray.get(ranIntY).get(ranIntX).setIndividual(ind);

        while(indCount < individuals.size()){//loop through ind list
            Boolean randFits = false;
            while(randFits == false){// find if any empty place is exist
                ranIntX = randomNumber(this.width - 5);
                ranIntY = randomNumber(this.height - 5);
                Boolean yesGo = false;

                for (int i = ranIntY; i < ranIntY + 5; i++) {
                    for (int j = ranIntX; j < ranIntX + 5; j++) {
                        if(this.canvas2DArray.get(i).get(j).getOccupied() == 'y'){
                            yesGo = true;
                        }
                    }
                }
                if (!yesGo){
                    randFits = true;
                }
            }

            for (int i = ranIntY; i < ranIntY + 5; i++) {//mark pixels as occupied of new ind pos
                this.individuals.get(indCount).setxCoordinate(ranIntX);
                this.individuals.get(indCount).setyCoordinate(ranIntY);

                for (int j = ranIntX; j < ranIntX + 5; j++) {
                    this.canvas2DArray.get(i).get(j).setOccupied('y');
                    this.canvas2DArray.get(i).get(j).setIndividual(this.individuals.get(indCount));
                }
            }
            indCount++;
        }
    }

    /**
     * this method is run my a thread and it finds collistion
     * between inds and if any then find prop and act accordingly
     */
    public void runCheck(){//
        int indCount = 0,ranIntX = 0,ranIntY = 0;
        while (indCount < this.individuals.size()){//loop through ind list
            Individual indoo = controlCoordinates(this.individuals.get(indCount));
            ranIntX = indoo.getxCoordinate();
            ranIntY =  indoo.getyCoordinate();


            Boolean collides = false;
            OnePixelInfo onePixelInfoTemp = new OnePixelInfo();

            for(int i = ranIntY; i < ranIntY + 5; i++){//find if any collision occurs
                for(int j = ranIntX; j < ranIntX + 5; j++) {
                    if(this.canvas2DArray.get(i).get(j).getOccupied() == 'y'){
                        onePixelInfoTemp.setIndividual(this.canvas2DArray.get(i).get(j).getIndividual());
                        collides = true;
                        break;
                    }
                }
                if (collides){ break; }
            }

            if (!collides){//if do not collides with any individual in canvas.
                if (individuals.size() > 0) {
                    this.individuals.get(indCount).setxCoordinate(ranIntX);
                    this.individuals.get(indCount).setyCoordinate(ranIntY);
                    for (int i = ranIntY; i < ranIntY + 5; i++){
                        for (int j = ranIntX; j < ranIntX + 5; j++) {
                            this.canvas2DArray.get(i).get(j).setOccupied('y');
                            this.canvas2DArray.get(i).get(j).setIndividual(this.individuals.get(indCount));
                        }
                    }
                }
            }else if(collides){//if collides with onePixelInfoTemp.individual.
                if(this.individuals.size() > 0){
                    Boolean first = onePixelInfoTemp.getIndividual().getInfected();
                    Boolean second = this.individuals.get(indCount).getInfected();
                    if (first && second){

                    }else{// if any one is infected not both
                        int c = Math.max(this.individuals.get(indCount).getHowSocial(),onePixelInfoTemp.getIndividual().getHowSocial());
                        int d = Math.min(this.individuals.get(indCount).getSocialDistance(),onePixelInfoTemp.getIndividual().getSocialDistance());
                        double m1 = this.individuals.get(indCount).getWearMask();
                        double m2 = onePixelInfoTemp.getIndividual().getWearMask();
                        double probOfInfection = Math.min(this.spreadingFactor * (1 + c / 10) * m1 * m2 * (1 -  d / 10),1);//probability of infection to disease.

                        if(first){// if collided person is infected.
                            if(probOfInfection > 0.3){//if the second person will be infected.
                                this.individuals.get(indCount).setInfected(true);
                                this.individuals.get(indCount).setInfectedTime(System.currentTimeMillis());
                                this.setInfectedCount(this.getInfectedCount()+1);
                            }
                        }else if(second){//if onePixelInfoTemp.getIndividual().setInfected(true).
                            onePixelInfoTemp.getIndividual().setInfected(true);
                            onePixelInfoTemp.getIndividual().setInfectedTime(System.currentTimeMillis());
                            this.setInfectedCount(this.getInfectedCount()+1);
                        }
                    }
                }
            }
            indCount++;
        }
    }

    /**
     * @param individual
     * @return ind
     *  set random directions for an individual
     */
    private Individual controlCoordinates(Individual individual){//
        int randomNum = randomNumber(4);
        if (randomNum == 0){
            individual = controlCoordinates1(individual);
            return individual;
        }else if (randomNum == 1){
            individual = controlCoordinates2(individual);
            return individual;
        }else if (randomNum == 2){
            individual = controlCoordinates3(individual);
            return individual;
        }else if (randomNum == 3){
            individual = controlCoordinates4(individual);
            return individual;
        }
        return individual;
    }

    /**
     * @param individual
     * @return ind
     * set random directions for an individual
     */
    private Individual controlCoordinates1(Individual individual) {// set random directions for an individual
        int five = 5;
        if (individual.getxCoordinate() + individual.getSpeed() + five < 999) {// go right
            individual.setxCoordinate(individual.getxCoordinate() + individual.getSpeed());
            return individual;
        } else if (individual.getyCoordinate() + individual.getSpeed() + five < 599) {// go down
            individual.setyCoordinate(individual.getyCoordinate() + individual.getSpeed());
            return individual;
        } else if (individual.getyCoordinate() - individual.getSpeed() - five > 0) {// go up
            individual.setyCoordinate(individual.getyCoordinate() - individual.getSpeed());
            return individual;
        } else if (individual.getxCoordinate() - individual.getSpeed() - five > 0) { //
            individual.setxCoordinate(individual.getxCoordinate() - individual.getSpeed());
            return individual;
        }
        return individual;
    }

    /**
     * @param individual
     * @return set random directions for an individual
     */
    private Individual controlCoordinates2(Individual individual) {// set random directions for an individual

        int five = 5;
        if (individual.getxCoordinate() - individual.getSpeed()- five > 0){ // go left
            individual.setxCoordinate(individual.getxCoordinate() - individual.getSpeed());
            return individual;
        }else if(individual.getxCoordinate() + individual.getSpeed()+five < 999){// go right
            individual.setxCoordinate(individual.getxCoordinate() + individual.getSpeed());
            return individual;
        }else if (individual.getyCoordinate() + individual.getSpeed()+five < 599){// go down
            individual.setyCoordinate(individual.getyCoordinate() + individual.getSpeed());
            return individual;
        }else if (individual.getyCoordinate() - individual.getSpeed() - five > 0){// go up
            individual.setyCoordinate(individual.getyCoordinate() - individual.getSpeed());
            return individual;
        }
        return individual;
    }

    /**
     * @param individual
     * @return ind
     * set random directions for an individual
     */
    private Individual controlCoordinates3(Individual individual) {// set random directions for an individual

        int five = 5;
        if (individual.getyCoordinate() - individual.getSpeed() - five > 0){// go up
            individual.setyCoordinate(individual.getyCoordinate() - individual.getSpeed());
            return individual;
        }else if (individual.getxCoordinate() - individual.getSpeed()- five > 0){ // go left
            individual.setxCoordinate(individual.getxCoordinate() - individual.getSpeed());
            return individual;
        }else if(individual.getxCoordinate() + individual.getSpeed()+five < 999){// go right
            individual.setxCoordinate(individual.getxCoordinate() + individual.getSpeed());
            return individual;
        }else if (individual.getyCoordinate() + individual.getSpeed()+five < 599){// go down
            individual.setyCoordinate(individual.getyCoordinate() + individual.getSpeed());
            return individual;
        }
        return individual;
    }

    /**
     * @param individual
     * @return ind
     * set random directions for an individual
     */
    private Individual controlCoordinates4(Individual individual) {// set random directions for an individual
        int five = 5;
        if (individual.getyCoordinate() + individual.getSpeed()+five < 599){// go down
            individual.setyCoordinate(individual.getyCoordinate() + individual.getSpeed());
            return individual;
        }
        else if (individual.getyCoordinate() - individual.getSpeed() - five > 0){// go up
            individual.setyCoordinate(individual.getyCoordinate() - individual.getSpeed());
            return individual;
        }else if (individual.getxCoordinate() - individual.getSpeed()- five > 0){ // go left
            individual.setxCoordinate(individual.getxCoordinate() - individual.getSpeed());
            return individual;
        }else if(individual.getxCoordinate() + individual.getSpeed()+five < 999){// go right
            individual.setxCoordinate(individual.getxCoordinate() + individual.getSpeed());
            return individual;
        }
        return individual;
    }

    /**
     * starts all threads
     */
    @Override
    public void update() {// starts all threads
        this.backEndThread.start();
        this.guiThread.start();
        this.findDeadIndividualsThread.start();
        this.hospitalThread.start();
    }

    /**
     * pauses all thread using mutex class
     */
    @Override
    public void pause() {// pauses all thread using mutex class
        this.backEndThread.getMutex().lock();
        this.guiThread.getMutex().lock();
        this.findDeadIndividualsThread.getMutex().lock();
        this.hospitalThread.getMutex().lock();
    }

    /**
     * resumes all class using mutex class
     */
    @Override
    public void resume() {//resumes all class using mutex class
        this.backEndThread.getMutex().unlock();
        this.guiThread.getMutex().unlock();
        this.findDeadIndividualsThread.getMutex().unlock();
        this.hospitalThread.getMutex().unlock();
    }

    /**
     * @param x
     * @return int
     * generates num between 0 and x
     */
    int randomNumber(int x){
        Random random = new java.util.Random();
        int rand;
        while (true){
            rand = random.nextInt(x);
            if(rand !=0) break;
        }
        return rand;
    }

    /**
     * @param x
     * @return int
     * generates num between 1 and x
     */
    int randomNumberStartFrom1(int x){
        Random random = new java.util.Random();
        int rand;
        while (true){
            rand = random.nextInt(x);
            if(rand !=1) break;
        }
        return rand;
    }

    /**
     * @param minInclusive
     * @param maxInclusive
     * @param precision
     * @return double
     * generates float num between minInc and max Ind with precision pre
     */
    public static double randomFloat(double minInclusive, double maxInclusive, double precision) {
        int max = (int)(maxInclusive/precision);
        int min = (int)(minInclusive/precision);
        Random rand = new Random();
        int randomInt = rand.nextInt((max - min) + 1) + min;
        double randomNum = randomInt * precision;
        return randomNum;
    }

    /**
     * initializes timer into system time
     * used just after start button is pressed.
     */
    public void initializeIndTimer(){
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < this.individuals.size(); i++) {
                if (this.individuals.get(i).getInfected()){
                    this.individuals.get(i).setInfectedTime(currentTime);
                }
        }
    }

    /**
     * @return int
     */
    public int getIndividualCount() {
        return this.individualCount ;
    }

    /**
     * @param individualCount int
     */
    public void setIndividualCount(int individualCount) {
        this.individualCount = individualCount;
    }

    /**
     * @return EpidemisSim Obj
     */
    public EpidemicSimulatorGUI getEpidemicSimulatorGUI() {
        return epidemicSimulatorGUI;
    }

    /**
     * @param epidemicSimulatorGUI epide
     */
    public void setEpidemicSimulatorGUI(EpidemicSimulatorGUI epidemicSimulatorGUI) {
        this.epidemicSimulatorGUI = epidemicSimulatorGUI;
    }

    /**
     * @return EpidemicStatics Obj
     */
    public EpidemicStatisticsGUI getEpidemicStatisticsGUI() {
        return epidemicStatisticsGUI;
    }

    /**
     * @param epidemicStatisticsGUI
     */
    public void setEpidemicStatisticsGUI(EpidemicStatisticsGUI epidemicStatisticsGUI) {
        this.epidemicStatisticsGUI = epidemicStatisticsGUI;
    }

    /**
     * @return Arraylist
     */
    public ArrayList<Individual> getIndividuals() {
        return individuals;
    }

    /**
     * @param individuals
     */
    public void setIndividuals(ArrayList<Individual> individuals) {
        this.individuals = individuals;
    }

    /**
     * @return long
     */
    public long getTimer() {
        return timer;
    }

    /**
     * @param timer long
     */
    public void setTimer(long timer) {
        this.timer = timer;
    }

    /**
     * @return double
     */
    public double getMortalityRate() {
        return mortalityRate;
    }

    /**
     * @return Arraylist
     */
    public ArrayList<Double> getInfectedCountArr() {
        return infectedCountArr;
    }

    /**
     * @return used to schetch graphs
     */
    public double[] getInfectedCountArrForSketch(){

        double x[] = new double[this.infectedCountArr.size()];
        for(int i = 0; i < x.length; i++){
            x[i] = this.infectedCountArr.get(i);
        }
        return x;
    }

    /**
     * @param infectedCountArr
     */
    public void setInfectedCountArr(ArrayList<Double> infectedCountArr) {
        this.infectedCountArr = infectedCountArr;
    }

    public ArrayList<Double> getDeadCountArr() {
        return deadCountArr;
    }
    public double[] getDeadCountArrForSketch(){
        double x[] = new double[this.deadCountArr.size()];
        for(int i = 0; i < x.length; i++){
            x[i] = this.deadCountArr.get(i);
        }
        return x;
    }

    /**
     * @return Arraylist
     */
    public ArrayList<Double> getTimerCountArr() {
        return timerCountArr;
    }
    public double[] getTimerArrForSketch(){
        double x[] = new double[this.timerCountArr.size()];
        for(int i = 0; i < x.length; i++){
            x[i] = this.timerCountArr.get(i);
        }
        return x;
    }

    /**
     * @param timerCountArr
     */
    public void setTimerCountArr(ArrayList<Double> timerCountArr) {
        this.timerCountArr = timerCountArr;
    }

    /**
     * @param deadCountArr
     */
    public void setDeadCountArr(ArrayList<Double> deadCountArr) {
        this.deadCountArr = deadCountArr;
    }

    /**
     * @param mortalityRate
     */
    public void setMortalityRate(double mortalityRate) {
        this.mortalityRate = mortalityRate;
    }

    /**
     * @return int
     */
    public int getInfectedCount() {
        return infectedCount;
    }

    /**
     * @param infectedCount int
     */
    public void setInfectedCount(int infectedCount) {
        this.infectedCount = infectedCount;
    }

    /**
     * @return int
     */
    public int getHealthyCount() {
        healthyCount = individuals.size() - infectedCount;
        return healthyCount;
    }

    /**
     * @param healthyCount int
     */
    public void setHealthyCount(int healthyCount) {
        this.healthyCount = healthyCount;
    }

    /**
     * @return int
     */
    public int getHospitalizedCount() {
        return hospitalizedCount;
    }

    /**
     * @param hospitalizedCount int
     */
    public void setHospitalizedCount(int hospitalizedCount) {
        this.hospitalizedCount = hospitalizedCount;
    }

    /**
     * @return int
     */
    public int getDeadCount() {
        return deadCount;
    }

    /**
     * @param deadCount int
     */
    public void setDeadCount(int deadCount) {
        this.deadCount = deadCount;
    }

    /**
     * @return Arraylist
     */
    public ArrayList<ArrayList<OnePixelInfo>> getCanvas2DArray() {
        return canvas2DArray;
    }

    /**
     * @param canvas2DArray Arralist
     */
    public void setCanvas2DArray(ArrayList<ArrayList<OnePixelInfo>> canvas2DArray) {
        this.canvas2DArray = canvas2DArray;
    }

    /**
     * @return int
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width int
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return int
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height int
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return backenthread
     */
    public BackEndThread getBackEndThread() {
        return backEndThread;
    }

    /**
     * @param backEndThread thread
     */
    public void setBackEndThread(BackEndThread backEndThread) {
        this.backEndThread = backEndThread;
    }

    /**
     * @return thread
     */
    public GUIThread getGuiThread() {
        return guiThread;
    }

    /**
     * @param guiThread guithread
     */
    public void setGuiThread(GUIThread guiThread) {
        this.guiThread = guiThread;
    }

    /**
     * @return hospital
     */
    public Hospital getHospital() {
        return hospital;
    }

    /**
     * @param hospital hospital
     */
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    /**
     * @param individual ind
     */
    public void  addIndividual(Individual individual){
        this.individuals.add(individual);
    }

    /**
     * @return thread
     */
    public FindDeadIndividualsThread getFindDeadIndividuals() {
        return findDeadIndividualsThread;
    }

    /**
     * @param findDeadIndividualsThread thread
     */
    public void setFindDeadIndividualsThread(FindDeadIndividualsThread findDeadIndividualsThread) {
        this.findDeadIndividualsThread = findDeadIndividualsThread;
    }

    /**
     * @return thread
     */
    public HospitalThread getHospitalThread() {
        return hospitalThread;
    }

    /**
     * @param hospitalThread thread
     */
    public void setHospitalThread(HospitalThread hospitalThread) {
        this.hospitalThread = hospitalThread;
    }

    /**
     * @return double
     */
    public double getSpreadingFactor() {
        return spreadingFactor;
    }

    /**
     * @param spreadingFactor double
     */
    public void setSpreadingFactor(double spreadingFactor) {
        this.spreadingFactor = spreadingFactor;
    }

    /**
     * @param individual ind
     *this method sets individuals at random pos after they are released from hospital.
     */
    public void setAtRandom(Individual individual) {
        Boolean randFits = false;
        int ranIntX = 0,ranIntY = 0;
        while(!randFits){

            ranIntY=randomNumber(this.height - 5);
            ranIntX=randomNumber(this.width - 5);
            Boolean yesGo;
            yesGo = false;
            for(int i = ranIntY; i < ranIntY + 5; i++){
                for (int j = ranIntX; j < ranIntX + 5; j++) {
                    if(this.canvas2DArray.get(i).get(j).getOccupied() == 'y'){ yesGo = true; }
                }
            }
            if (!yesGo){ randFits = true; }
        }

        individual.setWearMask(0.2);
        individual.setSpeed(this.randomNumberStartFrom1(500));
        individual.setSocialDistance(randomNumberStartFrom1(9));
        individual.setHowSocial(randomNumberStartFrom1(5));
        individual.setInfected(false);
        individual.setxCoordinate(ranIntX);
        individual.setyCoordinate(ranIntY);

        this.individuals.add(individual);
        for (int i = ranIntY; i < ranIntY + 5; i++) {
            for (int j = ranIntX; j < ranIntX + 5; j++) {
                this.canvas2DArray.get(i).get(j).setOccupied('y');
                this.canvas2DArray.get(i).get(j).setIndividual(individual);
            }
        }
    }
}