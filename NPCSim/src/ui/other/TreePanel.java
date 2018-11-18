package ui.other;

import people.Person;
import util.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.*;

import static ui.map.DetailsListener.DLListener;
import static ui.map.DetailsPanel.DetailsObject;
import static ui.map.DetailsPanel.Type;
import static ui.map.DetailsPanel.Type.PERSON;

public class TreePanel extends JPanel implements DLListener, ComponentListener {
    private static final int
            PERSON_SIZE = 40,
            HSPACING = 120,
            VSPACING = 60,
            UPPER_LIMIT = Integer.MAX_VALUE,
            LOWER_LIMIT = Integer.MAX_VALUE;

    private Person person;
    private HashMap<TreeNode, Integer> widths = new HashMap<>();
    private ArrayList<ArrayList<TreeNode>> layers = new ArrayList<>();
    private ArrayList<Integer> layerWidths = new ArrayList<>();
    private Rectangle[] dimensions = new Rectangle[2];
    private boolean update = false;

    public TreePanel() {
        addComponentListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (update) update();
        if (person != null) redraw(g);
    }

    @Override
    public void onChange(DetailsObject o) {
        person = (Person) o;
        update = true;
    }

    public void update() {
        widths.clear();
        layers.clear();
        layerWidths.clear();
        dimensions[0] = dimensions[1] = null;
        calcWidths(person.getNode());
        update = false;
    }

    private void calcWidths(TreeNode localRoot) {
        expandUp(new HashSet<>(), localRoot, 0, true);
        layers.stream().map(arr -> arr.stream().mapToInt(this::getSize).sum() + (arr.size() - 1) * HSPACING)
                .forEach(layerWidths::add);
        dimensions[0] = new Rectangle(0, 0,
                layers.stream().mapToInt(arr -> arr.stream()
                        .mapToInt(this::getSize).sum() + (arr.size() - 1) * HSPACING
                ).max().orElse(0), (layers.size() - 1) * (PERSON_SIZE + VSPACING) + PERSON_SIZE);
        Dimension down = expandDown(localRoot, 0);
        dimensions[1] = new Rectangle(new Point(0, dimensions[0].height - PERSON_SIZE), down);
        int x = getPoint(0, layers.get(0).indexOf(localRoot)).x;
        int offset = x - down.width / 2;
        System.out.println(offset);
        dimensions[0].x = Math.max(-offset, 0);
        dimensions[1].x = Math.max(offset, 0);
        Dimension minSize = new Dimension(Arrays.stream(dimensions).mapToInt(d -> d.x + d.width).max().orElse(0),
                dimensions[0].height + dimensions[1].height - PERSON_SIZE);

        setPreferredSize(minSize);
        updateSpacing();
    }

    private void updateSpacing() {
        if(Arrays.stream(dimensions).noneMatch(Objects::isNull)) {
            int minX = Arrays.stream(dimensions).mapToInt(d -> d.x).min().orElse(0);
            int minWidth = Arrays.stream(dimensions).mapToInt(d -> d.x - minX + d.width).max().orElse(0);
            int space = (getWidth() - minWidth) / 2;
            Arrays.stream(dimensions).forEach(d -> d.x = d.x - minX + space);
        }
    }

    private void expandUp(HashSet<TreeNode> added, TreeNode localRoot, int depth, boolean leftFree) {
        if (depth > UPPER_LIMIT || added.contains(localRoot)) return;
        added.add(localRoot);
        if (layers.size() <= depth) layers.add(new ArrayList<>());
        if (localRoot.parents.length > 0) {
            expandUp(added, localRoot.parents[0], depth + 1, true);
        } else if (leftFree && localRoot.spouse != null) {
            added.add(localRoot.spouse);
            if (localRoot.spouse.parents.length > 0) {
                expandUp(added, localRoot.spouse.parents[0], depth + 1, false);
            }
        }
        localRoot.children.forEach(c -> expandBackDown(added, c, depth - 1));
        if (!layers.get(depth).contains(localRoot.spouse)) layers.get(depth).add(localRoot);
        if (localRoot.spouse != null && !added.contains(localRoot.spouse)) {
            added.add(localRoot.spouse);
            if (localRoot.spouse.parents.length > 0) {
                expandUp(added, localRoot.spouse.parents[0], depth + 1, false);
            }
        }
    }

    private void expandBackDown(HashSet<TreeNode> added, TreeNode localRoot, int depth) {
        if (depth < 0) return;
        if (added.contains(localRoot) || (localRoot.spouse != null && added.contains(localRoot.spouse))) return;
        added.add(localRoot);
        for (TreeNode node : localRoot.children) {
            expandBackDown(added, node, depth - 1);
        }
        layers.get(depth).add(localRoot);
    }

