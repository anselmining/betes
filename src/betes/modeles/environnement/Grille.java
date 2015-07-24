package betes.modeles.environnement;

import betes.visiteurs.Visiteur;

public class Grille {
	private int tailleX;
	private int tailleY;

	public Grille() {

	}

	public Grille(int tailleX, int tailleY) {

		this.tailleX = tailleX;
		this.tailleY = tailleY;
	}

	public int getTailleX() {
		return tailleX;
	}

	public int getTailleY() {
		return tailleY;
	}

	public void accepteVisiteur(Visiteur visiteur) {
		visiteur.visite(this);
	}
}
