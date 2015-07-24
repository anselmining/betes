package betes.console;

import java.util.Collection;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.donnees.ContraintesParametres;
import betes.donnees.EnvironnementDepot;
import betes.donnees.EnvironnementFabrique;
import betes.donnees.GenerationAleatoire;
import betes.donnees.GenerationAleatoireEtFixeConsole;
import betes.donnees.exceptions.BeteSurCaseException;
import betes.donnees.exceptions.CaseInexistanteException;
import betes.donnees.exceptions.CaseNonTraversableException;
import betes.donnees.exceptions.NourritureSurCaseException;
import betes.modeles.bete.Bete;
import betes.modeles.environnement.Case;
import betes.modeles.environnement.Grille;
import betes.statistiques.StatistiquesMoteur;
import betes.visiteurs.Visiteur;
import betes.visiteurs.VisiteurBouger;
import betes.visiteurs.VisiteurCreerBete;
import betes.visiteurs.VisiteurDessinerSurGrille;
import betes.visiteurs.VisiteurPlacerDecor;
import betes.visiteurs.VisiteurPlacerNourriture;
import betes.visiteurs.VisiteurVerifierCase;

public class Console {
	@SuppressWarnings("unused")
	private static Logger logger = LoggerUtility.getLogger(Console.class);
	private static Console instance = new Console("Bêtes Console");
	private static EnvironnementDepot eDepot = EnvironnementDepot.getInstance();
	private String nomConsole;
	private int modeAleatoire;
	private static Scanner lecteur;
	private static int idBeteControlee;
	private static VisiteurDessinerSurGrille peintre = VisiteurDessinerSurGrille.getInstance();
	private EnvironnementFabrique eFabrique = new EnvironnementFabrique();
	VisiteurPlacerNourriture nourrisseur = new VisiteurPlacerNourriture();
	VisiteurPlacerDecor decorateur = new VisiteurPlacerDecor();
	VisiteurCreerBete createur = new VisiteurCreerBete();
	
	 
	

	private Console(String nomConsole) {
		this.nomConsole = nomConsole;
	}

	public static Console getInstance() {
		return instance;
	}

	public int getModeAleatoire() {
		return modeAleatoire;
	}

	public void setModeAleatoire(int modeAleatoire) {
		this.modeAleatoire = modeAleatoire;
	}

	/**
	 * Message de bienvenue et de choix du mode de jeu
	 */
	public void bienvenueEtChoixDeMode() {
		System.out.println("Bienvenue sur " + nomConsole + " !\n");
		System.out.println("Jeu de mini-simulation d’une évolution génétique simplifiée.\n");
		System.out.println("Entrez un entier pour choisir un mode de jeu.\n");

		Boolean bonChoix = false;
		int choix;

		do {
			System.out.println("(0) Mode élevage");
			System.out.println("(1) Mode nature");
			System.out.println("(2) Description des modes\n");

			choix = GenerationAleatoireEtFixeConsole.nombre();
			bonChoix = true;

			switch (choix) {
			case 0:
				setModeAleatoire(0);
				System.out.println("Mode élevage choisi.\n");
				break;
			case 1:
				setModeAleatoire(1);
				System.out.println("Mode nature choisi.\n");
				break;
			case 2:
				System.out.println("\nMode élevage_\n\nComme dans un élevage, " + "vous pouvez contrôler le nombre de bêtes, leurs caractéristiques\n"
						+ "et la taille de leur environnement avec la quantité de nourriture présente.\n"
						+ "Le déroulement des combats est également manuel.\n" + "Cette simulation vous donne la main pour la durée d’une partie.\n");
				System.out.println("Mode nature_\n\nLa nature décide elle-même des caractéristiques de la partie.\n"
						+ "Les bêtes sont de caractéristiques aléatoires.\n" + "La nourriture est repartie au hasard dans l’environnement, "
						+ "de même pour les éléments du décor.\n" + "Vous êtes seulement spectateur, néanmoins vous pouvez agir sur le destin des bêtes\n"
						+ "en arrêteant la partie avant la date décidée par la nature.\n");
			default:
				bonChoix = false;
			}

		} while (bonChoix.equals(false));

		choixGrille();
	}