    private Dimension expandDown(TreeNode localRoot, int depth) {
        int w = getSize(localRoot), h = PERSON_SIZE;
        if (localRoot.children.size() > 0 && depth < LOWER_LIMIT) {
            Dimension[] dimensionList = localRoot.children.stream()
                    .map(n -> expandDown(n, depth + 1)).toArray(Dimension[]::new);
            w = Math.max(w,
                    Arrays.stream(dimensionList).mapToInt(d -> d.width).sum() +
                            (localRoot.children.size() - 1) * HSPACING);
            h = dimensionList[0].height + PERSON_SIZE + VSPACING;
        }
        widths.put(localRoot, w);
        return new Dimension(w, h);
    }

    private Point getPoint(int layer, int pos) {
        int spacing = dimensions[0].width - layerWidths.get(layer);
        spacing /= layers.get(layer).size();
        return new Point(dimensions[0].x + layers.get(layer).stream().limit(pos)
                    //sum previous points & spacing
                    .mapToInt(this::getSize).sum() + (HSPACING + spacing) * pos
                    //center
                    + (getSize(layers.get(layer).get(pos)) + spacing)/2,
                dimensions[0].height - layer * (PERSON_SIZE + VSPACING) - PERSON_SIZE / 2);
    }

    private void redraw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        TreeNode node = this.person.getNode();
        System.out.println(Arrays.toString(dimensions));
        drawUp(g);
        if (widths.size() > 1)
            drawDown(g, node, dimensions[1].x, dimensions[1].y);
    }

    private void drawUp(Graphics g) {
        for (int i = 0; i < layers.size(); i++) {
            for (int j = 0; j < layers.get(i).size(); j++) {
                Point p1 = getPoint(i, j);
                TreeNode entry = layers.get(i).get(j);
                if (i < layers.size() - 1) {
                    g.setColor(Color.GRAY);
                    for (int k = 0; k < layers.get(i + 1).size(); k++) {
                        TreeNode entry2 = layers.get(i + 1).get(k);
                        if (entry.isParent(entry2)
                                || (entry.spouse != null && entry.spouse.isParent(entry2))) {
                            Point p2 = getPoint(i + 1, k);
                            int xOff = 0;
                            if (entry.spouse != null) {
                                xOff = PERSON_SIZE / (entry.spouse.isParent(entry2) ? 2 : -2);
                            }

                            g.drawLine(p1.x + xOff, p1.y, p2.x, p2.y);
                        }
                    }
                    g.setColor(Color.BLACK);
                }
                drawNode(g, entry, p1);
            }
        }
    }

    private Point drawDown(Graphics g, TreeNode node, int x, int y) {
        Point p = new Point(x + widths.get(node) / 2,
                y + PERSON_SIZE / 2);
        int offset = 0;
        for (TreeNode n : node.children) {
            Point p1 = drawDown(g, n, x + offset, y + VSPACING + PERSON_SIZE);
            int xOff = 0;
            if (n.spouse != null) {
                xOff = -PERSON_SIZE / 2;
            }
            g.setColor(Color.GRAY);
            g.drawLine(p.x, p.y, p1.x + xOff, p1.y);
            g.setColor(Color.BLACK);
            offset += widths.get(n) + HSPACING;
        }
        drawNode(g, node, p);
        return p;
    }

    private void drawNode(Graphics g, TreeNode node, Point p) {
        g.setColor(Color.GRAY);
        g.drawRect(p.x - getSize(node) / 2, p.y - PERSON_SIZE / 2,
                getSize(node), PERSON_SIZE);
        g.setColor(Color.BLACK);
        if (node.spouse != null) {
            FontMetrics fm = g.getFontMetrics();
            String plus = "+";
            int plusWidth = fm.stringWidth(plus);
            g.drawString(plus, p.x - plusWidth / 2, p.y + fm.getHeight() / 2);
            drawText(g, node.toString(), p.x - plusWidth / 2, p.y, -1);
            drawText(g, node.spouse.toString(), p.x + plusWidth / 2, p.y, 1);
        } else {
            drawText(g, node.toString(), p.x, p.y, 0);
        }
    }

    private void drawText(Graphics g, String text, int x, int y, int offset) {
        FontMetrics fm = g.getFontMetrics();
        String[] ss = text.split(" ");
        int width = Arrays.stream(ss).mapToInt(fm::stringWidth).max().orElse(0);
        for (int i = 0; i < ss.length; i++) {
            g.drawString(ss[i], x + (offset * width - fm.stringWidth(ss[i])) / 2,
                    y + fm.getHeight() * (2 * i + 2 - ss.length) / 2);
        }
    }

    private int getSize(TreeNode n) {
        return PERSON_SIZE * (n.spouse != null ? 2 : 1);
    }

    @Override
    public boolean listening(Type t) {
        return t == PERSON;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        updateSpacing();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
