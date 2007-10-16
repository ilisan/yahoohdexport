package surfer.yahoohd.desktop;

import surfer.yahoohd.core.LogWindow;

import javax.swing.*;
import java.awt.*;

/**
 * created on: 2007-10-12 by tzvetan
 */
public class SimpleLogWindow implements LogWindow {
    JFrame frame;
    JTextArea textArea;

    public SimpleLogWindow() {
        frame = new JFrame("Log");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        textArea = new JTextArea(30, 40);
        textArea.setBackground(Color.LIGHT_GRAY);
        textArea.setForeground(Color.WHITE);
        Container c = frame.getContentPane();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        c.add(scrollPane);
        frame.pack();
        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(ss.width / 2 - frame.getWidth() / 2, ss.height / 2 - frame.getHeight() / 2,
                frame.getWidth(), frame.getHeight());

    }

    public synchronized void log(String log) {
        textArea.append(log);
        textArea.append("\n");
    }

    public synchronized void setToggle() {
        frame.setVisible(!frame.isVisible());
        if (frame.isVisible()) {
            frame.requestFocus();
        }
    }
}
