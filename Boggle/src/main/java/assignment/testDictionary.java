package assignment;

import java.io.IOException;
import java.util.Iterator;

public class testDictionary implements BoggleDictionary{
    @Override
    public void loadDictionary(String filename) throws IOException {

    }

    @Override
    public boolean isPrefix(String prefix) {
        return false;
    }

    @Override
    public boolean contains(String word) {
        return false;
    }

    @Override
    public Iterator<String> iterator() {
        return null;
    }
}
