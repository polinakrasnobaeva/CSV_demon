package panel;
import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.Credentials;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAuthenticationHandler;


public class WebBrowserPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JWebBrowser webBrowser;
	
	public static String metricaURL = "http://xn----7sbkbf0bzcxeva.xn--p1ai/metrika-api/";
	
	public WebBrowserPanel() {
		super(new BorderLayout());
		
		NativeInterface.open();
		
		JPanel webBrowserPanel = new JPanel(new BorderLayout());
		webBrowserPanel.setBorder(BorderFactory.createTitledBorder("δελόςΰ-ώγ.πτ/metrika-api/"));
		webBrowserPanel.setBounds(0, 0, 1195, 705);

		webBrowser = new JWebBrowser();
		webBrowser.setDefaultPopupMenuRegistered(true);
		webBrowser.setAuthenticationHandler(new WebBrowserAuthenticationHandler() {
			@Override
			public Credentials getCredentials(JWebBrowser webBrowser, String resourceLocation) {
				Credentials credentials = new Credentials("report", "300919");
				return credentials;
			}
		});
		
		
		webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
		add(webBrowserPanel, BorderLayout.CENTER);
		webBrowser.setMenuBarVisible(false);
		webBrowser.setStatusBarVisible(true);
		webBrowser.setButtonBarVisible(false);
		webBrowser.setLocationBarVisible(false);
		
		webBrowser.navigate(metricaURL);
	}
	
}