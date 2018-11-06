package ui.map;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import people.Person;
import tasks.Task;
import util.Convert;

public class TaskPane extends JPanel {
	private JList<Person> people;
	private Task task;
	private JTabbedPane tabbedPane;
	private int labelIndex;
	private JPanel mainPanel;
	private JLabel nameLabel;

	/**
	 * Create the panel.
	 */
	public TaskPane() {
		setLayout(new BorderLayout(0, 0));

		mainPanel = new JPanel();
		add(mainPanel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{1, 0};
		mainPanel.setLayout(gbl_panel);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.SOUTH);

		JScrollPane peopleScroll = new JScrollPane();
		tabbedPane.addTab("People", null, peopleScroll, null);

		people = new JList<Person>();
		peopleScroll.setViewportView(people);
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task t) {
		task = t;
		if(t == null) return;
		for(int i = 1; i < tabbedPane.getTabCount(); i++) {
			tabbedPane.removeTabAt(i);
		}
		mainPanel.removeAll();
		labelIndex = 0;
		nameLabel = new JLabel(task.toString());
		addLabel(nameLabel);
		t.addToPane(this);
		update();
	}
	public void update() {
		if(task != null) {
			task.updatePane();
			nameLabel.setText(task.toString());
			Convert.listToJList(task.people(), people);
		}
	}
	public void addLabel(JLabel... labels) {
		for(JLabel label : labels) {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = labelIndex;
			mainPanel.add(label, gbc);
		}
		labelIndex++;
	}
	public JTabbedPane getTabs() {
		return tabbedPane;
	}
}
