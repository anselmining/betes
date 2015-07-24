package betes.visiteurs;

import betes.console.Console;
import betes.donnees.EnvironnementDepot;
import betes.donnees.EnvironnementFabrique;
import betes.donnees.exceptions.BeteSurCaseException;
import betes.donnees.exceptions.CaseInexistanteException;
import betes.donnees.exceptions.CaseNonTraversableException;
import betes.donnees.exceptions.NourritureSurCaseException;
import betes.gui.GraphicUserInterface;
import betes.gui.dessin.JCanvas;
import betes.gui.frames.InGame;
import betes.gui.moteur.EvenementsRegistre;
import betes.modeles.bete.Bete;
import betes.modeles.environnement.Case;
import betes.modeles.environnement.Grille;

public interface Visiteur {

	EnvironnementDepot eDepot = EnvironnementDepot.getInstance();

	EvenementsRegistre rEvent = EvenementsRegistre.getInstance();

	EnvironnementFabrique eFabrique = new EnvironnementFabrique();

	void visite(Bete bete);

	void visite(Bete bete, Bete autreBete);

	void visite(Case laCase) throws CaseInexistanteException, CaseNonTraversableException, BeteSurCaseException, NourritureSurCaseException;

	void visite(Grille grille);

	void visite(Console console);

	void visite(GraphicUserInterface gui);
	
	JCanvas visite(InGame ig, JCanvas jc, int r, int g, int b);

}
