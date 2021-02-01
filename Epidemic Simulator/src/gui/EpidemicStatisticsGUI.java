package gui;

import com.company.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * this class used to visualize statistics of program
 */
public class EpidemicStatisticsGUI extends JFrame implements ActionListener{

    private Controller controller;

    private JButton startButton = new JButton("START");
    private JButton pauseButton = new JButton("PAUSE");
    private JButton resumeButton = new JButton("RESUME");
    private JButton plotButton = new JButton("PLOT GRAPHS");
    private JButton terminateButton = new JButton("TERMINATE PROGRAM");

    private JPanel p;

    /**
     * @param controller controller obj
     */
    public EpidemicStatisticsGUI(Controller controller){
        this.controller = controller;
        this.p = new JPanel();
        setLayout(new BorderLayout());
        setTitle("Epidemic Statistics");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300,145);

        this.p.add(this.startButton,"     ");
        this.p.add(this.pauseButton,"     ");
        this.p.add(this.resumeButton,"    ");
        this.p.add(this.plotButton,"    ");
        this.p.add(this.terminateButton,"    ");

        this.startButton.addActionListener(this);
        this.pauseButton.addActionListener(this);
        this.resumeButton.addActionListener(this);
        this.plotButton.addActionListener(this);
        this.terminateButton.addActionListener(this);

        add(this.p);
    }

    /**
     * @param e buttons clicked
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("START")){
            this.controller.initializeIndTimer();
            this.controller.update();
            startButton.setEnabled(false);

        }else if(s.equals("PAUSE")){
            this.controller.pause();
        }else if(s.equals("RESUME")){
            this.controller.resume();
        }else if(s.equals("PLOT GRAPHS")){
            this.controller.getEpidemicSimulatorGUI().dispose();
            pauseButton.setEnabled(false);
            resumeButton.setEnabled(false);
            new PlotInfectedStatistics(controller);
            new PlotDeadStatistics(controller);
        }else if (s.equals("TERMINATE PROGRAM")){
            System.exit(1);
        }
    }
}