package org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio;

import java.io.Serializable;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public abstract class Permanencia implements Serializable{

	private LocalDate dia;
	protected static final DateTimeFormatter FORMATO_DIA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public Permanencia(LocalDate dia) {
		setDia(dia);
	}

	public Permanencia(Permanencia p) {
		if(p == null) {
			throw new NullPointerException("ERROR: No se puede copiar una permanencia nula.");
		}
		setDia(p.getDia());
	}

	public LocalDate getDia() {
		return dia;
	}

	private void setDia(LocalDate dia) {
		if(dia == null) {
			throw new NullPointerException("ERROR: El día de una permanencia no puede ser nulo.");
		}
		this.dia = dia;
	}

	public abstract int getPuntos();
	
	@Override
	public abstract int hashCode();
	
	@Override
	public abstract boolean equals(Object obj);

	@Override
	public String toString() {
		return "día=" + dia.format(FORMATO_DIA);
	}

}
