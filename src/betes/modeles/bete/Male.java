package betes.modeles.bete;

import java.io.Serializable;

public class Male extends Bete implements Serializable {

	private static final long serialVersionUID = -1737802894940928067L;

	public Male() {

	}

	public Male(String nomBete, String sexe, int age, int energie, int energieMax, int nbCombat, int nbAccoulpement, int virilite) {
		super(nomBete, sexe, age, energie, energieMax, nbCombat, nbAccoulpement);
		super.facteurReproduction = virilite;
	}

}