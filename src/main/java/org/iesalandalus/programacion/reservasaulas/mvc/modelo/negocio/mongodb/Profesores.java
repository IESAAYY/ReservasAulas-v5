package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IProfesores;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb.utilidades.MongoDB;
import org.bson.Document;
import com.mongodb.client.MongoCollection;

public class Profesores implements IProfesores {
	
	private static final String COLECCION = "profesores";
	private MongoCollection<Document> coleccionProfesores;
	
	public Profesores () {
		
	}

	@Override
	public void comenzar() {
		coleccionProfesores = MongoDB.getBD().getCollection(COLECCION);
		
	}

	@Override
	public void terminar() {
		MongoDB.cerrarConexion();
		
	}

	@Override
	public List<Profesor> getProfesores() {
		List<Profesor> profesores = new ArrayList<>();
		for (Document documentoProfesor : coleccionProfesores.find().sort(MongoDB.getCriterioOrdenacionProfesor())) {
			Profesor profesor = MongoDB.getProfesor(documentoProfesor);
			profesores.add(profesor);
		}
		return profesores;
	}


	@Override
	public int getNumProfesores() {
		return (int)coleccionProfesores.countDocuments();
	}

	@Override
	public void insertar(Profesor profesor) throws OperationNotSupportedException {
		if (profesor == null) {
			throw new IllegalArgumentException("No se puede insertar un profesor nulo.");
		}
		if (buscar(profesor) != null) {
			throw new OperationNotSupportedException("El profesor ya existe.");
		} else {
			coleccionProfesores.insertOne(MongoDB.getDocumento(profesor));
		}
	}

	/*
	 *  Bson com.mongodb.client.model.Filters.eq: Un filtro donde el primer parámetro es el nombre del atributo 
	 *  y el segundo es el valor a buscar
	 */
	@Override
	public Profesor buscar(Profesor profesor) {
		Document documentoProfesor = coleccionProfesores.find().filter(eq(MongoDB.CORREO, profesor.getCorreo())).first();
		return MongoDB.getProfesor(documentoProfesor);
	}
	
	@Override
	public void borrar(Profesor profesor) throws OperationNotSupportedException {
		if (profesor == null) {
			throw new IllegalArgumentException("No se puede borrar un profesor nulo.");
		}
		if (buscar(profesor) != null) {
			coleccionProfesores.deleteOne(eq(MongoDB.CORREO, profesor.getCorreo()));
		} else {
			throw new OperationNotSupportedException("El profesor a borrar no existe.");
		} 
		
	}

	@Override
	public List<String> representar() {
		// TODO Auto-generated method stub
		return null;
	}

}
