package betes.visiteurs;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.console.Console;
import betes.donnees.ContraintesParametres;
import betes.donnees.GenerationAleatoire;
import betes.donnees.exceptions.BeteSurCaseException;
import betes.donnees.exceptions.CaseInexistanteException;
import betes.donnees.exceptions.CaseNonTraversableException;
import betes.donnees.exceptions.PasDeCaseDisponibleException;
import betes.gui.GraphicUserInterface;
import betes.gui.dessin.JCanvas;
import betes.gui.frames.InGame;
import betes.modeles.bete.Bete;
import betes.modeles.environnement.Case;
import betes.modeles.environnement.Grille;

public class VisiteurRencontreBete implements Visiteur {
	private static Logger logger = LoggerUtility.getLogger(GenerationAleatoire.class);

	@Override
	public void visite(Bete beteRencontree, Bete beteRencontrante) {
		logger.info(beteRencontrante.getNomBete() + " rencontre " + beteRencontree.getNomBete());

		if (beteRencontrante.getSexe() == beteRencontree.getSexe()) {
			VisiteurCombat combattant = new VisiteurCombat();
			combattant.visite(beteRencontrante, beteRencontree);
		} else {
			VisiteurAccouplement accoupleur = new VisiteurAccouplement(beteRencontrante, beteRencontree);
			try {
				Case caseDeReproduction = eDepot.rechercher(beteRencontree.getPosition());

				if (beteRencontree.getEnergie() < beteRencontree.getEnergieMax() / ContraintesParametres.FACTEUR_ENERGETIQUE_PROCREATION
						|| beteRencontrante.getEnergie() < beteRencontrante.getEnergieMax() / ContraintesParametres.FACTEUR_ENERGETIQUE_PROCREATION) {
					logger.info("[---------[ Les parents n'ont pas assez d'énergie pour procréer.");
				} else {
					if (beteRencontrante.getAge() < ContraintesParametres.AGE_MIN_PROCREATION
							|| beteRencontree.getAge() < ContraintesParametres.AGE_MIN_PROCREATION) {
						logger.info("[---------[ Les parents ne sont pas assez matures pour procréer.");
					} else {
						beteRencontrante.setNbAccouplements(beteRencontrante.getNbAccoulpements() + 1);
						beteRencontree.setNbAccouplements(beteRencontree.getNbAccoulpements() + 1);

						if (GenerationAleatoire.nombreAleatoire(0, 100) <= caseDeReproduction.getPourcentageMutation()) {
							logger.info("Accouplement par transfert en cours entre " + beteRencontree.getNomBete() + " et " + beteRencontrante.getNomBete());
							accoupleur.procreerParTransfert(beteRencontrante, beteRencontree);
						} else {
							logger.info("Accouplement par mutation en cours entre " + beteRencontree.getNomBete() + " et " + beteRencontrante.getNomBete());
							accoupleur.procreerAleatoirement(beteRencontrante, beteRencontree);
						}
					}
				}
			} catch (PasDeCaseDisponibleException e) {
				logger.info("[         [ Le bébé est un mort né !");
			}
		}
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
	public void visite(Bete bete) {
	}

	@Override
	public void visite(GraphicUserInterface gui) {
	}


	@Override
	public JCanvas visite(InGame ig, JCanvas jc, int r, int g, int b) {
		return jc;
	}

}
