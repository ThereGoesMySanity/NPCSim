package ui.map;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import map.Map;
import map.Town;
import people.Person;
import people.Stats.Stat;
import tasks.Task;
import ui.MainWindow;
import util.Convert;

public class PersonDetailsPanel extends JPanel {
	private static final String[] relationsColumns = {"Person", "Relation"};
	private Person person;
	private JTextField name;
	private JTextField xp;
	private JTextField searchField;
	private JTable stats;
	private JTable relationsTable;
	private JComboBox<Town> town;
	private JList<String> history;
	private JList<Task> tasks;
	private JLabel level;
	private JLabel alignment;
	private JLabel age;
	private JLabel race;
	private JLabel spouse;
	private JEditorPane notes;
	private JScrollPane historyScroll;

	/**
	 * Create the panel.
	 * @param mw 
	 */
	public PersonDetailsPanel(MainWindow mw, Map map) {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JButton btnSave = new JButton("Commit");
		btnSave.addActionListener(a -> commit());
		panel.add(btnSave);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(a -> update());
		panel.add(btnReset);
		
		JPanel infoPanel = new JPanel();
		add(infoPanel, BorderLayout.CENTER);
		GridBagLayout gbl_infoPanel = new GridBagLayout();
		gbl_infoPanel.columnWidths = new int[] {0, 0, 0, 0};
		gbl_infoPanel.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_infoPanel.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0};
		gbl_infoPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		infoPanel.setLayout(gbl_infoPanel);
		
		name = new JTextField();
		GridBagConstraints gbc_name = new GridBagConstraints();
		gbc_name.gridwidth = 2;
		gbc_name.fill = GridBagConstraints.BOTH;
		gbc_name.insets = new Insets(0, 0, 5, 5);
		gbc_name.gridx = 0;
		gbc_name.gridy = 0;
		infoPanel.add(name, gbc_name);
		name.setColumns(10);
		
		town = new JComboBox<Town>();
		town.setModel(new DefaultComboBoxModel<Town>(map.towns()));
		GridBagConstraints gbc_town = new GridBagConstraints();
		gbc_town.gridwidth = 2;
		gbc_town.fill = GridBagConstraints.HORIZONTAL;
		gbc_town.insets = new Insets(0, 0, 5, 0);
		gbc_town.anchor = GridBagConstraints.NORTH;
		gbc_town.gridx = 2;
		gbc_town.gridy = 0;
		infoPanel.add(town, gbc_town);
		
		stats = new JTable();
		
		JLabel lblStats = new JLabel("Stats");
		GridBagConstraints gbc_lblStats = new GridBagConstraints();
		gbc_lblStats.insets = new Insets(0, 0, 5, 5);
		gbc_lblStats.gridx = 0;
		gbc_lblStats.gridy = 1;
		infoPanel.add(lblStats, gbc_lblStats);
		
		age = new JLabel("Age:");
		GridBagConstraints gbc_age = new GridBagConstraints();
		gbc_age.insets = new Insets(0, 0, 5, 5);
		gbc_age.gridx = 1;
		gbc_age.gridy = 1;
		infoPanel.add(age, gbc_age);
		
		level = new JLabel("Level");
		GridBagConstraints gbc_level = new GridBagConstraints();
		gbc_level.insets = new Insets(0, 0, 5, 0);
		gbc_level.gridx = 3;
		gbc_level.gridy = 1;
		infoPanel.add(level, gbc_level);
		GridBagConstraints gbc_stats = new GridBagConstraints();
		gbc_stats.fill = GridBagConstraints.BOTH;
		gbc_stats.gridheight = 4;
		gbc_stats.insets = new Insets(0, 0, 5, 5);
		gbc_stats.gridx = 0;
		gbc_stats.gridy = 2;
		infoPanel.add(stats, gbc_stats);
		
		race = new JLabel("race");
		GridBagConstraints gbc_race = new GridBagConstraints();
		gbc_race.insets = new Insets(0, 0, 5, 5);
		gbc_race.gridx = 1;
		gbc_race.gridy = 2;
		infoPanel.add(race, gbc_race);
		
		JLabel lblXp = new JLabel("XP");
		GridBagConstraints gbc_lblXp = new GridBagConstraints();
		gbc_lblXp.insets = new Insets(0, 0, 5, 5);
		gbc_lblXp.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblXp.gridx = 2;
		gbc_lblXp.gridy = 2;
		infoPanel.add(lblXp, gbc_lblXp);
		
		xp = new JTextField();
		GridBagConstraints gbc_xp = new GridBagConstraints();
		gbc_xp.fill = GridBagConstraints.HORIZONTAL;
		gbc_xp.anchor = GridBagConstraints.NORTH;
		gbc_xp.insets = new Insets(0, 0, 5, 0);
		gbc_xp.gridx = 3;
		gbc_xp.gridy = 2;
		infoPanel.add(xp, gbc_xp);
		xp.setColumns(10);
		
		JLabel lblSpouse = new JLabel("Spouse:");
		GridBagConstraints gbc_lblSpouse = new GridBagConstraints();
		gbc_lblSpouse.insets = new Insets(0, 0, 5, 5);
		gbc_lblSpouse.gridx = 2;
		gbc_lblSpouse.gridy = 3;
		infoPanel.add(lblSpouse, gbc_lblSpouse);
		
