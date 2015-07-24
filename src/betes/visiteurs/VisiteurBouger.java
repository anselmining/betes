package betes.visiteurs;

import java.util.Collection;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import log.LoggerUtility;

import betes.astar.RechercheAstar;
import betes.console.Console;
import betes.donnees.ContraintesParametres;
import betes.donnees.GenerationAleatoire;
import betes.donnees.exceptions.BeteSurCaseException;
import betes.donnees.exceptions.CaseInexistanteException;
import betes.donnees.exceptions.CaseNonTraversableException;
import betes.donnees.exceptions.NourritureSurCaseException;
import betes.gui.GraphicUserInterface;
import betes.gui.dessin.JCanvas;
import betes.gui.frames.InGame;
import betes.modeles.bete.Bete;
import betes.modeles.environnement.Case;
import betes.modeles.environnement.Grille;
import betes.modeles.environnement.Nourriture;
import betes.statistiques.StatistiquesMoteur;

public class VisiteurBouger implements Visiteur, Runnable {
	private static Logger logger = LoggerUtility.getLogger(VisiteurBouger.class);
	private static Scanner lecteur;
	private int idBeteControlee = -1;
	private boolean stop = true;
	private static VisiteurBouger instance = new VisiteurBouger();
	private RechercheAstar astar = new RechercheAstar();
	float v=(float) 0.2;
	private VisiteurBouger() {
	}

	/**
	 * @return (VisiteurBouger) instance: une instance de VisiteurBouger.
	 */
	public static VisiteurBouger getInstance() {
		return instance;
	}

	/**
	 * @return (boolean) stop: arrête ou non un thread.
	 */
	public boolean isStop() {
		return stop;
	}

	@Override
	public void visite(Bete bete) {
		lecteur = new Scanner(System.in);
		String ligne = "", description;

		logger.warn("[  AIDE   [ j - à gauche ; i - en haut       ]---*-*-*-*-*-*-*-*-----]");
		logger.warn("[---------[ l - à droite ; k - en bas        ]-*-[Bêtes-CONSOLE]-*---]\n");

		while (!ligne.equalsIgnoreCase("q")) {
			description = "[---------[ (" + bete.getIdBete() + ")";

			if (bete.getSexe().equals("male")) {
				description += " ♂ ";
			} else {
				description += " ♀ ";
			}

			description += bete.getNomBete() + "[" + bete.getPositionX() + "][" + bete.getPositionY() + "] [" + bete.getEnergie() + "/" + bete.getEnergieMax()
					+ "♥] [" + bete.getAge() + " µs]";

			if (bete.getSatieteBete() > 50) {
				description += " N'a pas faim. ] ";
			} else {
				description += "  A faim. ] ";
			}

			logger.warn(description);
			logger.warn("[---------[ a - afficher aide ; g - revoir grille ; q - quitter  ]");

			try {
				ligne = lecteur.next("[jiklagq]+").substring(0, 1);
			} catch (InputMismatchException ime) {
				lecteur.nextLine();
				System.err.println("N'entrez pas n'importe quoi s'il vous plaît !\n");
				continue;
			}

			int vieuxX = bete.getPositionX();
			int vieuxY = bete.getPositionY();

			Case caseNouvelle = null;

			try {
				switch (ligne) {
				case "j":
					caseNouvelle = eDepot.rechercher(vieuxX, vieuxY - 1);
					break;
				case "i":
					caseNouvelle = eDepot.rechercher(vieuxX - 1, vieuxY);
					break;
				case "l":
					caseNouvelle = eDepot.rechercher(vieuxX, vieuxY + 1);
					break;
				case "k":
					caseNouvelle = eDepot.rechercher(vieuxX + 1, vieuxY);
					break;
				case "a":
					logger.warn("[  AIDE   [ j - à gauche ; i - en haut       ]---*-*-*-*-*-*-*-*-----]");
					logger.warn("[         [ l - à droite ; k - en bas        ]-*-[Bêtes-CONSOLE]-*---]\n");
					continue;
				case "g":
					Console.afficherGrille();
					continue;
				case "q":
					logger.warn("Fermeture...");
					continue;
				}

				verifierCaseEtPlacerBete(bete, caseNouvelle);

			} catch (BeteSurCaseException esce) {
				continue;
			} catch (NourritureSurCaseException e) {
			} catch (CaseNonTraversableException cnte) {
				logger.info("Case non traversable exception rencontrée.");
				afficherBump();
				continue;
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				logger.info("Array Index Out Of Bound exception rencontrée.");
				afficherBump();
				continue;
			} catch (CaseInexistanteException cie) {
				logger.info("Case inexistante exception rencontrée.");
				afficherBump();
				continue;
			} finally {
				bete.setAge(bete.getAge() + 1);
				bete.setEnergie(bete.getEnergie() - 1);
				if (isBeteControleeMorte(bete).equals(true)) {
					break;
				}
			}

			deplacerIA();
			VisiteurPlacerNourriture.placerNourriture();
			if (ContraintesParametres.modeConsole.equals(true)) {
				Console.afficherGrille();
			}
			eDepot.setTempsEcoule(eDepot.getTempsEcoule() + 1);
		}
	}

