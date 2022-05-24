package org.iesalandalus.programacion.reservasaulas.mvc.vista.vistaGUI.controladorGUI;

import java.io.IOException;
import java.util.List;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.Controlador;
import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.vistaGUI.controladorGUI.LoginControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.vistaGUI.controladorGUI.MarcoControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.FactoriaFuenteDatos;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.IModelo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.Modelo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.IVista;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.Vista;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.vistaGUI.recursos.LocalizadorRecursos;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class GUI extends Application implements IVista{
	
	private double xOffset = 0;
	private double yOffset = 0;

	private static IControlador controlador;
	
	@Override
	public void setControlador(IControlador controlador) {
		GUI.controlador = controlador;
		
	}
	
	@Override
	public void comenzar() {
		launch(this.getClass());
	}

	@Override
	public void salir() {
		controlador.terminar();
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {	
			FXMLLoader loader = new FXMLLoader(LocalizadorRecursos.class.getResource("vistas/Login.fxml"));
			Parent root = loader.load();
			
			LoginControlador controlador = loader.getController();
			controlador.setControlador(GUI.controlador);
			
			//org.iesalandalus.programacion.reservasaulas.mvc.vista.vistaGUI.estilos
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(LocalizadorRecursos.class.getResource("estilos/application.css").toExternalForm());
				
			primaryStage.getIcons().add(new Image(LocalizadorRecursos.class.getResource("iconos/CartelAula.png").toExternalForm()));
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.show();
			
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
					primaryStage.setX(event.getScreenX() - xOffset);
					primaryStage.setY(event.getScreenY() - yOffset);
				}
			});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
