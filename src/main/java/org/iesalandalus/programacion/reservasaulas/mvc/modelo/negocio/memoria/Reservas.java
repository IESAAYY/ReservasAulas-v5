package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.memoria;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
//Esta clase es prácticamente idéntica a la clase Aulas



import java.time.*;
import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IReservas;

public class Reservas implements IReservas{

	private static final float MAX_PUNTOS_PROFESOR_MES = 200f;
	
	private List<Reserva> arrayListReserva;

	public Reservas() {
		arrayListReserva = new ArrayList<>();
	}

	public Reservas(Reservas reservas) {
		setReservas(reservas);
	}
	
	public void comenzar() {
		
	}

	public void terminar() {
		
	}

	
	private List<Reserva> copiaProfundaReservas(List<Reserva> arrayListReserva) {
		List<Reserva> cArrayListReserva = new ArrayList<Reserva>();
		Iterator <Reserva> it = arrayListReserva.iterator();
		
		while(it.hasNext()) {
			Reserva cReserva = it.next();
			cArrayListReserva.add(new Reserva(cReserva));
		}
		
		return cArrayListReserva;
	}
	
	@Override
	public List<Reserva> getReservas() {
		List<Reserva> aLROrdenadaPorAulaYPermanencia = copiaProfundaReservas(arrayListReserva);
		
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

		aLROrdenadaPorAulaYPermanencia.sort(Comparator.comparing(Reserva::getAula, comparadorAula).thenComparing(Reserva::getPermanencia, comparadorPermanencia));
		
		return aLROrdenadaPorAulaYPermanencia;
	}
	
	private void setReservas(Reservas reservas) {
		if (reservas == null) {
			throw new NullPointerException("ERROR: No se pueden copiar reservas nulas.");
		}
		
		arrayListReserva = copiaProfundaReservas(reservas.arrayListReserva);
	}
	
	@Override
	public int getNumReservas() {
		return arrayListReserva.size();
	}
	
	@Override
	public void insertar(Reserva reserva) throws OperationNotSupportedException {
		if (reserva == null) {
			throw new NullPointerException("ERROR: No se puede insertar una reserva nula.");
		}	
		
		// Se guarda reserva que desea realizar usuario en reservaDia
		Reserva reservaDia = getReservaAulaDia(reserva.getAula(), reserva.getPermanencia().getDia());
		if (reservaDia != null) {
			// Compara la reserva que desea realizar usuario con reservas creadas en el mismo día
			if (reserva.getPermanencia() instanceof PermanenciaPorTramo && reservaDia.getPermanencia() instanceof PermanenciaPorHora ) {
				throw new OperationNotSupportedException("ERROR: Ya se ha realizado una reserva de otro tipo de permanencia para este día.");
			}
			if (reserva.getPermanencia() instanceof PermanenciaPorHora && reservaDia.getPermanencia() instanceof PermanenciaPorTramo) {
				throw new OperationNotSupportedException("ERROR: Ya se ha realizado una reserva de otro tipo de permanencia para este día.");
			}
		}	
		if (arrayListReserva.contains(reserva)) {
			throw new OperationNotSupportedException("ERROR: Ya existe una reserva igual.");
		}
		if (!esMesSiguienteOPosterior(reserva)) {
			throw new OperationNotSupportedException("ERROR: Sólo se pueden hacer reservas para el mes que viene o posteriores.");
		}
		if (getPuntosGastadosReserva(reserva) > MAX_PUNTOS_PROFESOR_MES) {
			throw new OperationNotSupportedException("ERROR: Esta reserva excede los puntos máximos por mes para dicho profesor.");
		}	
		
		arrayListReserva.add(new Reserva(reserva));
	}
	
	private boolean esMesSiguienteOPosterior(Reserva reserva) {
		int agnoReserva = reserva.getPermanencia().getDia().getYear();
		int agnoActual = LocalDate.now().getYear();
		int mesReserva = reserva.getPermanencia().getDia().getMonthValue();
		int mesActual = LocalDate.now().getMonthValue();
		boolean valido = false;
		
		// Caso de reserva en mismo año
		if ((agnoReserva == agnoActual && (mesReserva - mesActual == 1 || mesReserva - mesActual == 2))) {
			valido = true;
		}
		// Caso de reserva en año siguiente
		if ((agnoReserva - agnoActual == 1 && (mesReserva - mesActual == -11 || mesReserva - mesActual == -10))) {
			valido = true;
		}
		
		return valido;
	}
	
