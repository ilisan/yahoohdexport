package bg.bulsi.municipalityweb.esign.sign.util.applet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

public class PKCS11LibraryFileAndPINCodeDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final String CONFIG_FILE_NAME = ".smart_card_signer_applet.config";
	private static final String PKCS11_LIBRARY_FILE_NAME_KEY = "last-PKCS11-file-name";
	private final JButton mBrowseForLibraryFileButton = new JButton();
	private final JTextField mLibraryFileNameTextField = new JTextField();
	private final JLabel mChooseLibraryFileLabel = new JLabel();
	private final JTextField mPINCodeTextField = new JPasswordField();
	private final JLabel mEnterPINCodeLabel = new JLabel();
	private final JButton mSignButton = new JButton();
	private final JButton mCancelButton = new JButton();
	private final JButton mHelpButton = new JButton();

	private final JComboBox mChooseDriverMenu = new JComboBox();
	private static final String BROWSE_FOR_FILE = "Избор на файл...";
	private static Map<String, String> mMenuDrivers;
	private boolean mResult = false;

	public PKCS11LibraryFileAndPINCodeDialog() {
		getContentPane().setLayout(null);
		setSize(new Dimension(450, 180));
		setBackground(SystemColor.control);
		setTitle("Изберете драйвер и PIN код на вашата карта");
		setResizable(false);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dialogSize = getSize();
		int centerPosX = (screenSize.width - dialogSize.width) / 2;
		int centerPosY = (screenSize.height - dialogSize.height) / 2;
		setLocation(centerPosX, centerPosY);

		this.mChooseLibraryFileLabel.setText("Моля изберете драйвер за вашата карта:");

		this.mChooseLibraryFileLabel.setBounds(new Rectangle(10, 8, 240, 15));
		this.mChooseLibraryFileLabel.setFont(new Font("Dialog", 0, 12));

		this.mLibraryFileNameTextField.setBounds(new Rectangle(10, 30, 425, 20));
		this.mLibraryFileNameTextField.setFont(new Font("DialogInput", 0, 12));
		this.mLibraryFileNameTextField.setEditable(false);
		this.mLibraryFileNameTextField.setBackground(SystemColor.control);

		this.mBrowseForLibraryFileButton.setText(BROWSE_FOR_FILE);
		this.mBrowseForLibraryFileButton.setBounds(new Rectangle(250, 10, 160, 20));
		this.mBrowseForLibraryFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PKCS11LibraryFileAndPINCodeDialog.this.browseForLibraryButtonActionPerformed();
			}
		});
		if ((mMenuDrivers != null) && (mMenuDrivers.size() > 0)) {
			this.mChooseDriverMenu.addItem(null);
			this.mChooseDriverMenu.addItem(BROWSE_FOR_FILE);

			Set keySet = mMenuDrivers.keySet();
			Iterator keyItr = keySet.iterator();
			while (keyItr.hasNext()) {
				this.mChooseDriverMenu.addItem(keyItr.next());
			}

			this.mChooseDriverMenu.setBounds(new Rectangle(250, 4, 160, 20));

			this.mChooseDriverMenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					PKCS11LibraryFileAndPINCodeDialog.this.browseForLibraryButtonActionPerformed();
				}
			});
		}
		this.mEnterPINCodeLabel.setText("Моля въведете PIN кода на вашата smart карта:");

		this.mEnterPINCodeLabel.setBounds(new Rectangle(10, 62, 350, 15));
		this.mEnterPINCodeLabel.setFont(new Font("Dialog", 0, 12));

		this.mPINCodeTextField.setBounds(new Rectangle(10, 80, 425, 20));
		this.mPINCodeTextField.setFont(new Font("DialogInput", 0, 12));

		this.mSignButton.setText("Подпиши");
		this.mSignButton.setBounds(new Rectangle(265, 110, 90, 20));
		this.mSignButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PKCS11LibraryFileAndPINCodeDialog.this.signButtonActionPerformed();
			}
		});
		this.mCancelButton.setText("Отказ");
		this.mCancelButton.setBounds(new Rectangle(365, 110, 70, 20));
		this.mCancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PKCS11LibraryFileAndPINCodeDialog.this.cancelButtonActionPerformed();
			}
		});
		this.mHelpButton.setText("?");
		this.mHelpButton.setToolTipText("Помощ");
		this.mHelpButton.setBorder(new LineBorder(Color.gray, 1));
		this.mHelpButton.setBounds(new Rectangle(415, 4, 20, 20));
		this.mHelpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PKCS11LibraryFileAndPINCodeDialog.this.helpButtonActionPerformed();
			}
		});
		getContentPane().add(this.mChooseLibraryFileLabel, null);
		getContentPane().add(this.mLibraryFileNameTextField, null);

		if (this.mChooseDriverMenu.getItemCount() > 0)
			getContentPane().add(this.mChooseDriverMenu, null);
		else {
			getContentPane().add(this.mBrowseForLibraryFileButton, null);
		}
		getContentPane().add(this.mEnterPINCodeLabel, null);
		getContentPane().add(this.mPINCodeTextField, null);
		getContentPane().add(this.mSignButton, null);
		getContentPane().add(this.mCancelButton, null);
		getContentPane().add(this.mHelpButton, null);
		getRootPane().setDefaultButton(this.mSignButton);

		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent windowEvent) {
				String libraryFileName = PKCS11LibraryFileAndPINCodeDialog.this.mLibraryFileNameTextField
						.getText();
				if ((libraryFileName != null) && (libraryFileName.length() != 0))
					PKCS11LibraryFileAndPINCodeDialog.this.mPINCodeTextField.requestFocus();
				else
					PKCS11LibraryFileAndPINCodeDialog.this.mBrowseForLibraryFileButton
							.requestFocus();
			}
		});
	}

	private void browseForLibraryButtonActionPerformed() {
		boolean browseButton = this.mChooseDriverMenu.getItemCount() == 0;

		if ((!browseButton) && (this.mChooseDriverMenu.getSelectedItem() == null)) {
			this.mChooseDriverMenu.hidePopup();
			return;
		}

		if ((browseButton) || (this.mChooseDriverMenu.getSelectedItem().equals(BROWSE_FOR_FILE))) {
			JFileChooser fileChooser = new JFileChooser();
			LibraryFileFilter libraryFileFilter = new LibraryFileFilter();
			fileChooser.addChoosableFileFilter(libraryFileFilter);
			String libraryFileName = this.mLibraryFileNameTextField.getText();
			File directory = new File(libraryFileName).getParentFile();
			fileChooser.setCurrentDirectory(directory);

			this.mChooseDriverMenu.hidePopup();

			if (fileChooser.showOpenDialog(this) == 0) {
				String selectedLibFile = fileChooser.getSelectedFile().getAbsolutePath();

				this.mLibraryFileNameTextField.setText(selectedLibFile);
			}
		} else if (mMenuDrivers.containsKey(this.mChooseDriverMenu.getSelectedItem())) {
			this.mLibraryFileNameTextField.setText((String) mMenuDrivers.get(this.mChooseDriverMenu
					.getSelectedItem()));

			this.mChooseDriverMenu.hidePopup();
		} else {
			this.mChooseDriverMenu.hidePopup();
		}
	}

	private void signButtonActionPerformed() {
		this.mResult = true;
		setVisible(false);
	}

	private void cancelButtonActionPerformed() {
		this.mResult = false;
		setVisible(false);
	}

	private void helpButtonActionPerformed() {
		JOptionPane
				.showMessageDialog(
						this,
						"<html><p>Java Applet за логин използва PKCS#11 библиотека за четене на сертификата (PKCS#11 Certificate Provider DLL).<br/> За целта потребителят трябва да въведе пътя до тази библиотека. Пътят зависи от софтуера на карточетеца и операционната система:</p><ul><li><p>Windows (oбикновено %WINDIR%=C:\\Windows и %ProgramFiles%= c:\\Program Files):</p><table bgcolor=\"#B0B0B0\" cellspacing=\"1px\"><thead><tr bgcolor=\"#E0E0E0\"><td>Софтуер</td><td>Път до DLL</td><td>Доставчици; Четци</td></tr><tr><td bgcolor=\"#FFFFFF\">Charismatics</td><td bgcolor=\"#FFFFFF\">%WINDIR%\\System32\\cmP11.dll<br/>или<br/>%WINDIR%\\SysWOW64\\cmP1164.dll</td><td bgcolor=\"#FFFFFF\">СЕП, ИнфоНотари, ИО</td></tr><tr><td bgcolor=\"#FFFFFF\">Bit4id</td><td bgcolor=\"#FFFFFF\">%WINDIR%\\System32\\bit4ipki.dll<br/></td><td bgcolor=\"#FFFFFF\">ИнфоНотари</td></tr><tr><td bgcolor=\"#FFFFFF\">Crypto Vision</td><td bgcolor=\"#FFFFFF\">%WINDIR%\\System32\\cvp11.dll<br/>или<br/>%WINDIR%\\SysWOW64\\cvp11.dll</td><td bgcolor=\"#FFFFFF\">Банксервиз</td></tr><tr><td bgcolor=\"#FFFFFF\">Siemens CardOS</td><td bgcolor=\"#FFFFFF\">%Windir%\\System32\\siecap11.dll</td><td bgcolor=\"#FFFFFF\">Спектър, ИнфоНотари, B-Trust; Omnikey 6121/ 3121</td></tr><tr><td bgcolor=\"#FFFFFF\">SafeNet=Datakey</td><td bgcolor=\"#FFFFFF\">%Windir%\\system32\\dkck201.dll</td><td bgcolor=\"#FFFFFF\">Спектър, ИнфоНотари</td></tr><tr><td bgcolor=\"#FFFFFF\">ActivCard Gold</td><td bgcolor=\"#FFFFFF\">%Windir%\\System32\\acpkcs.dll</td><td bgcolor=\"#FFFFFF\">Спектър</td></tr><tr><td bgcolor=\"#FFFFFF\">Setec SetWeb</td><td bgcolor=\"#FFFFFF\">%ProgramFiles%\\SetWeb\\settoki.dll</td><td bgcolor=\"#FFFFFF\">ИнфоНотари, ИО, Спектър</td></tr><tr><td bgcolor=\"#FFFFFF\">Gemplus</td><td bgcolor=\"#FFFFFF\">%ProgramFiles%\\GemPlus\\GemSafe Libraries\\BIN\\gclib.dll</td><td bgcolor=\"#FFFFFF\">Спектър</td></tr><tr><td bgcolor=\"#FFFFFF\">Utimaco SafeGuard</td><td bgcolor=\"#FFFFFF\">%Windir%\\system32\\pkcs201n.dll</td><td bgcolor=\"#FFFFFF\">ИО</td></tr><tr><td bgcolor=\"#FFFFFF\">ActiveKey</td><td bgcolor=\"#FFFFFF\"></td><td bgcolor=\"#FFFFFF\">ИнфоНотари</td></tr><tr><td bgcolor=\"#FFFFFF\">Aladdin</td><td bgcolor=\"#FFFFFF\"></td><td bgcolor=\"#FFFFFF\">ИнфоНотари</td></table></li><li>Linux: /usr/lib/opensc-pkcs11.so или /usr/lib/onepin-opensc-pkcs11.so, или /usr/local/lib/libsiecap11.so (Siemens), или libcmP11.so (Charismatics)</li><li>Linux 64bit: /usr/lib64/opensc-pkcs11.so или /usr/lib64/onepin-opensc-pkcs11.so</li><li>Mac OS X: /Library/OpenSC/lib/opensc-pkcs11.so или /Library/OpenSC/lib/onepin-opensc-pkcs11.so или libcmP11.dylib</li></ul> <p>Следните линкове може да Ви бъдат полезни:<ul><li>http://www.freeotfe.org/docs/Main/pkcs11_drivers.htm</li><li>http://wiki.cacert.org/wiki/Pkcs11TaskForce</li></ul></p><p>Пътят се кешира, за да не се налага повторното му въвеждане след рестартиране на браузъра.<br/>За целта потребителят трябва да разреши кеширане в браузъра и кешът да не се изтрива при изход от браузъра.</p></html>",
						"Помощ", 1);
	}

	private String getConfigFileName() {
		String configFileName = System.getProperty("user.home")
				+ System.getProperty("file.separator") + CONFIG_FILE_NAME;

		return configFileName;
	}

	private void loadSettings() throws IOException {
		String configFileName = getConfigFileName();
		FileInputStream configFileStream = new FileInputStream(configFileName);
		try {
			Properties configProps = new Properties();
			configProps.load(configFileStream);

			String lastLibraryFileName = configProps.getProperty(PKCS11_LIBRARY_FILE_NAME_KEY);

			if (lastLibraryFileName != null)
				this.mLibraryFileNameTextField.setText(lastLibraryFileName);
			else
				this.mLibraryFileNameTextField.setText("");
		} finally {
			configFileStream.close();
		}
	}

	private void saveSettings() throws IOException {
		Properties configProps = new Properties();
		String currentLibraryFileName = this.mLibraryFileNameTextField.getText();
		configProps.setProperty(PKCS11_LIBRARY_FILE_NAME_KEY, currentLibraryFileName);

		String configFileName = getConfigFileName();
		FileOutputStream configFileStream = new FileOutputStream(configFileName);
		try {
			configProps.store(configFileStream, "");
		} finally {
			configFileStream.close();
		}
	}

	public String getLibraryFileName() {
		String libraryFileName = this.mLibraryFileNameTextField.getText();
		return libraryFileName;
	}

	public String getSmartCardPINCode() {
		String pinCode = this.mPINCodeTextField.getText();
		return pinCode;
	}

	public boolean run() {
		try {
			loadSettings();
		} catch (IOException ioex) {
			System.out.println("loading saving failed");
		}

		setModal(true);
		setVisible(true);
		try {
			if (this.mResult) saveSettings();
		} catch (IOException ioex) {
			System.out.println("settings saving failed");
		}

		return this.mResult;
	}

	static {
		String windir = System.getenv("windir");
		String pfiles = System.getenv("ProgramFiles");

		if ((windir != null) && (windir.length() > 0) && (pfiles != null) && (pfiles.length() > 0)) {
			mMenuDrivers = new LinkedHashMap();
			mMenuDrivers.put("Charismatics(32 bit)", windir + "\\System32\\cmP11.dll");

			mMenuDrivers.put("Charismatics(64 bit)", windir + "\\SysWOW64\\cmP1164.dll");

			mMenuDrivers.put("Bit4id", windir + "\\System32\\bit4ipki.dll");

			mMenuDrivers.put("CryptoVision(32 bit)", windir + "\\System32\\cvP11.dll");

			mMenuDrivers.put("CryptoVision(64 bit)", windir + "\\SysWOW64\\cvP11.dll");

			mMenuDrivers.put("Siemens CardOS", windir + "\\System32\\siecap11.dll");

			mMenuDrivers.put("SafeNet/Datakey", windir + "\\system32\\dkck201.dll");

			mMenuDrivers.put("ActivCard Gold", windir + "\\System32\\acpkcs.dll");

			mMenuDrivers.put("Setec SetWeb", pfiles + "\\SetWeb\\settoki.dll");
			mMenuDrivers.put("Gemplus", pfiles + "\\GemPlus\\GemSafe Libraries\\BIN\\gclib.dll");

			mMenuDrivers.put("Utimaco SafeGuard", windir + "\\system32\\pkcs201n.dll");
		}
	}

	private static class LibraryFileFilter extends FileFilter {
		public boolean accept(File aFile) {
			if (aFile.isDirectory()) {
				return true;
			}

			String fileName = aFile.getName().toLowerCase();
			boolean accepted = (fileName.endsWith(".dll")) || (fileName.endsWith(".so"))
					|| (fileName.endsWith(".dylib"));

			return accepted;
		}

		public String getDescription() {
			return "PKCS#11 v2.0 ot later implementation library (.dll, .so)";
		}
	}
}
