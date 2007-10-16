/**
 * Created on 2007-10-16 by Surfer Dude (surfer.bg@gmail.com)
 */

package surfer.yahoohd.core;

import java.io.OutputStream;

public interface Logger {
    public void log(String s);
    public void log(String s, int level);
    public void setLevel(int level);
    public void setOutput(OutputStream out);
}
