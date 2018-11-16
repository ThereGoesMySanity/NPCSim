package ui.map;

import ui.map.DetailsListener.DLListener;

import javax.swing.*;

public abstract class DetailsPanel extends JPanel implements DLListener {
    public interface DetailsObject {
        Type getType();
    }
    public enum Type {TASK, PERSON, TOWN}
    private boolean pinned = false;

    abstract DetailsObject getObject();
    abstract Type getType();
    abstract void refreshSub();
    abstract void setObject(DetailsObject det);
    final void refresh() {
        if(getObject() != null) refreshSub();
    }

    DetailsPanel newInstance(DetailsListener dl) {
        try {
            return getClass().getConstructor(DetailsListener.class).newInstance(dl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    void pin() {
        pinned = true;
    }

    @Override
    final public boolean listening(Type t) {
        return getType() == t;
    }

    @Override
    final public void onChange(DetailsObject o) {
        if(!pinned) setObject(o);
    }
}
