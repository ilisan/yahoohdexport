package bg.bulsi.estatement.session;

import javax.ejb.Local;

/**
 * @author tzvetan.stefanov@bul-si.bg
 *
 */
@Local
public interface EStatement
{
    public void eStatement();
    public String getValue();
    public void setValue(String value);
    public void destroy();
    public void clear();

    // add additional interface methods here

}
