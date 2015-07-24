package betes.test;

import betes.console.Console;
import betes.donnees.ContraintesParametres;

public class TestConsole {

	public static void main(String[] args) {
		ContraintesParametres.modeConsole = true;
		Console console = Console.getInstance();
		console.bienvenueEtChoixDeMode();
	}

}
