package bg.bulsi.municipalityweb.esign.sign.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.X509Extensions;

public final class CertificateUtil implements Serializable {
	public static final String PKCS11_KEYSTORE_TYPE = "PKCS11";
	public static final String X509_CERTIFICATE_TYPE = "X.509";
	public static final String CERTIFICATION_CHAIN_ENCODING = "PkiPath";
	public static final String SUN_PKCS11_PROVIDER_CLASS = "sun.security.pkcs11.SunPKCS11";
	private static final long serialVersionUID = 5964936019519196466L;

	public static String getAuthorityKey(X509Certificate certificate) {
		String issuerId = "";

		byte[] policyInformationExtensionValue = certificate
				.getExtensionValue(X509Extensions.AuthorityKeyIdentifier.toString());

		if (policyInformationExtensionValue == null) {
			throw new IllegalArgumentException("The issuer of the certificate is not valid!");
		}

		ASN1InputStream asn1Input = new ASN1InputStream(policyInformationExtensionValue);

		DEROctetString policyInformationOctetString = null;
		DERSequence transfer = null;
		try {
			policyInformationOctetString = (DEROctetString) asn1Input.readObject();

			asn1Input = new ASN1InputStream(policyInformationOctetString.getOctets());

			transfer = (DERSequence) asn1Input.readObject();
		} catch (IOException e) {
			throw new IllegalArgumentException("The issuer of the certificate is not valid!");
		}

		Enumeration transferList = transfer.getObjects();

		while (transferList.hasMoreElements()) {
			Object element = transferList.nextElement();
			if (element == null) {
				throw new IllegalArgumentException("The issuer of the certificate is not valid!");
			}

			if ((element instanceof DERObject)) {
				DERTaggedObject derO = (DERTaggedObject) element;

				issuerId = derO.hashCode() + "";

				break;
			}
		}
		return issuerId;
	}

	public static X509CRL loadCRL(InputStream inputStream) throws DigitalSignatureException {
		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

			X509CRL localX509CRL = (X509CRL) certificateFactory.generateCRL(inputStream);
			return localX509CRL;
		} catch (CertificateException e) {
			throw new DigitalSignatureException(e.getMessage(), e);
		} catch (CRLException e) {
			throw new DigitalSignatureException(e.getMessage(), e);
		} finally {
			try {
				if (inputStream != null) inputStream.close();
			} catch (Exception ex) {
				return null;
			}
		}
//		throw localObject;
	}
}
