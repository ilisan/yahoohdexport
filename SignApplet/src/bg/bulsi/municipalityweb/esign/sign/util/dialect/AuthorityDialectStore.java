package bg.bulsi.municipalityweb.esign.sign.util.dialect;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import bg.bulsi.municipalityweb.esign.sign.util.CertificateUtil;

public final class AuthorityDialectStore {
	Map<String, AuthorityDialect> dialects = null;

	public static AuthorityDialectStore getInstance(String authorityDialectXml)
			throws UnsupportedEncodingException, XMLStreamException {
		AuthorityDialectStore store = new AuthorityDialectStore();
		store.dialects = loadConfig(authorityDialectXml);
		return store;
	}

	static Map<String, AuthorityDialect> loadConfig(String authorityDialectXml)
			throws XMLStreamException, UnsupportedEncodingException {
		Map<String, AuthorityDialect> result = new HashMap<String, AuthorityDialect>();

		InputStream io = new ByteArrayInputStream(authorityDialectXml.getBytes("UTF-8"));

		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = inputFactory.createXMLEventReader(io);

		AuthorityDialect dialect = new AuthorityDialect();

		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();

			if (event.isStartElement()) {
				if (AuthorityDialectProperty.AUTHORITY.getValue().equals(
						event.asStartElement().getName().getLocalPart())) {
					dialect = new AuthorityDialect();
					continue;
				}
				if (AuthorityDialectProperty.ID.getValue().equals(
						event.asStartElement().getName().getLocalPart())) {
					event = eventReader.nextEvent();
					if (event.isCharacters()) {
						dialect.setId(event.asCharacters().getData());
						continue;
					}
					dialect.setId(null);

					continue;
				}
				if (AuthorityDialectProperty.SEPARATOR.getValue().equals(
						event.asStartElement().getName().getLocalPart())) {
					event = eventReader.nextEvent();
					if (event.isCharacters()) {
						dialect.setSeparator(event.asCharacters().getData());
						continue;
					}
					dialect.setSeparator(null);

					continue;
				}
				if (AuthorityDialectProperty.NAME.getValue().equals(
						event.asStartElement().getName().getLocalPart())) {
					event = eventReader.nextEvent();
					if (event.isCharacters()) {
						dialect.setName(event.asCharacters().getData());
						continue;
					}
					dialect.setName(null);

					continue;
				}
				if (AuthorityDialectProperty.EMAIL.getValue().equals(
						event.asStartElement().getName().getLocalPart())) {
					event = eventReader.nextEvent();
					if (event.isCharacters()) {
						dialect.setEmail(event.asCharacters().getData());
						continue;
					}
					dialect.setEmail(null);

					continue;
				}
				if (AuthorityDialectProperty.BULSTAT.getValue().equals(
						event.asStartElement().getName().getLocalPart())) {
					event = eventReader.nextEvent();
					if (event.isCharacters()) {
						dialect.setBulstat(event.asCharacters().getData());
						continue;
					}
					dialect.setBulstat(null);

					continue;
				}
				if (AuthorityDialectProperty.EGN.getValue().equals(
						event.asStartElement().getName().getLocalPart())) {
					event = eventReader.nextEvent();
					if (event.isCharacters()) {
						dialect.setEgn(event.asCharacters().getData());
						continue;
					}
					dialect.setEgn(null);

					continue;
				}
			}

			if ((event.isEndElement())
					&& (AuthorityDialectProperty.AUTHORITY.getValue().equals(event.asEndElement()
							.getName().getLocalPart()))) {
				result.put(dialect.getId(), dialect);
				continue;
			}

		}

		return result;
	}

	public AuthorityDialect getDialect(String authorityId) {
		return (AuthorityDialect) this.dialects.get(authorityId);
	}

	public AuthorityDialect getDialect(X509Certificate certificate) {
		String authorityId = CertificateUtil.getAuthorityKey(certificate);
		return (AuthorityDialect) this.dialects.get(authorityId);
	}
}
