package assignment;

import java.util.*;

public class TrieNode {
    private char val;
    private TrieNode parent;
    private ArrayList<TrieNode> children;
    private boolean end;

    TrieNode(char c, boolean e, TrieNode par) {
        val = c;
        end = e;
        children = new ArrayList<>(0);
        parent = par;
    }

    public char getVal() {
        return val;
    }

    public TrieNode getParent() {
        return parent;
    }

    public ArrayList<TrieNode> getChildren() {
        return children;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean e) {
        end = e;
    }
    public void addChild(TrieNode newChild) {
        children.add(newChild);
    }

}
