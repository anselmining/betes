package betes.modeles.environnement;

import java.io.Serializable;

import betes.donnees.GenerationAleatoire;
import betes.donnees.exceptions.BeteSurCaseException;
import betes.donnees.exceptions.CaseInexistanteException;
import betes.donnees.exceptions.CaseNonTraversableException;
import betes.donnees.exceptions.NourritureSurCaseException;
import betes.visiteurs.Visiteur;

public class Case implements Serializable {
	private static final long serialVersionUID = -5431109933811030676L;
	private int positionX;
	private int positionY;
	private Boolean traversable;
	private String image;
	private String nomDecor;
	private int pourcentageMutation;

	public Case() {
		this.pourcentageMutation = GenerationAleatoire.nombreAleatoire(0, 100);
		this.traversable = true;
	}

	public Case(int positionX, int positionY) {
		this();
		this.positionX = positionX;
		this.positionY = positionY;
		this.image = " . ";
	}

	public Case(int positionX, int positionY, String image) {
		this(positionX, positionY);
		this.image = image;
	}

	public Case(int x, int y, String nomDecor, String image, Boolean traversable) {
		this(x, y, image);
		this.setTraversable(traversable);
		this.nomDecor = nomDecor;
	}

	public void setTraversable(Boolean traversable) {
		this.traversable = traversable;
	}

	public int getX() {
		return positionX;
	}

	public int getY() {
		return positionY;
	}

	public Case getPosition() {
		return this;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	public Boolean isTraversable() {
		return traversable;
	}

	public String getImage() {
		return image;
	}

	public String getNomDecor() {
		return nomDecor;
	}

	public int getPourcentageMutation() {
		return pourcentageMutation;
	}

	public void setPourcentageMutation(int pourcentageMutation) {
		this.pourcentageMutation = pourcentageMutation;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + positionX;
		result = PRIME * result + positionY;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Case other = (Case) obj;
		if (positionX != other.positionX)
			return false;
		if (positionY != other.positionY)
			return false;
		return true;
	}

	public void accepteVisiteur(Visiteur visiteur) throws CaseInexistanteException, CaseNonTraversableException, BeteSurCaseException,
			NourritureSurCaseException {
		visiteur.visite(this);
	}

}
