package bg.bulsi.municipalityweb.esign.sign.util.xml.validation;

import bg.bulsi.municipalityweb.esign.sign.util.DigitalSignatureException;

public class SignatureValidationException extends DigitalSignatureException {
	private static final long serialVersionUID = 1L;
	private final XMLSignatureErrorCode errorCode;

	public SignatureValidationException(XMLSignatureErrorCode errorCode) {
		super(null);
		this.errorCode = errorCode;
	}

	public SignatureValidationException(String message, XMLSignatureErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public SignatureValidationException(String message, XMLSignatureErrorCode errorCode,
			Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public XMLSignatureErrorCode getErrorCode() {
		return this.errorCode;
	}
}
