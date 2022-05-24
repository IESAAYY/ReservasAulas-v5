package org.iesalandalus.programacion.reservasaulas.mvc.vista.vistaGUI.controladorGUI;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Tramo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import javax.naming.OperationNotSupportedException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class MarcoControlador {

	private IControlador iControlador;

	@FXML
	private Button btMinimizar;

	@FXML
	private Button btCerrar;

	@FXML
	private Label lbUsuario;

	@FXML
	private Label lbCiudad;

	@FXML
	private Label lbCentro;

	@FXML
	private ImageView ivMasculino;

	@FXML
	private Label lbGenero;

	@FXML
	private ImageView ivFemenino;

	private ObservableList<Aula> olBuscarAula = FXCollections.observableArrayList();

	@FXML
	private TextField tfBuscarAula;

	@FXML
	private TextField tfNombreAula;

	@FXML
	private TextField tfhoraD;

	@FXML
	private RadioButton rbHoraD;

	@FXML
	private ToggleGroup tgTramoHoraD;

	@FXML
	private Button btConsultarD;

	@FXML
	private ImageView ivDisponibilidad;

	@FXML
	private Label lbDisponibilidad;

	@FXML
	private ImageView ivDisponible;

	@FXML
	private ImageView ivOcupada;

	@FXML
	private Label lbDisponible;

	@FXML
	private Label lbNoDisponible;

	@FXML
	private DatePicker dpReservaD;

	@FXML
	private ComboBox<Aula> comboBoxAulaD;

	@FXML
	private RadioButton rbTramoD;

	@FXML
	private ComboBox<Tramo> comboBoxTramoD;

	@FXML
	private TextField tfPuesto;

	@FXML
	private TableView<Aula> tvAula;

	@FXML
	private TableColumn<Aula, String> tcNombreAula;

	@FXML
	private TableColumn<Aula, String> tcPuesto;

	@FXML
	private Button btBorrarAula;

	private ObservableList<Profesor> olBuscarProfesor = FXCollections.observableArrayList();

	@FXML
	private TableView<Profesor> tvProfesor;

	@FXML
	private TableColumn<Profesor, String> tcNombreProfesor;

	@FXML
	private TableColumn<Profesor, String> tcCorreo;

	@FXML
	private TableColumn<Profesor, String> tcTélefono;

	@FXML
	private Button btAñadirProfesor;

	@FXML
	private TextField tfNombreProfesor;

	@FXML
	private TextField tfCorreo;

	@FXML
	private Button btBorrarProfesor;

	@FXML
	private TextField tfBuscarProfesor;

	@FXML
	private TextField tfTeléfono;

	@FXML
	private TableView<Reserva> tvReserva;

	@FXML
	private TableColumn<Reserva, String> tcNombreProfesorR;

	@FXML
	private TableColumn<Reserva, String> tcCorreoR;

	@FXML
	private TableColumn<Reserva, String> tcTélefonoR;

	@FXML
	private TableColumn<Reserva, String> tcNombreAulaR;

	@FXML
	private TableColumn<Reserva, String> tcPuestosR;

	@FXML
	private TableColumn<Reserva, String> tcDía;

	@FXML
	private TableColumn<Reserva, String> tcTramoHora;

	@FXML
	private TableColumn<Reserva, String> tcPuntosR;

	@FXML
	private ComboBox<Profesor> comboBoxCorreo;

	@FXML
	private ComboBox<Aula> comboBoxAula;

	@FXML
	private ComboBox<Tramo> comboBoxTramo;

	@FXML
	private DatePicker dpReserva;

	@FXML
	private RadioButton rbTramo;

	@FXML
	private ToggleGroup tgTramoHora;

	@FXML
	private RadioButton rbHora;

	@FXML
	private TextField tfHora;

	@FXML
	private Button btAñadirReserva;

	@FXML
	private Button btBorrarReserva;

	public void initialize() {
		ivMasculino.setVisible(false);
		ivFemenino.setVisible(false);

		// tvAula
		tcNombreAula.setCellValueFactory(aula -> new SimpleStringProperty(aula.getValue().getNombre()));
		tcPuesto.setCellValueFactory(aula -> new SimpleStringProperty(Integer.toString(aula.getValue().getPuestos())));
		tgTramoHoraD.selectedToggleProperty().addListener((e, anteriorE, actualE) -> desactivarPermanenciaD());
		comboBoxTramoD.setItems(FXCollections.observableArrayList(Tramo.values()));
		comboBoxAulaD.setConverter(new StringConverter<Aula>() {

			@Override
			public String toString(Aula aula) {
				return aula.getNombre();
			}

			@Override
			public Aula fromString(String string) {
				return null;
			}
		});

		// tvProfesor
		tcNombreProfesor.setCellValueFactory(profesor -> new SimpleStringProperty(profesor.getValue().getNombre()));
		tcCorreo.setCellValueFactory(profesor -> new SimpleStringProperty(profesor.getValue().getCorreo()));
		tcTélefono.setCellValueFactory(profesor -> new SimpleStringProperty(profesor.getValue().getTelefono()));

		// TvReserva
		tcNombreProfesorR
				.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getProfesor().getNombre()));
		tcCorreoR
				.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getProfesor().getCorreo()));
		tcTélefonoR.setCellValueFactory(
				reserva -> new SimpleStringProperty(reserva.getValue().getProfesor().getTelefono()));
		tcNombreAulaR
				.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getAula().getNombre()));
		tcPuestosR.setCellValueFactory(
				reserva -> new SimpleStringProperty(Integer.toString(reserva.getValue().getAula().getPuestos())));
		tcDía.setCellValueFactory(reserva -> {
			String dia = reserva.getValue().getPermanencia().getDia().toString();
			return new SimpleStringProperty(dia);
		});
		tcTramoHora.setCellValueFactory(reserva -> {

			String permanencia = reserva.getValue().getPermanencia().toString();
			if (permanencia.contains("tramo")) {

				return new SimpleStringProperty(permanencia.substring(permanencia.indexOf("tramo=") + 6));
			} else {

				return new SimpleStringProperty(permanencia.substring(permanencia.indexOf("hora=") + 5));
			}
		});
		tcPuntosR.setCellValueFactory(
				reserva -> new SimpleStringProperty(Integer.toString(reserva.getValue().getPermanencia().getPuntos())));
		comboBoxCorreo.setConverter(new StringConverter<Profesor>() {
			@Override
			public String toString(Profesor profesor) {
				return profesor.getCorreo();
			}

			@Override
			public Profesor fromString(String string) {
				return null;
			}
		});
		comboBoxAula.setConverter(new StringConverter<Aula>() {

			@Override
			public String toString(Aula aula) {
				return aula.getNombre();
			}

			@Override
			public Aula fromString(String string) {
				return null;
			}
		});
		comboBoxTramo.setItems(FXCollections.observableArrayList(Tramo.values()));
		tgTramoHora.selectedToggleProperty().addListener((e, anteriorE, actualE) -> desactivarPermanencia());
	}

	public void inicializarDatosP(String nombre, String ciudad, String centro, boolean masculino, boolean femenino) {
		lbUsuario.setText(nombre);
		lbCiudad.setText(ciudad);
		lbCentro.setText(centro);

		if (masculino) {
			ivMasculino.setVisible(true);
			lbGenero.setText("Hombre");
		}

		if (femenino) {
			ivFemenino.setVisible(true);
			lbGenero.setText("Mujer");

		}
	}

	public void inicializarDato() {

		tvAula.setItems(FXCollections.observableArrayList(iControlador.getAulas()));
		comboBoxAulaD.getItems().addAll(iControlador.getAulas());
		tvProfesor.setItems(FXCollections.observableArrayList(iControlador.getProfesores()));
		tvReserva.setItems(FXCollections.observableArrayList(iControlador.getReservas()));
		comboBoxCorreo.getItems().addAll(iControlador.getProfesores());
		comboBoxAula.getItems().addAll(iControlador.getAulas());
	}

	@FXML
	void cerrar(ActionEvent event) {
		Stage stage = (Stage) btCerrar.getScene().getWindow();
		stage.close();
		iControlador.terminar();
	}

	@FXML
	void minmizar(ActionEvent event) {
		Stage stage = (Stage) btMinimizar.getScene().getWindow();
		stage.setIconified(true);
	}

	public void setControlador(IControlador iControlador) {
		this.iControlador = iControlador;
	}

	@FXML
	void buscarAula(ActionEvent event) {
		String tfBuscarAula = this.tfBuscarAula.getText();

		if (tfBuscarAula.isEmpty()) {
			tvAula.setItems(FXCollections.observableArrayList(iControlador.getAulas()));
		} else {
			olBuscarAula.clear();

			for (Aula a : iControlador.getAulas()) {
				if (a.getNombre().toLowerCase().contains(tfBuscarAula.toLowerCase())) {
					olBuscarAula.add(a);
				}
			}

			tvAula.setItems(olBuscarAula);

		}
	}

	@FXML
	void añadirAula(ActionEvent event) throws OperationNotSupportedException {
		try {
			iControlador.insertarAula(aulaTf());
			tvAula.setItems(FXCollections.observableArrayList(iControlador.getAulas()));
			comboBoxAula.getItems().clear();
			comboBoxAula.getItems().addAll(iControlador.getAulas());
			comboBoxAulaD.getItems().clear();
			comboBoxAulaD.getItems().addAll(iControlador.getAulas());
		} catch (NullPointerException | OperationNotSupportedException | IllegalArgumentException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Aviso");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	void borrarAula(ActionEvent event) throws OperationNotSupportedException {
		try {
			Aula aulaSelecionada = tvAula.getSelectionModel().getSelectedItem();

			if (aulaSelecionada == null) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Aviso");
				alert.setHeaderText(null);
				alert.setContentText("ERROR: Debes seleccionar a un aula");
				alert.showAndWait();
			} else {

				// if: Simula DELETE RESTRICT impidiendo que se borre un profesor con reservas
				if (iControlador.getReservasAula(aulaSelecionada).size() > 0) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Aviso");
					alert.setHeaderText(null);
					alert.setContentText("ERROR: No se puede borrar un aula con reserva");
					alert.showAndWait();
					return;
				}
				iControlador.borrarAula(aulaSelecionada);
				tvAula.setItems(FXCollections.observableArrayList(iControlador.getAulas()));
				comboBoxAula.getItems().clear();
				comboBoxAula.getItems().addAll(iControlador.getAulas());
				comboBoxAulaD.getItems().clear();
				comboBoxAulaD.getItems().addAll(iControlador.getAulas());
			}
		} catch (NullPointerException | OperationNotSupportedException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Aviso");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	void seleccionarTvAula(MouseEvent event) {
		Aula aula = tvAula.getSelectionModel().getSelectedItem();

		if (aula != null) {
			tfNombreAula.setText(aula.getNombre());
			tfPuesto.setText(Integer.toString(aula.getPuestos()));
		}
	}

	@FXML
	void consultarDisponibilidad(ActionEvent event) {
		try {
			Aula aula = comboBoxAulaD.getValue();
			Permanencia permanencia;
			LocalDate fecha = dpReservaD.getValue();

			if (rbTramoD.isSelected()) {
				permanencia = new PermanenciaPorTramo(fecha, comboBoxTramoD.getValue());
			} else {
				permanencia = new PermanenciaPorHora(fecha, LocalTime.parse(tfhoraD.getText()));
			}

			if (iControlador.consultarDisponibilidad(aula, permanencia)) {
				ivDisponibilidad.setVisible(false);
				lbDisponibilidad.setVisible(false);
				ivDisponible.setVisible(true);
				lbDisponible.setVisible(true);
				ivOcupada.setVisible(false);
				lbNoDisponible.setVisible(false);
			} else {
				ivDisponibilidad.setVisible(false);
				lbDisponibilidad.setVisible(false);
				ivDisponible.setVisible(false);
				lbDisponible.setVisible(false);
				ivOcupada.setVisible(true);
				lbNoDisponible.setVisible(true);
			}
		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Aviso");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		} catch (DateTimeParseException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Aviso");
			alert.setHeaderText(null);
			alert.setContentText("Formato de hora incorrecto");
			alert.showAndWait();
		}
	}

	private Aula aulaTf() {
		Aula aula = null;
		try {
			aula = new Aula(tfNombreAula.getText(), Integer.parseInt(tfPuesto.getText()));

		} catch (NullPointerException | IllegalArgumentException e) {
			// Dialogos.mostrarDialogoError("Error", e.getMessage());
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Aviso");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
		return aula;
	}

	private void desactivarPermanenciaD() {
		if (rbTramoD.isSelected()) {
			comboBoxTramoD.setDisable(false);
			tfhoraD.setDisable(true);
		}
		if (rbHoraD.isSelected()) {
			comboBoxTramoD.setDisable(true);
			tfhoraD.setDisable(false);
		}
	}

	@FXML
	void seleccionarTvProfesor(MouseEvent event) {
		Profesor profesor = tvProfesor.getSelectionModel().getSelectedItem();

		if (profesor != null) {
			tfNombreProfesor.setText(profesor.getNombre());
			tfCorreo.setText(profesor.getCorreo());
			tfTeléfono.setText(profesor.getTelefono());

		}
	}

	@FXML
	void buscarProfesor(ActionEvent event) {
		String tfBuscarProfesor = this.tfBuscarProfesor.getText();

		if (tfBuscarProfesor.isEmpty()) {
			tvProfesor.setItems(FXCollections.observableArrayList(iControlador.getProfesores()));
		} else {
			olBuscarProfesor.clear();

			for (Profesor p : iControlador.getProfesores()) {
				if (p.getCorreo().toLowerCase().contains(tfBuscarProfesor.toLowerCase())) {
					olBuscarProfesor.add(p);
				}
			}

			tvProfesor.setItems(olBuscarProfesor);

		}
	}

	@FXML
	void añadirProfesor(ActionEvent event) {
		try {
			iControlador.insertarProfesor(profesorTf());
			tvProfesor.setItems(FXCollections.observableArrayList(iControlador.getProfesores()));
			comboBoxCorreo.getItems().clear();
			comboBoxCorreo.getItems().addAll(iControlador.getProfesores());
		} catch (NullPointerException | OperationNotSupportedException e) {
			// Dialogos.mostrarDialogoError("Error", e.getMessage());
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Aviso");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	void borrarProfesor(ActionEvent event) {
		try {
			Profesor profesorSelecionado = tvProfesor.getSelectionModel().getSelectedItem();

			if (profesorSelecionado == null) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Aviso");
				alert.setHeaderText(null);
				alert.setContentText("ERROR: Debes seleccionar a un profesor");
				alert.showAndWait();
			} else {
				// Simula DELETE RESTRICT impidiendo que se borre un profesor con reservas
				if (iControlador.getReservasProfesor(profesorSelecionado).size() > 0) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Aviso");
					alert.setHeaderText(null);
					alert.setContentText("ERROR: No se puede borrar un profesor con reserva");
					alert.showAndWait();
					return;
				}
				iControlador.borrarProfesor(profesorSelecionado);
				tvProfesor.setItems(FXCollections.observableArrayList(iControlador.getProfesores()));
				comboBoxCorreo.getItems().clear();
				comboBoxCorreo.getItems().setAll(iControlador.getProfesores());
			}
		} catch (OperationNotSupportedException | NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Aviso");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	private Profesor profesorTf() {
		Profesor profesor = null;
		try {
			profesor = new Profesor(tfNombreProfesor.getText(), tfCorreo.getText(), tfTeléfono.getText());

		} catch (NullPointerException | IllegalArgumentException e) {
			// Dialogos.mostrarDialogoError("Error", e.getMessage());
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Aviso");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
		return profesor;

	}

	@FXML
	void añadirReserva(ActionEvent event) {
		try {
			iControlador.realizarReserva(reserva());
			tvReserva.setItems(FXCollections.observableArrayList(iControlador.getReservas()));
		} catch (OperationNotSupportedException  | NullPointerException| IllegalArgumentException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Aviso");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		} catch (DateTimeParseException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Aviso");
			alert.setHeaderText(null);
			alert.setContentText("Formato de hora incorrecto");
			alert.showAndWait();
		}
		

	}

	@FXML
	void borrarReserva(ActionEvent event) {
		try {
			Reserva reservaSeleccionada = tvReserva.getSelectionModel().getSelectedItem();

			if (reservaSeleccionada == null) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Aviso");
				alert.setHeaderText(null);
				alert.setContentText("ERROR: Debes seleccionar a una reserva");
				alert.showAndWait();
			} else {
				iControlador.anularReserva(reservaSeleccionada);
				tvReserva.setItems(FXCollections.observableArrayList(iControlador.getReservas()));
			}
		} catch (OperationNotSupportedException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Aviso");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}

	}

	@FXML
	void seleccionarTvReserva(MouseEvent event) {

	}

	private Reserva reserva() {
		Profesor profesor = comboBoxCorreo.getValue();
		Aula aula = comboBoxAula.getValue();
		Permanencia permanencia;
		LocalDate fecha = dpReserva.getValue();

		if (rbTramo.isSelected()) {
			permanencia = new PermanenciaPorTramo(fecha, comboBoxTramo.getValue());
		} else {
			permanencia = new PermanenciaPorHora(fecha, LocalTime.parse(tfHora.getText()));
		}

		Reserva reserva = new Reserva(profesor, aula, permanencia);
		return reserva;
	}

	private void desactivarPermanencia() {
		if (rbTramo.isSelected()) {
			comboBoxTramo.setDisable(false);
			tfHora.setDisable(true);
		}
		if (rbHora.isSelected()) {
			comboBoxTramo.setDisable(true);
			tfHora.setDisable(false);
		}
	}

}
