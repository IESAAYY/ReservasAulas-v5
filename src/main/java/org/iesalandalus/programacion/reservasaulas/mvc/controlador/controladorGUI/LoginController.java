package org.iesalandalus.programacion.reservasaulas.mvc.controlador.controladorGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class LoginController {
    @FXML
    private Button btEntrar;

    @FXML
    private ImageView ivLoading;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfCiudad;

    @FXML
    private TextField tfCentro;

    @FXML
    private ToggleGroup tgGenero;

    @FXML
    void cerrar(MouseEvent event) {
    	System.exit(0);
    }

    @FXML
    void entrar(ActionEvent event) {

    }
}
