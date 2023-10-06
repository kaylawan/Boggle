package assignment;

import java.io.IOException;
import java.io.File;
import java.util.Iterator;
import java.util.Scanner;

public class GameDictionary implements BoggleDictionary {

    private TrieNode root;

    //change back
    public void addWord(String word, int index, TrieNode currNode) {
        if(currNode == null) return;
        for(TrieNode child : currNode.getChildren()) {
            if(child.getVal() == word.charAt(index)) {
                if(index == word.length()-1) child.setEnd(true);
                else addWord(word, index+1, child);
                return;
            }
        }

        TrieNode newChild = new TrieNode(word.charAt(index), (index == word.length()-1), currNode);
        currNode.addChild(newChild);

        if(index != word.length()-1) addWord(word, index+1, newChild);
    }

    private TrieNode findWord(String word, int index, TrieNode currNode) {
        if(currNode == null) return null;
        for(TrieNode child : currNode.getChildren()) {
            if(child.getVal() == word.charAt(index)) {
                if(index == word.length()-1) return child;
                else return findWord(word, index+1, child);
            }
        }
        return null;
    }

    public void loadDictionary(String filename) throws IOException {
        root = new TrieNode(Character.MIN_VALUE, false, null);
        String word;
        Scanner sc = new Scanner(new File(filename));
        while(sc.hasNextLine()) {
            word = sc.nextLine();
            addWord(word.toLowerCase(), 0, root);
        }
        sc.close();
    }

    public boolean isPrefix(String prefix) {
        if(prefix == null) return true;
        TrieNode getWord = findWord(prefix.toLowerCase(), 0, root);
        return (getWord != null);
    }

    public boolean contains(String word) {
        if(word == null) return true;
        TrieNode getWord = findWord(word.toLowerCase(), 0, root);
        return (getWord != null && getWord.isEnd());
    }

    @Override
    public Iterator<String> iterator() {
        return (new Iter());
    }

    class Iter implements Iterator<String> {
        private TrieNode currNext;

        Iter() {
            currNext = root;
        }
        private String constructString(TrieNode curr) {
            if(curr == null) return "";
            StringBuilder ret = new StringBuilder();
            while(curr.getParent() != null) {
                ret.append(curr.getVal());
                curr = curr.getParent();
            }
            return ret.reverse().toString();
        }

        private TrieNode nextChild(TrieNode child) {
            if(child == null || child.getParent() == null) return null;
            for(int i=0;i<child.getParent().getChildren().size();i++) {
                if(child.getParent().getChildren().get(i) == child) {
                    if(i+1 == child.getParent().getChildren().size()) return null;
                    else return child.getParent().getChildren().get(i+1);
                }
            }
            return null; // should never be here
        }

        public boolean hasNext() {
            if(currNext == null) return false;

            if(currNext.getChildren().size() > 0) return true;
            TrieNode temp = currNext;
            while(nextChild(temp) == null && temp.getParent() != null) temp = temp.getParent();
            return temp.getParent() != null;
        }

        public String next() {
            if(currNext == null) return null;

            if(currNext.getChildren().size() == 0) {
                while(nextChild(currNext) == null) currNext = currNext.getParent();
                if(currNext.getParent() == null) return null; // this is the last one
                currNext = nextChild(currNext);
            }
            else {
                currNext = currNext.getChildren().get(0);
            }
            while(!currNext.isEnd()) currNext = currNext.getChildren().get(0);
            return constructString(currNext);
        }
    }

}

