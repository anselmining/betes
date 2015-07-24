package betes.gui.moteur;

public class EvenementsRegistre {

	private String event;
	private static EvenementsRegistre instance = new EvenementsRegistre();

	private EvenementsRegistre() {
	}

	public void registreEvent(String text) {
		event += "\n" + text;
	}

	public void creerEvent(String text) {
		event = text;
	}

	public String getEvent() {
		return event;
	}

	public void clearEvent() {
		event = "";
	}

	public static EvenementsRegistre getInstance() {
		return instance;
	}
}
