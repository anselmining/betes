package betes.donnees;

public class ContraintesParametres {

	/**
	 * Paramètres fondamentaux
	 */

	public static Boolean modeConsole = false;
	
	public static final int TAILLE_GRILLE_MAX_X = 40;

	public static final int TAILLE_GRILLE_MAX_Y = 40;

	public static final int TAILLE_GRILLE_MIN_X = 30;
	
	public static final int TAILLE_GRILLE_MIN_Y = 30;
	
	//X est la largeur de la case 
	public static final int DIMX = 20;
	//Y est la hauteur de la case 
	public static final int DIMY = 20;

	public static final Boolean isTailleValide(int tailleX, int tailleY) {
		return tailleX <= ContraintesParametres.TAILLE_GRILLE_MAX_X && tailleY <= ContraintesParametres.TAILLE_GRILLE_MAX_Y
				&& tailleX >= ContraintesParametres.TAILLE_GRILLE_MIN_X && tailleY >= ContraintesParametres.TAILLE_GRILLE_MIN_Y;
	}

	// définit le nombre de bêtes affichés à la fois lors de leur sélection
	public static final int INTERVALLE_SELECTION_BETES = 4;

	// définit la distance maximale dont fuit une bête perdante d'un combat
	public static final int DISTANCE_FUITE_MAX = 3;
	// définit la distance minimale dont fuit une bête perdante d'un combat
	public static final int DISTANCE_FUITE_MIN = 1;

	// définit la valeur à laquelle l'énergie maximale de toute bête sera
	// supérieure ou égale
	public static final int ENERGIE_BETE_MIN = 20;

	// vitesse d'éxecution du jeu (en ms)
	public static final int VITESSE_EXECUTION = 500;

	// âge (en millisecondes) minimal pour pouvoir procréer
	public static final int AGE_MIN_PROCREATION = 5;

	/*
	 * une Bête doit avoir EnergieCourante >= EnergieMax/Facteur pour procréer,
	 * autrement dit plus ce facteur est élévé, plus une Bête avec peu d'énergie
	 * courante a de chances de se reproduire
	 */
	public static final int FACTEUR_ENERGETIQUE_PROCREATION = 3;

	public static final int NOMBRE_BETES_GENEREES_NATURE_MIN = 12;

	public static final int NOMBRE_BETES_GENEREES_NATURE_MAX = 8;

	public static final int POURCENTAGE_BETES_INTELLIGENTES = 100;

	public static final int DISTANCE_VISION_MIN = 5;
	
	public static final int DISTANCE_VISION_MAX = 20;

	public static final int SATIETE_MIN = 20;

	public static final int SATIETE_MAX = 100;
	
	/**
	 * Quantité maximale de nourriture présente sur la grille en mode graphique
	 */
	public static final int NB_NOURRITURE_MAX = 15;
	
	
	/**
	 *  Temps en ms entre chaque climat
	 */
	public static final int TEMPS_ENTRE_CLIMAT=20;

}
