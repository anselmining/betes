package betes.modeles.bete;

import java.io.Serializable;

public class Femelle extends Bete implements Serializable {

	private static final long serialVersionUID = -2007415558147119121L;

	public Femelle() {

	}

	public Femelle(String nomBete, String sexe, int age, int energie, int energieMax, int nbCombat, int nbAccoulpement, int attractivite) {
		super(nomBete, sexe, age, energie, energieMax, nbCombat, nbAccoulpement);
		super.facteurReproduction = attractivite;
	}

}
