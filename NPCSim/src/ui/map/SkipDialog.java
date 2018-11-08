package ui.map;

import ui.MainWindow;
import util.Time;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SkipDialog extends JDialog implements ChangeListener, ActionListener {
    private final int WIDTH = 256, HEIGHT = 128;
    private final JPanel contentPanel = new JPanel();
    private JLabel lblTime;
    private JSlider slider;
    private Time time;
    private int value;
    private MainWindow mw;

    /**
     * Create the dialog.
     */
    public SkipDialog(Time t, MainWindow mw) {
        super(mw, true);
        time = t;
        this.mw = mw;
        setBounds(0, 0, WIDTH, HEIGHT);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        slider = new JSlider(0, 120, 0);
        slider.addChangeListener(this);
        contentPanel.add(slider);
        lblTime = new JLabel(t.toString());
        lblTime.setAlignmentY(Component.TOP_ALIGNMENT);
        lblTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(lblTime);
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        JButton okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        okButton.addActionListener(this);
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);
        buttonPane.add(cancelButton);
    }

    public int getResult() {
        slider.setValue(0);
        setLocation(mw.getX() + (mw.getWidth() - WIDTH) / 2, mw.getY() + (mw.getHeight() - HEIGHT) / 2);
        setVisible(true);
        return value;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        lblTime.setText(time.plus(slider.getValue()).toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OK")) {
            value = slider.getValue();
        } else {
            value = 0;
        }
        this.setVisible(false);
    }
}
