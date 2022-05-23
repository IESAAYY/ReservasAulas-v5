package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb.utilidades;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Tramo;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDB {

	public static final DateTimeFormatter FORMATO_DIA = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

	private static final String SERVIDOR = "localhost";
	private static final int PUERTO = 27017;
	private static final String BD = "reservasaulas";
	private static final String USUARIO = "reservasaulas";
	private static final String CONTRASENA = "reservasaulas-2022";

	public static final String AULA = "aula";
	public static final String PUESTOS = "puestos";
	public static final String PROFESOR = "profesor";
	public static final String NOMBRE = "nombre";
	public static final String CORREO = "correo";
	public static final String TELEFONO = "telefono";
	public static final String PERMANENCIA = "permanencia";
	public static final String TIPO = "tipo";
	public static final String DIA = "dia";
	public static final String TRAMO = "tramo";
	public static final String HORA = "hora";
	public static final String PROFESOR_CORREO = PROFESOR + "." + CORREO;
	public static final String AULA_NOMBRE = AULA + "." + NOMBRE;
	public static final String PERMANENCIA_TIPO = PERMANENCIA + "." + TIPO;
	public static final String TIPO_TRAMO = "POR_TRAMO";
	public static final String TIPO_HORA = "POR_HORA";
	public static final String PERMANENCIA_DIA = PERMANENCIA + "." + DIA;
	public static final String PERMANENCIA_TRAMO = PERMANENCIA + "." + TRAMO;
	public static final String PERMANENCIA_HORA = PERMANENCIA + "." + HORA;

	private static MongoClient mongoClient = null;

	private MongoDB() {

	}

	public static MongoDatabase getBD() {
		if (mongoClient == null) {
			establecerConexion();
		}
		return mongoClient.getDatabase(BD);
	}

	private static MongoClient establecerConexion() {
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);
		if (mongoClient == null) {
			MongoCredential credenciales = MongoCredential.createScramSha1Credential(USUARIO, BD,
					CONTRASENA.toCharArray());
			mongoClient = MongoClients.create(MongoClientSettings.builder()
					.applyToClusterSettings(
							builder -> builder.hosts(Arrays.asList(new ServerAddress(SERVIDOR, PUERTO))))
					.credential(credenciales).build());
			System.out.println("Conexión a MongoDB establecida.");
		}
		return mongoClient;
	}

	public static void cerrarConexion() {
		if (mongoClient != null) {
			mongoClient.close();
			mongoClient = null;
			System.out.println("Conexión a MongoDB cerrada.");
		}
	}

	public static Document getDocumento(Profesor profesor) {
		if (profesor == null) {
			return null;
		}
		return new Document().append(NOMBRE, profesor.getNombre()).append(CORREO, profesor.getCorreo()).append(TELEFONO, profesor.getTelefono());
	}

	/*
	 *  1: Ascendente
	 * -1: Descentete
	 */
	public static Document getCriterioOrdenacionProfesor() {
		return new Document().append(CORREO, 1);
	}

	public static Profesor getProfesor(Document documentoProfesor) {
		if (documentoProfesor == null) {
			return null;
		}
		return new Profesor(documentoProfesor.getString(NOMBRE), documentoProfesor.getString(CORREO),
				documentoProfesor.getString(TELEFONO));
	}

	public static Document getDocumento(Aula aula) {
		if (aula == null) {
			return null;
		}
		return new Document().append(NOMBRE, aula.getNombre()).append(PUESTOS, aula.getPuestos());
	}

	public static Document getCriterioOrdenacionAula() {
		return new Document().append(NOMBRE, 1);
	}

	public static Aula getAula(Document documentoAula) {
		if (documentoAula == null) {
			return null;
		}
		return new Aula(documentoAula.getString(NOMBRE), documentoAula.getInteger(PUESTOS));
	}

	public static Document getDocumento(Reserva reserva) {
		if (reserva == null) {
			return null;
		}
		Aula aula = reserva.getAula();
		Profesor profesor = reserva.getProfesor();
		Permanencia permanencia = reserva.getPermanencia();
		Document dPermanencia = new Document().append(DIA, permanencia.getDia().format(FORMATO_DIA));
		
		// Si getPuntos() devuelve 10 es por tramo, si devuelve 3 es por hora.
		if (permanencia.getPuntos() == 10) {
			String tramo = ((PermanenciaPorTramo) permanencia).getTramo().name();
			dPermanencia.append(TIPO, TIPO_TRAMO).append(TRAMO, tramo);
		} else {
			dPermanencia.append(TIPO, TIPO_HORA).append(HORA, ((PermanenciaPorHora) permanencia).getHora().format(FORMATO_HORA));
		}
		return new Document().append(PROFESOR, getDocumento(profesor)).append(AULA, getDocumento(aula)).append(PERMANENCIA, dPermanencia);
	}

	public static Reserva getReserva(Document documentoReserva) {
		if (documentoReserva == null) {
			return null;
		}
		Permanencia permanencia = null;
		Document dPermanencia = (Document) documentoReserva.get(PERMANENCIA);
		LocalDate dia = LocalDate.parse(dPermanencia.getString(DIA), FORMATO_DIA);

		if (dPermanencia.getString(TIPO).equals(TIPO_TRAMO)) {
			Tramo tramo = Tramo.valueOf(dPermanencia.getString(TRAMO));
			permanencia = new PermanenciaPorTramo(dia, tramo);
		} else {
			LocalTime hora = LocalTime.parse(dPermanencia.getString(HORA), FORMATO_HORA);
			permanencia = new PermanenciaPorHora(dia, hora);
		}
		return new Reserva(new Profesor(getProfesor((Document) documentoReserva.get(PROFESOR))),
				new Aula(getAula((Document) documentoReserva.get(AULA))), permanencia);
	}

}