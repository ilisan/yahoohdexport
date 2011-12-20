package bg.bulsi.estatement.register;


/**
 * @author tzvetan.stefanov@bul-si.bg
 *
 */
public interface IRegPortal {
	/**
	 * Регистрира XML документ.
	 * @param xmlDoc
	 * @return входящия номер на документа.
	 */
	public String registerXmlDocument(String xmlDoc);
	
	/**
	 * Извлича регистриран документ по идентификатор.
	 * @param documentId
	 * @return
	 */
	public String getXmlDocument(String documentId);
	
	/**
	 * Редактиране на съществуващ документ.
	 * @param documentId
	 * @param xmlDoc
	 */
	public void editDocument(String documentId, String xmlDoc);

}
