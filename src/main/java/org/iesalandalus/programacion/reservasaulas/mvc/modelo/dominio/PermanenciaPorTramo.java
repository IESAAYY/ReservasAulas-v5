package org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio;

import java.time.LocalDate;
import java.util.Objects;

public class PermanenciaPorTramo extends Permanencia{

	public static final int PUNTOS = 10;
	private Tramo tramo;
	
	public PermanenciaPorTramo(LocalDate dia, Tramo tramo) {
		super(dia);
		setTramo(tramo);
	}
	
	public PermanenciaPorTramo(PermanenciaPorTramo p) {
		super(p);	
		setTramo(p.tramo);
	}

	public Tramo getTramo() {
		return tramo;
	}

	private void setTramo(Tramo tramo) {
		if(tramo == null) {
			throw new NullPointerException("ERROR: El tramo de una permanencia no puede ser nulo.");
		}
		this.tramo = tramo;
	}
	
	@Override
	public int getPuntos() {
		return PUNTOS;
	}

	@Override
	public int hashCode() {
		return Objects.hash(tramo, getDia());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PermanenciaPorTramo other = (PermanenciaPorTramo) obj;
		return Objects.equals(super.getDia(), other.getDia()) && tramo == other.tramo;
	}

	@Override
	public String toString() {
		return super.toString() + ", tramo=" + tramo;
	}
	

	
	
}
