package betes.donnees;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.donnees.exceptions.PasDeCaseDisponibleException;
import betes.gui.dessin.RectangleDrawable;
import betes.modeles.environnement.Case;
import betes.modeles.environnement.Grille;

public class GenerationAleatoire {
	@SuppressWarnings("unused")
	private static Logger logger = LoggerUtility.getLogger(GenerationAleatoire.class);

	public static String nomMaleAleatoire() {
		return "Coccinelle Male " + nombreAleatoire(0, 99) + " ";
	}

	public static String nomFemelleAleatoire() {
		return "Coccinelle Femalle " + nombreAleatoire(0, 99) + " ";
	}

	public static String sexeAleatoire() {
		String sexe = "male";
		if (Math.random() < 0.5) {
			sexe = "femelle";
		}
		return sexe;
	}

	/*
	 * Donne une Case aléatoire.
	 * 
	 * @param traversabilite Il est possible de choisir une case traversable ou
	 * non.
	 * 
	 * @return Une instance de la classe Case.
	 */
	public static Case positionAleatoire(Boolean traversabilite) throws PasDeCaseDisponibleException {
		EnvironnementDepot eDepot = EnvironnementDepot.getInstance();
		Grille grille = eDepot.getGrille();

		int positionX, positionY; // positions de la case aléatoire
		int positionXMax = grille.getTailleX();
		int positionYMax = grille.getTailleY();
		int nombreDeCasesTotal = grille.getTailleX() * grille.getTailleY();
		int nombreDeCasesRecherche = 0;

		Boolean traversable;
		Case laCase = null;

		do {
			if (nombreDeCasesRecherche > nombreDeCasesTotal) {
				throw new PasDeCaseDisponibleException();
			}
			positionX = (int) ((Math.random() * (positionXMax)));
			positionY = (int) ((Math.random() * (positionYMax)));
			laCase = eDepot.rechercher(positionX, positionY);
			traversable = laCase.isTraversable();
			nombreDeCasesRecherche++;

		} while (traversabilite != traversable);

		return laCase;
	}
	
	/*
	 * Donne un rectangle aléatoire.
	 * 
	 * @param traversabilite Il est possible de choisir une case traversable ou
	 * non.
	 * 
	 * @return Une instance de la classe Case.
	 */
	public static RectangleDrawable positionRectangleAleatoire(Boolean traversabilite) throws PasDeCaseDisponibleException {
		EnvironnementDepot eDepot = EnvironnementDepot.getInstance();
		Grille grille = eDepot.getGrille();

		int positionX, positionY; // positions de la case aléatoire
		int positionXMax = grille.getTailleX();
		int positionYMax = grille.getTailleY();
		int nombreDeCasesTotal = grille.getTailleX() * grille.getTailleY();
		int nombreDeCasesRecherche = 0;

		Boolean traversable;
		RectangleDrawable rectangle = null;

		do {
			if (nombreDeCasesRecherche > nombreDeCasesTotal) {
				throw new PasDeCaseDisponibleException();
			}
			positionX = (int) ((Math.random() * (positionXMax)));
			positionY = (int) ((Math.random() * (positionYMax)));
			
			rectangle = eDepot.rechercherRectangle(positionX, positionY);
			traversable = rectangle.isTraversable();
			nombreDeCasesRecherche++;

		} while (traversabilite != traversable);

		return rectangle;
	}

	/*
	 * Génère un nombre aléatoire entre deux valeurs.
	 * 
	 * @param (int) min : La valeur minimale.
	 * 
	 * @param (int) max : La valeur maximale.
	 * 
	 * @return Le nombre nombre aléatoire entre min et max inclus.
	 */
	public static int nombreAleatoire(int min, int max) {
		return (int) ((Math.random() * (max + 1 - min)) + min);
	}

	/*
	 * Génère un nombre aléatoire entre deux valeurs (double).
	 * 
	 * @param min La valeur minimale.
	 * 
	 * @param max La valeur maximale.
	 * 
	 * @return Le nombre nombre aléatoire entre min et max inclus.
	 */
	public static double nombreAleatoireDouble(int min, int max) {
		return ((Math.random() * (max + 1 - min)) + min);
	}

}
