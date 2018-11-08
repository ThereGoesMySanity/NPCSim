package ui;

import main.Main;
import tasks.Task;
import ui.map.MapPane;
import ui.map.TaskPane;
import ui.vars.VariablePane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    private static final Main main = new Main();
    private JPanel contentPane;
    private MapPane mapPane;
    private VariablePane varPane;
    private ArrayList<TaskPane> pins = new ArrayList<>();
    TaskPane taskPane;
    private JTabbedPane tabbedPane;
    private JPopupMenu menu;
    private Component tab;
    private boolean visible = false;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainWindow frame = new MainWindow();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public MainWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(2000, 100, 778, 516);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        menu = new JPopupMenu();
        JMenuItem pin = new JMenuItem("Pin");
        pin.addActionListener(a -> pin());
        JMenuItem unpin = new JMenuItem("Unpin");
        unpin.addActionListener(a -> unpin((TaskPane) tab));
        menu.add(pin);
        menu.add(unpin);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int i = tabbedPane.getUI().tabForCoordinate(tabbedPane, e.getX(), e.getY());
                    tab = tabbedPane.getComponentAt(i);
                    if (taskPane.equals(tab)) {
                        pin.setVisible(true);
                        unpin.setVisible(false);
                    } else if (pins.contains(tab)) {
                        pin.setVisible(false);
                        unpin.setVisible(true);
                    } else {
                        return;
                    }
                    menu.show(tabbedPane, e.getX(), e.getY());
                }
            }
        });
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        mapPane = new MapPane(this, main);
        tabbedPane.addTab("Towns", mapPane);
        varPane = new VariablePane();
        tabbedPane.addTab("Variables", varPane);
        taskPane = new TaskPane();
    }

    public void addTaskPane(Task t) {
        if (pins.stream().allMatch(p -> p.getTask() != t)) {
            Main.taskMan.panes.remove(taskPane.getTask());
            taskPane.setTask(t);
            if (t != null) {
                Main.taskMan.panes.put(t, taskPane);
                tabbedPane.addTab(t.toString(), taskPane);
                setTaskPane(taskPane);
            }
        } else {
            tabbedPane.remove(taskPane);
            visible = false;
        }
    }

    public void setTaskPane(TaskPane t) {
        taskPane = t;
        visible = true;
    }

    public void pin() {
        pins.add(taskPane);
        taskPane = new TaskPane();
        visible = false;
    }

    public void unpin(TaskPane t) {
        pins.remove(t);
        if (!visible) setTaskPane(t);
        else {
            tabbedPane.remove(t);
            Main.taskMan.panes.remove(t.getTask());
        }
    }

    public void update() {
        pins.forEach(TaskPane::update);
        taskPane.update();
    }

}
