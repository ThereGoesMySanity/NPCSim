package ui.map;

import main.Main;
import tasks.Task;
import ui.map.DetailsPanel.DetailsObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EnumMap;

import static ui.map.DetailsPanel.Type;
import static ui.map.DetailsPanel.Type.*;

@SuppressWarnings("SuspiciousMethodCalls")
public class MapPanel extends JPanel {
    private final ArrayList<DetailsPanel> pins = new ArrayList<>();
    private final EnumMap<Type, DetailsPanel> panelMap = new EnumMap<>(Type.class);
    public final Main main;
    private final JTabbedPane tabbedPane;
    private final JPopupMenu menu;
    private DetailsPanel tab;
    final DetailsListener listener = new DetailsListener(this);
    private final JFileChooser fileChooser = new JFileChooser();
    private final ListPanel list;

    public MapPanel(Main main) {
        super(new BorderLayout());
        this.main = main;

        menu = new JPopupMenu();
        JMenuItem pin = new JMenuItem("Pin");
        pin.addActionListener(a -> pin());
        JMenuItem unpin = new JMenuItem("Unpin");
        unpin.addActionListener(a -> unpin());
        menu.add(pin);
        menu.add(unpin);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int i = tabbedPane.getUI().tabForCoordinate(tabbedPane, e.getX(), e.getY());
                    tab = (DetailsPanel) tabbedPane.getComponentAt(i);
                    if (panelMap.get(tab.getType()).equals(tab)) {
                        pin.setVisible(true);
                        unpin.setVisible(false);
                    } else {
                        pin.setVisible(false);
                        unpin.setVisible(true);
                    }
                    menu.show(tabbedPane, e.getX(), e.getY());
                }
            }
        });
        add(tabbedPane, BorderLayout.CENTER);

        panelMap.put(PERSON, new PersonDetailsPanel(this, main.map));
        panelMap.put(TOWN, new TownDetailsPanel(this));
        panelMap.put(TASK, new TaskDetailsPanel(this));

        list = new ListPanel(main.map, this);
        add(list, BorderLayout.WEST);
    }

    private void addTab(Type t) {
        DetailsPanel dp = panelMap.get(t);
        tabbedPane.addTab(dp.getObject().toString(), dp.toComponent());
        tabbedPane.setSelectedComponent(dp.toComponent());
    }
    public DetailsPanel get(Type type) {
        return panelMap.get(type);
    }

    public void add(DetailsObject obj) {
        if(obj != null) set(obj.getType(), obj);
    }

    private void set(Type type, DetailsObject o) {
        if(o != null) {
            if(type == TASK) {
                Main.taskMan.panes.remove(panelMap.get(type).getObject());
                Main.taskMan.panes.put((Task)o, (TaskDetailsPanel)panelMap.get(type));
            }
            panelMap.get(type).setObject(o);
            addTab(type);
        }
    }

    private void pin() {
        pins.add(tab);
        panelMap.put(tab.getType(), tab.newInstance(this));
    }

    private void unpin() {
        pins.remove(tab);
        tabbedPane.remove(tab.toComponent());
        if(tab.getType() == TASK) Main.taskMan.panes.remove(tab.getObject());
    }

    public void reload() {
        list.reload();
    }

    public void refresh() {
        pins.forEach(DetailsPanel::refresh);
        panelMap.forEach((t, p) -> p.refresh());
    }
    public void save() {
        if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            main.save(fileChooser.getSelectedFile());
        }
    }
    public void load() {
        if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            main.load(fileChooser.getSelectedFile());
        }
    }
}