	/**
	 * Menu de choix de la grille
	 */
	private void choixGrille() {
		if (getModeAleatoire() == 0) { // mode élevage, grille de taille
										// déterminée
			System.out.println("Étape 1/3 : Veuillez choisir la taille de la grille de jeu.");

			Boolean bonChoix = false;

			do {
				System.out.println("Valeurs entières, taille min Grille" + "[" + ContraintesParametres.TAILLE_GRILLE_MIN_X + "]" + "["
						+ ContraintesParametres.TAILLE_GRILLE_MIN_Y + "] " + "et taille max Grille" + "[" + ContraintesParametres.TAILLE_GRILLE_MAX_X + "]"
						+ "[" + ContraintesParametres.TAILLE_GRILLE_MAX_Y + "].\n");

				System.out.println("Grille[x][y] : Entrez la valeur de x.");
				int x = GenerationAleatoireEtFixeConsole.nombre();
				System.out.println("Grille[" + x + "][y] : Entrez la valeur de y :");
				int y = GenerationAleatoireEtFixeConsole.nombre();
				System.out.println("\nGrille[" + x + "][" + y + "] : Vérification ...");

				if (ContraintesParametres.isTailleValide(x, y)) {
					eFabrique.creerGrille(x, y);
					System.out.println("Grille[" + x + "][" + y + "] : Initialisée.");
					bonChoix = true;

				} else {
					System.out.println("Grille[" + x + "][" + y + "] : Invalide, recommencez.\n");
				}

			} while (bonChoix.equals(false));

			// placement aléatoire de décor
			eDepot.getGrille().accepteVisiteur(decorateur);

			afficherGrille();

		} else { // mode nature, grille de taille aléatoire
			int x = GenerationAleatoire.nombreAleatoire(ContraintesParametres.TAILLE_GRILLE_MIN_X, ContraintesParametres.TAILLE_GRILLE_MAX_X);
			int y = GenerationAleatoire.nombreAleatoire(ContraintesParametres.TAILLE_GRILLE_MIN_Y, ContraintesParametres.TAILLE_GRILLE_MAX_Y);
			eFabrique.creerGrille(x, y);
			System.out.println("Grille[" + x + "][" + y + "] : Initialisée.");

			// placement aléatoire de décor
			eDepot.getGrille().accepteVisiteur(decorateur);
		}
		creerBete();
	}

	/**
	 * Affiche la grille
	 */
	public static void afficherGrille() {
		peintre.setIdBeteControlee(idBeteControlee);
		Grille grille = eDepot.getGrille();
		grille.accepteVisiteur(peintre);
	}

	/**
	 * Crée des bêtes
	 */
	public void creerBete() {
		VisiteurCreerBete createurDeBetes = new VisiteurCreerBete();
		createurDeBetes.visite(this);

		if (modeAleatoire == 0) {
			setIdBeteControlee(0);
			choisirActionElevage();
		} else {
			setIdBeteControlee(-1);
			Grille grille = eDepot.getGrille();
			Console.getInstance().accepteVisiteur(peintre);
			grille.accepteVisiteur(peintre);
			choisirActionNature();
		}

	}

	/**
	 * Affiche le menu du mode nature
	 */
	public static void afficherMenuNature() {
		if (VisiteurBouger.getInstance().isStop()) {
			System.out.println("[---------[ Temps écoulé : " + eDepot.getTempsEcoule() + "ms ][ Nombre de bêtes vivantes : " + eDepot.getNombreBetesVivantes()
					+ " ]");
			System.out.println("[ MESSAGE [ Sélectionnez la prochaine action s'il vous plaît !       ]---*-*-*-*-*-*-*-*-----]");
			System.out.println("[---------[ r - regarder ;  d - démarrer ; s - stats ;  q - quitter  ]-*-----------------*---]");
			System.out.println("[---------[    n - placer nourriture     ;      o - placer décor     ]-*-[Bêtes-CONSOLE]-*---]");
			System.out.println("[---------[  g - revoir grille  ;  c - créer Bête ; v - vendre Bête  ]-*-----------------*---]");
		} else {
			System.out.println("[---------[ Temps écoulé : " + eDepot.getTempsEcoule() + "ms ][ Nombre de bêtes vivantes : " + eDepot.getNombreBetesVivantes()
					+ " ]");
			System.out.println("[---------[              a - arrêter la simulation                   ]-*-[Bêtes-CONSOLE]-*---]");
			
			
		}
		StatistiquesMoteur.enregistrerNombreDeBetesVivantes();
	
	}

