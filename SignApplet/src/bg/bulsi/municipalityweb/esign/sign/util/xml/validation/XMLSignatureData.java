package bg.bulsi.municipalityweb.esign.sign.util.xml.validation;

import java.security.cert.X509Certificate;

public class XMLSignatureData {
	private X509Certificate cert = null;
	private String signedXml = null;
	private String xml = null;

	public XMLSignatureData(X509Certificate cert, String signedXml, String xml) {
		this.cert = cert;
		this.signedXml = signedXml;
		this.xml = xml;
	}

	public X509Certificate getCert() {
		return this.cert;
	}

	public String getSignedXml() {
		return this.signedXml;
	}

	public String getXml() {
		return this.xml;
	}

	public String toString() {
		return "XMLSignatureData [cert=" + this.cert + ", signedXml=" + this.signedXml + ", xml="
				+ this.xml + "]";
	}
}