	/**
	 * Déplace les bêtes contrôlées par l'intelligence artificielle.
	 */
	public void deplacerIA() {
		// betesHM est une copie du conteneur de toutes les bêtes
		// nécessaire pour éviter ConcurrentModificationException
		HashMap<Integer, Bete> betesHM = new HashMap<Integer, Bete>();
		betesHM.putAll(eDepot.getBetes());
		betesHM.remove(getIdBeteControlee());

		Collection<Bete> betes = betesHM.values();

		for (Bete beteIA : betes) {
			if (beteIA.getEnergie() > 0) {
				mouvementIA(beteIA);
			}
		}
	}

	/**Déplace une bête par l'intelligence artificielle.
	 * @param (Bete) bete :bête controlée.
	 */
	public void mouvementIA(Bete bete) {
		VisiteurVision vision = new VisiteurVision(bete.getDistanceVision());
		bete.accepteVisiteur(vision);
		Case debut = bete.getPosition();
		bete.setPositionAvantX(debut.getX());
		bete.setPositionAvantY(debut.getY());
		Case fin = null;
		Nourriture nourritureProche = vision.getNourritureProche();

		if (bete.getIntelligente().equals(true)) {
			// recherche de nourriture en priorité
			if (bete.getSatieteBete() < 50 && nourritureProche != null) {
				logger.info(bete.getNomBete() + "[" + bete.getPositionX() + "][" + bete.getPositionY() + "] veut manger "
						+ vision.getNourritureProche().getNomNourriture());
				fin = nourritureProche.getPosition();
			} else {
				Bete beteProche = vision.getBeteProche();
				if (beteProche != null) {
					if (!vision.isMemeSexe()) {
						if (vision.isProbableReproduction().equals(true)) {
							logger.info(bete.getNomBete() + "[" + bete.getPositionX() + "][" + bete.getPositionY() + "] veut se reproduire avec "
									+ beteProche.getNomBete() + "[" + beteProche.getPositionX() + "][" + beteProche.getPositionY() + "]");
							fin = eDepot.rechercher(beteProche.getPosition());
						} else {
							logger.info(bete.getNomBete() + "[" + bete.getPositionX() + "][" + bete.getPositionY() + "] veut éviter de procréer avec "
									+ beteProche.getNomBete() + "[" + beteProche.getPositionX() + "][" + beteProche.getPositionY() + "]");
							VisiteurFuire fuite = new VisiteurFuire(beteProche.getPosition());
							fin = eDepot.rechercher(fuite.donneCaseDEchappement(bete));
						}
					} else {// sexe différent
						if (vision.isProbableVictoire().equals(true)) {
							logger.info(bete.getNomBete() + "[" + bete.getPositionX() + "][" + bete.getPositionY() + "] veut battre " + beteProche.getNomBete()
									+ "[" + beteProche.getPositionX() + "][" + beteProche.getPositionY() + "]");
							fin = eDepot.rechercher(beteProche.getPosition());
						} else {
							logger.info(bete.getNomBete() + "[" + bete.getPositionX() + "][" + bete.getPositionY() + "] veut éviter de combattre avec "
									+ beteProche.getNomBete() + "[" + beteProche.getPositionX() + "][" + beteProche.getPositionY() + "]");
							VisiteurFuire fuite = new VisiteurFuire(beteProche.getPosition());
							fin = eDepot.rechercher(fuite.donneCaseDEchappement(bete));
						}
					}
				}
			}
		}

		if (fin == null) { // cas où Bête non intelligente ou chemin de
							// destination inconnu
			mouvementIAAleatoire(bete);
		} else {
			Case caseProche = astar.donneCaseProche(debut, fin);
			if (caseProche == null) {
				caseProche = debut;
			}

			fin = eFabrique.creer(fin.getY(), fin.getX());

			logger.info("Mouvement intelligent par " + bete.getNomBete() + " de " + debut.getX() + ";" + debut.getY() + " à (" + caseProche.getX() + ";"
					+ caseProche.getY() + ") dans le but d'atteindre (" + fin.getY() + ";" + fin.getX() + ")");
			try {
				verifierCaseEtPlacerBete(bete, caseProche);
			} catch (NourritureSurCaseException e) {
			} catch (CaseNonTraversableException cnte) {
			} catch (ArrayIndexOutOfBoundsException aioobe) {
			} catch (BeteSurCaseException bsce) {
			} catch (CaseInexistanteException cie) {
			} finally {
				bete.setAge(bete.getAge() + 1);
				bete.setEnergie(bete.getEnergie() - 1);
				verifierMortBeteIA(bete);
			}
			VisiteurPlacerNourriture.placerNourriture();
		}
	}

