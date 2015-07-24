package betes.donnees;

import java.util.InputMismatchException;

import java.util.Scanner;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.donnees.exceptions.BeteSurCaseException;
import betes.donnees.exceptions.CaseInexistanteException;
import betes.donnees.exceptions.CaseNonTraversableException;
import betes.donnees.exceptions.NourritureSurCaseException;
import betes.modeles.environnement.Case;
import betes.modeles.environnement.Grille;
import betes.visiteurs.VisiteurVerifierCase;

/*
 * <p>Classe de traitement de données adaptée au mode console qui génère aléatoirement ou non:</p>
 * 
 * <ol>
 * <li>Un nom de bête</li>
 * <li>Un sexe de bête</li>
 * <li>Une position d'élément</li>
 * <li>Un nombre entier positif ou nul</li>
 * </ol>
 * 
 *@author NDEKE A. 
 *
 */
public class GenerationAleatoireEtFixeConsole extends GenerationAleatoire {
	private static Logger logger = LoggerUtility.getLogger(GenerationAleatoireEtFixeConsole.class);
	private static Scanner lecteur;

	/*
	 * Demande le nom d'une bête.
	 * 
	 * @return La chaîne correspondant au nom de la bête.
	 */
	public static String nomBete() {
		String nomBete = "";

		do {
			try {
				System.out.println("Entrez un nom de bête (de 2 à 15 caractères) s'il vous plaît:");
				lecteur = new Scanner(System.in);
				nomBete = lecteur.next("[\\wàáâãäçèéêëìíîïñòóôõöùúûüýÿÀÁÂÃÄÇÈÉÊËÌÍÎÏÑÒÓÔÕÖÙÚÛÜÝ]*");
			} catch (InputMismatchException ime) {
				nomBete = "";
				System.err.println("\nCaractères alphanumériques seulement s'il vous plaît !\n");
			}
		} while (nomBete.length() < 2 || 15 < nomBete.length());

		return nomBete;
	}

	/*
	 * Demande le sexe d'une bête.
	 * 
	 * @return La chaîne "male" ou "femelle".
	 */
	public static String sexe() {
		int sexe = -1;
		do {
			try {
				System.out.println("\nEntrez un entier pour choisir le sexe de la bête:\n");
				System.out.println("(0) Femelle");
				System.out.println("(1) Male");
				lecteur = new Scanner(System.in);
				sexe = lecteur.nextInt();
			} catch (InputMismatchException ime) {
				System.err.println("N'entrez pas n'importe quoi s'il vous plaît !\n");
			}
		} while (sexe != 0 && sexe != 1);

		if (sexe == 1) {
			return "male";
		} else {
			return "femelle";
		}
	}

	// public Instrument instrument()

	/*
	 * Donne une Case choisie et accessible (traversable).
	 * 
	 * @return Une instance de la classe Case.
	 */
	public static Case position() {
		EnvironnementDepot eDepot = EnvironnementDepot.getInstance();
		Grille grille = eDepot.getGrille();

		Boolean bonChoix = false;
		Case laCase = null;

		int positionXMax = grille.getTailleX();
		int positionYMax = grille.getTailleY();

		do {
			System.out.println("\nChoisissez une Case sur Grille[" + positionXMax + "][" + positionYMax + "]. (-1) quitter");

			try {
				lecteur = new Scanner(System.in);
				System.out.println("Case[x][y] : Entrez la valeur de x :");
				int x = lecteur.nextInt();
				if (x == -1) {
					return null;
				}
				lecteur.nextLine();
				System.out.println("Case[" + x + "][y] : Entrez la valeur de y :");
				int y = lecteur.nextInt();
				if (y == -1) {
					return null;
				}
				lecteur.nextLine();
				System.out.println("\nCase[" + x + "][" + y + "] : Recherche ...");

				Boolean decrireDecor = false, rencontrerBete = false, manger = false, detecterCollisions = true;
				VisiteurVerifierCase caseVerificateur = new VisiteurVerifierCase(null, decrireDecor, rencontrerBete, manger, detecterCollisions);
				laCase = eDepot.rechercher(x, y);
				caseVerificateur.visite(laCase);

				bonChoix = true;

			} catch (InputMismatchException ime) {
				System.err.println("N'entrez pas n'importe quoi s'il vous plaît !\n");
			} catch (ArrayIndexOutOfBoundsException aiooe) {
				System.err.println("Il n'y a aucune case à la position demandée.\n");
			} catch (NullPointerException npe) {
				logger.fatal("[NPE] Il n'y a aucune case à la position demandée.\n");
			} catch (BeteSurCaseException bsce) {
				System.err.println("La case demandé est déjà occupée.\n");
			} catch (CaseInexistanteException e) {
				System.err.println("La case demandée est inexistante.\n");
			} catch (CaseNonTraversableException e) {
				System.err.println("La case demandée est inaccessible.\n");
			} catch (NourritureSurCaseException e) {
			}
		} while (bonChoix == false);

		eDepot.enregistrer(laCase);
		System.out.println("La Case [" + laCase.getX() + "][" + laCase.getY() + "] est sélectionnée.");

		return laCase;

	}

	/*
	 * Demande un nombre entier positif ou nul. Ce nombre peut être l'âge d'une
	 * bête, son énergie, son nombre de combats ou d'accouplements.
	 * 
	 * @return L'entier choisi.
	 */
	public static int nombre() {

		Boolean bonChoix = false;
		int nombre = -1;
		do {
			try {
				lecteur = new Scanner(System.in);
				System.out.println("Entrez un entier positif ou nul :");
				nombre = lecteur.nextInt();
				if (nombre > -1) {
					bonChoix = true;
				}
			} catch (InputMismatchException ime) {
				bonChoix = false;
				System.err.println("N'entrez pas n'importe quoi s'il vous plaît !\n");
			}

		} while (bonChoix == false);

		return nombre;
	}

	public static int nombre(int min, int max) {

		Boolean bonChoix = false;
		int nombre = -1;
		do {
			try {
				lecteur = new Scanner(System.in);
				System.out.println("Entrez un entier compris entre " + min + " et " + max + " inclus :");
				nombre = lecteur.nextInt();
				if (nombre <= max && min <= nombre) {
					bonChoix = true;
				}
			} catch (InputMismatchException ime) {
				bonChoix = false;
				System.err.println("N'entrez pas n'importe quoi s'il vous plaît !\n");
			}

		} while (bonChoix == false);

		return nombre;
	}
}
