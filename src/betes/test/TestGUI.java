package betes.test;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import betes.gui.frames.Accueil;

public class TestGUI {

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			UIManager.getSystemLookAndFeelClassName();
		}

		new Accueil().setVisible(true);
	}
}
