package betes.donnees;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.modeles.bete.Arme;
import betes.modeles.bete.Armure;
import betes.modeles.bete.Femelle;
import betes.modeles.bete.Male;
import betes.modeles.environnement.Case;
import betes.modeles.environnement.Grille;
import betes.modeles.environnement.Nourriture;

/*
 * Classe qui fabrique (instancie) les différents éléments de l'environnement.
 */
public class EnvironnementFabrique {
	private static Logger logger = LoggerUtility.getLogger(EnvironnementFabrique.class);

	/*
	 * Instancie et enregistre une grille dans l'environnement (dépôt).
	 * 
	 * @param tailleX La taille X de la grille.
	 * 
	 * @param tailleY La taille Y de la grille.
	 */
	public void creerGrille(int tailleX, int tailleY) {

		Grille grille = new Grille(tailleX, tailleY);

		EnvironnementDepot environnementDepot = EnvironnementDepot.getInstance();
		environnementDepot.setGrille(grille);

		for (int i = 0; i <= tailleX; i++) {
			for (int j = 0; j <= tailleY; j++) {
				logger.trace("Case[" + i + "][" + j + "] crée.");
				environnementDepot.enregistrer(creer(i, j));
			}
		}
		
		for (int i = 0; i <= tailleX; i++) {
			environnementDepot.rechercher(i, 0).setTraversable(false);
			environnementDepot.rechercher(i, tailleY).setTraversable(false);
		}
		for (int j = 0; j <= tailleY; j++) {
			environnementDepot.rechercher(0, j).setTraversable(false);
			environnementDepot.rechercher(tailleX, j).setTraversable(false);
		}
	}

	/*
	 * Instancie une Case à partir de ses coordonnées x et y.
	 * 
	 * @param x La position x de la Case.
	 * 
	 * @param y La position y de la Case.
	 * 
	 * @return Une Case à la position (x;y).
	 */
	public Case creer(int x, int y) {
		return new Case(x, y);
	}
	/*
	 * Instancie une Case à partir de coordonnées d'une autre Case
	 * 
	 * @param laCase l'autre Case.
	 * 
	 * @return Une Case à la position (x;y).
	 */
	public Case creer(Case laCase) {
		return new Case(laCase.getX(), laCase.getY());
	}

	/*
	 * Instancie un élément de décor. (Case)
	 * 
	 * @param x La position x de la Case.
	 * 
	 * @param y La position y de la Case.
	 * 
	 * @param nomDecor Le nom du décor.
	 * 
	 * @param image image du décor.
	 * 
	 * @param traversable Définit si le décor est traversable ou non.
	 */
	public Case creer(int x, int y, String nomDecor, String image, Boolean traversable) {
		return new Case(x, y, nomDecor, image, traversable);
	}

	/*
	 * Instancie un élément de décor.
	 * 
	 * @param x La position x de la Nourriture.
	 * 
	 * @param y La position y de la Nourriture.
	 * 
	 * @param nomNourriture Le nom de la Nourriture.
	 * 
	 * @param satiete Entier qui augmente l'énergie de la Bête
	 * 
	 * @param traversable Définit si le décor est traversable ou non.
	 */
	public Nourriture creerNourriture(int x, int y, String nomNourriture, String image, int satiete, Boolean traversable) {
		return new Nourriture(x, y, nomNourriture, image, satiete, traversable);
	}

	/*
	 * Instancie une bête Male
	 * 
	 * @param nomBete Le nom de la bête.
	 * 
	 * @param sexe Le sexe de la bête.
	 * 
	 * @param age L'âge de la bête.
	 * 
	 * @param energie L'énergie de la bête.
	 * 
	 * @param nbCombats Le nombre de combats effectués par la bête.
	 * 
	 * @param nbAccouplement Le nombre d'accouplements effectués par la bête.
	 * 
	 * @param facteurProcreation Facteur qui favorise une procreation efficace
	 * ou non.
	 * 
	 * @return Une Bete Male
	 */
	public Male creerMale(String nomBete, String sexe, int age, int energie, int energieMax, int nbCombats, int nbAccouplements, int facteurProcreation) {
		return new Male(nomBete, sexe, age, energie, energieMax, nbCombats, nbAccouplements, facteurProcreation);
	}

	/*
	 * Instancie une bête Femelle
	 * 
	 * @param nomBete Le nom de la bête.
	 * 
	 * @param sexe Le sexe de la bête.
	 * 
	 * @param age L'âge de la bête.
	 * 
	 * @param energie L'énergie de la bête.
	 * 
	 * @param nbCombats Le nombre de combats effectués par la bête.
	 * 
	 * @param nbAccouplement Le nombre d'accouplements effectués par la bête.
	 * 
	 * @param facteurProcreation Facteur qui favorise une procreation efficace
	 * ou non.
	 * 
	 * @return Une Bete Femelle
	 */
	public Femelle creerFemelle(String nomBete, String sexe, int age, int energie, int energieMax, int nbCombats, int nbAccouplements,
			int facteurProcreation) {
		return new Femelle(nomBete, sexe, age, energie, energieMax, nbCombats, nbAccouplements, facteurProcreation);
	}
	
	public Arme creerArme(int attaque) {
		return new Arme(attaque);
	}
	
	public Armure creerArmure(int defense) {
		return new Armure(defense);
	}
}