	private void mouvementIAAleatoire(Bete bete) {
		int vieuxX = bete.getPositionX();
		int vieuxY = bete.getPositionY();
		int mouvement = GenerationAleatoire.nombreAleatoire(0, 10);

		Case caseNouvelle = null;

		try {
			switch (mouvement) {
			case 1:
				caseNouvelle = eDepot.rechercher(vieuxX, vieuxY - v);
				break;
			case 2:
				caseNouvelle = eDepot.rechercher(vieuxX - v, vieuxY);
				break;
			case 3:
				caseNouvelle = eDepot.rechercher(vieuxX, vieuxY + v);
				break;
			case 4:
				caseNouvelle = eDepot.rechercher(vieuxX + v, vieuxY);
				break;
			default:
				caseNouvelle = eDepot.rechercher(vieuxX, vieuxY);
				break;
			}

			verifierCaseEtPlacerBete(bete, caseNouvelle);

		} catch (NourritureSurCaseException e) {
		} catch (CaseNonTraversableException cnte) {
		} catch (ArrayIndexOutOfBoundsException aioobe) {
		} catch (BeteSurCaseException bsce) {
		} catch (CaseInexistanteException cie) {
		} finally {
			bete.setAge(bete.getAge() + 1);
			bete.setEnergie(bete.getEnergie() - 1);
			verifierMortBeteIA(bete);
		}
		VisiteurPlacerNourriture.placerNourriture();
	}

	private void verifierCaseEtPlacerBete(Bete bete, Case caseNouvelle) throws CaseInexistanteException, CaseNonTraversableException, BeteSurCaseException,
			NourritureSurCaseException {

		Case vieilleCase = eDepot.rechercher(bete.getPosition());

		if (!vieilleCase.equals(caseNouvelle)) {
			Boolean decrireDecor = false, rencontrerBete = true, manger = true, detecterCollisions = true;
			VisiteurVerifierCase caseVerificateur = new VisiteurVerifierCase(bete, decrireDecor, rencontrerBete, manger, detecterCollisions);
			caseVerificateur.visite(caseNouvelle);

			if (caseNouvelle instanceof Nourriture) {
				logger.info("La vieille case est de la Nourriture!");
				caseNouvelle = eFabrique.creer(bete.getPosition());
			}

			caseNouvelle = eFabrique.creer(caseNouvelle.getX(), caseNouvelle.getY());

			// logger.info("Nouvelle Position Case[" + caseNouvelle.getX() +
			// "][" + caseNouvelle.getY() + "]");
			bete.setPosition(caseNouvelle);
			bete.setAge(bete.getAge() + 1);
			bete.setSatieteBete(bete.getSatieteBete() - 1);

			if (bete.getSatieteBete() < ContraintesParametres.SATIETE_MAX / 2) {
				bete.setEnergie(bete.getEnergie() - 1);
			}

			eDepot.enregistrer(vieilleCase);
			eDepot.enregistrer(caseNouvelle);
			eDepot.enregistrer(bete);
		}

	}

