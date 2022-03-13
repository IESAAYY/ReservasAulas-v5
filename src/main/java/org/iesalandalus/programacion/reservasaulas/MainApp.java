package org.iesalandalus.programacion.reservasaulas;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.Controlador;
import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.FactoriaFuenteDatos;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.IModelo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.Modelo;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.IVista;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.Vista;
/**
 * 
 * @author Yulei Yan
 * ReservasAulaV3
 * Intento 17:10
 */
public class MainApp {

	public static final void main(String[] args) {
		final IModelo modelo = new Modelo(FactoriaFuenteDatos.FICHEROS.crear());
		final IVista vista = new Vista();
		final IControlador controlador = new Controlador(modelo, vista);
		controlador.comenzar();
	}

}
