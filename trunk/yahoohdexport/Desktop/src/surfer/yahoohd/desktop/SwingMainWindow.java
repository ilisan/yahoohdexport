package surfer.yahoohd.desktop;

import surfer.yahoohd.core.LogWindow;
import surfer.yahoohd.core.MainPanel;
import surfer.yahoohd.core.HDGrabbler;

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
    Thread fetcher;
    YahooHDGrabbler grabbler;


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
                setVisible(false);
                logWindow.setVisible(false);
                if(fetcher != null && grabbler != null){
                    grabbler.stop();
                    try {
                        fetcher.join();
                    }
                    catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
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
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        mainPanel = new IndexesPanel();
        c.add((JPanel)mainPanel, BorderLayout.CENTER);
        c.add(log, BorderLayout.SOUTH);
        logWindow.log("Application init...");

        // Start indexes fetcher thread
        grabbler = new YahooHDGrabbler();
        grabbler.setLog(logWindow);
        grabbler.setMainPanel(mainPanel);
        fetcher = new Thread(grabbler);
        fetcher.start();

        setVisible(true);

    }

}