	private Boolean isBeteControleeMorte(Bete bete) {
		if (bete.getEnergie() <= 0) {
			logger.info("++++++ " + bete.getNomBete() + " vient de mourir ! ++++++");

			Case laCase = eDepot.rechercher(bete.getPositionX(), bete.getPositionY());
			eDepot.enregistrer(laCase);

			eDepot.setNombreBetesVivantes(eDepot.getNombreBetesVivantes() - 1);
			StatistiquesMoteur.enregistrerNombreDeBetesVivantes();

			bete.setPosition(-1, -1);
			bete.setEnergie(0);

			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
			}
			bete.setNomBete("(☠)" + bete.getNomBete());
			setIdBeteControlee(-1);
			Console.getInstance().setIdBeteControlee(-1);
			if (ContraintesParametres.modeConsole.equals(true)) {
				Console.afficherGrille();
			}
			return true;
		}
		return false;
	}

	/**Vérifie si une bête contrôlée par l'intelligence artificielle est morte.
	 * @param (Bete) bete: bete contrôlée.
	 */
	public void verifierMortBeteIA(Bete bete) {
		if (bete.getEnergie() <= 0 && bete.getPositionX() != -1) {
			logger.info("++++++ " + bete.getNomBete() + "[" + bete.getPositionX() + "][" + bete.getPositionY() + "] vient de mourir ! ++++++");
			rEvent.registreEvent("+++ " + bete.getNomBete() + " vient de mourir! +++");

			Case laCase = eDepot.rechercher(bete.getPosition());
			eDepot.enregistrer(laCase);
			eDepot.setNombreBetesVivantes(eDepot.getNombreBetesVivantes() - 1);

			StatistiquesMoteur.enregistrerNombreDeBetesVivantes();
			bete.setPosition(-1, -1);
			bete.setEnergie(0);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void visite(Console console) {
		setIdBeteControlee(console.getIdBeteControlee());
	}

	public int getIdBeteControlee() {
		return idBeteControlee;
	}

	public void setIdBeteControlee(int idBeteControlee) {
		this.idBeteControlee = idBeteControlee;
	}

	@Override
	public void run() {

		while (!stop) {
			if (eDepot.getNombreBetesVivantes() <= 0) {
				stop = true;
				break;
			}

			try {
				Thread.sleep(ContraintesParametres.VITESSE_EXECUTION);
			} catch (InterruptedException e) {
				logger.warn(e.getMessage());
			}

			deplacerIA();
			eDepot.setTempsEcoule(eDepot.getTempsEcoule() + 1);
			if (ContraintesParametres.modeConsole.equals(true)) {
				Console.afficherGrille();
				Console.afficherMenuNature();
			}

			try {
				testFin();
			} catch (InterruptedException e) {
				logger.trace("thread arrêté");
			}
		}
	}

	public synchronized void testFin() throws InterruptedException {
		if (stop) {
			throw new InterruptedException();
		}
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	private void afficherBump() {
		logger.warn("~~~~~BUMP~~~~~~~~~BUMP~~~~~~~~~~BUMP~~~~~~");
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
		}
		if (ContraintesParametres.modeConsole.equals(true)) {
			Console.afficherGrille();
		}
	}

	@Override
	public void visite(Case laCase) {

	}

	@Override
	public void visite(Grille grille) {
	}

	@Override
	public void visite(Bete bete, Bete autreBete) {
	}

	@Override
	public void visite(GraphicUserInterface gui) {
	}


	@Override
	public JCanvas visite(InGame ig, JCanvas jc, int r, int g, int b) {
		return jc;
	}

}
