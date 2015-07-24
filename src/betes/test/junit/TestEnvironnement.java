package betes.test.junit;

import static org.junit.Assert.*;

import org.junit.Test;

import betes.donnees.EnvironnementDepot;
import betes.donnees.EnvironnementFabrique;
import betes.modeles.environnement.Case;

public class TestEnvironnement {

	@Test
	public void testFabriqueGrille() {
		EnvironnementDepot eDepot = EnvironnementDepot.getInstance();
		EnvironnementFabrique eFabrique = new EnvironnementFabrique();
		eFabrique.creerGrille(30, 30);
		Case laCase = eDepot.rechercher(25, 22);
		assertEquals(25, laCase.getX());
	}
	
	

}
