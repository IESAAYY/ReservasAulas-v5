package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.memoria;

import java.util.*;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;

public class Aulas implements IAulas {

	private List<Aula> arrayListAula;

	public Aulas() {
		arrayListAula = new ArrayList<>();
	}

	public Aulas(Aulas aulas) {
		setAulas(aulas);
	}

	private void setAulas(Aulas aulas) {
		if (aulas == null) {
			throw new NullPointerException("ERROR: No se pueden copiar aulas nulas.");
		}
		arrayListAula = copiaProfundaAulas(aulas.arrayListAula);
	}

	@Override
	public List<Aula> getAulas() {
		List<Aula> aLAulaOrdenadaPorNombre = copiaProfundaAulas(arrayListAula);
		
		 aLAulaOrdenadaPorNombre.sort(Comparator.comparing(Aula :: getNombre));
		 
		 return aLAulaOrdenadaPorNombre;
	}
	
	private List<Aula> copiaProfundaAulas(List<Aula> arraylistAula) {
		List<Aula> cArrayListAula = new ArrayList<Aula>();
		Iterator<Aula> it = arraylistAula.iterator();

		while (it.hasNext()) {
			Aula cAula = it.next();
			cArrayListAula.add(new Aula(cAula));
		}

		return cArrayListAula;
	}

	@Override
	public int getNumAulas() {
		return arrayListAula.size();
	}

	@Override
	public void insertar(Aula aula) throws OperationNotSupportedException {
		if (aula == null) {
			throw new NullPointerException("ERROR: No se puede insertar un aula nula.");
		}
		if (arrayListAula.contains(aula)) {
			throw new OperationNotSupportedException("ERROR: Ya existe un aula con ese nombre.");
		}

		arrayListAula.add(aula);
	}

	@Override
	
	public Aula buscar(Aula aula) {
		if (aula == null) {
			throw new NullPointerException("ERROR: No se puede buscar un aula nula.");
		}
		
		// Algoritmo: Permite devolver datos reales del aula introducido en método insertarAula de Vista
		Aula[] cAula = new Aula[arrayListAula.size()];
		arrayListAula.toArray(cAula);	
		
		for (int i = 0; i < arrayListAula.size(); i++) {
			if (cAula[i].equals(aula)) {
				aula = cAula[i];		
			} 
		}
		
		if (!arrayListAula.contains(aula)) {
			return null;
		} else {
			return new Aula(aula);
		}
	}

	@Override
	public void borrar(Aula aula) throws OperationNotSupportedException {
		if (aula == null) {
			throw new NullPointerException("ERROR: No se puede borrar un aula nula.");
		}
		if (!arrayListAula.contains(aula)) {
			throw new OperationNotSupportedException("ERROR: No existe ningún aula con ese nombre.");
		}

		arrayListAula.remove(aula);
	}

	@Override
	public List<String> representar() {
		List<String> representacion = new ArrayList<>();

		for (Aula a : arrayListAula) {
			representacion.add(a.toString());
		}

		if (representacion.isEmpty()) {
			throw new NullPointerException("ERROR: Inserte primero un aula.");
		}

		return representacion;
	}

}
