package bg.bulsi.municipalityweb.esign.sign.util.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xml.security.Init;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;

import bg.bulsi.municipalityweb.esign.sign.util.Base64Utils;
import bg.bulsi.municipalityweb.esign.sign.util.DigitalSignatureException;

public final class CertificateSignUtils {
	public static String signXML(String xmlToSign, PrivateKeyAndCertChain privateKeyAndCertChain)
			throws IOException, DigitalSignatureException {
		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			Init.init();

			String sigAlgorithm = "http://www.w3.org/2000/09/xmldsig#rsa-sha1";

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

			docBuilderFactory.setNamespaceAware(true);

			DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();

			Document doc = documentBuilder.parse(new ByteArrayInputStream(xmlToSign
					.getBytes("UTF-8")));

			XMLSignature signature = new XMLSignature(doc, null, sigAlgorithm);
			doc.getDocumentElement().appendChild(signature.getElement());

			Transforms transforms = new Transforms(doc);

			transforms.addTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature");
			transforms.addTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments");
			signature.addDocument("", transforms, "http://www.w3.org/2000/09/xmldsig#sha1");

			X509Certificate cert = (X509Certificate) privateKeyAndCertChain.certificationChain[0];

			signature.addKeyInfo(cert);
			signature.addKeyInfo(cert.getPublicKey());
			signature.sign(privateKeyAndCertChain.privateKey);

			baos = new ByteArrayOutputStream();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			XMLUtils.outputDOMc14nWithComments(doc, os);
			result = new String(os.toByteArray(), "UTF-8");
		} catch (Exception ex) {
			throw new DigitalSignatureException(ex.getMessage(), ex);
		} finally {
			if (baos != null) {
				baos.close();
			}
		}
		return result;
	}

	public static CertificationChainAndSignatureBase64 getCertificationChain(KeyStore keystore,
			String selectedCertAlias) throws DigitalSignatureException {
		try {
			PrivateKeyAndCertChain privateKeyAndCertChain = null;
			privateKeyAndCertChain = getPrivateKeyAndCertChain(keystore, selectedCertAlias);

			CertificationChainAndSignatureBase64 signingResult = new CertificationChainAndSignatureBase64();
			signingResult.privateKeyAndCertChain = privateKeyAndCertChain;

			signingResult.certificationChain = encodeX509CertChainToBase64(privateKeyAndCertChain.certificationChain);

			return signingResult;
		} catch (Exception e) {
			throw new DigitalSignatureException(e.getMessage(), e);
		}
	}

	private static PrivateKeyAndCertChain getPrivateKeyAndCertChain(KeyStore aKeyStore, String alias)
			throws Exception {
		Enumeration aliasesEnum = aKeyStore.aliases();
		if (aliasesEnum.hasMoreElements()) {
			List aliasList = new ArrayList();
			List certList = new ArrayList();

			while (aliasesEnum.hasMoreElements()) {
				String currentAlias = (String) aliasesEnum.nextElement();
				Certificate cert = aKeyStore.getCertificate(currentAlias);
				if (cert != null) {
					certList.add(cert.toString());
					aliasList.add(currentAlias);
				}
			}

			Certificate[] certificationChain = aKeyStore.getCertificateChain(alias);

			PrivateKey privateKey = (PrivateKey) aKeyStore.getKey(alias, null);
			PrivateKeyAndCertChain result = new PrivateKeyAndCertChain();
			result.privateKey = privateKey;
			result.certificationChain = certificationChain;
			return result;
		}
		throw new KeyStoreException("The keystore is empty!");
	}

	private static String encodeX509CertChainToBase64(Certificate[] certificationChain)
			throws CertificateException {
		List certList = Arrays.asList(certificationChain);
		CertificateFactory certFactory = CertificateFactory.getInstance("X.509");

		CertPath certPath = certFactory.generateCertPath(certList);
		byte[] certPathEncoded = certPath.getEncoded("PkiPath");

		String base64encodedCertChain = Base64Utils.base64Encode(certPathEncoded);

		return base64encodedCertChain;
	}
}
