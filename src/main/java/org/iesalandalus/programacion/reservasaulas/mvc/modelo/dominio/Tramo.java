package org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio;

public enum Tramo {
	MANANA("Mañana"), TARDE("Tarde");

	private String tramo;

	private Tramo(String tramo) {
		this.tramo = tramo;
	}

	@Override
	public String toString() {
		return tramo;
	}

}
