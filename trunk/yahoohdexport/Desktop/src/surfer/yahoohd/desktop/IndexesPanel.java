package surfer.yahoohd.desktop;

import surfer.yahoohd.core.MainPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

/**
 * created on: 2007-10-12 by tzvetan
 */
public class IndexesPanel extends JPanel implements MainPanel {
//    List<String> availableIndexes =  new ArrayList<String>();
    DefaultListModel listModel = new DefaultListModel();
    JList indexesList;
    JTextField textField;
    int selectedIndex = -1;

    public IndexesPanel() {
        setLayout(new BorderLayout());
        indexesList = new JList(listModel);
        indexesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(indexesList);
        add(scrollPane, BorderLayout.CENTER);

        textField = new JTextField(6);
        JButton addIndex = new JButton("Add");
        JButton removeIndex = new JButton("Remove");
        JPanel controls = new JPanel(new BorderLayout(4, 2));

        controls.add(textField, BorderLayout.WEST);
        controls.add(addIndex, BorderLayout.CENTER);
        controls.add(removeIndex, BorderLayout.EAST);
        add(controls, BorderLayout.SOUTH);

        addIndex.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                String index = textField.getText();
                if(!index.isEmpty()){
                    listModel.addElement(index.toUpperCase());
                }
            }
        });
        indexesList.addListSelectionListener(new ListSelectionListener(){

            public void valueChanged(ListSelectionEvent e) {
                selectedIndex = indexesList.getSelectedIndex();
                textField.setText((String) indexesList.getSelectedValue());
            }
        });

        removeIndex.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                if(selectedIndex > -1){
                    listModel.remove(selectedIndex);
                    textField.setText("");
                    selectedIndex = -1;
                }
            }
        });
    }

    public List<String> getSelectedIndexes() {
        List<String> result = new ArrayList<String>(listModel.size());
        for(int i = 0; i < listModel.size(); i++){
            result.add((String) listModel.getElementAt(i));
        }
        return result;
    }
}
