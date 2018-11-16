package util;

import people.Person;
import people.PersonListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FamilyTree implements PersonListener {
    public static class TreeNode {
        public Person value;
        TreeNode parent;
        TreeNode spouse;
        public ArrayList<TreeNode> children;
        TreeNode(Person value, TreeNode parent) {
            this.value = value;
            this.parent = parent;
            children = new ArrayList<>();
        }
        void addChild(Person child) {
            children.add(new TreeNode(child, this));
        }
        void setSpouse(TreeNode spouse) {
            this.spouse = spouse;
        }
    }
    private HashMap<Person, TreeNode> nodes;
    public FamilyTree() {
        nodes = new HashMap<>();
    }
    public void add(Person p) {
        TreeNode node = new TreeNode(p, null);
        nodes.put(p, node);
    }
    private void addChild(Person p, Person child) {
        nodes.get(p).addChild(child);
    }
    private void addSpouse(Person p, Person p1) {
        nodes.get(p).setSpouse(nodes.get(p1));
    }
    public TreeNode getRoot(Person p) {
        TreeNode node = nodes.get(p);
        while(node.parent != null) node = node.parent;
        return node;
    }
    @Override
    public void onChild(Person p, Person child) {
        addChild(p, child);
    }

    @Override
    public void onMarry(Person p, Person spouse) {
        addSpouse(p, spouse);
    }

    @Override
    public void onDeath(Person p) { }
}