		JLabel lblAlignment = new JLabel("Alignment:");
		GridBagConstraints gbc_lblAlignment = new GridBagConstraints();
		gbc_lblAlignment.insets = new Insets(0, 0, 5, 0);
		gbc_lblAlignment.gridx = 3;
		gbc_lblAlignment.gridy = 3;
		infoPanel.add(lblAlignment, gbc_lblAlignment);
		
		spouse = new JLabel("spouse");
		GridBagConstraints gbc_spouse = new GridBagConstraints();
		gbc_spouse.insets = new Insets(0, 0, 5, 5);
		gbc_spouse.gridx = 2;
		gbc_spouse.gridy = 4;
		infoPanel.add(spouse, gbc_spouse);
		
		alignment = new JLabel("Alignment");
		GridBagConstraints gbc_alignment = new GridBagConstraints();
		gbc_alignment.insets = new Insets(0, 0, 5, 0);
		gbc_alignment.gridx = 3;
		gbc_alignment.gridy = 4;
		infoPanel.add(alignment, gbc_alignment);
		
		JLabel lblNotes = new JLabel("Notes");
		GridBagConstraints gbc_lblNotes = new GridBagConstraints();
		gbc_lblNotes.insets = new Insets(0, 0, 5, 5);
		gbc_lblNotes.gridx = 0;
		gbc_lblNotes.gridy = 6;
		infoPanel.add(lblNotes, gbc_lblNotes);
		
		notes = new JEditorPane();
		GridBagConstraints gbc_notes = new GridBagConstraints();
		gbc_notes.gridwidth = 4;
		gbc_notes.fill = GridBagConstraints.BOTH;
		gbc_notes.gridx = 0;
		gbc_notes.gridy = 7;
		infoPanel.add(notes, gbc_notes);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.EAST);
		
		tasks = new JList<Task>();
		tasks.addListSelectionListener(e -> mw.addTaskPane(tasks.getSelectedValue()));
		JScrollPane tasksScroll = new JScrollPane(tasks);
		tasksScroll.setPreferredSize(tasks.getPreferredSize());
		tabbedPane.addTab("Tasks", null, tasksScroll, null);
		
		history = new JList<String>();
		historyScroll = new JScrollPane(history);
		historyScroll.setPreferredSize(history.getPreferredSize());
		tabbedPane.addTab("History", null, historyScroll, null);
		
		JPanel relations = new JPanel();
		tabbedPane.addTab("Relations", null, relations, null);
		relations.setLayout(new BorderLayout(0, 0));
		
		JPanel searchBar = new JPanel();
		relations.add(searchBar, BorderLayout.SOUTH);
		GridBagLayout gbl_searchBar = new GridBagLayout();
		gbl_searchBar.columnWidths = new int[]{0, 0, 0};
		gbl_searchBar.rowHeights = new int[] {0};
		gbl_searchBar.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_searchBar.rowWeights = new double[]{0.0};
		searchBar.setLayout(gbl_searchBar);
		
		JLabel lblSearch = new JLabel("Search");
		GridBagConstraints gbc_lblSearch = new GridBagConstraints();
		gbc_lblSearch.anchor = GridBagConstraints.WEST;
		gbc_lblSearch.insets = new Insets(0, 0, 5, 0);
		gbc_lblSearch.gridx = 0;
		gbc_lblSearch.gridy = 0;
		searchBar.add(lblSearch, gbc_lblSearch);
		
		searchField = new JTextField();
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.fill = GridBagConstraints.BOTH;
		gbc_txtName.gridx = 1;
		gbc_txtName.gridy = 0;
		searchBar.add(searchField, gbc_txtName);
		searchField.setColumns(10);
		
		relationsTable = new JTable();
		JScrollPane relationsScroll = new JScrollPane(relationsTable);
		relationsTable.setPreferredScrollableViewportSize(relationsTable.getPreferredSize());
		relationsScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		relations.add(relationsScroll, BorderLayout.CENTER);
	}
	public void setPerson(Person p) {
		person = p;
		update();
	}
	public void commit() {
		String[] names = name.getText().split(" ", 2);
		person.setName(names[0], (names.length > 0 ? names[1] : ""));
		for(Stat s : Stat.values()) {
			Integer value = (Integer) stats.getValueAt(s.ordinal(), 1);
			person.setStat(s, value);
		}
		person.xp = Integer.parseInt(xp.getText());
		person.notes = notes.getText();
	}
	public void update() {
		if (person != null) {
			Convert.listToJList(person.getHistory(), history);
			Convert.listToJList(person.getTasks(), tasks);
			Convert.mapToTable(person.relationships, relationsTable, relationsColumns);
			town.setSelectedItem(person.getTown());
			stats.setModel(person.stats);
			level.setText("Level " + person.level);
			name.setText(person.toString());
			xp.setText(person.xp+"");
			notes.setText(person.notes);
			alignment.setText(person.getAlignment().toString());
			age.setText("Age "+person.getAge());
			race.setText(person.getRace().toString());
			if(person.spouse != null) spouse.setText(person.spouse.toString());
			else spouse.setText("None");
		}
	}
}
