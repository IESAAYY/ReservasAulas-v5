package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.ficheros;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;

public class Aulas implements IAulas {

	public static final String NOMBRE_FICHERO_AULAS = "datos/aulas.txt";
	private List<Aula> arrayListAula;

	public Aulas() {
		arrayListAula = new ArrayList<>();
	}

	public Aulas(Aulas aulas) {
		setAulas(aulas);
	}

	public void comenzar() {
		leer();

	}
	
	private void leer() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOMBRE_FICHERO_AULAS));
			
			Aula aula = null;
			
			do {
				aula = (Aula) ois.readObject();
				insertar(aula);
			} while (aula != null);
			
			ois.close();
			
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: No puedo encontrar la clase que tengo que leer.");
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No puedo abrir el fichero aulas.");
		} catch (EOFException e) {
			System.out.println("Fichero aulas leído satisfactoriamente.");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida(Aulas).");
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void terminar() {
		escribir();
	}

	private void escribir() {
		try {
			File file = new File(NOMBRE_FICHERO_AULAS);
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));

			for (Aula a : arrayListAula) {
				oos.writeObject(a);
			}
			oos.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No puedo crear el fichero alumnos.");
		} catch (IOException e) {

			System.out.println("Error inesperado de Entrada/Salida.");
		}
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
		
		// Algoritmo: Permite devolver datos reales del aula introducido en mÃ©todo insertarAula de Vista
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
			throw new OperationNotSupportedException("ERROR: No existe ningÃºn aula con ese nombre.");
		}

		arrayListAula.remove(aula);
	}

	@Override
	public List<String> representar() {
		List<String> representacion = new ArrayList<>();

		// Llamamos a getAulas y no arrayListAula para devolver las aulas ordenadas
		for (Aula a : getAulas()) {
			representacion.add(a.toString());
		}

		if (representacion.isEmpty()) {
			throw new NullPointerException("ERROR: Inserte primero un aula.");
		}

		return representacion;
	}

}
