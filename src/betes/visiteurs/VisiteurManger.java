package betes.visiteurs;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.console.Console;
import betes.donnees.ContraintesParametres;
import betes.donnees.exceptions.BeteSurCaseException;
import betes.donnees.exceptions.CaseInexistanteException;
import betes.donnees.exceptions.CaseNonTraversableException;
import betes.gui.GraphicUserInterface;
import betes.gui.dessin.JCanvas;
import betes.gui.frames.InGame;
import betes.modeles.bete.Bete;
import betes.modeles.environnement.Case;
import betes.modeles.environnement.Grille;
import betes.modeles.environnement.Nourriture;

public class VisiteurManger implements Visiteur {
	private static Logger logger = LoggerUtility.getLogger(VisiteurManger.class);
	private Nourriture nourriture;

	public VisiteurManger(Nourriture nourriture) {
		this.nourriture = nourriture;
	}

	@Override
	public void visite(Bete bete) {
		int ancienneEnergie = bete.getEnergie();
		bete.setEnergie(ancienneEnergie + nourriture.getSatieteNourriture());
		bete.setSatieteBete(bete.getSatieteBete() + nourriture.getSatieteNourriture());
		if (bete.getEnergie() > bete.getEnergieMax()) {
			bete.setEnergie(bete.getEnergieMax());
		}
		if (bete.getSatieteBete() > ContraintesParametres.SATIETE_MAX) {
			bete.setSatieteBete(ContraintesParametres.SATIETE_MAX);
		}

		bete.setSatieteBete(bete.getSatieteBete() + nourriture.getSatieteNourriture());
		logger.info("[---------[ " + bete.getNomBete() + " [" + ancienneEnergie + "♥ → " + bete.getEnergie() + "♥] a mangé " + nourriture.getNomNourriture()
				+ " !");
	}

	@Override
	public void visite(Case laCase) throws CaseInexistanteException, CaseNonTraversableException, BeteSurCaseException {

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
