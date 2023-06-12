package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ahorcadoController {

    /*private static final String[] PALABRAS = {"BOGOTA"};, "PYTHON", "HTML", "CSS", "JAVASCRIPT"};*/
    private String palabraSeleccionada;
    private int intentosRestantes;
    private StringBuilder palabraActual;

    @FXML
    private Label palabraLabel;

    @FXML
    private Label intentosLabel;

    @FXML
    private TextField letraTextField;

    public void initialize() {
        palabraSeleccionada = ("BOGOTA");
        intentosRestantes = 7;
        palabraActual = new StringBuilder();

        for (int i = 0; i < palabraSeleccionada.length(); i++) {
            palabraActual.append("_");
        }

        actualizarVista();
    }

    @FXML
    private void adivinarButtonAction(ActionEvent event) throws IOException {
        String letra = letraTextField.getText().toUpperCase();
        letraTextField.clear();
        if (letra.length() == 1) {
            actualizarPalabra(letra.charAt(0));
            actualizarVista();
            if (intentosRestantes == 0 || palabraActual.indexOf("_") == -1) {
                mostrarResultado();
            }
        }
    }

   /* private String seleccionarPalabra() {
        int indice = (int) (Math.random() * PALABRAS.length);
        return PALABRAS[indice];
    }*/

    private void actualizarPalabra(char letra) {
        boolean letraAdivinada = false;
        for (int i = 0; i < palabraSeleccionada.length(); i++) {
            if (palabraSeleccionada.charAt(i) == letra) {
                palabraActual.setCharAt(i, letra);
                letraAdivinada = true;
            }
        }
        if (!letraAdivinada) {
            intentosRestantes--;
        }
    }

    private void actualizarVista() {
        palabraLabel.setText(palabraActual.toString());
        intentosLabel.setText("Intentos restantes: " + intentosRestantes);
    }

    private void mostrarResultado() throws IOException {
        /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Resultado");*/
        if (intentosRestantes == 0) {
        	Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Resultado");
            alert.setHeaderText("Perdiste");
            alert.setContentText("La palabra era: " + palabraSeleccionada);
            alert.showAndWait();
        } else {
            /*alert.setHeaderText("Ganaste");
            alert.setContentText("¡Felicidades, adivinaste la palabra!");*/
            segundoahorcado();
        }
        //alert.showAndWait();
        /*System.exit(0);*/
    }
    
    private void segundoahorcado() throws IOException {
    	        Stage secondStage = new Stage(); 
    	        Parent root = FXMLLoader.load(getClass().getResource("ahorcado2.fxml"));
    	        secondStage.setTitle("Juego de Ahorcado2");
    	        secondStage.setScene(new Scene(root));
    	        secondStage.show();
    	    
    }
}
