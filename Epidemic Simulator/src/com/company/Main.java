package com.company;

import gui.IndividualsInsertionSelectionGUI;

/**
 * @author Ashraf
 * @version 1.0
 * main class
 */
public class Main{
    public static void main(String[] args){
        Controller controller = new Controller();
        new IndividualsInsertionSelectionGUI(controller);
    }
}