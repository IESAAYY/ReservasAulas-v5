package org.iesalandalus.programacion.reservasaulas.mvc.controlador;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;

public interface IControlador {

	void comenzar();

	void terminar();
	
	// Método no indicado en diagrama
	List<Reserva> getReservas();

	void insertarAula(Aula aula) throws OperationNotSupportedException;

	void borrarAula(Aula aula) throws OperationNotSupportedException;

	Aula buscarAula(Aula aula);

	List<String> representarAulas();

	void insertarProfesor(Profesor profesor) throws OperationNotSupportedException;

	void borrarProfesor(Profesor profesor) throws OperationNotSupportedException;

	Profesor buscarProfesor(Profesor profesor);

	List<String> representarProfesores();

	void realizarReserva(Reserva reserva) throws OperationNotSupportedException;

	List<String> representarReservas();

	void anularReserva(Reserva reserva) throws OperationNotSupportedException;

	List<Reserva> getReservasAula(Aula aula);

	List<Reserva> getReservasProfesor(Profesor profesor);

	List<Reserva> getReservasPermanencia(Permanencia permanencia);

	boolean consultarDisponibilidad(Aula aula, Permanencia permanencia);
	
	List<Aula> getAulas();
	
	List<Profesor> getProfesores();

}