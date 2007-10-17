package surfer.yahoohd.desktop;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * created on: 2007-10-17 by tzvetan
 */
public class StockData {
    private String symbol;
    private String name;
    private Date date;
    private double open;
    private double high;
    private double low;
    private double close;
    private double adjClose;
    private long volume;

    private static final int columns = 9;
    private static final String[] columnNames = {"Symbol", "Name", "Date", "Open", "High",
    "Low", "Close", "Adj Close", "Volume"};
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-DD");


    public static SimpleDateFormat getDateFormat() {
        return DATE_FORMAT;
    }


    public static int getColumnCount() {
        return columns;
    }

    public static String getColumnName(int index) {
        return columnNames[index];
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getAdjClose() {
        return adjClose;
    }

    public void setAdjClose(double adjClose) {
        this.adjClose = adjClose;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }
}
