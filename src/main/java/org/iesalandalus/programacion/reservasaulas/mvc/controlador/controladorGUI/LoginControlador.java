package org.iesalandalus.programacion.reservasaulas.mvc.controlador.controladorGUI;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;

import java.io.IOException;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class LoginControlador {

	private double xOffset, yOffset = 0;

	private IControlador iControlador;

	@FXML
	private Button btMinimizar;

	@FXML
	private Button btCerrar;

	@FXML
	private Node btEntrar;

	@FXML
	private ImageView ivLoading;

	@FXML
	private TextField tfNombre;

	@FXML
	private TextField tfCiudad;

	@FXML
	private TextField tfCentro;

	@FXML
	private RadioButton rbHombre;

	@FXML
	private ToggleGroup tgGenero;

	@FXML
	private RadioButton rbMujer;

	public TextField getTfNombre() {
		return tfNombre;
	}

	public void initialize() {
		ivLoading.setVisible(false);
	}

	public void setControlador(IControlador controlador) {
		this.iControlador = controlador;
	}

	@FXML
	void minmizar(ActionEvent event) {
		Stage stage = (Stage) btMinimizar.getScene().getWindow();
		stage.setIconified(true);
	}

	@FXML
	void cerrar(ActionEvent event) {
		Stage stage = (Stage) btCerrar.getScene().getWindow();
		stage.close();
		iControlador.terminar();
	}

	@FXML
	void entrar(ActionEvent event) {
		try {
			if (tfNombre.getText().trim().isEmpty() || tfCiudad.getText().trim().isEmpty()
					|| tfCentro.getText().trim().isEmpty() || !(rbHombre.isSelected() || rbMujer.isSelected())) {
				throw new NullPointerException("ERROR: Debes rellenar todos los datos");
			}
			ivLoading.setVisible(true);
			PauseTransition pt = new PauseTransition();
			pt.setDuration(Duration.seconds(1.5));
			pt.setOnFinished(ef -> {

				btEntrar.getScene().getWindow().hide();

				FXMLLoader loader = new FXMLLoader(getClass()
						.getResource("/org/iesalandalus/programacion/reservasaulas/mvc/vista/vistaGUI/Marco.fxml"));
				Parent root;
				try {
					root = loader.load();
					MarcoControlador marcoControlador = loader.getController();
					marcoControlador.setControlador(iControlador);
					marcoControlador.inicializarDatosP(tfNombre.getText(), tfCiudad.getText(), tfCentro.getText(),
							rbHombre.isSelected(), rbMujer.isSelected());

					marcoControlador.inicializarDato();
					Scene scene = new Scene(root);
					Stage stage = new Stage();

					stage.setScene(scene);
					stage.initStyle(StageStyle.UNDECORATED);
					stage.setFullScreen(true);
					stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
					stage.show();

					// Permite arrastrar la aplicación
					/*
					root.setOnMousePressed((EventHandler<? super MouseEvent>) new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent event) {
							xOffset = event.getSceneX();
							yOffset = event.getSceneY();
						}
					});
					root.setOnMouseDragged(new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent event) {
							stage.setX(event.getScreenX() - xOffset);
							stage.setY(event.getScreenY() - yOffset);
						}
					});
					*/
				} catch (IOException | IllegalStateException e) {

					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setHeaderText(null);
					alert.setTitle("Aviso");
					alert.setContentText(e.getMessage());
					alert.showAndWait();

				}

			});

			pt.play();
		} catch (IllegalStateException | NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Aviso");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

}