	/**
	 * Gère le choix d'action en mode nature
	 */
	private void choisirActionNature() {
		lecteur = new Scanner(System.in);
		String line = "";
		Boolean finDeSimulationAnoncee = false;

		while (!line.equalsIgnoreCase("q")) {

			if (eDepot.getNombreBetesVivantes() <= 0 && finDeSimulationAnoncee.equals(false)) {
				System.out.println("[         [ Fin de simulation.  Il n'y a plus de bêtes vivantes.\n");
				finDeSimulationAnoncee = true;
			}

			Console.afficherMenuNature();

			try {
				line = lecteur.next("[rdsaqnogcv]");
			} catch (InputMismatchException ime) {
				lecteur.nextLine();
				System.err.println("N'entrez pas n'importe quoi s'il vous plaît !\n");
				continue;
			}

			VisiteurBouger bougeurDeBetes = VisiteurBouger.getInstance();
			Thread bougerThread = new Thread(bougeurDeBetes);

			switch (line) {
			case "r":
				if (bougeurDeBetes.isStop() == true) {
					regarderCase();
				}
				break;
			case "d":
				if (bougeurDeBetes.isStop() == true) {
					if (eDepot.getNombreBetesVivantes() <= 0) {
						System.out.println("[         [ Démarrage impossible.  Il n'y a plus de bêtes vivantes.\n");
						continue;
					}
					bougeurDeBetes.setStop(false);
					bougerThread.start();
					break;
				}
			case "s":
				if (bougeurDeBetes.isStop() == true) {
					peintre.statsNbBetesParRapportAuTemps();
					System.out.println("\n");
				}
				break;
			case "g":
				if (bougeurDeBetes.isStop() == true) {
					afficherGrille();
				}
				break;
			case "a":
				bougeurDeBetes.setStop(true);
				break;
			case "q":
				if (bougeurDeBetes.isStop() == false) {
					line = "";
					break;
				}
				bougeurDeBetes.setStop(true);
				System.out.println("Fermeture...");
				break;
			case "n":
				if (bougeurDeBetes.isStop() == true) {
					nourrisseur.placerManuellementNourriture();
				}
				break;
			case "o":
				if (bougeurDeBetes.isStop() == true) {
					decorateur.placerDecor();
				}
				break;
			case "c":
				if (bougeurDeBetes.isStop() == true) {
					this.accepteVisiteur(createur);
				}
				break;
			case "v":
				if (bougeurDeBetes.isStop() == true) {
					createur.vendreBete();
				}
				break;
			}

		}

		System.out.println("Fin du jeu, bonne journée.\n\n" + "Entrez n'importe quoi pour relancer une partie.");

		lecteur.next();
	
	}

	public void accepteVisiteur(Visiteur visiteur) {
		visiteur.visite(this);
	}

	/**
	 * Gére le choix d'action en mode élevage
	 */
	private void choisirActionElevage() {
		lecteur = new Scanner(System.in);
		String line = "";
		VisiteurPlacerNourriture nourrisseur = new VisiteurPlacerNourriture();
		VisiteurPlacerDecor decorateur = new VisiteurPlacerDecor();

		while (!line.equalsIgnoreCase("q")) {
			Bete bete = eDepot.rechercher(getIdBeteControlee());

			if (eDepot.getNombreBetesVivantes() <= 0) {
				System.out.println("[         [  Il n'y a plus de bêtes vivantes.\n");
				break;
			}

			System.out.println("[---------[ Sélectionnez la prochaine action s'il vous plaît !       ]------------------]");
			System.out.println("[ MESSAGE [ r - regarder ; d - déplacer Bête ; s - sélectionner Bête ]---*-*-*-*-*-*-*-*-----]");
			System.out.println("[---------[  g - revoir grille ; c - créer Bête ; v - vendre Bête    ]-*-[Bêtes-CONSOLE]-*---]");
			System.out.println("[---------[ t - stats ;   n - placer nourriture ;  o - placer décor  ]-*-[Bêtes-CONSOLE]-*---]");

			try {
				line = lecteur.next("[rdgsqtnocv]");
			} catch (InputMismatchException ime) {
				lecteur.nextLine();
				System.err.println("N'entrez pas n'importe quoi s'il vous plaît !\n");
				continue;
			}

			switch (line) {
			case "r":
				regarderCase();
				break;
			case "d":
				if (getIdBeteControlee() == -1) {
					System.err.println("[  ERREUR [ Sélectionnez une bête à contrôler !");
				} else {
					VisiteurBouger bougeurDeBetes = VisiteurBouger.getInstance();
					this.accepteVisiteur(bougeurDeBetes);
					bete.accepteVisiteur(bougeurDeBetes);
				}
				break;
			case "g":
				afficherGrille();
				break;
			case "s":
				selectionnerBete();
				break;
			case "q":
				System.out.println("Fermeture...");
				break;
			case "n":
				nourrisseur.placerManuellementNourriture();
				break;
			case "o":
				decorateur.placerDecor();
				break;
			case "t":
				peintre.statsNbBetesParRapportAuTemps();
				System.out.println("\n");
				break;
			case "c":
				this.accepteVisiteur(createur);
				break;
			case "v":
				createur.vendreBete();
				break;
			}

		}

		System.out.println("Fin du jeu, bonne journée.\n\n" + "Entrez n'importe quoi pour relancer une partie.");

		lecteur.next();
		
	}

