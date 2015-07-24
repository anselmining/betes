package betes.modeles.environnement;

import java.io.Serializable;

public class Nourriture extends Case implements Serializable {
	private static final long serialVersionUID = 3169436086342187703L;
	private String nomNourriture;
	private int satiete;

	public Nourriture() {
	}

	public Nourriture(int x, int y, String nomNourriture, String image, int satiete, Boolean traversable) {
		super(x, y, image);
		super.setTraversable(traversable);
		this.nomNourriture = nomNourriture;
		this.satiete = satiete;
	}

	public int getSatieteNourriture() {
		return satiete;
	}

	public String getNomNourriture() {
		return nomNourriture;
	}

}
