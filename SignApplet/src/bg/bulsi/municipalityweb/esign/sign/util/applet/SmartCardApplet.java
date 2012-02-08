package bg.bulsi.municipalityweb.esign.sign.util.applet;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.security.AuthProvider;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.security.auth.login.LoginException;
import javax.swing.JOptionPane;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import bg.bulsi.municipalityweb.esign.sign.util.DigitalSignatureException;
import bg.bulsi.municipalityweb.esign.sign.util.xml.CertificateSignUtils;
import bg.bulsi.municipalityweb.esign.sign.util.xml.CertificationChainAndSignatureBase64;

public abstract class SmartCardApplet extends Applet {
	private static final long serialVersionUID = -813323231904181L;
	private static final String APPLET_LABEL = "appletLabel";
	private Button mSignButton = null;

	public void init() {
		this.mSignButton = new Button();
		this.mSignButton.setLabel(getParameter(APPLET_LABEL));
		this.mSignButton.setLocation(0, 0);
		this.mSignButton.setBackground(new Color(13684944));

		Dimension appletSize = getSize();
		this.mSignButton.setSize(appletSize);
		this.mSignButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SmartCardApplet.this.signButtonAction();
			}
		});
		setLayout(null);
		add(this.mSignButton);
	}

	protected abstract void signButtonPressed(X509Certificate paramX509Certificate,
			JSObject paramJSObject,
			CertificationChainAndSignatureBase64 paramCertificationChainAndSignatureBase64)
			throws DigitalSignatureException;

	private void signButtonAction() {
		String oldButtonLabel = this.mSignButton.getLabel();

		KeyStore keyStore = null;
		try {
			this.mSignButton.setLabel("Обработване...");
			this.mSignButton.setEnabled(false);

			JSObject browserWindow = JSObject.getWindow(this);

			keyStore = getKeyStoreFromUser();

			if (keyStore != null) {
				String alias = getCertificateAliasFromKeystore(keyStore);

				System.out.println("alias: " + alias);

				System.out.println("get certificate from keystore");
				X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);

				System.out.println("get certification chain");
				CertificationChainAndSignatureBase64 certificationChain = CertificateSignUtils
						.getCertificationChain(keyStore, alias);

				System.out.println("call signButtonPressed");
				signButtonPressed(certificate, browserWindow, certificationChain);
			} else {
				return;
			}
		} catch (SecurityException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
							"Файловата система не може да бъде достъпена.\nТози аплет трябва да бъде стартиран с пълни права.\nМоля приемете сертификата, когато Plug-In ви попита.");
		} catch (JSException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
							"Unable to access some of the fields of the\nHTML form. Please check the applet parameters.");
		} catch (Exception e) {
			Writer result = new StringWriter();
			PrintWriter printWriter = new PrintWriter(result);
			e.printStackTrace(printWriter);
			JOptionPane.showMessageDialog(this, "Неочаквана грешка: " + result.toString());
		} finally {
			this.mSignButton.setLabel(oldButtonLabel);
			this.mSignButton.setEnabled(true);
			if (keyStore != null) try {
				((AuthProvider) keyStore.getProvider()).logout();
			} catch (LoginException e) {
				e.printStackTrace();
			}
		}
	}

	protected KeyStore getKeyStoreFromUser() throws DigitalSignatureException {
		KeyStore result = null;
		System.out.println("ask user for credentials");

		PKCS11LibraryFileAndPINCodeDialog pkcs11Dialog = new PKCS11LibraryFileAndPINCodeDialog();
		boolean dialogConfirmed;
		try {
			dialogConfirmed = pkcs11Dialog.run();
		} finally {
			pkcs11Dialog.dispose();
		}

		if (dialogConfirmed) {
			String pkcs11LibraryFileName = pkcs11Dialog.getLibraryFileName();
			String pinCode = pkcs11Dialog.getSmartCardPINCode();

			if (pkcs11LibraryFileName.length() == 0) {
				String errorMessage = "Трябва да изберете PCKS#11 драйвер за вашата smart карта (.dll или .so файл)!";

				throw new DigitalSignatureException(errorMessage);
			}

			System.out.println("get keystore");
			try {
				result = loadKeyStoreFromSmartCard(pkcs11LibraryFileName, pinCode);
			} catch (Exception ex) {
				String errorMessage = "Ключът не може да бъде прочетен от вашата карта.\nВъзможни причини:\n - Четеца на smart карти не е свързан.\n - Картата не е вкарана.\n - PKCS#11 драйвера е невалиден.\n - Въведения PIN е невалиден.\nДетайли:: "
						+ ex.getMessage();

				throw new DigitalSignatureException(errorMessage, ex);
			}
		}

		return result;
	}

	protected KeyStore loadKeyStoreFromSmartCard(String aPKCS11LibraryFileName, String aSmartCardPIN)
			throws GeneralSecurityException, IOException {
		String pkcs11ConfigSettings = "name = SmartCard\nlibrary = " + aPKCS11LibraryFileName;

		byte[] pkcs11ConfigBytes = pkcs11ConfigSettings.getBytes();
		ByteArrayInputStream confStream = new ByteArrayInputStream(pkcs11ConfigBytes);
		try {
			Class sunPkcs11Class = Class.forName("sun.security.pkcs11.SunPKCS11");

			Constructor pkcs11Constr = sunPkcs11Class
					.getConstructor(new Class[] { InputStream.class });

			Provider pkcs11Provider = (Provider) pkcs11Constr
					.newInstance(new Object[] { confStream });

			Security.addProvider(pkcs11Provider);
		} catch (Exception e) {
			throw new KeyStoreException("Can initialize Sun PKCS#11 security provider. Reason: "
					+ e.getCause().getMessage());
		}

		char[] pin = aSmartCardPIN.toCharArray();
		KeyStore keyStore = KeyStore.getInstance("PKCS11");

		((AuthProvider) keyStore.getProvider()).logout();
		keyStore.load(null, pin);
		return keyStore;
	}

	protected String getCertificateAliasFromKeystore(KeyStore aKeyStore)
			throws GeneralSecurityException {
		System.out.println("ask user for cerficate alias");

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
			String alias = null;
			if (aliasList.size() == 0) {
				alias = "";
			} else {
				if (aliasList.size() == 1) {
					alias = (String) aliasList.get(0);
				} else {
					PKCS11SelectCertificateDialog scd = new PKCS11SelectCertificateDialog(certList);

					alias = (String) aliasList.get(scd.run());
				}
			}
			return alias;
		}
		throw new KeyStoreException("The keystore is empty!");
	}
}
