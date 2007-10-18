package surfer.yahoohd.core;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * created on: 2007-10-12 by tzvetan
 */
public interface MainPanel{
    ListModel getSelectedIndexes();
    Map<String, JTable> getTables();
    JPanel getPanel();

}
