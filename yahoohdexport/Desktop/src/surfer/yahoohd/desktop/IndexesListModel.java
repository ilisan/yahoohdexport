package surfer.yahoohd.desktop;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;

/**
 * created on: 2007-10-16 by tzvetan
 */
public class IndexesListModel extends AbstractListModel {
    private List<String> indexes = new ArrayList<String>();
    private static final String INDEXES_FILE = "indexes.dat";

    public IndexesListModel() {
        load();
    }

    private void save() {
        try {
            XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(INDEXES_FILE)));
            encoder.writeObject(indexes);
            encoder.close();
        }
        catch (FileNotFoundException e) {
            System.err.println(e.toString());
        }
    }

    private void load() {
        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(INDEXES_FILE)));
            indexes = (List<String>) decoder.readObject();
            decoder.close();
        }
        catch (FileNotFoundException e) {
            System.err.println(e.toString());
        }
    }

    public synchronized int getSize() {
        return indexes.size();
    }

    public synchronized Object getElementAt(int index) {
        return indexes.get(index);
    }

    public synchronized void add(String s) {
        indexes.add(s.toUpperCase());
        save();
    }

    public synchronized void remove(int index) {
        indexes.remove(index);
        save();
    }

    public synchronized void remove(String s){
        indexes.remove(s.toUpperCase());
        save();
    }
}
