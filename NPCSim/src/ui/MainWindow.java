package ui;

import main.Main;
import ui.map.MapPanel;
import ui.map.SkipDialog;
import ui.other.GeneratorPanel;
import ui.other.NamesPanel;
import ui.other.SearchPanel;
import ui.vars.VariablePane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainWindow extends JFrame {
    private final JLabel lblTime;
    private SkipDialog skip;
    private final MapPanel mapPanel;
    private ActionListener stop;
    private ActionListener start;
    private final AtomicBoolean running;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainWindow frame = new MainWindow(args);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    private MainWindow(String[] args) {
        if(args.length > 0) Main.rand.setSeed(Integer.parseInt(args[0]));
        Main main = new Main();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(2000, 100, 778, 516);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane();
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        SearchPanel searchPanel = new SearchPanel();
        mapPanel = new MapPanel(main, searchPanel);
        tabbedPane.addTab("Towns", mapPanel);

        tabbedPane.addTab("Variables", new VariablePane(Main.vars));

        tabbedPane.addTab("Search", searchPanel);

        tabbedPane.addTab("Generators", new GeneratorPanel());

        tabbedPane.addTab("Names", new NamesPanel());

        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setLayout(new GridLayout(0, 3, 0, 0));

        JPanel north = new JPanel();
        add(north, BorderLayout.NORTH);

        JButton btnStep = new JButton("Step");
        btnStep.addActionListener(a -> update());
        buttonPanel.add(btnStep);

        JButton btnStepX = new JButton("Skip to...");
        btnStepX.addActionListener(a -> update(skip.getResult()));
        buttonPanel.add(btnStepX);
        running = new AtomicBoolean(false);
        JButton btnStepWhile = new JButton("Start");

        start = e -> {
            running.set(true);
            btnStepWhile.setText("Stop");
            new Thread(() -> mapPanel.main.update(running::get)).start();
            btnStepWhile.removeActionListener(start);
            btnStepWhile.addActionListener(stop);
        };
        stop = e -> {
            running.set(false);
            refresh();
            btnStepWhile.setText("Start");
            btnStepWhile.removeActionListener(stop);
            btnStepWhile.addActionListener(start);
        };

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        menuBar.add(file);

        JMenuItem save = new JMenuItem("Save...");
        save.addActionListener(a -> mapPanel.save());
        file.add(save);

        JMenuItem load = new JMenuItem("Load...");
        load.addActionListener(a -> {
            mapPanel.load();
            reload();
        });
        file.add(load);

        setJMenuBar(menuBar);

        btnStepWhile.addActionListener(start);
        buttonPanel.add(btnStepWhile);

        lblTime = new JLabel("Time");
        north.add(lblTime);

        skip = new SkipDialog(Main.time, this);
        refresh();
    }

    private void update() {
        mapPanel.main.update();
        refresh();
    }
    private void update(int num) {
        mapPanel.main.update(num);
        refresh();
    }

    private void refresh() {
        mapPanel.refresh();
        lblTime.setText(Main.time.toString());
    }
    private void reload() {
        mapPanel.reload();
        refresh();
    }
}
