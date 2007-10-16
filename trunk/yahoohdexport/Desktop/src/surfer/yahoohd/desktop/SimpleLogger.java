/**
 * Created on 2007-10-16 by Surfer Dude (surfer.bg@gmail.com)
 */

package surfer.yahoohd.desktop;

import surfer.yahoohd.core.Logger;
import java.io.*;

public class SimpleLogger implements Logger {
    private BufferedWriter out;

    public void log(String s) {
        try {
            out.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String s, int level) {
        log(s);
    }

    public void setLevel(int level) {
        // TODO Implement method
    }

    public void setOutput(OutputStream out) {
        this.out = new BufferedWriter(new OutputStreamWriter(out));
    }
}
