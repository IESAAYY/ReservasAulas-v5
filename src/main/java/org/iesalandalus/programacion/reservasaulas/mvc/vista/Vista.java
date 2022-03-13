package org.iesalandalus.programacion.reservasaulas.mvc.vista;

import java.util.*;
import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;


/*
 * En esta clase se atrapa a todas las excepciones lanzadas anteriormente.
 * También se encarga de interactuar con el usuario sustituyendo a la 
 * función de la clase main en tareas anteriores.
 */
public class Vista implements IVista {
	
	private static final String ERROR = "ERROR: ";
	private static final String NOMBRE_VALIDO = "Nombre válido";
	private static final String CORREO_VALIDO = "Correo válido";
	
	// Constantes no indicados en diagrama
	private static final String OPERACION_CORRECTA = "La operación ha sido realizada correctamente.";
	private static final String LINEA_SEPARACION = "------------------------------------------------------------------------"
			+ "-------------------------------------------------------------------------------------------------------------";

	private IControlador controlador;

	public Vista() {
		Opcion.setVista(this);
	}

	@Override
	public void setControlador(IControlador controlador) {
		this.controlador = controlador;
	}

	@Override
	public void comenzar() {
		int ordinalOpcion;

		System.out.println("    =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		System.out.println("    | Gestor de reserva de aulas |");
		System.out.println("    =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		System.out.println("");

		do {
			System.out.println(LINEA_SEPARACION);
			System.out.println("    =-=-=-=-=-=-=-=-=-");
			System.out.println("    |Menú de opciones|");
			System.out.println("    =-=-=-=-=-=-=-=-=-");
			Consola.mostrarMenu();
			ordinalOpcion = Consola.elegirOpcion();
			Opcion opcion = Opcion.getOpcionSegunOrdinal(ordinalOpcion);
			System.out.println(LINEA_SEPARACION);
			opcion.ejecutar();
		} while (ordinalOpcion != Opcion.SALIR.ordinal());
	}

	@Override
	public void salir() {
		Consola.mostrarCabecera(Opcion.SALIR.getMensaje());
		controlador.terminar();
		System.out.println(LINEA_SEPARACION);
	}

	public void insertarAula() {
		Consola.mostrarCabecera(Opcion.INSERTAR_AULA.getMensaje());
		
		try {
			controlador.insertarAula(Consola.leerAula());
			System.out.println("");
			System.out.println(OPERACION_CORRECTA);
		} catch (NullPointerException | IllegalArgumentException | OperationNotSupportedException e) {
			System.out.println("");
			System.out.println(e.getMessage());
			System.out.println(e.getClass());
		}
	}
	
	// if: Simula DELETE RESTRICT impidiendo que se borre un aula con reservas
	public void borrarAula() {
		Consola.mostrarCabecera(Opcion.BORRAR_AULA.getMensaje());

		try {
			Aula aulaFicticia = Consola.leerAulaFicticia();
			List<Reserva> reservasAula = controlador.getReservasAula(aulaFicticia);
			System.out.println("");
			if (reservasAula.size() > 0) {
				System.out.println("No se puede borrar un aula con reserva");
			} else {
				controlador.borrarAula(aulaFicticia);
				System.out.println(OPERACION_CORRECTA + NOMBRE_VALIDO + ".");
			}
		} catch (NullPointerException | OperationNotSupportedException | IllegalArgumentException e) {
			System.out.println("");
			System.out.println(e.getMessage());
			System.out.println(e.getClass());
		}
	}
	
	/*
	// if: Simula DELETE CASCADE borrando también las reservas del aula introducida
	public void borrarAula() {
		Consola.mostrarCabecera(Opcion.BORRAR_AULA.getMensaje());

		try {
			Aula aulaFicticia = Consola.leerAulaFicticia();
			List<Reserva> reservasAula = controlador.getReservasAula(aulaFicticia);

			if (reservasAula.size() > 0) {
				for (Reserva r : reservasAula) {
						controlador.anularReserva(r);
				}
			}

			controlador.borrarAula(aulaFicticia);
			System.out.println("");
			System.out.println(OPERACION_CORRECTA + NOMBRE_VALIDO + ".");

		} catch (NullPointerException | OperationNotSupportedException | IllegalArgumentException e) {
			System.out.println("");
			System.out.println(e.getMessage());
			System.out.println(e.getClass());
		}
	}
	*/
	
	public void buscarAula() {
		Consola.mostrarCabecera(Opcion.BUSCAR_AULA.getMensaje());
		try {
			 Aula aulaFicticia = Consola.leerAulaFicticia();
			 Aula Aula = controlador.buscarAula(aulaFicticia);
			System.out.println("");
			
			if (Aula == null) {
				System.out.println(ERROR + "No está registrado el aula " + aulaFicticia.getNombre() + " en el sistema.");
			} else {
				System.out.println("Aula encontrado: ");
				System.out.println(Aula);
			}
		} catch (NullPointerException | IllegalArgumentException e) {
			System.out.println("");
			System.out.println(e.getMessage());
			System.out.println(e.getClass());
		}	
	}

	public void listarAulas() {
		Consola.mostrarCabecera(Opcion.LISTAR_AULAS.getMensaje());
		try {	
			List<String> aula = controlador.representarAulas();
			
			for (String s : aula) {
				System.out.println(s);
			}
		} catch (NullPointerException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getClass());
		}
	}
	
	public void insertarProfesor() {
		Consola.mostrarCabecera(Opcion.INSERTAR_PROFESOR.getMensaje());

		try {
			controlador.insertarProfesor(Consola.leerProfesor());
			System.out.println("");
			System.out.println(OPERACION_CORRECTA  + " " + NOMBRE_VALIDO + "/" + CORREO_VALIDO + ".");
		} catch (NullPointerException | OperationNotSupportedException | IllegalArgumentException e) {
			System.out.println("");
			System.out.println(e.getMessage());
			System.out.println(e.getClass());
		}	
	}
	
	// if: Simula DELETE RESTRICT impidiendo que se borre un profesor con reservas
	public void borrarProfesor() {
		Consola.mostrarCabecera(Opcion.BORRAR_PROFESOR.getMensaje());
		
		try {
			Profesor profesorFicticio = Consola.leerProfesorFicticio();
			List<Reserva> reservasProfesor = controlador.getReservasProfesor(profesorFicticio);
			
			System.out.println("");
			if (reservasProfesor.size() > 0) {
				System.out.println("No se puede borrar un profesor con reserva");
			} else {
				controlador.borrarProfesor(profesorFicticio);
				System.out.println(OPERACION_CORRECTA + CORREO_VALIDO + ".");
			}
		} catch (NullPointerException | OperationNotSupportedException | IllegalArgumentException e) {
			System.out.println("");
			System.out.println(e.getMessage());
			System.out.println(e.getClass());
		}
	}

	public void buscarProfesor() {
		Consola.mostrarCabecera(Opcion.BUSCAR_PROFESOR.getMensaje());

		try {
			 Profesor profesorFicticio = Consola.leerProfesorFicticio();
			 Profesor profesor = controlador.buscarProfesor(profesorFicticio);
			System.out.println("");
			
			if (profesor == null) {
				System.out.println(ERROR + "No está registrado el profesor con correo " + profesorFicticio.getCorreo() + " en el sistema.");
			} else {
				System.out.println("Profesor encontrado: ");
				System.out.println(profesor);
			}
		} catch (NullPointerException | IllegalArgumentException e) {
			System.out.println("");
			System.out.println(e.getMessage());
			System.out.println(e.getClass());
		}	
	}
	
	public void listarProfesores() {
		Consola.mostrarCabecera(Opcion.LISTAR_PROFESORES.getMensaje());	
		
		 try {
			 List<String> profesores = controlador.representarProfesores();
			
			 for(String s : profesores) {
				 System.out.println(s);
			 }
		 } catch (NullPointerException | IllegalArgumentException  e) {
			 System.out.println(e.getMessage());
			 System.out.println(e.getClass());
		 }
	}
	
	public void realizarReserva() {
		Consola.mostrarCabecera(Opcion.INSERTAR_RESERVA.getMensaje());
		
		 try {
			 
			 Profesor profesorFicticio = Consola.leerProfesorFicticio();
			 Profesor profesor = controlador.buscarProfesor(profesorFicticio);
			 Aula aulaFicticia = Consola.leerAulaFicticia();
			 Aula aula = controlador.buscarAula(aulaFicticia);
			 
			 System.out.println("");
			 if (profesor == null && aula == null) {
				 System.out.println(ERROR + "No está registrado el profesor con correo " + profesorFicticio.getCorreo() + " en el sistema.");
				 System.out.println(ERROR + "No está registrada el aula " + aulaFicticia.getNombre() + " en el sistema.");
			 } else if (profesor == null) {
				 System.out.println(ERROR + "No está registrada el profesor con correo " + profesorFicticio.getCorreo() + " en el sistema.");
			 } else if (aula == null) {
				 System.out.println(ERROR + "No está registrado el aula " + aulaFicticia.getNombre() + " en el sistema.");
			 } else {
				 Permanencia permanencia = Consola.leerPermanencia();
				 Reserva reserva = new Reserva(profesor, aula, permanencia);
				 controlador.realizarReserva(reserva);
				 System.out.println("");
				 System.out.println(OPERACION_CORRECTA);
				 System.out.println("");
				 System.out.println("Datos de la reserva realizada: ");
				 System.out.println(reserva);
			 }
		 } catch (NullPointerException | IllegalArgumentException | OperationNotSupportedException e) {
			 System.out.println("");
			 System.out.println(e.getMessage());
			 System.out.println(e.getClass());
		 }
	
	}
	
	public void anularReserva() {
		Consola.mostrarCabecera("Anular Reserva");

		try {
			controlador.anularReserva(Consola.leerReservaFicticia());
			System.out.println("");
			System.out.println("Reserva anulada correctamente, "  + " " + NOMBRE_VALIDO + "/" + CORREO_VALIDO + ".");
		} catch (OperationNotSupportedException | IllegalArgumentException | NullPointerException e) {
			System.out.println("");
			System.out.println(e.getMessage());
			System.out.println(e.getClass());
		}
	}
	
	/*
	 * Existen varios métodos para mostrar ArrayList
	 * A continuación se muestra 2 de ellas:
	 * 1. Bucle for each
	 * 2. Iterator + while(o variación Iterator + for)
	 */
	
	// 1. Bucle for each
	public void listarReservas() {
		Consola.mostrarCabecera(Opcion.LISTAR_RESERVAS.getMensaje());
		
		try {
			List<Reserva> reserva = controlador.getReservas();
			for (Reserva r : reserva) {
				System.out.println(r);
			}	
		} catch (NullPointerException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getClass());
		}
	}
	
