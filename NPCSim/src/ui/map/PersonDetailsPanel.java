package ui.map;

import map.AreaMap;
import map.Town;
import people.Person;
import people.Stats.Stat;
import tasks.Task;
import util.Convert;

import javax.swing.*;
import java.awt.*;

import static ui.map.DetailsPanel.Type.PERSON;

public class PersonDetailsPanel extends DetailsPanel {
    private static final String[] relationsColumns = {"Person", "Relation"};
    private Person person;
    private final JTextField name;
    private final JTextField xp;
    private final JTable stats;
    private final JTable relationsTable;
    private final JComboBox<Town> town;
    private final JList<Person> children;
    private final JList<String> history;
    private final JList<Task> tasks;
    private final JLabel level;
    private final JLabel alignment;
    private final JLabel age;
    private final JLabel race;
    private final JLabel spouse;
    private final JEditorPane notes;
    private final AreaMap map;
    private JTextArea personality;
    private JLabel gender;

    /**
     * Create the panel.
     */
    PersonDetailsPanel(DetailsListener listener, AreaMap map) {
        this.map = map;
        setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        add(panel, BorderLayout.SOUTH);
        panel.setLayout(new GridLayout(1, 0, 0, 0));

        JButton btnSave = new JButton("Commit");
        btnSave.addActionListener(a -> commit());
        panel.add(btnSave);

        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(a -> refresh());
        panel.add(btnReset);

        JPanel infoPanel = new JPanel();
        add(infoPanel, BorderLayout.CENTER);
        GridBagLayout gbl_infoPanel = new GridBagLayout();
        gbl_infoPanel.columnWidths = new int[]{90, 0, 0, 0};
        gbl_infoPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_infoPanel.columnWeights = new double[]{1.0, 0.0, 0.0, 0.5};
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

        town = new JComboBox<>();
        town.setModel(new DefaultComboBoxModel<>(map.towns()));
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
        
        gender = new JLabel("M/F");
        GridBagConstraints gbc_gender = new GridBagConstraints();
        gbc_gender.insets = new Insets(0, 0, 5, 5);
        gbc_gender.gridx = 1;
        gbc_gender.gridy = 1;
        infoPanel.add(gender, gbc_gender);
        
                age = new JLabel("Age:");
                GridBagConstraints gbc_age = new GridBagConstraints();
                gbc_age.insets = new Insets(0, 0, 5, 5);
                gbc_age.gridx = 2;
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

        personality = new JTextArea("Personality");
        personality.setLineWrap(true);
        personality.setEditable(false);
        GridBagConstraints gbc_personality = new GridBagConstraints();
        gbc_personality.gridwidth = 3;
        gbc_personality.gridheight = 2;
        gbc_personality.insets = new Insets(0, 0, 5, 0);
        gbc_personality.gridx = 1;
        gbc_personality.gridy = 5;
        gbc_personality.fill = GridBagConstraints.BOTH;
        infoPanel.add(personality, gbc_personality);

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

        tasks = new JList<>();
        tasks.addListSelectionListener(listener);
        JScrollPane tasksScroll = new JScrollPane(tasks);
        tasksScroll.setPreferredSize(tasks.getPreferredSize());
        tabbedPane.addTab("Tasks", tasksScroll);

        history = new JList<>();
        JScrollPane historyScroll = new JScrollPane(history);
        historyScroll.setPreferredSize(history.getPreferredSize());
        tabbedPane.addTab("History", historyScroll);

        children = new JList<>();
        children.addListSelectionListener(listener);
        tabbedPane.addTab("Children", children);

        JPanel relations = new JPanel();
        tabbedPane.addTab("Relations", relations);
        relations.setLayout(new BorderLayout(0, 0));

        JPanel searchBar = new JPanel();
        relations.add(searchBar, BorderLayout.SOUTH);
        GridBagLayout gbl_searchBar = new GridBagLayout();
        gbl_searchBar.columnWidths = new int[]{0, 0, 0};
        gbl_searchBar.rowHeights = new int[]{0};
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

        JTextField searchField = new JTextField();
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

    private void commit() {
        String[] names = name.getText().split(" ", 2);
        person.setName(names[0], names[1]);
        for (Stat s : Stat.values()) {
            Integer value = (Integer) stats.getValueAt(s.ordinal(), 1);
            person.setStat(s, value);
        }
        person.xp = Integer.parseInt(xp.getText());
        person.setNotes(notes.getText());
    }

    @Override
    void refreshSub() {
        Convert.listToJList(person.getHistory(), history);
        Convert.listToJList(person.getTasks(), tasks, t -> t.toString(person));
        Convert.listToJList(person.getChildren(), children);
        Convert.mapToTable(person.relationships, relationsTable, relationsColumns);
        town.setSelectedItem(person.getTown());
        stats.setModel(person.stats);
        level.setText("Level " + person.level);
        name.setText(person.toString());
        xp.setText(person.xp + "");
        notes.setText(person.getNotes());
        alignment.setText(person.getAlignment().toString());
        age.setText(person.getAge() + " year(s) old");
        race.setText(person.getRace().toString());
        gender.setText(person.gender==0?"Male":"Female");
        personality.setText(person.personality);
        if (person.getSpouse() != null) spouse.setText(person.getSpouse().toString());
        else spouse.setText("None");
    }

    @Override
    public Person getObject() {
        return person;
    }

    @Override
    public Type getType() {
        return PERSON;
    }

    @Override
    public DetailsPanel newInstance(DetailsListener dl) {
        return new PersonDetailsPanel(dl, map);
    }

    @Override
    public void setObject(DetailsObject o) {
        person = (Person) o;
    }
}
