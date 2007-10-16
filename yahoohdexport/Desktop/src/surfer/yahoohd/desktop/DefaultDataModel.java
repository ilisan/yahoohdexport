package surfer.yahoohd.desktop;

import surfer.yahoohd.core.DataModel;

import java.util.List;
import java.util.ArrayList;

/**
 * created on: 2007-10-12 by tzvetan
 */
public class DefaultDataModel implements DataModel {
    private static DefaultDataModel model;
    List<String> selectedIndexes;

    static {
        model = new DefaultDataModel();
    }

    private DefaultDataModel() {
        selectedIndexes = new ArrayList<String>();
    }

    public String[] getSelectedIndexesAsArray() {
        return (String[]) selectedIndexes.toArray();
    }

    public List<String> getSelectedIndexes() {
        return selectedIndexes;
    }

    public void setSelectedIndexesFromArray(String[] array) {
        selectedIndexes.clear();
        for(String index: array){
            selectedIndexes.add(index);
        }
    }

    public void setSelectedIndexes(List<String> list) {
        selectedIndexes = list;
    }

    public DataModel getInstance() {
        return model;
    }
}
