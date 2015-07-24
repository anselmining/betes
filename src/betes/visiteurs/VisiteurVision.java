package betes.visiteurs;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.console.Console;
import betes.donnees.ContraintesParametres;
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

public class VisiteurVision implements Visiteur {
	private int distanceVisible;
	private Nourriture nourritureProche;
	private int distanceBeteNourriture = ContraintesParametres.TAILLE_GRILLE_MAX_X;
	private int distanceBeteBete = ContraintesParametres.TAILLE_GRILLE_MAX_X;
	private Bete beteProche;
	private VisiteurVerifierCase descripteur = new VisiteurVerifierCase(null, true, false, false, false);
	private VisiteurCombat combattant = new VisiteurCombat();
	private String description = "";
	@SuppressWarnings("unused")
	private static Logger logger = LoggerUtility.getLogger(VisiteurVision.class);
	private Boolean probableVictoire = false;
	private Boolean probableReproduction = false;
	private Boolean memeSexe;

	public VisiteurVision(int distanceVisible) {
		this.distanceVisible = distanceVisible;
	}

	@Override
	public void visite(Bete bete) {
		Case caseBete = bete.getPosition();
		int x = bete.getPositionX();
		int y = bete.getPositionY();
		Grille grille = eDepot.getGrille();
		int grilleX = grille.getTailleX();
		int grilleY = grille.getTailleY();

		for (int i = x - distanceVisible; i <= x + distanceVisible; i++) {
			for (int j = y - distanceVisible; j <= y + distanceVisible; j++) {
				if (i == x && j == y) { // ignore la position de la Bête
					continue;
				}
				if (i < 1 || j < 1 || i > grilleX || j > grilleY) {
					continue;
				}
				Case caseVue = eDepot.rechercher(i, j);
				// logger.info(caseVue.getNomDecor() + " vue à (" + i + ";" + j
				// + ")");
				int distanceBeteCaseVue = calculDistance(caseVue, caseBete);

				if (caseVue instanceof Nourriture) {
					if (distanceBeteCaseVue < distanceBeteNourriture) {
						setNourritureProche((Nourriture) caseVue);
						setDistanceBeteNourriture(distanceBeteCaseVue);
					}
				}

				Bete beteVue = eDepot.rechercherBete(caseVue);

				if (beteVue != null) {
					// logger.info(beteVue.getNomBete() + " vue à (" + i + ";" +
					// j + ")");
					if (distanceBeteCaseVue < distanceBeteBete) {
						setBeteProche(beteVue);
						setDistanceBeteBete(distanceBeteCaseVue);
					}
				}
			}
		}

		String descriptionProche = "";

		if (beteProche != null) {

			if (beteProche.getSexe() == bete.getSexe()) {
				memeSexe = true;
				probableVictoire = combattant.simulationCombat(bete, beteProche);
			} else {
				memeSexe = false;
				if (bete.getSatieteBete() < ContraintesParametres.SATIETE_MAX / 3
						|| bete.getEnergie() < beteProche.getEnergieMax() / ContraintesParametres.FACTEUR_ENERGETIQUE_PROCREATION
						|| bete.getEnergie() < beteProche.getEnergieMax() / ContraintesParametres.FACTEUR_ENERGETIQUE_PROCREATION
						|| bete.getAge() < ContraintesParametres.AGE_MIN_PROCREATION || beteProche.getAge() < ContraintesParametres.AGE_MIN_PROCREATION) {
					probableReproduction = false;
				} else {
					probableReproduction = bete.getFacteurReproduction() <= beteProche.getFacteurReproduction() / 2;
				}
			}

			descriptionProche += beteProche.toString();
			if (memeSexe.equals(true)) {// même sexe
				if (probableVictoire.equals(true)) {
					descriptionProche += "[---------[ Probable VICTOIRE de " + bete.getNomBete() + " contre " + beteProche.getNomBete();
				} else {
					descriptionProche += "[---------[ Probable DÉFAITE de " + bete.getNomBete() + " contre " + beteProche.getNomBete();
				}
			} else {// sexe different
				if (probableReproduction.equals(true)) {
					descriptionProche += "[---------[ PROBABLE REPRODUCTION entre " + bete.getNomBete() + " et " + beteProche.getNomBete();
				} else {
					descriptionProche += "[---------[ REPRODUCTION IMPROBABLE entre " + bete.getNomBete() + " et " + beteProche.getNomBete();
				}
			}
		}

		if (nourritureProche != null) {
			descriptionProche += descripteur.decrireDecor(nourritureProche.getPosition());
		}

		if (!descriptionProche.equals("")) {
			descriptionProche = "\nÉlements proches:\n" + descriptionProche;
			setDescription(descriptionProche);
		} else {
			descriptionProche = "\nPas de bête ni nourriture proche.\n";
			setDescription(descriptionProche);
		}

		setDescription("\n");

	}

	@Override
	public void visite(Bete bete, Bete autreBete) {
	}

	@Override
	public void visite(Case laCase) throws CaseInexistanteException, CaseNonTraversableException, BeteSurCaseException, NourritureSurCaseException {
	}

	private void setDescription(String description) {
		this.description += description;
	}

	public String getDescription() {
		return description;
	}

	public Nourriture getNourritureProche() {
		return nourritureProche;
	}

	public void setNourritureProche(Nourriture nourritureProche) {
		this.nourritureProche = nourritureProche;
	}

	public Bete getBeteProche() {
		return beteProche;
	}

	public void setBeteProche(Bete beteProche) {
		this.beteProche = beteProche;
	}

	public int getDistanceBeteNourriture() {
		return distanceBeteNourriture;
	}

	public void setDistanceBeteNourriture(int distanceBeteNourriture) {
		this.distanceBeteNourriture = distanceBeteNourriture;
	}

	public int getDistanceBeteBete() {
		return distanceBeteBete;
	}

	public void setDistanceBeteBete(int distanceBeteBete) {
		this.distanceBeteBete = distanceBeteBete;
	}

	private int calculDistance(Case caseVue, Case caseBete) {
		double distance = 0;

		double dX2 = Math.pow((caseVue.getX() - caseBete.getX()), 2);
		double dY2 = Math.pow((caseVue.getY() - caseBete.getY()), 2);
		distance = Math.sqrt(dX2 + dY2);
		return (int) distance;
	}

	public Boolean isProbableVictoire() {
		return probableVictoire;
	}

	public Boolean isProbableReproduction() {
		return probableReproduction;
	}

	public Boolean isMemeSexe() {
		return memeSexe;
	}

	@Override
	public void visite(Grille grille) {
	}

	@Override
	public void visite(Console console) {
	}

	@Override
	public void visite(GraphicUserInterface gui) {
	}



	@Override
	public JCanvas visite(InGame ig, JCanvas jc, int r, int g, int b) {
		return jc;
	}

}
