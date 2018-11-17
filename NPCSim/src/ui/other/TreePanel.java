package ui.other;

import people.Person;
import util.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static ui.map.DetailsListener.DLListener;
import static ui.map.DetailsPanel.DetailsObject;
import static ui.map.DetailsPanel.Type;
import static ui.map.DetailsPanel.Type.PERSON;

public class TreePanel extends JPanel implements DLListener {
    private static final int
            PERSON_SIZE = 40,
            HSPACING = 120,
            VSPACING = 60,
            UPPER_LIMIT = 2,
            LOWER_LIMIT = Integer.MAX_VALUE;

    private Person person;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(person != null) redraw(g);
    }

    @Override
    public void onChange(DetailsObject o) {
        person = (Person) o;
    }
    private void redraw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        TreeNode node = this.person.getNode();
        HashMap<TreeNode, Integer> widths = new HashMap<>();
        ArrayList<ArrayList<TreeNode>> layers = new ArrayList<>();
        Dimension[] d = calcWidths(widths, layers, node);
        //possible bug with the preferred size being too small since we don't
        //check the point until after drawing
        setPreferredSize(new Dimension(Math.max(d[0].width, d[1].width),
                d[0].height + d[1].height - PERSON_SIZE));
        int x = getWidth() / 2;
        if(layers.size() > 1) {
            g.setColor(Color.CYAN);
            g.fillRect(x - d[0].width / 2, 0, d[0].width, d[0].height);
            x = drawUp(g, node, layers, x - d[0].width / 2,
                    d[0].width, layers.size() * (PERSON_SIZE + VSPACING) + PERSON_SIZE).x;
        }
        if(widths.size() > 1) drawDown(g, node, widths, x - d[1].width/2, layers.size() * (PERSON_SIZE + VSPACING) + PERSON_SIZE / 2);
    }
    private Point drawUp(Graphics g, TreeNode root, ArrayList<ArrayList<TreeNode>> layers,
                        int x, int width, int height) {
        FontMetrics fm = g.getFontMetrics();
        Point p = new Point();
        ArrayList<Integer> widths = new ArrayList<>();
        layers.stream().map(arr -> arr.stream().mapToInt(this::getSize).sum())
                .forEach(widths::add);
        for(int i = 0; i < layers.size(); i++) {
            for(int j = 0; j < layers.get(i).size(); j++) {
                Point p1 = getPoint(layers, widths, x, width, height, i, j);
                TreeNode entry = layers.get(i).get(j);
                g.setColor(Color.GRAY);
                g.drawRect(p1.x - (getSize(entry) - HSPACING) / 2, p1.y - PERSON_SIZE / 2,
                        getSize(entry) - HSPACING, PERSON_SIZE);
                g.setColor(Color.BLACK);
                drawNode(g, entry, p1);
                if(i < layers.size() - 1) {
                    for(int k = 0; k < layers.get(i + 1).size(); k++) {
                        TreeNode entry2 = layers.get(i + 1).get(k);
                        if(entry.isParent(entry2)
                                || (entry.spouse != null && entry.spouse.isParent(entry2))) {
                            Point p2 = getPoint(layers, widths, x, width, height, i + 1, k);
                            int xOff = 0;
                            if(entry.spouse != null) {
                                xOff = PERSON_SIZE / (entry.spouse.isParent(entry2)? 2 : -2);
                            }
                            g.drawLine(p1.x + xOff, p1.y, p2.x, p2.y);
                        }
                    }
                }
                if(entry.equals(root)) {
                    p = p1;
                }
            }
        }
        return p;
    }
    private Point getPoint(ArrayList<ArrayList<TreeNode>> layers, ArrayList<Integer> widths,
                           int x, int width, int height, int layer, int pos) {
        int spacing = width - widths.get(layer);
        spacing /= layers.get(layer).size();
        return new Point(x + layers.get(layer).stream().limit(pos).mapToInt(this::getSize).sum() + spacing * pos
                + (spacing + getSize(layers.get(layer).get(pos))) / 2,
                height - layer * (PERSON_SIZE + VSPACING) - PERSON_SIZE / 2);
    }
    private Point drawDown(Graphics g, TreeNode node, HashMap<TreeNode, Integer> widths, int x, int y) {
        Point p = new Point(x + widths.get(node)/2,
                y + PERSON_SIZE / 2);
        drawNode(g, node, p);
        int offset = 0;
        for(TreeNode n : node.children) {
            Point p1 = drawDown(g, n, widths, x + offset, y + VSPACING + PERSON_SIZE);
            g.drawLine(p.x, p.y, p1.x, p1.y);
            offset += widths.get(n);
        }
        return p;
    }
    private Dimension[] calcWidths(HashMap<TreeNode, Integer> widths,
                                   ArrayList<ArrayList<TreeNode>> layers, TreeNode localRoot) {
        expandUp(layers, new HashSet<>(), localRoot, 0);
        Dimension d = expandDown(widths, localRoot, 0);
        return new Dimension[]{new Dimension(layers.stream()
                .mapToInt(arr -> arr.stream()
                        .mapToInt(this::getSize).sum()
                ).max().orElse(0),
                layers.size() * (PERSON_SIZE + VSPACING) + PERSON_SIZE), d};
    }
    private void expandUp(ArrayList<ArrayList<TreeNode>> layers, HashSet<TreeNode> added,
                         TreeNode localRoot, int depth) {
        if(depth > UPPER_LIMIT) return;
        added.add(localRoot);
        if(layers.size() <= UPPER_LIMIT)layers.add(new ArrayList<>());
        if(localRoot.parents.length > 0) expandUp(layers, added, localRoot.parents[0], depth + 1);
        localRoot.children.forEach(c -> expandBackDown(layers, added, c, depth - 1));
        layers.get(depth).add(localRoot);
        if(localRoot.spouse != null && localRoot.spouse.parents.length > 0) {
            added.add(localRoot.spouse);
            expandUp(layers, added, localRoot.spouse.parents[0], depth + 1);
        }
    }
    private void expandBackDown(ArrayList<ArrayList<TreeNode>> layers, HashSet<TreeNode> added,
                               TreeNode localRoot, int depth) {
        if(depth < 0) return;
        if(added.contains(localRoot) || (localRoot.spouse != null && added.contains(localRoot.spouse))) return;
        added.add(localRoot);
        for(TreeNode node : localRoot.children) {
            expandBackDown(layers, added, node, depth - 1);
        }
        layers.get(depth).add(localRoot);
    }
    private Dimension expandDown(HashMap<TreeNode, Integer> widths, TreeNode localRoot, int depth) {
        int w = getSize(localRoot),
                h = PERSON_SIZE;
        if(localRoot.children.size() > 0 && depth < LOWER_LIMIT) {
            Dimension[] dimensionList = localRoot.children.stream()
                    .map(n -> expandDown(widths, n, depth + 1)).toArray(Dimension[]::new);
            w = Arrays.stream(dimensionList).mapToInt(d -> d.width).sum();
            h = dimensionList[0].height + PERSON_SIZE + VSPACING;
        }
        widths.put(localRoot, w);
        return new Dimension(w, h);
    }

    private int getSize(TreeNode n) {
        return PERSON_SIZE * (n.spouse != null? 2 : 1) + HSPACING;
    }
    private void drawNode(Graphics g, TreeNode node, Point p) {
        g.drawRect(p.x - (getSize(node) + HSPACING) / 2, p.y - PERSON_SIZE / 2,
                getSize(node) - HSPACING, PERSON_SIZE);
        FontMetrics fm = g.getFontMetrics();
        String s0 = node.value.toString().replace(' ', '\n');
        Rectangle2D rect0 = fm.getStringBounds(s0, g);
        if(node.spouse != null) {
            String s1 = node.spouse.value.toString().replace(' ', '\n');
            Rectangle2D rect1 = fm.getStringBounds(s1, g);
            String plus = "+";
            Rectangle2D plusRect = fm.getStringBounds(plus, g);
            g.drawString(s0, (int)(p.x - rect0.getWidth() - plusRect.getWidth() / 2),
                             (int)(p.y - rect0.getHeight() / 2));
            g.drawString(plus, (int)(p.x - plusRect.getWidth() / 2), (int)(p.y - plusRect.getHeight() / 2));
            g.drawString(s1, (int)(p.x + plusRect.getWidth() / 2), (int)(p.y - rect1.getHeight() / 2));
        } else {
            g.drawString(s0, (int)(p.x - rect0.getWidth() / 2), (int)(p.y - rect0.getHeight() / 2));
        }
    }

    @Override
    public boolean listening(Type t) {
        return t == PERSON;
    }
}
