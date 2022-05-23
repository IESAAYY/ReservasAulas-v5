package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IReservas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb.utilidades.MongoDB;

import com.mongodb.client.MongoCollection;

public class Reservas implements IReservas {
	
	private static final float MAX_PUNTOS_PROFESOR_MES = 200.0f;
	private static final String COLECCION = "reservas";
	
	private MongoCollection<Document> coleccionReservas;
	
	public Reservas()
	{
		
	}
	
	@Override
	public void comenzar() {
		coleccionReservas = MongoDB.getBD().getCollection(COLECCION);
	}

	@Override
	public void terminar() {
		MongoDB.cerrarConexion();
	}
	
	@Override
	public List<Reserva> getReservas() {
		List<Reserva> reservas = new ArrayList<>();
		for (Document documentoReserva : coleccionReservas.find()) {
			reservas.add(MongoDB.getReserva(documentoReserva));
		}
		ordenarPorPermanencia(reservas);
		return reservas;
	}

	private void ordenarPorPermanencia(List<Reserva> reservas) {
		Comparator<Aula> comparadorAula = Comparator.comparing(Aula::getNombre);
		Comparator<Permanencia> comparadorPermanencia = (Permanencia p1, Permanencia p2) -> {
			int comparacion = -1;
			if (p1.getDia().equals(p2.getDia())) {
				if (p1 instanceof PermanenciaPorTramo && p2 instanceof PermanenciaPorTramo) {
					comparacion = Integer.compare(((PermanenciaPorTramo)p1).getTramo().ordinal(), ((PermanenciaPorTramo)p2).getTramo().ordinal());
				} else if (p1 instanceof PermanenciaPorHora && p2 instanceof PermanenciaPorHora) {
					comparacion = ((PermanenciaPorHora)p1).getHora().compareTo(((PermanenciaPorHora)p2).getHora());
				}
			} else {
				comparacion = p1.getDia().compareTo(p2.getDia());
			}
			return comparacion;
		};
		reservas.sort(Comparator.comparing(Reserva::getAula, comparadorAula).thenComparing(Reserva::getPermanencia, comparadorPermanencia));
	}
	
	
	public List<Reserva> get(Profesor profesor) {
		List<Reserva> reservasProfesor = new ArrayList<>();
		for (Document documentoReserva : coleccionReservas.find().filter(eq(MongoDB.PROFESOR_CORREO, profesor.getCorreo()))) {
			reservasProfesor.add(MongoDB.getReserva(documentoReserva));
		}
		ordenarPorPermanencia(reservasProfesor);
		return reservasProfesor;
	}
	
	
	public List<Reserva> get(Aula aula) {
		List<Reserva> reservasAula = new ArrayList<>();
		for (Document documentoReserva : coleccionReservas.find().filter(eq(MongoDB.AULA_NOMBRE, aula.getNombre()))) {
			reservasAula.add(MongoDB.getReserva(documentoReserva));
		}
		ordenarPorPermanencia(reservasAula);
		return reservasAula;	
	}
	
	
	@Override
	public void insertar(Reserva reserva) throws OperationNotSupportedException {
		if (reserva == null) {
			throw new IllegalArgumentException("No se puede realizar una reserva nula.");
		}
		Reserva reservaExistente = getReservaAulaDia(reserva.getAula(), reserva.getPermanencia().getDia());
		if (reservaExistente != null) { 
			if (reservaExistente.getPermanencia() instanceof PermanenciaPorTramo &&
					reserva.getPermanencia() instanceof PermanenciaPorHora) {
				throw new OperationNotSupportedException("Ya se ha realizado una reserva por tramo para este día y aula.");
			}
			if (reservaExistente.getPermanencia() instanceof PermanenciaPorHora &&
					reserva.getPermanencia() instanceof PermanenciaPorTramo) {
				throw new OperationNotSupportedException("Ya se ha realizado una reserva por hora para este día y aula.");
			}
		}
		if (!esMesSiguienteOPosterior(reserva)) {
			throw new OperationNotSupportedException("Sólo se pueden hacer reservas para el mes que viene o posteriores.");
		}
		if (getPuntosGastadosReserva(reserva) > MAX_PUNTOS_PROFESOR_MES) {
			throw new OperationNotSupportedException("Esta reserva excede los puntos máximos por mes para dicho profesor.");
		}
		if (buscar(reserva) != null) {
			throw new OperationNotSupportedException("La reserva ya existe.");
		} else {
			coleccionReservas.insertOne(MongoDB.getDocumento(reserva));
		}
	}
	
	private boolean esMesSiguienteOPosterior(Reserva reserva) {
		LocalDate diaReserva = reserva.getPermanencia().getDia();
		LocalDate dentroDeUnMes = LocalDate.now().plusMonths(1);
		LocalDate primerDiaMesSiguiente = LocalDate.of(dentroDeUnMes.getYear(), dentroDeUnMes.getMonth(), 1);
		return diaReserva.isAfter(primerDiaMesSiguiente) || diaReserva.equals(primerDiaMesSiguiente);
	}
	
