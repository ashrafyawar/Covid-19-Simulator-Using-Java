package gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import com.company.Controller;
import javax.swing.*;

/**
 * this class plots infected statistics.
 */
public class PlotInfectedStatistics extends JFrame{

    private Controller controller;
    /**
     * @param controller controller
     *          constructor.
     */
    public PlotInfectedStatistics(Controller controller){
        this.controller = controller;
        JFrame frame = new JFrame("INFECTED INDIVIDUALS GRAPH");

        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        XYDataset ds = createDataset();
        JFreeChart chart = ChartFactory.createXYLineChart("INFECTED INDIVIDUALS GRAPH",
                "Timer In Seconds", "Infected Individuals", ds, PlotOrientation.VERTICAL, true, true,
                false);

        ChartPanel cp = new ChartPanel(chart);
        frame.getContentPane().add(cp);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
    /**
     * @return creates dataframe for chart
     */
    private XYDataset createDataset() {

        DefaultXYDataset ds = new DefaultXYDataset();
        double[] infected =  controller.getInfectedCountArrForSketch();
        double[] timer =  controller.getTimerArrForSketch();
        double[][] data = {timer, infected};
        ds.addSeries("SECONDS", data);
        return ds;
    }
}