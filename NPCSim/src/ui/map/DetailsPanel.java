package ui.map;

import java.awt.*;

public interface DetailsPanel {
    interface DetailsObject {
        Type getType();
    }
    enum Type {TASK, PERSON, TOWN}
    DetailsObject getObject();
    void setObject(DetailsObject object);
    Type getType();
    void refresh();
    Component toComponent();
    DetailsPanel newInstance(MapPanel mapPanel);
}
