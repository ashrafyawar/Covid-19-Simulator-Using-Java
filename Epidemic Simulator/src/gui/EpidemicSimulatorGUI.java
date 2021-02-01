package gui;

import com.company.Controller;
import javax.swing.*;
import java.awt.*;

/**
 * this class is used to visualize simulator
 */
public class EpidemicSimulatorGUI extends JFrame {
    private Controller controller;
    private MyCanvas canvas;

    /**
     * @param controller controller
     */
    public EpidemicSimulatorGUI(Controller controller){
        this.canvas = new MyCanvas();
        this.controller = controller;
        setLayout(new BorderLayout());
        setSize(1020,800);
        setTitle("Epidemic Simulator");
        add("Center",canvas);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * used to print inds on canvas
     */
    private class MyCanvas extends JPanel{
        @Override
        public void paint(Graphics g){
            Graphics2D graphics2D= (Graphics2D) g;
            Dimension d = this.getSize();
            long currentTime = System.currentTimeMillis();
            for (int i = 0; i < controller.getIndividuals().size(); i++) {
                float timeLapsed = (currentTime - controller.getIndividuals().get(i).getInfectedTime());

                if (controller.getIndividuals().get(i).getInfected()){
                    graphics2D.setColor(Color.red);
                }else{
                    graphics2D.setColor(Color.black);
                }
                if (timeLapsed >= 25000 && controller.getIndividuals().get(i).getInfected()){
                    graphics2D.setColor(Color.magenta);
                }
                    graphics2D.fillRect(controller.getIndividuals().get(i).getxCoordinate(),controller.getIndividuals().get(i).getyCoordinate(),5,5);
            }

            g.setColor(Color.black);
            g.drawLine(0,605,d.width,605);

            g.setColor(Color.black);
            graphics2D.drawString("Individuals: ",5,620);
            g.setColor(Color.green);
            graphics2D.drawString(String.valueOf(controller.getIndividuals().size()),120,620);

            g.setColor(Color.black);
            graphics2D.drawString("Timer: ",5,650);
            graphics2D.drawString(controller.getTimer() + " Seconds",120,650);
            controller.setTimer(controller.getTimer()+1);

            graphics2D.drawString("Total Infected: ",5,670);
            g.setColor(Color.red);
            graphics2D.drawString(String.valueOf(controller.getInfectedCount()),120,670);
            g.setColor(Color.black);
            graphics2D.drawString("Total Healthy: ",5,690);
            g.setColor(Color.green);
            graphics2D.drawString(String.valueOf(controller.getHealthyCount()),120,690);

            g.setColor(Color.black);
            graphics2D.drawString("Total Hospitalized: ",5,710);
            g.setColor(Color.BLUE);
            graphics2D.drawString(String.valueOf(controller.getHospital().getPatients().size()),120,710);

            g.setColor(Color.black);
            graphics2D.drawString("Total Dead: ",5,730);
            g.setColor(Color.red);
            graphics2D.drawString(String.valueOf(controller.getDeadCount()),120,730);
        }
    }

    /**
     * @return controller
     */
    public Controller getController() {
        return controller;
    }

    /**
     * @param controller controller
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * @return canvas
     */
    public MyCanvas getCanvas() {
        return canvas;
    }

    /**
     * @param canvas canvas
     */
    public void setCanvas(MyCanvas canvas) {
        this.canvas = canvas;
    }
}