	// 1. Bucle for each
	public void listarReservasAula() {
		Consola.mostrarCabecera(Opcion.LISTAR_RESERVAS_AULA.getMensaje());

		try {
			Aula aulaFicticia = Consola.leerAulaFicticia();
			Aula aula = controlador.buscarAula(aulaFicticia);
			List<Reserva> reservasAula = controlador.getReservasAula(aulaFicticia);
			System.out.println("");
			
			if (aula == null) {
				System.out.println(ERROR + "No está registrado el aula " + aulaFicticia.getNombre() + " en el sistema.");
			} else if (reservasAula.size() <= 0) {
				System.out.println("El aula " + aulaFicticia.getNombre() + " no tiene reservas aún.");
			} else {
				System.out.println("Reservas del aula " + aulaFicticia.getNombre() + ": ");
				for (Reserva r : reservasAula) {
					System.out.println(r);
				}
			}
		} catch (NullPointerException | IllegalArgumentException e) {
			System.out.println("");
			System.out.println(e.getMessage());
			System.out.println(e.getClass());
		}
	}
	
	// 2. Variación Iterator + for
	public void listarReservasProfesor() {
		Consola.mostrarCabecera(Opcion.LISTAR_RESERVAS_PROFESOR.getMensaje());

		try {
			Profesor profesorFicticio = Consola.leerProfesorFicticio();
			Profesor profesor = controlador.buscarProfesor(profesorFicticio);
			List<Reserva> reservasProfesor = controlador.getReservasProfesor(profesorFicticio);
			System.out.println("");

			if (profesor == null) {
				System.out.println(ERROR + "No está registrado el profesor con correo " + profesorFicticio.getCorreo() + " en el sistema.");
			} else if (reservasProfesor.size() <= 0) {
				System.out.println("El profesor con correo " + profesorFicticio.getCorreo() + " no tiene reservas aún.");
			} else {
				System.out.println("Reservas del profesor con correo " + profesorFicticio.getCorreo() + ": ");
				for (Iterator<Reserva> it = reservasProfesor.iterator(); it.hasNext();) {
					System.out.println(it.next());
				}
			}

		} catch (NullPointerException | IllegalArgumentException e) {
			System.out.println("");
			System.out.println(e.getMessage());
			System.out.println(e.getClass());
		}
	}
	
	public void consultarDisponibilidad() {
		Consola.mostrarCabecera(Opcion.CONSULTAR_DISPONIBILIDAD.getMensaje());

		try {
			Aula aula = Consola.leerAulaFicticia();
			System.out.println("");
			if (controlador.buscarAula(aula) == null) {
				System.out.println("ERROR: No es posible consultar la disponibilidad de un aula inexistente.");
			} else {
				if(controlador.consultarDisponibilidad(aula, Consola.leerPermanencia())) {
					System.out.println("");
					System.out.println("El aula " + aula.getNombre() + " se encuentra disponible en la permanencia introducida.");
				} else {
					System.out.println("");
					System.out.println("El aula " + aula.getNombre() + " no se encuentra disponible en la permanencia introducida.");
				}
			}
		} catch (NullPointerException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getClass());
		}
	}

}
