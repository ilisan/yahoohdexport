package surfer.yahoohd.desktop;

import surfer.yahoohd.core.MainPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

/**
 * created on: 2007-10-12 by tzvetan
 */
public class IndexesPanel extends JPanel implements MainPanel {
    //    List<String> availableIndexes =  new ArrayList<String>();
    IndexesListModel listModel = new IndexesListModel();
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


        // Clear list selection on type in text field
        textField.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
                indexesList.clearSelection();
                indexesList.updateUI();
                selectedIndex = -1;
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }
        });

        // Add text field value to indexes list
        addIndex.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String index = textField.getText();
                if (!index.isEmpty()) {
                    listModel.add(index);
                    indexesList.updateUI();
                }
            }
        });


        indexesList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    selectedIndex = indexesList.getSelectedIndex();
                    textField.setText((String) indexesList.getSelectedValue());
                }
            }
        });

        // Remove index from list
        removeIndex.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex > -1) {
                    listModel.remove(selectedIndex);
                    textField.setText("");
                    indexesList.updateUI();
                    selectedIndex = -1;
                } else {
                    listModel.remove(textField.getText());
                    textField.setText("");
                    indexesList.updateUI();
                    selectedIndex = -1;
                }
            }
        });
    }

    public List<String> getSelectedIndexes() {
        List<String> result = new ArrayList<String>(listModel.getSize());
        for (int i = 0; i < listModel.getSize(); i++) {
            result.add((String) listModel.getElementAt(i));
        }
        return result;
    }
}
