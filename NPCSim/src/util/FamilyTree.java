package util;

import people.Person;

import java.util.ArrayList;
import java.util.HashMap;

public class FamilyTree {
    public static class TreeNode<T> {
        T value;
        TreeNode<T> parent;
        TreeNode<T> spouse;
        ArrayList<TreeNode<T>> children;
        public TreeNode(T value, TreeNode<T> parent) {
            this.value = value;
            this.parent = parent;
            children = new ArrayList<>();
        }
        public void addChild(T child) {
            children.add(new TreeNode<>(child, this));
        }
    }
    private ArrayList<TreeNode<Person>> roots;
    private HashMap<Person, TreeNode<Person>> nodes;
    public FamilyTree() {
        roots = new ArrayList<>();
        nodes = new
    }
}
