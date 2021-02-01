package com.company;

/**
 * this class used as to keep info of one pixel in the canvas
 */
public class OnePixelInfo {

    private Individual individual;
    private Character occupied;

    /**
     * constructor
     */
     public OnePixelInfo(){
         occupied = 'n';
     }

    /**
     * @return ind
     */
    public Individual getIndividual() {
        return individual;
    }

    /**
     * @param individual ind
     */
    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    /**
     * @return char
     */
    public Character getOccupied() {
        return occupied;
    }

    /**
     * @param occupied char
     */
    public void setOccupied(Character occupied) {
        this.occupied = occupied;
    }
}