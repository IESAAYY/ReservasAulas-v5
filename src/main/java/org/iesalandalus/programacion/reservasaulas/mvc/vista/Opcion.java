package org.iesalandalus.programacion.reservasaulas.mvc.vista;

public enum Opcion {
	
	SALIR("Salir del programa") {	
		public void ejecutar() {
			vista.salir();
		}
	},
	
	INSERTAR_AULA("Insertar un aula") {	
		public void ejecutar() {
			vista.insertarAula();
		}
	},
	
	BORRAR_AULA("Borrar un aula insertado") {		
		public void ejecutar() {
			vista.borrarAula();
		}
	},
	
	BUSCAR_AULA("Buscar un aula insertado") {		
		public void ejecutar() {
			vista.buscarAula();
		}
	},
	
	LISTAR_AULAS("Listar aulas insertadas") {		
		public void ejecutar() {
			vista.listarAulas();
		}
	}, 
	
	INSERTAR_PROFESOR("Insertar un profesor") {		
		public void ejecutar() {
			vista.insertarProfesor();			
		}
	},
	
	BORRAR_PROFESOR("Borrar un profesor insertado") {
		public void ejecutar() {
			vista.borrarProfesor();
		}
	},
	
	BUSCAR_PROFESOR("Buscar un profesor insertado") {
		public void ejecutar() {
			vista.buscarProfesor();		
		}
	},
	
	LISTAR_PROFESORES("Listar profesores insertados") {
		public void ejecutar() {
			vista.listarProfesores();
		}
	},
	
	INSERTAR_RESERVA("Realizar una reserva") {		
		public void ejecutar() {
			vista.realizarReserva();
		}
	}, 
	
	BORRAR_RESERVA("Borrar una reserva insertada") {
		public void ejecutar() {
			vista.anularReserva();
		}
	},
	
	LISTAR_RESERVAS("Listar reservas insertadas") {	
		public void ejecutar() {
			vista.listarReservas();		
		}
	}, 
	
	LISTAR_RESERVAS_AULA("Listar reservas de un aula") {		
		public void ejecutar() {
		vista.listarReservasAula();		
		}
	},
	LISTAR_RESERVAS_PROFESOR("Listar reservas de un profesor") {		
		public void ejecutar() {
			vista.listarReservasProfesor();		
		}
	},
	
	CONSULTAR_DISPONIBILIDAD("Consultar disponibilidad") {		
		public void ejecutar() {
			vista.consultarDisponibilidad();
		}
	};
	
	private String mensajeAMostrar;
	private static Vista vista;
	
	private Opcion(String mensajeAMostrar) {
		this.mensajeAMostrar = mensajeAMostrar;
	}
	public String getMensaje() {
		return mensajeAMostrar;
	}
	
	public abstract void ejecutar();
	
	protected static void setVista(Vista vista) {
		Opcion.vista = vista;
	}
	
	public static Opcion getOpcionSegunOrdinal(int ordinal) {
		if (!esOrdinalValido(ordinal)) {
			throw new IllegalArgumentException("Ordinal de la opción no válido");
		}
		
		return Opcion.values()[ordinal];
	}
	
	public static boolean esOrdinalValido(int ordinal) {
		return (ordinal >= 0 && ordinal <= Opcion.values().length -1);
	}
	
	@Override
	public String toString() {
		if (ordinal() < 10) {
			return ordinal() + ".  " + getMensaje();
		}
		return ordinal() + ". " + getMensaje();
	}
		
}
