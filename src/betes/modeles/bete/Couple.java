package betes.modeles.bete;

import java.io.Serializable;
import java.util.HashMap;

public class Couple implements Serializable {

	private static final long serialVersionUID = -7386274934491657308L;
	private int idCouple;
	private Bete femelle;
	private Bete male;
	private HashMap<String, Bete> enfants;

	public Couple() {

	}

	public Couple(Bete femelle, Bete male) {
		super();
		this.femelle = femelle;
		this.male = male;
	}

	public void setEnfants(HashMap<String, Bete> enfants) {
		this.enfants = enfants;
	}

	@Override
	public String toString() {
		return "Couple [idCouple=" + idCouple + ", femelle=" + femelle + ", male=" + male + ", enfants=" + enfants + "]";
	}

}
