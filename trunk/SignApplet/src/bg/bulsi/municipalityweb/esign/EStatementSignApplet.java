package bg.bulsi.municipalityweb.esign;

import java.io.IOException;
import java.security.cert.X509Certificate;

import netscape.javascript.JSObject;
import bg.bulsi.municipalityweb.esign.sign.util.DigitalSignatureException;
import bg.bulsi.municipalityweb.esign.sign.util.applet.SmartCardApplet;
import bg.bulsi.municipalityweb.esign.sign.util.xml.CertificateSignUtils;
import bg.bulsi.municipalityweb.esign.sign.util.xml.CertificationChainAndSignatureBase64;

/**
 * @author tzvetan.stefanov@bul-si.bg
 *
 */
public class EStatementSignApplet extends SmartCardApplet {

	private static final long serialVersionUID = 1L;
	/**
	 * Идентификатор на формата, която съдържа елемента
	 * съдържащ XML документа за подписване.
	 */
	private static final String FORM_ID = "formId";
	/**
	 * Елемента съдържащ XML-а за подписване
	 */
	private static final String XML_TO_SIGN_PARAMETER = "xmlToSign";
	/**
	 * Елемента в който се записва подписания документ.
	 */
	private static final String SIGNED_XML_MESSAGE = "signedXml";
	/**
	 * Бутона за изпращане на формата след
	 * като е извършено подписването.
	 */
	private static final String SUBMIT_BUTTON = "submitButton";

	protected void signButtonPressed(X509Certificate certificate, JSObject browserWindow,
			CertificationChainAndSignatureBase64 certificationChain)
			throws DigitalSignatureException {
		String formId = getParameter(FORM_ID);
		String xmlToSignId = getParameter(XML_TO_SIGN_PARAMETER);
		String signedXmlId = getParameter(SIGNED_XML_MESSAGE);
		String submitButtonId = getParameter(SUBMIT_BUTTON);

		JSObject mainForm = (JSObject) browserWindow.eval(
				"document.getElementById('" + formId + "')");

		System.out.println("get xml to sign");

		JSObject xmlToSignField = (JSObject) mainForm.getMember(xmlToSignId);
		String xml = (String) xmlToSignField.getMember("value");
		try {
			System.out.println("sign xml");
			String signedXml = CertificateSignUtils.signXML(xml,
					certificationChain.privateKeyAndCertChain);

			System.out.println("submit the form");

			JSObject signedXmlField = (JSObject) mainForm.getMember(signedXmlId);

			signedXmlField.setMember("value", signedXml);

			mainForm.eval("document.getElementById('" + submitButtonId + "').onclick();");
		} catch (IOException e) {
			throw new DigitalSignatureException(e.getMessage(), e);
		}
	}
}
