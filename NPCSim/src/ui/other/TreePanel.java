package ui.other;

import main.Main;
import people.Person;
import util.FamilyTree;
import util.FamilyTree.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

import static ui.map.DetailsListener.DLListener;
import static ui.map.DetailsPanel.DetailsObject;
import static ui.map.DetailsPanel.Type;
import static ui.map.DetailsPanel.Type.PERSON;

public class TreePanel extends JPanel implements DLListener {
    private static final int
            PERSON_SIZE = 40,
            HSPACING = 20,
            VSPACING = 60;
    private final FamilyTree tree = Main.tree;
    private Person person;
    private boolean redraw = false;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(redraw) redraw(g);
    }

    @Override
    public void onChange(DetailsObject o) {
        if (o != null) {
            person = (Person) o;
            redraw = true;
        }
    }
    private void redraw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        TreeNode root = tree.getRoot(person);
        HashMap<TreeNode, Integer> widths = new HashMap<>();
        Dimension d = calcWidths(widths, root, 0);
        setPreferredSize(d);
        drawNode(g, root, widths, (getWidth() - d.width)/2, 0);
        redraw = false;
    }
    private Point drawNode(Graphics g, TreeNode node, HashMap<TreeNode, Integer> widths, int x, int y) {
        Point p = new Point(x + widths.get(node)/2,
                y + PERSON_SIZE / 2);
        System.out.println(p);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(node.value.toString(), p.x - fm.stringWidth(node.value.toString()) / 2, p.y);
        int offset = 0;
        for(TreeNode n : node.children) {
            Point p1 = drawNode(g, n, widths, x + offset, y + VSPACING + PERSON_SIZE);
            g.drawLine(p.x, p.y, p1.x, p1.y);
            offset += widths.get(n);
        }
        return p;
    }
    private Dimension calcWidths(HashMap<TreeNode, Integer> widths, TreeNode localRoot, int depth) {
        int w = getSize(localRoot) + HSPACING,
                h = PERSON_SIZE;
        if(localRoot.children.size() > 0) {
            Dimension[] dimensionList = localRoot.children.stream()
                    .map(n -> this.calcWidths(widths, n, depth + 1)).toArray(Dimension[]::new);
            w = Arrays.stream(dimensionList).mapToInt(d -> d.width).sum();
            h = dimensionList[0].height + PERSON_SIZE + VSPACING;
        }
        widths.put(localRoot, w);
        return new Dimension(w, h);
    }

    private int getSize(TreeNode n) {
        return PERSON_SIZE * (n.value.spouse != null? 2 : 1);
    }

    @Override
    public boolean listening(Type t) {
        return t == PERSON;
    }
}
