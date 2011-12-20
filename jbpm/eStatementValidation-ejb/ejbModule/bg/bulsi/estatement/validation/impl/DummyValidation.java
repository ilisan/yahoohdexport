package bg.bulsi.estatement.validation.impl;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import bg.bulsi.estatement.validation.IValidation;

/**
 * @author tzvetan.stefanov@bul-si.bg
 *
 */
@Name("DummyValidation")
public class DummyValidation implements IValidation {

    @Logger 
    private Log log;

    public String validate(String xmlDoc) {
        log.info("Validating document: " + xmlDoc);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "valid";
	}

}
