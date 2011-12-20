package bg.bulsi.estatement.regio.impl;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import bg.bulsi.estatement.regio.IRegIo;

/**
 * @author tzvetan.stefanov@bul-si.bg
 *
 */
@Name("DummyRegIo")
public class DummyRegIo implements IRegIo {

    @Logger 
    private Log log;

    public int getPaymentCode(String docId) {
    	log.info("Payment info request for document with id: #0", docId);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Always payed.
		return 0;
	}

}
