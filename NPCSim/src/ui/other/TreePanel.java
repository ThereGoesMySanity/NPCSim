package ui.other;

import main.Main;
import people.Person;
import util.FamilyTree;
import util.FamilyTree.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static ui.map.DetailsListener.DLListener;
import static ui.map.DetailsPanel.DetailsObject;
import static ui.map.DetailsPanel.Type;
import static ui.map.DetailsPanel.Type.PERSON;

public class TreePanel extends JPanel implements DLListener {
    private static final int PERSON_SIZE = 100, HSPACING = 50, VSPACING = 150;
    private final FamilyTree tree = Main.tree;
    private Canvas canvas = new Canvas();
    private int height;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setPreferredSize(canvas.getSize());
        canvas.paint(g);
    }

    @Override
    public void onChange(DetailsObject o) {
        Graphics g = canvas.getGraphics();
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if(o == null) return;
        Person current = (Person) o;
        TreeNode root = tree.getRoot(current);
        HashMap<TreeNode, Integer> widths = new HashMap<>();
        height = 0;
        int width = calcWidths(widths, root, 1);
        height *= PERSON_SIZE * 2;
        canvas.setSize(width, height);
        drawNode(g, root, widths, 0, 0);
    }
    private Point drawNode(Graphics g, TreeNode node, HashMap<TreeNode, Integer> widths, int x, int y) {
        int w = widths.get(node);
        Point p = new Point(x + w/2, y + PERSON_SIZE / 2);
        g.fillRect(p.x - getSize(node)/2, y, getSize(node), PERSON_SIZE);
        int offset = 0;
        for(TreeNode n : node.children) {
            Point p1 = drawNode(g, n, widths, x + offset, y + VSPACING + PERSON_SIZE);
            g.drawLine(p.x, p.y, p1.x, p1.y);
            offset += widths.get(n);
        }
        return p;
    }
    private int calcWidths(HashMap<TreeNode, Integer> widths, TreeNode localRoot, int depth) {
        if(depth > height) height = depth;
        int w;
        if(localRoot.children.size() == 0) w = getSize(localRoot) + HSPACING;
        else w = localRoot.children.stream()
                    .mapToInt(n -> this.calcWidths(widths, n, depth + 1)).sum();
        widths.put(localRoot, w);
        return w;
    }

    private int getSize(TreeNode n) {
        return PERSON_SIZE * (n.value.spouse != null? 2 : 1);
    }

    @Override
    public boolean listening(Type t) {
        return t == PERSON;
    }
}