	private float getPuntosGastadosReserva(Reserva reserva) {
		float puntosGastados = 0;
		for (Reserva reservaProfesor : getReservasProfesorMes(reserva.getProfesor(), reserva.getPermanencia().getDia())) {
			puntosGastados += reservaProfesor.getPuntos();
		}
		return puntosGastados + reserva.getPuntos();
	}
	
	private List<Reserva> getReservasProfesorMes(Profesor profesor, LocalDate mes) {
		if (profesor == null) {
			throw new NullPointerException("No se pueden buscar reservas de un profesor nulo.");
		}
		List<Reserva> reservasProfesor = new ArrayList<>();
		for (Reserva reserva : get(profesor)) {
			LocalDate diaReserva = reserva.getPermanencia().getDia();
			if (reserva.getProfesor().equals(profesor) && 
					diaReserva.getMonthValue() == mes.getMonthValue() &&
					diaReserva.getYear() == mes.getYear()) {
				reservasProfesor.add(new Reserva(reserva));
			}
		}
		return reservasProfesor;
	}
	
	private Reserva getReservaAulaDia(Aula aula, LocalDate dia) {
		if (dia == null) {
			throw new NullPointerException("No se puede buscar reserva para un día nulo.");
		}
		for (Reserva reserva : get(aula)) {
			LocalDate diaReserva = reserva.getPermanencia().getDia();
			Aula aulaReserva = reserva.getAula();
			if (diaReserva.equals(dia) && aulaReserva.equals(aula)) {
				return reserva;
			}
		}
		return null;
	}
	
	@Override
	public Reserva buscar(Reserva reserva) {
		Bson filtroPermanencia = null;
		if (reserva.getPermanencia() instanceof PermanenciaPorHora) {
			filtroPermanencia = eq(MongoDB.PERMANENCIA_HORA, ((PermanenciaPorHora) reserva.getPermanencia()).getHora().format(MongoDB.FORMATO_HORA)); 
		} else {
			filtroPermanencia = eq(MongoDB.PERMANENCIA_TRAMO, ((PermanenciaPorTramo) reserva.getPermanencia()).getTramo().name());
		}
		Document documentoReserva = coleccionReservas.find().filter(
			and(
				eq(MongoDB.PROFESOR_CORREO, reserva.getProfesor().getCorreo()),
				eq(MongoDB.AULA_NOMBRE, reserva.getAula().getNombre()),
				eq(MongoDB.PERMANENCIA_DIA, reserva.getPermanencia().getDia().format(MongoDB.FORMATO_DIA)),
				filtroPermanencia
			)).first();
		
		return MongoDB.getReserva(documentoReserva);
	}
	
	@Override
	public void borrar(Reserva reserva) throws OperationNotSupportedException {
		if (reserva == null) {
			throw new IllegalArgumentException("No se puede anular una reserva nula.");
		}
		if (!esMesSiguienteOPosterior(reserva)) {
			throw new OperationNotSupportedException("Sólo se pueden anular reservas para el mes que viene o posteriores.");
		}
		if (buscar(reserva) != null) {
			Bson filtroPermanencia = null;
			if (reserva.getPermanencia() instanceof PermanenciaPorHora) {
				filtroPermanencia = eq(MongoDB.PERMANENCIA_HORA, ((PermanenciaPorHora) reserva.getPermanencia()).getHora().format(MongoDB.FORMATO_HORA)); 
			} else {
				filtroPermanencia = eq(MongoDB.PERMANENCIA_TRAMO, ((PermanenciaPorTramo) reserva.getPermanencia()).getTramo().name());
			}
			coleccionReservas.deleteOne(
				and(
						eq(MongoDB.PROFESOR_CORREO, reserva.getProfesor().getCorreo()),
						eq(MongoDB.AULA_NOMBRE, reserva.getAula().getNombre()),
						eq(MongoDB.PERMANENCIA_DIA, reserva.getPermanencia().getDia().format(MongoDB.FORMATO_DIA)),
						filtroPermanencia
					)
				);
		} else {
			throw new OperationNotSupportedException("La reserva a anular no existe.");

		}
	}

