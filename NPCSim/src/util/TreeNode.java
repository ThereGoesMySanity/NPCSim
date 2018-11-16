package util;

import people.Person;

import java.util.ArrayList;
import java.util.Arrays;

public class TreeNode {
    public Person value;
    public TreeNode[] parents = new TreeNode[0];
    public TreeNode spouse;
    public ArrayList<TreeNode> children;
    public TreeNode(Person value) {
        this.value = value;
        children = new ArrayList<>();
        value.setNode(this);
    }
    public void setParents(Person p) {
        parents = new TreeNode[2];
        parents[p.gender] = p.getNode();
        parents[p.getSpouse().gender] = p.getSpouse().getNode();
        Arrays.stream(parents).forEach(n -> n.addChild(this));
    }
    public void addChild(TreeNode node) {
        children.add(node);
    }
    public void setSpouse(Person spouse) {
        this.spouse = spouse.getNode();
    }
}
//    public TreeNode getRoot(Person p) {
//        TreeNode node = p.getNode();
//        while(node.parents != null) node = node.parents[0];
//        return node;
//    }
