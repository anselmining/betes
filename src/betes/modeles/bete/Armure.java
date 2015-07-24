package betes.modeles.bete;

import java.io.Serializable;

public class Armure extends Instrument implements Serializable {

	private static final long serialVersionUID = 6429880913670673011L;
	private int defense;

	public Armure() {

	}

	public Armure(int defense) {
		super();
		this.defense = defense;
	}

	public int getDefense() {
		return defense;
	}

}
