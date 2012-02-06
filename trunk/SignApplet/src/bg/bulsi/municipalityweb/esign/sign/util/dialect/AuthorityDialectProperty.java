package bg.bulsi.municipalityweb.esign.sign.util.dialect;

public enum AuthorityDialectProperty {
	AUTHORITY("authority"), ID("id"), SEPARATOR("separator"), NAME("name"), EMAIL("email"), BULSTAT(
			"bulstat"), EGN("egn"), KEY("authorityKey"), CERT_SERIAL_NUM("sn");

	private String value;

	private AuthorityDialectProperty(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
