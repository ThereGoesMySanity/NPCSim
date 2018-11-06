package ui.map;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.Main;
import ui.MainWindow;

public class MapPane extends JPanel {
	private Main main;
	private PersonDetailsPanel personDetailsPanel;
	private TownDetailsPanel townDetailsPanel;
	private ListPanel list;
	private JLabel lblTime;
	private SkipDialog skip;

	public MapPane(MainWindow mw, Main main) {
		super(new BorderLayout());
		this.main = main;
		
		JPanel cards = new JPanel(new CardLayout());
		add(cards, BorderLayout.CENTER);

		JPanel blank = new JPanel();
		cards.add(blank, "blank");
		
		personDetailsPanel = new PersonDetailsPanel(mw, main.map);
		cards.add(personDetailsPanel, "Person");
		
		townDetailsPanel = new TownDetailsPanel(mw);
		cards.add(townDetailsPanel, "Town");
		
		list = new ListPanel(main.map, personDetailsPanel, townDetailsPanel, cards);
		add(list, BorderLayout.WEST);
		
		JPanel buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JButton btnStep = new JButton("Step");
		btnStep.addActionListener(a -> update());
		buttonPanel.add(btnStep);
		
		JButton btnStepX = new JButton("Skip to...");
		btnStepX.addActionListener(a -> update(skip.getResult()));
		buttonPanel.add(btnStepX);
		
		JPanel north = new JPanel();
		add(north, BorderLayout.NORTH);
		
		lblTime = new JLabel("Time");
		north.add(lblTime);
		
		skip = new SkipDialog(Main.time, mw);
		refresh();
	}
	public void update() {
		main.update();
		refresh();
	}
	public void update(int i) {
		main.update(i);
		refresh();
	}
	public void refresh() {
		lblTime.setText(Main.time.toString());
		personDetailsPanel.update();
		townDetailsPanel.update();
	}
}
