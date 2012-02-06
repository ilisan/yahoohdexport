package bg.bulsi.municipalityweb.esign.sign.util.xml.validation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import org.apache.xml.security.Init;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.keyresolver.KeyResolverException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public final class XMLSignatureUtil {
	public static XMLSignatureData extractSignature(String signedXML)
			throws SignatureValidationException {
		Init.init();

		X509Certificate cert = null;
		boolean valid = false;
		String xml = "";
		try {
			Document doc = getXMLDocument(signedXML);

			String uri = doc.getFirstChild().getNodeName();
			Element sigElement = findSignatureElement(doc);

			if (sigElement != null) {
				XMLSignature signature = new XMLSignature(sigElement, uri);
				KeyInfo keyInfo = signature.getKeyInfo();
				if (keyInfo != null) {
					cert = signature.getKeyInfo().getX509Certificate();
					if (cert != null) {
						valid = signature.checkSignatureValue(cert);
						if (!valid)
							throw new SignatureValidationException(
									XMLSignatureErrorCode.INVALID_SIGNATURE);
					} else {
						PublicKey pk = signature.getKeyInfo().getPublicKey();
						if (pk != null) {
							valid = signature.checkSignatureValue(pk);
							if (!valid)
								throw new SignatureValidationException(
										XMLSignatureErrorCode.INVALID_SIGNATURE);
						} else {
							throw new SignatureValidationException(
									XMLSignatureErrorCode.INVALID_SIGNATURE);
						}
					}
				} else {
					throw new SignatureValidationException(XMLSignatureErrorCode.INVALID_SIGNATURE);
				}

				doc.getDocumentElement().removeChild(signature.getElement());
			} else {
				throw new SignatureValidationException(XMLSignatureErrorCode.MISSING_SIGNATURE);
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLUtils.outputDOMc14nWithComments(doc, baos);
			xml = baos.toString("utf-8");
			return new XMLSignatureData(cert, signedXML, xml);
		} catch (TransformerException e) {
			throw new SignatureValidationException(XMLSignatureErrorCode.MISSING_SIGNATURE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XMLSignatureException e) {
			throw new SignatureValidationException(XMLSignatureErrorCode.INVALID_SIGNATURE);
		} catch (KeyResolverException e) {
			throw new SignatureValidationException(XMLSignatureErrorCode.INVALID_SIGNATURE);
		} catch (XMLSecurityException e) {
			throw new SignatureValidationException(XMLSignatureErrorCode.INVALID_SIGNATURE);
		}

		return null;
	}

	private static Element findSignatureElement(Document doc) throws TransformerException {
		NodeList nodeList = doc.getChildNodes();
		Element result = null;

		if (nodeList.getLength() > 0) {
			Node root = nodeList.item(0);

			NodeList rootChildren = root.getChildNodes();

			for (int i = 0; i < rootChildren.getLength(); i++) {
				Node node = rootChildren.item(i);

				if ("ds:Signature".equals(node.getNodeName())) {
					result = (Element) node;
					break;
				}
			}
		} else {
			throw new TransformerException("Root element not found");
		}

		if (result != null) {
			return result;
		}
		throw new TransformerException("ds:Signature element not found");
	}

	private static Document getXMLDocument(String message) throws SignatureValidationException {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

			docBuilderFactory.setNamespaceAware(true);

			DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();

			byte[] tmpByteArray = message.getBytes("UTF-8");
			byte[] byteArray = null;
			;
			if ((tmpByteArray[0] == 239) && (tmpByteArray[1] == 187) && (tmpByteArray[2] == 191)) {
				byteArray = new byte[tmpByteArray.length - 3];
				System.arraycopy(tmpByteArray, 3, byteArray, 0, tmpByteArray.length - 3);
			} else {
				byteArray = tmpByteArray;
			}
			return documentBuilder.parse(new InputSource(new ByteArrayInputStream(byteArray)));
		} catch (Exception e) {
		}
		throw new SignatureValidationException(XMLSignatureErrorCode.INVALID_XML_STRUCTURE);
	}
}
