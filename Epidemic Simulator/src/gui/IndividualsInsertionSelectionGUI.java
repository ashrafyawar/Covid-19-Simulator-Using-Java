package gui;

import com.company.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * this class is used to visualize a jframe.
 */
public class IndividualsInsertionSelectionGUI extends JFrame implements ActionListener {

    private JButton selectionButton = new JButton("Submit");
    static JTextField t;
    private Controller controller;

    /**
     * @param controller controller
     *                   constructor.
     */
    public IndividualsInsertionSelectionGUI(Controller controller) {
        this.controller = controller;

        setLayout(new BorderLayout());
        setSize(600, 100);
        setTitle("Individuals Insertion Selections");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel();

        JLabel selection1 = new JLabel("Enter 1 For Entering Individuals One By One Or Enter 2 For Entering Them As Bulk");
        JLabel newLine = new JLabel("\n");
        t = new JTextField(10);
        selectionButton.addActionListener(this);

        p.add(selection1);
        p.add(newLine);
        p.add(t);
        p.add(selectionButton);
        this.add(p);
        setVisible(true);
    }

    /**
     * @param e an event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("Submit")) {
            if (t.getText().equals("1")) {
                this.dispose();
                new UsersSelectionGUI("yes", this.controller);
            } else if (t.getText().equals("2")) {
                this.dispose();
                new UsersSelectionGUI("no", this.controller);
            }
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
}