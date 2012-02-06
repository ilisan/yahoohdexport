package bg.bulsi.municipalityweb.esign.sign.util.applet;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class PKCS11SelectCertificateDialog extends JDialog {
	private static final long serialVersionUID = -5037326736611630123L;
	private final List<String> certificates;
	private final JList mCertificateList = new JList();
	private final JButton mChooseButton = new JButton();
	private JScrollPane mCertListScroll = new JScrollPane();
	private int mResult;

	public PKCS11SelectCertificateDialog(List<String> certs) {
		this.certificates = certs;

		getContentPane().setLayout(null);
		setSize(new Dimension(426, 255));
		setBackground(SystemColor.control);
		setTitle("Изберете сертификат");
		setResizable(false);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dialogSize = getSize();
		int centerPosX = (screenSize.width - dialogSize.width) / 2;
		int centerPosY = (screenSize.height - dialogSize.height) / 2;
		setLocation(centerPosX, centerPosY);

		List subjList = new ArrayList();

		int count = 0;

		for (String crt : this.certificates) {
			count++;
			String subject = crt;

			Pattern sbjPattern = Pattern.compile("Subject:[^\r\n]*");

			Matcher sbjMatcher = sbjPattern.matcher(crt);

			if (sbjMatcher.find()) {
				String sbjString = crt.substring(sbjMatcher.start(), sbjMatcher.end());

				Pattern cnPattern = Pattern.compile("CN=[^\r\n,]*");
				Matcher cnMatcher = cnPattern.matcher(sbjString);
				if (cnMatcher.find()) {
					String cnString = sbjString.substring(cnMatcher.start(), cnMatcher.end());

					cnString = cnString.substring(3);
					subject = cnString + " (" + sbjString + ")";
					subjList.add(count + ": " + subject);
				} else {
					subjList.add(count + ": " + crt);
				}
			} else {
				subjList.add(count + ": " + crt);
			}
		}

		this.mCertificateList.setSelectionMode(0);
		this.mCertificateList.setBounds(10, 10, 400, 190);
		this.mCertificateList.setListData(subjList.toArray());
		this.mCertificateList.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2)
					PKCS11SelectCertificateDialog.this.chooseButtonActionPerformed();
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}
		});
		this.mCertListScroll = new JScrollPane(this.mCertificateList);
		this.mCertListScroll.setBounds(10, 10, 400, 190);

		this.mChooseButton.setText("Избери");
		this.mChooseButton.setBounds(new Rectangle(10, 200, 400, 20));
		this.mChooseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PKCS11SelectCertificateDialog.this.chooseButtonActionPerformed();
			}
		});
		getContentPane().add(this.mCertListScroll, null);
		getContentPane().add(this.mChooseButton, null);

		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent windowEvent) {
				PKCS11SelectCertificateDialog.this.mCertificateList.requestFocus();
			}
		});
	}

	private void chooseButtonActionPerformed() {
		this.mResult = this.mCertificateList.getSelectedIndex();
		if (this.mResult == -1) {
			return;
		}
		setVisible(false);
	}

	public int run() {
		setModal(true);
		setVisible(true);
		return this.mResult;
	}
}