	private void selectionnerBete() {
		// betesHM est une copie du conteneur de toutes les bêtes
		// nécessaire pour éviter ConcurrentModificationException
		HashMap<Integer, Bete> betesHM = new HashMap<Integer, Bete>();
		betesHM.putAll(eDepot.getBetes());
		betesHM.remove(getIdBeteControlee());

		Collection<Bete> betes = betesHM.values();

		int i = 0, quitter, tailleHM = betes.size();

		for (Bete bete : betes) {
			System.out.println(bete.toString());
			i++;
			if (i % ContraintesParametres.INTERVALLE_SELECTION_BETES == 0 && i < tailleHM) {
				System.out.println("[---------[ Afficher d'autres bêtes ? (0) Non (1) Oui");
				quitter = GenerationAleatoireEtFixeConsole.nombre(0, 1);
				if (quitter == 0) {
					break;
				}
			}
		}

		String quitterVerification;
		Boolean bonChoix = false;
		int nouveauIdBeteControlee;

		do {
			try {
				lecteur = new Scanner(System.in);
				System.out.println("[---------[ Entrez l'identifiant de la bête à contrôler [q - quitter]");
				quitterVerification = String.valueOf(lecteur.next("[\\dq]+"));

				if (quitterVerification.equals("q")) {
					break;
				}

				nouveauIdBeteControlee = Integer.valueOf(quitterVerification);

				if (nouveauIdBeteControlee == getIdBeteControlee()) {
					System.err.println("Vous contrôlez déjà Bête (" + nouveauIdBeteControlee + ") !");
					continue;
				}

				if (betesHM.get(nouveauIdBeteControlee) == null) {
					System.err.println("Il n'y a aucune bête à cet identifiant !");
					continue;
				}

				setIdBeteControlee(nouveauIdBeteControlee);

				VisiteurBouger bougeurDeBetes = VisiteurBouger.getInstance();
				bougeurDeBetes.visite(this);// maintenant il sait qui bouger
				bonChoix = true;

			} catch (InputMismatchException ime) {
				System.err.println("N'entrez pas n'importe quoi s'il vous plaît !\n");
			}

		} while (bonChoix == false);

	}

	private void regarderCase() {
		Grille grille = eDepot.getGrille();
		Case laCase;
		Boolean bonChoix = false;
		String quitterVerification;

		System.out.println("\nChoisissez une Case sur Grille[" + grille.getTailleX() + "][" + grille.getTailleY() + "] [ q - quitter ]");

		do {
			try {
				lecteur = new Scanner(System.in);

				System.out.println("Case[x][y] : Entrez la valeur de x :");
				quitterVerification = String.valueOf(lecteur.next("[\\dq]+"));
				if (quitterVerification.equals("q")) {
					break;
				}
				int x = Integer.valueOf(quitterVerification);
				lecteur.nextLine();

				System.out.println("Case[" + x + "][y] : Entrez la valeur de y :");
				quitterVerification = String.valueOf(lecteur.next("[\\dq]+"));
				if (quitterVerification.equals("q")) {
					break;
				}
				int y = Integer.valueOf(quitterVerification);
				lecteur.nextLine();

				Boolean decrireDecor = true, rencontrerBete = false, manger = false, detecterCollisions = false;
				VisiteurVerifierCase caseDescripteur = new VisiteurVerifierCase(null, decrireDecor, rencontrerBete, manger, detecterCollisions);
				if (x == 0 || y == 0) {
					throw new CaseInexistanteException();
				}
				laCase = eDepot.rechercher(x, y);
				caseDescripteur.visite(laCase);

				bonChoix = true;

			} catch (InputMismatchException ime) {
				System.err.println("N'entrez pas n'importe quoi s'il vous plaît !\n");
			} catch (ArrayIndexOutOfBoundsException aiooe) {
				System.err.println("Il n'y a aucune case à la position demandée.\n");
			} catch (NullPointerException npe) {
				System.err.println("Il n'y a aucune case à la position demandée.\n");
			} catch (BeteSurCaseException bsce) {
				System.err.println("La case demandé est déjà occupée.\n");
			} catch (CaseInexistanteException e) {
				System.err.println("La case demandée est inexistante.\n");
			} catch (CaseNonTraversableException e) {
				System.err.println("La case demandée est inaccessible.\n");
			} catch (NourritureSurCaseException e) {
			}
		} while (bonChoix == false);
	}

	

	public int getIdBeteControlee() {
		return idBeteControlee;
	}

	public void setIdBeteControlee(int idBeteControlee) {
		Console.idBeteControlee = idBeteControlee;
	}

}
