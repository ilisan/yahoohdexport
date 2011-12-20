package bg.bulsi.estatement.register.impl;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import bg.bulsi.estatement.register.IRegPortal;

/**
 * @author tzvetan.stefanov@bul-si.bg
 *
 */
@Name("DummyRegPortal")
public class DummyRegPortal implements IRegPortal {

    @Logger 
    private Log log;

    public String registerXmlDocument(String xmlDoc) {
        log.info("Registering statement: #0", xmlDoc);
        String docId = Long.toString(System.currentTimeMillis());
        log.info("Generated document id: #0", docId);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Dummy document id.
		return docId;
	}

	public void editDocument(String documentId, String xmlDoc) {
		log.info("Editing document with id:", documentId);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getXmlDocument(String documentId) {
		log.info("Getting document with id:", documentId);
		return "<DocId>" + documentId + "</DocId>";
	}

}
