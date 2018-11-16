package ui.map;

import ui.map.DetailsListener.DLListener;

import java.awt.*;

public interface DetailsPanel extends DLListener {
    interface DetailsObject {
        Type getType();
    }
    enum Type {TASK, PERSON, TOWN}
    DetailsObject getObject();
    Type getType();
    void refresh();
    Component toComponent();
    DetailsPanel newInstance(DetailsListener dl);
}
