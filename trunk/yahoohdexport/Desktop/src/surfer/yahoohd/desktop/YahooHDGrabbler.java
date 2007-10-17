package surfer.yahoohd.desktop;

import surfer.yahoohd.core.LogWindow;
import surfer.yahoohd.core.MainPanel;
import surfer.yahoohd.core.HDGrabbler;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.ParseException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.StringTokenizer;

/**
 * created on: 2007-10-12 by tzvetan
 */
public class YahooHDGrabbler implements Runnable, HDGrabbler {
    private LogWindow log;
    private MainPanel mainPanel;
    private boolean running = true;
    private static final long SLEEP_TIME = 10000;
    private static final String WEB_ADDRESS_FORMAT =
            "http://ichart.finance.yahoo.com/table.csv?s={0}&ignore=.csv";

    public void run() {
        ListModel indexes;
        indexes = mainPanel.getSelectedIndexes();
        if (log == null || mainPanel == null) {
            running = false;
            System.err.println("ERROR: No Log and MainPanel properties set!");
        }
        while (running) {
            try {
                log.log("Indexes to fetch:");
                // Cycle through indexes list
                for (int i = 0; i < indexes.getSize(); i++) {
                    String index = (String) indexes.getElementAt(i);
                    log.log(index);
                    TableModel model = fetchIndex(index);
                    if (mainPanel.getTables().containsKey(index)) {
                        JTable table = mainPanel.getTables().get(index);
                        table.setModel(model);
                        table.updateUI();
                        table.revalidate();
                    }
                    if (!running) {
                        break;
                    }
                }
                if (running) {
                    Thread.sleep(SLEEP_TIME);
                }
            }
            catch (Exception e) {
                log.log(e.toString());
                stop();
            }
        }
    }

    private TableModel fetchIndex(String index) throws IOException {

        URL url = new URL(MessageFormat.format(WEB_ADDRESS_FORMAT, URLEncoder.encode(index, "utf-8")));
        log.log("Request: " + url.toString());
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));


        TableModel model;
        try {
            model = parseCSVData(in, index);
        }
        catch (Exception e) {
            log.log(e.toString());
            model = null;
        }
        in.close();
        return model;
    }

    // Parse index csv historical data
    private TableModel parseCSVData(BufferedReader in, String index) throws IOException, ParseException {
        String inputLine;

        // Skip header line
        // TODO: Add functionality to use provided headers!?
        in.readLine();
        HDTableModel model = new HDTableModel();

        while ((inputLine = in.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(inputLine, ",");
            StockData sd = new StockData();

            sd.setDate(StockData.getDateFormat().parse(st.nextToken()));
            sd.setOpen(Double.parseDouble(st.nextToken()));
            sd.setHigh(Double.parseDouble(st.nextToken()));
            sd.setLow(Double.parseDouble(st.nextToken()));
            sd.setClose(Double.parseDouble(st.nextToken()));
            sd.setVolume(Long.parseLong(st.nextToken()));
            sd.setAdjClose(Double.parseDouble(st.nextToken()));
            sd.setSymbol(index);

            model.add(sd);
        }
        return model;
    }

    public void setMainPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void setLog(LogWindow log) {
        this.log = log;
    }

    public void stop() {
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
