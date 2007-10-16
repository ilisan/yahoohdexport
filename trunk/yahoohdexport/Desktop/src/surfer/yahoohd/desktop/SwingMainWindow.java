package surfer.yahoohd.desktop;

import surfer.yahoohd.core.LogWindow;
import surfer.yahoohd.core.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * created on: 2007-10-12 by tzvetan
 */
public class SwingMainWindow extends JFrame{
    private LogWindow logWindow;
    private MainPanel mainPanel;


    public SwingMainWindow(String title) throws HeadlessException {
        init();
        setTitle(title);
    }

    public SwingMainWindow() throws HeadlessException {
        init();
    }

    private void init(){
        setTitle("YahooHD");
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        logWindow = new SimpleLogWindow();
        JButton log = new JButton("Log Show/Hide");
        log.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                logWindow.setToggle();
            }
        });
        setLayout(new BorderLayout());
        mainPanel = new IndexesPanel();
        add((JPanel)mainPanel, BorderLayout.CENTER);
        add(log, BorderLayout.SOUTH);
        logWindow.log("Application init...");

    }

}
