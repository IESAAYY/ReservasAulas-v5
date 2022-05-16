package org.iesalandalus.programacion.reservasaulas.mvc.controlador;

import java.util.*;
import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.IModelo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.Modelo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.IVista;

public class Controlador implements IControlador {
	
	private IModelo modelo;
	private IVista vista;
	
	public Controlador(IModelo modelo, IVista vista) {
		if (modelo == null) {
			throw new NullPointerException("ERROR: El modelo no puede ser nulo.");
		}
		if (vista == null) {
			throw new NullPointerException("ERROR: La vista no puede ser nula.");
		}
		this.modelo = modelo;
		this.vista = vista;
		this.vista.setControlador(this);
	}
	
	@Override
	public void comenzar() {
		modelo.comenzar();
		vista.comenzar();
	}
	
	@Override
	public void terminar() {
		modelo.terminar();
		System.out.println("Programa finalizado");
	}
	
	// Método no indicado en diagrama
	@Override
	public List<Reserva> getReservas() {
		return modelo.getReservas();
	}
	
	@Override
	public void insertarAula(Aula aula) throws OperationNotSupportedException {
		modelo.insertarAula(aula);
	}
	
	@Override
	public void borrarAula(Aula aula) throws OperationNotSupportedException {
		modelo.borrarAula(aula);
	}
	
	@Override
	public Aula buscarAula(Aula aula) {
		return modelo.buscarAula(aula);
	}
	
	@Override
	public List<String> representarAulas() {
		return modelo.representarAulas();
	}
	
	@Override
	public void insertarProfesor(Profesor profesor) throws OperationNotSupportedException {
		modelo.insertarProfesor(profesor);
	}
	
	@Override
	public void borrarProfesor(Profesor profesor) throws OperationNotSupportedException {
		modelo.borrarProfesor(profesor);
	}
	
	@Override
	public Profesor buscarProfesor(Profesor profesor) {
		return modelo.buscarProfesor(profesor);
	}
	
	@Override
	public List<String> representarProfesores() {
		return modelo.representarProfesores();
	}
	
	@Override
	public void realizarReserva(Reserva reserva) throws OperationNotSupportedException {
		modelo.realizarReserva(reserva);
	}
	
	@Override
	public List<String> representarReservas() {
		return modelo.representarReservas();
	}
	
	@Override
	public void anularReserva(Reserva reserva) throws OperationNotSupportedException {
		 modelo.anularReserva(reserva);
	}
	
	@Override
	public List<Reserva> getReservasAula(Aula aula)  {
		return modelo.getReservasAula(aula);
	}
	
	@Override
	public List<Reserva> getReservasProfesor(Profesor profesor) {
		return modelo.getReservasProfesor(profesor);
	}
	
	@Override
	public List<Reserva> getReservasPermanencia(Permanencia permanencia) {
		return modelo.getReservasPermanencia(permanencia);
	}
	
	@Override
	public boolean consultarDisponibilidad(Aula aula, Permanencia permanencia) {
		return modelo.consultarDisponibilidad(aula, permanencia);
	}
	
	public List<Aula> getAulas() {
		return modelo.getAulas();
	}

	@Override
	public List<Profesor> getProfesores() {
		return modelo.getProfesores();
	}
	
}
