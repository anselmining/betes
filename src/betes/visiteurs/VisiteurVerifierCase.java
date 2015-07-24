package betes.visiteurs;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.console.Console;
import betes.donnees.EnvironnementDepot;
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

public class VisiteurVerifierCase implements Visiteur {
	private static Logger logger = LoggerUtility.getLogger(VisiteurVerifierCase.class);
	private Bete beteALInitiative;
	Boolean decrireDecor, rencontrerBete, manger, detecterCollisions;
	private EnvironnementDepot eDepot = EnvironnementDepot.getInstance();

	public VisiteurVerifierCase(Bete beteALInitiative, Boolean decrireDecor, Boolean rencontrerBete, Boolean manger, Boolean detecterCollisions) {
		this.beteALInitiative = beteALInitiative;
		this.decrireDecor = decrireDecor;
		this.rencontrerBete = rencontrerBete;
		this.manger = manger;
		this.detecterCollisions = detecterCollisions;
	}

	@Override
	public void visite(Case laCase) throws CaseInexistanteException, CaseNonTraversableException, BeteSurCaseException, NourritureSurCaseException {
		try {
			if (decrireDecor) {
				logger.warn(decrireDecor(laCase));
			}
			if (rencontrerBete) {
				if (laCase != null && eDepot.rechercherBete(laCase) != null) {
					rencontreBete(laCase);
					throw new BeteSurCaseException();
				}
			}
			if (manger) {
				if (laCase instanceof Nourriture) {
					manger(getBeteALInitiative(), ((Nourriture) laCase));
					getBeteALInitiative().setPosition(laCase);
					VisiteurPlacerDecor decorateur = new VisiteurPlacerDecor();
					decorateur.creerDecor(laCase, "Vide", " . ", true);
					throw new NourritureSurCaseException();
				}
			}
			if (detecterCollisions) {
				if (eDepot.rechercher(laCase) != null && eDepot.rechercher(laCase).isTraversable().equals(false)) {
					throw new CaseNonTraversableException();
				}
			}
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			aioobe.printStackTrace();
			throw new CaseInexistanteException();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			throw new CaseInexistanteException();
		}
	}

	private void manger(Bete bete, Nourriture nourriture) {
		VisiteurManger nourrisseur = new VisiteurManger(nourriture);
		bete.accepteVisiteur(nourrisseur);
	}

	private void rencontreBete(Case laCase) {
		VisiteurRencontreBete entomologiste = new VisiteurRencontreBete();
		entomologiste.visite(eDepot.rechercherBete(laCase), getBeteALInitiative());
	}

	/**Décrit le décor d'une case.
	 * @param laCase :la case vérifier.
	 * @return (String) :la description du décor.
	 */
	public String decrireDecor(Case laCase) {
		String description, nomCase;

		if (laCase == null) {
			return null;
		}

		if (laCase.getNomDecor() != null) {
			nomCase = laCase.getNomDecor();
		} else {
			if (laCase instanceof Nourriture) {
				nomCase = ((Nourriture) laCase).getNomNourriture();
			} else {
				nomCase = "Vide";
			}
		}

		description = "[         [ " + nomCase + " [" + laCase.getX() + "][" + laCase.getY() + "] [Probabilité de mutation: " + laCase.getPourcentageMutation()
				+ "%]\n";

		Bete bete = eDepot.rechercherBete(laCase);

		if (!(laCase instanceof Nourriture) && bete != null) {
			description += bete.toString();
			description += bete.getVision();
		}

		return description;

	}

	public Bete getBeteALInitiative() {
		return beteALInitiative;
	}

	@Override
	public void visite(Bete bete) {
	}

	@Override
	public void visite(Grille grille) {
	}

	@Override
	public void visite(Console console) {
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
