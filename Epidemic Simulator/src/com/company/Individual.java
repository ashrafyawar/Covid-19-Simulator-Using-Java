package com.company;

/**
 * ind class represents one ind in society and canvas.
 */
public class Individual  implements Consumer{
    private double mortalityRate;
    private double wearMask;
    private int speed;
    private int socialDistance;
    private int howSocial;
    private int xCoordinate;
    private int yCoordinate;
    private long infectedTime;
    private long deadTime;
    private Boolean isInfected;
    private Boolean hospitalized;
    private Hospital hospital;

    /**
     * @param wearMask mask
     * @param speed speed
     * @param socialDistance socialDistance
     * @param howSocial howSocial
     * @param isInfected isInfected
     * @param xCoordinate xCoordinate
     * @param yCoordinate yCoordinate
     */
    public Individual(double wearMask, int speed, int socialDistance, int howSocial, Boolean isInfected, int xCoordinate, int yCoordinate){
        this.wearMask = wearMask;
        this.speed = speed;
        this.socialDistance = socialDistance;
        this.howSocial = howSocial;
        this.isInfected = isInfected;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.hospitalized = false;
        this.mortalityRate = 0.1;
        this.deadTime = (long) (100 * (1 - this.mortalityRate) * 1000);
    }

    /**
     * @return double
     */
    public double getWearMask() {
        return wearMask;
    }

    /**
     * @param wearMask double
     */
    public void setWearMask(double wearMask) {
        this.wearMask = wearMask;
    }

    /**
     * @return int
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * @param speed int
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * @return int
     */
    public int getSocialDistance() {
        return socialDistance;
    }

    /**
     * @param socialDistance int
     */
    public void setSocialDistance(int socialDistance) {
        this.socialDistance = socialDistance;
    }

    /**
     * @return int
     */
    public int getHowSocial() {
        return howSocial;
    }

    /**
     * @param howSocial int
     */
    public void setHowSocial(int howSocial) {
        this.howSocial = howSocial;
    }

    /**
     * @return Boolean
     */
    public Boolean getInfected() {
        return isInfected;
    }

    /**
     * @param infected Boolean
     */
    public void setInfected(Boolean infected) {
        isInfected = infected;
    }

    /**
     * @return int
     */
    public int getxCoordinate() { return xCoordinate; }

    /**
     * @param xCoordinate int
     */
    public void setxCoordinate(int xCoordinate) { this.xCoordinate = xCoordinate; }

    /**
     * @return int
     */
    public int getyCoordinate() { return yCoordinate; }

    /**
     * @param yCoordinate int
     */
    public void setyCoordinate(int yCoordinate) { this.yCoordinate = yCoordinate; }

    /**
     * @return Boolean
     */
    public Boolean getHospitalized() {
        return hospitalized;
    }

    /**
     * @param hospitalized Boolean
     */
    public void setHospitalized(Boolean hospitalized) {
        this.hospitalized = hospitalized;
    }

    /**
     * @return long
     */
    public long getInfectedTime() {
        return infectedTime;
    }

    /**
     * @param infectedTime long
     */
    public void setInfectedTime(long infectedTime) {
        this.infectedTime = infectedTime;
    }

    /**
     * @return double
     */
    public double getMortalityRate() {
        return mortalityRate;
    }

    /**
     * @param mortalityRate double
     */
    public void setMortalityRate(double mortalityRate) {
        this.mortalityRate = mortalityRate;
    }

    /**
     * @return long
     */
    public long getDeadTime() {
       return this.deadTime = (long) (100 * (1 - this.mortalityRate) * 1000);
    }

    /**
     * @param deadTime long
     */
    public void setDeadTime(long deadTime) {
        this.deadTime = deadTime;
    }

    /**
     * update method
     */
    @Override
    public void update(){}

    /**
     * @param s Producer Obj
     */
    @Override
    public void addProducer(Producer s) {
            this.hospital = (Hospital) s;
    }
}