	@Override
	public int getNumReservas() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> representar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reserva> getReservasAula(Aula aula) {
		if (aula == null) {
			throw new NullPointerException("ERROR: El aula no puede ser nula.");
		}
		
		List<Reserva> reservaAula = new ArrayList<Reserva>();
		Iterator<Reserva> it = getReservas().iterator();
		
		while(it.hasNext()) {
			Reserva cReserva = it.next();
			if(cReserva.getAula().equals(aula)) {
				reservaAula.add(new Reserva(cReserva));
			}
		}
		
		Comparator<Permanencia> comparadorPermanencia = (Permanencia p1, Permanencia p2) -> {
			int comparacion = -1;
			if (p1.getDia().equals(p2.getDia())) {
				if (p1 instanceof PermanenciaPorTramo && p2 instanceof PermanenciaPorTramo) {
					comparacion = Integer.compare(((PermanenciaPorTramo)p1).getTramo().ordinal() , ((PermanenciaPorTramo)p2).getTramo().ordinal());
				}
				if (p1 instanceof PermanenciaPorHora && p2 instanceof PermanenciaPorHora) {
					comparacion = ((PermanenciaPorHora)p1).getHora().compareTo(((PermanenciaPorHora)p2).getHora());
				}
			}
			if (!p1.getDia().equals(p2.getDia())) {
				comparacion = p1.getDia().compareTo(p2.getDia());
			}
			
			return comparacion;
		};
		
		reservaAula.sort(Comparator.comparing(Reserva :: getPermanencia, comparadorPermanencia));
		
		return reservaAula;
	}

	@Override
	public List<Reserva> getReservasProfesor(Profesor profesor) {
		if (profesor == null) {
			throw new NullPointerException("ERROR: El profesor no puede ser nulo.");
		}
		List<Reserva> reservaProfesor = new ArrayList<Reserva>();
		Iterator<Reserva> it = getReservas().iterator();
		
		while(it.hasNext()) {
			Reserva cReserva = it.next();
			if(cReserva.getProfesor().equals(profesor)) {
				reservaProfesor.add(new Reserva(cReserva));
			}
		}
		
		Comparator<Aula> comparadorAula = Comparator.comparing(Aula :: getNombre);
		Comparator<Permanencia> comparadorPermanencia = (Permanencia p1, Permanencia p2) -> {
			int comparacion = -1;
			if (p1.getDia().equals(p2.getDia())) {
				if (p1 instanceof PermanenciaPorTramo && p2 instanceof PermanenciaPorTramo) {
					comparacion = Integer.compare(((PermanenciaPorTramo)p1).getTramo().ordinal() , ((PermanenciaPorTramo)p2).getTramo().ordinal());
				}
				if (p1 instanceof PermanenciaPorHora && p2 instanceof PermanenciaPorHora) {
					comparacion = ((PermanenciaPorHora)p1).getHora().compareTo(((PermanenciaPorHora)p2).getHora());
				}
			}
			if (!p1.getDia().equals(p2.getDia())) {
				comparacion = p1.getDia().compareTo(p2.getDia());
			}
			
			return comparacion;
		};
		
		reservaProfesor.sort(Comparator.comparing(Reserva :: getAula, comparadorAula).thenComparing(Reserva :: getPermanencia, comparadorPermanencia));
		
		return reservaProfesor;
	}

	@Override
	public List<Reserva> getReservasPermanencia(Permanencia permanencia) {
		List<Reserva> reservaPermanencia = new ArrayList<Reserva>();
		Iterator<Reserva> it = getReservas().iterator();
		
		while(it.hasNext()) {
			Reserva cReserva = it.next();
			if(cReserva.getPermanencia().equals(permanencia)) {
				reservaPermanencia.add(new Reserva(cReserva));
			}
		}
		
		return reservaPermanencia;
	}

	/*
	 * Algoritmo 1: tiene limitaciones dado que existe polimorfismo, por lo tanto, 
	 * solo nos dice si para el aula y la fecha pasados al parámetro, hay una reserva, pero
	 * no del tipo de permanencia. 
	 * Para resolver este problema, se usa el último algoritmo.
	 * 
	 * Algoritmo 2: getPuntos devolverá int 10 si la es PermanenciaPorTramo, y 3 si es PermanenciaPorHora.
	 * Si la condición es cierta, sabremos que no estará disponible el aula introducido
	 * en el parámetro al no coincidir el tipo de permanencia.
	 */ 
	@Override
	public boolean consultarDisponibilidad(Aula aula, Permanencia permanencia) {
		boolean disponible = true;

		if (aula == null) {
			throw new NullPointerException("ERROR: No se puede consultar la disponibilidad de un aula nula.");
		}
		if (permanencia == null) {
			throw new NullPointerException("ERROR: No se puede consultar la disponibilidad de una permanencia nula.");
		}

		// Algoritmo 1
		Iterator<Reserva> it = getReservas().iterator();
		while (it.hasNext()) {
			Reserva cReserva = it.next();
			if (cReserva.getAula().equals(aula) && cReserva.getPermanencia().equals(permanencia)) {
				disponible = false;
			}
		}

		// Algoritmo 2
		Reserva reserva = getReservaAulaDia(aula, permanencia.getDia());
		if (reserva != null) {
			if (reserva.getPermanencia().getPuntos() != permanencia.getPuntos()) {
				disponible = false;
			}
		}

		return disponible;
	}

}
