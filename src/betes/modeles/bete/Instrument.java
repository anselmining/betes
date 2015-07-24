package betes.modeles.bete;

import java.io.Serializable;

public class Instrument implements Serializable {

	private static final long serialVersionUID = 6730259991082152448L;
	private String nomInstrument;

	public Instrument() {

	}

	public Instrument(String nomInstrument) {
		super();
		this.nomInstrument = nomInstrument;
	}

	public void setNomInstrument(String nomInstrument) {
		this.nomInstrument = nomInstrument;
	}

	public String getNomInstrument() {
		return nomInstrument;
	}

}
