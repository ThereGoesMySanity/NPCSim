package ui.other;

import people.Person;
import util.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import static ui.map.DetailsListener.DLListener;
import static ui.map.DetailsPanel.DetailsObject;
import static ui.map.DetailsPanel.Type;
import static ui.map.DetailsPanel.Type.PERSON;

public class TreePanel extends JPanel implements DLListener {
    private static final int
            PERSON_SIZE = 40,
            HSPACING = 80,
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
        ArrayList<ArrayList<Map.Entry<TreeNode, Integer>>> layers = new ArrayList<>();
        Dimension[] d = calcWidths(widths, layers, node);
        setPreferredSize(new Dimension(Math.max(d[0].width, d[1].width),
                d[0].height + d[1].height - PERSON_SIZE));
        drawUp(g, node, layers, (getWidth() - d[0].width) / 2,
                layers.size() * (PERSON_SIZE + VSPACING) + PERSON_SIZE);
        drawDown(g, node, widths, (getWidth() - d[1].width)/2, d[0].height - PERSON_SIZE);
    }
    private void drawUp(Graphics g, TreeNode node, ArrayList<ArrayList<Map.Entry<TreeNode, Integer>>> layers,
                        int x, int height) {
        FontMetrics fm = g.getFontMetrics();
        for(int i = 0; i < layers.size(); i++) {
            System.out.println(layers.get(i));
            int offset = 0;
            for(Map.Entry<TreeNode, Integer> entry : layers.get(i)) {
                String str = nodeToString(entry.getKey());
                Point p1 = new Point(x + offset + entry.getValue()/2,
                        height - i * (PERSON_SIZE + VSPACING) - PERSON_SIZE / 2);
                g.drawString(str, p1.x - fm.stringWidth(str), p1.y + PERSON_SIZE / 2);
                offset += entry.getValue();
                if(i > 0) {
                    int offset2 = 0;
                    for(Map.Entry<TreeNode, Integer> entry2 : layers.get(i - 1)) {
                        if(entry.getKey().children.contains(entry2.getKey())) {
                            Point p2 = new Point(x + offset2 + entry2.getValue()/2,
                                    height - (i - 1) * (PERSON_SIZE + VSPACING) - PERSON_SIZE / 2);
                            g.drawLine(p1.x, p1.y, p2.x, p2.y);
                        }
                    }
                }
            }
        }
    }
    private Point drawDown(Graphics g, TreeNode node, HashMap<TreeNode, Integer> widths, int x, int y) {
        Point p = new Point(x + widths.get(node)/2,
                y + PERSON_SIZE / 2);
        System.out.println(node.children.size());
        String str = nodeToString(node);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(str, p.x - fm.stringWidth(str) / 2, p.y);
        int offset = 0;
        for(TreeNode n : node.children) {
            Point p1 = drawDown(g, n, widths, x + offset, y + VSPACING + PERSON_SIZE);
            g.drawLine(p.x, p.y, p1.x, p1.y);
            offset += widths.get(n);
        }
        return p;
    }
    private Dimension[] calcWidths(HashMap<TreeNode, Integer> widths,
                                   ArrayList<ArrayList<Map.Entry<TreeNode, Integer>>> layers, TreeNode localRoot) {
        return new Dimension[] {expandUp(layers, new HashSet<>(), localRoot, 0),
                                expandDown(widths, localRoot, 0)};
    }
    private Dimension expandUp(ArrayList<ArrayList<Map.Entry<TreeNode, Integer>>> layers, HashSet<TreeNode> added,
                          TreeNode localRoot, int depth) {
        if(depth > UPPER_LIMIT) return new Dimension();
        added.add(localRoot);
        layers.add(new ArrayList<>());
        Dimension d = new Dimension(getSize(localRoot), 0);
        if(localRoot.parents.length > 0) d = expandUp(layers, added, localRoot.parents[0], depth + 1);
        int width = localRoot.children.stream().mapToInt(c -> expandBackDown(layers, added, c, depth - 1)).sum();
        layers.get(depth).add(Map.entry(localRoot, width));
        if(localRoot.spouse != null && localRoot.spouse.parents.length > 0) {
            Dimension d2 = expandUp(layers, added, localRoot.spouse.parents[0], depth + 1);
            d.setSize(d2.width+d.width, 0);
        }
        return new Dimension(Math.max(width, d.width), 0);
    }
    private int expandBackDown(ArrayList<ArrayList<Map.Entry<TreeNode, Integer>>> layers, HashSet<TreeNode> added,
                                TreeNode localRoot, int depth) {
        if(depth < 0) return 0;
        if(added.contains(localRoot)) return getSize(localRoot);
        added.add(localRoot);
        int width = 0;
        for(TreeNode node : localRoot.children) {
            width += expandBackDown(layers, added, node, depth - 1);
        }
        layers.get(depth).add(Map.entry(localRoot, width));
        return Math.max(getSize(localRoot) + HSPACING, width);
    }
    private Dimension expandDown(HashMap<TreeNode, Integer> widths, TreeNode localRoot, int depth) {
        int w = getSize(localRoot) + HSPACING,
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
        return PERSON_SIZE * (n.value.getSpouse() != null? 2 : 1);
    }
    private String nodeToString(TreeNode node) {
        return node.value.toString() + (node.spouse != null? " + "+node.spouse.value.toString() : "");
    }

    @Override
    public boolean listening(Type t) {
        return t == PERSON;
    }
}
