package betes.modeles.bete;

import java.io.Serializable;

public class Arme extends Instrument implements Serializable {

	private static final long serialVersionUID = 3222696683164981487L;
	private int attaque;

	public Arme() {

	}

	public Arme(int attaque) {
		super();
		this.attaque = attaque;
	}

	public int getAttaque() {
		return attaque;
	}

}
