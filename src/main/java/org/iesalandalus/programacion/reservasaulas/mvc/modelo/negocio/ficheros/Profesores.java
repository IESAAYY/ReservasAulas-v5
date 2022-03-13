// Esta clase es prÃ¡cticamente idÃ©ntica a la clase aulas

package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.ficheros;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IProfesores;


public class Profesores implements IProfesores{
	
	public static final String NOMBRE_FICHERO_PROFESORES = "datos/profesores.txt";

	private List<Profesor> arrayListProfesor;

	public Profesores() {
		arrayListProfesor = new ArrayList<>();
	}
	
	public Profesores(Profesores profesores) {
		setProfesores(profesores);
	}
	
	public void comenzar() {
		leer();
	}
	
	private void leer() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOMBRE_FICHERO_PROFESORES));
			Profesor profesor = null;
			
			do {
				profesor = (Profesor) ois.readObject();
				insertar(profesor);
			} while (profesor != null);
			
			ois.close();
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: No puedo encontrar la clase que tengo que leer.");
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No puedo abrir el fichero profesores.");
		} catch (EOFException e) {
			System.out.println("Fichero profesores leído satisfactoriamente.");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida(Profesores).");
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		}
			
		
	}
	
	public void terminar() {
		escribir();
	}
	
	private void escribir() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOMBRE_FICHERO_PROFESORES));
			
			for (Profesor p : arrayListProfesor) {
				oos.writeObject(p);
			}
			
		} catch (FileNotFoundException e) {

			System.out.println("ERROR: No puedo crear el fichero alumnos.");
		} catch (IOException e) {

			System.out.println("Error inesperado de Entrada/Salida.");
		}
	}
	
	private void setProfesores(Profesores profesores) {
		if (profesores == null) {
			throw new NullPointerException("ERROR: No se pueden copiar profesores nulos.");
		}
		
		arrayListProfesor = copiaProfundaProfesores(profesores.arrayListProfesor);
	}
	

	public List<Profesor> getProfesores() {
		List<Profesor> aLProfesorOrdenadaPorCorreo =  copiaProfundaProfesores(arrayListProfesor);
		
		aLProfesorOrdenadaPorCorreo.sort(Comparator.comparing(Profesor :: getCorreo));
		
		return aLProfesorOrdenadaPorCorreo;
	}
	
	private List<Profesor> copiaProfundaProfesores(List<Profesor> arraylistProfesor) {
		List<Profesor> cArrayListProfesor = new ArrayList<Profesor>();
		Iterator<Profesor> it = arraylistProfesor.iterator();

		while (it.hasNext()) {
			Profesor cProfesor = it.next();
			cArrayListProfesor.add(new Profesor(cProfesor));
		}

		return cArrayListProfesor;
	}
	
	@Override
	public int getNumProfesores() {
		return arrayListProfesor.size();
	}

	@Override
	public void insertar(Profesor profesor) throws OperationNotSupportedException {
		if(profesor == null) {
			throw new NullPointerException("ERROR: No se puede insertar un profesor nulo.");
		}
		if (arrayListProfesor.contains(profesor)) {
			throw new OperationNotSupportedException("ERROR: Ya existe un profesor con ese correo.");
		}
		
		arrayListProfesor.add(new Profesor(profesor));
	}
	
	@Override
	
	public Profesor buscar(Profesor profesor)  {
		if (profesor == null) {
			throw new NullPointerException("ERROR: No se puede buscar un profesor nulo.");
		}
		// Algoritmo: Permite devolver datos del profesor introducido en mÃ©todo insertarProfesor de Vista
		Profesor[] cProfesor = new Profesor[arrayListProfesor.size()];
		arrayListProfesor.toArray(cProfesor);	
		
		for (int i = 0; i < arrayListProfesor.size(); i++) {
			if (cProfesor[i].equals(profesor)) {
				profesor = cProfesor[i];		
			} 
		}
		
		if (!arrayListProfesor.contains(profesor)) {
			return null;
		} else {
			return profesor;
		}
	}
	
	
	@Override
	public void borrar(Profesor profesor) throws OperationNotSupportedException {
		if (profesor == null) {
			throw new NullPointerException("ERROR: No se puede borrar un profesor nulo.");
		}
		if (!arrayListProfesor.contains(profesor)) {
			throw new OperationNotSupportedException("ERROR: No existe ningÃºn profesor con ese correo.");
		}
		
		arrayListProfesor.remove(profesor);
	}
	
	@Override
	public List<String> representar()  {
		List <String> representacion = new ArrayList<>();
		
		for (Iterator <Profesor>it = getProfesores().iterator(); it.hasNext();) {
			representacion.add(it.next().toString());
		}
		
		if (representacion.isEmpty()) {
			throw new NullPointerException("ERROR: Inserte primero un profesor.");
		}
		
		return representacion;
	}


}
 