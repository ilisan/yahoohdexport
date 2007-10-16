package surfer.yahoohd.desktop;

import surfer.yahoohd.core.LogWindow;
import surfer.yahoohd.core.MainPanel;
import surfer.yahoohd.core.HDGrabbler;

import java.util.List;

/**
 * created on: 2007-10-12 by tzvetan
 */
public class YahooHDGrabbler implements Runnable, HDGrabbler {
    private LogWindow log;
    private MainPanel mainPanel;
    private boolean running = true;
    private static final long SLEEP_TIME = 3000;

    public void run() {
        if(log == null || mainPanel == null){
            running = false;
            System.err.println("ERROR: No Log and MainPanel set!");
        }
        while(running){
            List<String> indexes;
            try {
                indexes = mainPanel.getSelectedIndexes();
                System.out.println("Indexes to fetch:");
                log.log(indexes.toString());
                System.out.println("\n");
                Thread.sleep(SLEEP_TIME);
            }
            catch (Exception e) {
                e.printStackTrace();
                stop();
            }
        }
    }

    public void setMainPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void setLog(LogWindow log) {
        this.log = log;
    }

    public void stop(){
        running = false;
    }
}

//Sample output

//http://ichart.finance.yahoo.com/table.csv?s=MSFT&ignore=.csv

//Date,Open,High,Low,Close,Volume,Adj Close
//2007-10-11,30.30,30.63,29.79,29.91,50788400,29.91
//2007-10-10,30.04,30.37,30.03,30.23,32251500,30.23
//2007-10-09,30.03,30.39,30.00,30.10,63603100,30.10
//2007-10-08,29.66,29.85,29.60,29.84,30265400,29.84
//2007-10-05,29.89,29.99,29.73,29.84,45012300,29.84
//2007-10-04,29.56,29.77,29.44,29.71,37868000,29.71
