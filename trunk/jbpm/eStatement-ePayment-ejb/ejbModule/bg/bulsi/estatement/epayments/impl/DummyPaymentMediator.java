package bg.bulsi.estatement.epayments.impl;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import bg.bulsi.estatement.epayments.IPaymentMediator;

/**
 * @author tzvetan.stefanov@bul-si.bg
 *
 */
@Name("DummyPaymentMediator")
public class DummyPaymentMediator implements IPaymentMediator {

    @Logger 
    private Log log;
	
	public void createPayment(String docId, int paymentCode) {
		log.info("Create payment code: #0 document id #1.", paymentCode, docId);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
