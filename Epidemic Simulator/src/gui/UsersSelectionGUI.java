package gui;

import com.company.Controller;
import com.company.Hospital;
import com.company.Individual;
import threads.BackEndThread;
import threads.FindDeadIndividualsThread;
import threads.GUIThread;
import threads.HospitalThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * this class is used to interact with user
 */
public class UsersSelectionGUI extends JFrame implements ActionListener {
    private JButton selectionButton = new JButton("Submit");
    private JButton selectionButton1 = new JButton("Submit And Proceed");
    private String status;

    private JTextField individualsCount;
    private JTextField wearMask;
    private JTextField speed;
    private JTextField socialDistance;
    private JTextField howSocial;
    private JTextField isInfected;

    private Controller controller;

    /**
     * @param s string
     * @param controller controller
     *                   constructor
     */
    public UsersSelectionGUI(String s,Controller controller){
        this.controller = controller;
        status = s;
        JPanel p = new JPanel();
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("User Selection");
        JLabel newLine = new JLabel("\n");

        if(s.equals("no")){
            setSize(300,100);
            setLocationRelativeTo(null);

            JLabel selection = new JLabel("How Many Individuals?");
            individualsCount = new JTextField(10);

            p.add(selection);
            p.add(newLine);
            p.add(individualsCount);
            p.add(selectionButton);

        }else{
            setSize(250,200);
            setLocationRelativeTo(null);

            JLabel wearMaskLabel = new JLabel("Wear Mask ?");
            wearMask = new JTextField(10);
            JLabel speedLabel = new JLabel("Speed ?");
            speed = new JTextField(10);
            JLabel socialDistanceLabel = new JLabel("Social Distance ?");
            socialDistance = new JTextField(10);
            JLabel howSocialLabel = new JLabel("How Social ?");
            howSocial = new JTextField(10);
            JLabel isInfectedLabel = new JLabel("Is Infected ?");
            isInfected = new JTextField(10);

            p.add(wearMaskLabel);
            p.add(wearMask);
            p.add(newLine);

            p.add(speedLabel);
            p.add(speed);

            p.add(socialDistanceLabel);
            p.add(socialDistance);

            p.add(howSocialLabel);
            p.add(howSocial);

            p.add(isInfectedLabel);
            p.add(isInfected);
            p.add(selectionButton);
            p.add(selectionButton1);
        }

        selectionButton.addActionListener(this);
        selectionButton1.addActionListener(this);

        add(p);
        setVisible(true);
    }

    /**
     * @param e action performed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("Submit")) {
            this.dispose();
            if (this.status.equals("yes")){// if yes then add last  added info as individual.
                Individual individual = new Individual( Double.parseDouble(this.wearMask.getText()),Integer.parseInt(this.speed.getText())
                        ,Integer.parseInt(this.socialDistance.getText()),Integer.parseInt(this.howSocial.getText())
                        ,Boolean.parseBoolean(this.isInfected.getText()),0,0);
                this.controller.addIndividual(individual);

                new UsersSelectionGUI("yes",this.controller);
            }
            else if (this.status.equals("no")){
                int count = Integer.parseInt(this.individualsCount.getText()) - 1;
                this.controller.setIndividualCount(count);
                this.controller.initializeCanvas();

                EpidemicStatisticsGUI epidemicStatisticsGUI = new EpidemicStatisticsGUI(this.controller);
                EpidemicSimulatorGUI epidemicSimulatorGUI = new EpidemicSimulatorGUI(this.controller);

                this.controller.setEpidemicStatisticsGUI(epidemicStatisticsGUI);
                this.controller.setEpidemicSimulatorGUI(epidemicSimulatorGUI);

                this.controller.getEpidemicSimulatorGUI().setVisible(true);
                this.controller.getEpidemicStatisticsGUI().setVisible(true);

                BackEndThread backEndThread = new BackEndThread(this.controller);
                GUIThread guiThread = new GUIThread(this.controller);
                FindDeadIndividualsThread findDeadIndividualsThread = new FindDeadIndividualsThread(this.controller);
                Hospital hospital = new Hospital();
                HospitalThread hospitalThread = new HospitalThread(hospital);

                this.controller.setBackEndThread(backEndThread);
                this.controller.setGuiThread(guiThread);
                this.controller.setFindDeadIndividualsThread(findDeadIndividualsThread);
                this.controller.setHospitalThread(hospitalThread);
                this.controller.setHospital(hospital);
                hospital.setController(this.controller);
                hospital.setVentilators(count / 100);// ven count
            }
        }else if (s.equals("Submit And Proceed")){
            this.dispose();
            Individual individual = new Individual( Double.parseDouble(this.wearMask.getText()),Integer.parseInt(this.speed.getText())
                    ,Integer.parseInt(this.socialDistance.getText()),Integer.parseInt(this.howSocial.getText())
                    ,Boolean.parseBoolean(this.isInfected.getText()),0,0);
            this.controller.addIndividual(individual);

            this.controller.setIndividualCount(this.controller.getIndividualCount());
            this.controller.initializeFromOneByOne();
            EpidemicStatisticsGUI epidemicStatisticsGUI = new EpidemicStatisticsGUI(this.controller);
            EpidemicSimulatorGUI epidemicSimulatorGUI = new EpidemicSimulatorGUI(this.controller);

            this.controller.setEpidemicStatisticsGUI(epidemicStatisticsGUI);
            this.controller.setEpidemicSimulatorGUI(epidemicSimulatorGUI);

            this.controller.getEpidemicSimulatorGUI().setVisible(true);
            this.controller.getEpidemicStatisticsGUI().setVisible(true);

            BackEndThread backEndThread = new BackEndThread(this.controller);
            GUIThread guiThread = new GUIThread(this.controller);
            FindDeadIndividualsThread findDeadIndividualsThread = new FindDeadIndividualsThread(this.controller);
            Hospital hospital = new Hospital();
            HospitalThread hospitalThread = new HospitalThread(hospital);

            this.controller.setBackEndThread(backEndThread);
            this.controller.setGuiThread(guiThread);
            this.controller.setFindDeadIndividualsThread(findDeadIndividualsThread);
            this.controller.setHospitalThread(hospitalThread);
            this.controller.setHospital(hospital);
            hospital.setController(this.controller);
            hospital.setVentilators(controller.getIndividuals().size() / 100);
        }
    }
}