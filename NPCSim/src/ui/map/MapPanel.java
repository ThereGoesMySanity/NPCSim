package ui.map;

import main.Main;
import ui.map.DetailsListener.DLListener;
import ui.map.DetailsPanel.DetailsObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EnumMap;

import static ui.map.DetailsPanel.Type;
import static ui.map.DetailsPanel.Type.*;

public class MapPanel extends JPanel implements DLListener {
    private final ArrayList<DetailsPanel> pins = new ArrayList<>();
    private final EnumMap<Type, DetailsPanel> panelMap = new EnumMap<>(Type.class);
    private DetailsListener listener;
    public final Main main;
    private final JTabbedPane tabbedPane;
    private final JPopupMenu menu;
    private DetailsPanel tab;
    private final JFileChooser fileChooser = new JFileChooser();
    private final ListPanel list;

    public MapPanel(DetailsListener listener, Main main) {
        super(new BorderLayout());
        this.listener = listener;
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

        panelMap.put(PERSON, new PersonDetailsPanel(listener, main.map));
        panelMap.put(TOWN, new TownDetailsPanel(listener));
        panelMap.put(TASK, new TaskDetailsPanel(listener));
        panelMap.forEach((k, v) -> listener.addListener(v));

        list = new ListPanel(listener, main.map);
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

    private void pin() {
        pins.add(tab);
        panelMap.put(tab.getType(), tab.newInstance(listener));
    }

    private void unpin() {
        pins.remove(tab);
        tabbedPane.remove(tab.toComponent());
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

    @Override
    public void onChange(DetailsObject o) {
        if(o != null) addTab(o.getType());
    }

    @Override
    public boolean listening(Type t) {
        return true;
    }
}
