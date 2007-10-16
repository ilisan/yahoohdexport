package surfer.yahoohd.core;

import java.util.List;

/**
 * created on: 2007-10-12 by tzvetan
 */
public interface DataModel {
//    List<String> selectedIndexes = null;
    String[] getSelectedIndexesAsArray();
    List<String> getSelectedIndexes();
    void setSelectedIndexesFromArray(String[] array);
    void setSelectedIndexes(List<String> list);
    DataModel getInstance();
}