	private float getPuntosGastadosReserva(Reserva reserva) {
		float puntosGastados = 0;
		
		for (Reserva r : getReservasProfesorMes(reserva.getProfesor(), reserva.getPermanencia().getDia())) {
			puntosGastados += r.getPuntos();
		}
		
		return puntosGastados + reserva.getPuntos();
	}
	
	private List<Reserva> getReservasProfesorMes(Profesor profesor, LocalDate fechaMes) {

		List<Reserva> reservasMes = new ArrayList<>();

		Month mesReserva = fechaMes.getMonth();

		for (Iterator<Reserva> it = getReservas().iterator(); it.hasNext();) {

			Reserva reserva = it.next();

			if (reserva.getProfesor().equals(profesor) && reserva.getPermanencia().getDia().getMonth().equals(mesReserva)) {

				reservasMes.add(new Reserva(reserva));
			}

		}

		return reservasMes;
	}
	
	private Reserva getReservaAulaDia(Aula aula, LocalDate fechaDia) {
		for (Iterator<Reserva> it = arrayListReserva.iterator(); it.hasNext();) {
			Reserva reservaDia = it.next();
			if (reservaDia.getAula().equals(aula) && reservaDia.getPermanencia().getDia().equals(fechaDia)) {
				return new Reserva (reservaDia);
			}
		}
		
		return null;
	}

	@Override
	public Reserva buscar(Reserva reserva) {
		if (reserva == null) {
			throw new NullPointerException("ERROR: No se puede buscar una reserva nula.");
		}
		if (!arrayListReserva.contains(reserva)) {
			return null;
		}
		
		return new Reserva(reserva);
	}

	@Override
	public void borrar(Reserva reserva) throws OperationNotSupportedException {
		if (reserva == null) {
			throw new IllegalArgumentException("ERROR: No se puede borrar una reserva nula.");
		}
		if (!esMesSiguienteOPosterior(reserva)) {
			throw new OperationNotSupportedException("ERROR: Sólo se pueden anular reservas para el mes que viene o posteriores.");
		}
		if (!arrayListReserva.contains(reserva)) {
			throw new OperationNotSupportedException("ERROR: No existe ninguna reserva igual.");
		}
		
		arrayListReserva.remove(reserva);
	}
	
	@Override
	public List<String> representar()  {
		List <String> representacion = new ArrayList<>();
		
		for (Reserva a : arrayListReserva) {
			representacion.add(a.toString());
		}
		
		if (representacion.isEmpty()) {
			throw new NullPointerException("ERROR: Inserte primero una reserva para mostrar los datos.");
		}
		
		return representacion;
	}
	
	@Override
	public List<Reserva> getReservasAula(Aula aula) {
		if (aula == null) {
			throw new NullPointerException("ERROR: El aula no puede ser nula.");
		}
		
		List<Reserva> reservaAula = new ArrayList<Reserva>();
		Iterator<Reserva> it = arrayListReserva.iterator();
		
		while(it.hasNext()) {
			Reserva cReserva = it.next();
			if(cReserva.getAula().equals(aula)) {
				reservaAula.add(new Reserva(cReserva));
			}
		}
		
		Comparator<Permanencia> comparadorPermanencia = (Permanencia p1, Permanencia p2) -> {
			int comparacion = -1;
			if (p1.getDia().equals(p2.getDia())) {
				if (p1 instanceof PermanenciaPorHora && p2 instanceof PermanenciaPorTramo) {
					comparacion = Integer.compare(((PermanenciaPorTramo)p1).getTramo().ordinal() , ((PermanenciaPorTramo)p2).getTramo().ordinal());
				}
				if (p1 instanceof PermanenciaPorHora && p2 instanceof PermanenciaPorTramo) {
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
		Iterator<Reserva> it = arrayListReserva.iterator();
		
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
				if (p1 instanceof PermanenciaPorHora && p2 instanceof PermanenciaPorTramo) {
					comparacion = Integer.compare(((PermanenciaPorTramo)p1).getTramo().ordinal() , ((PermanenciaPorTramo)p2).getTramo().ordinal());
				}
				if (p1 instanceof PermanenciaPorHora && p2 instanceof PermanenciaPorTramo) {
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
		Iterator<Reserva> it = arrayListReserva.iterator();
		
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
	 * solo nos dice si para el aula y la fecha pasados al parámetros, hay una reserva, pero
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
		Iterator<Reserva> it = arrayListReserva.iterator();
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