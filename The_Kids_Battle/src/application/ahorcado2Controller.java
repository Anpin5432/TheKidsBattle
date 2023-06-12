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

public class ahorcado2Controller {

    /*private static final String[] PALABRAS = {"BOGOTA"};, "PYTHON", "HTML", "CSS", "JAVASCRIPT"};*/
    private String palabraSeleccionada2;
    private int intentosRestantes2;
    private StringBuilder palabraActual2;

    @FXML
    private Label palabraLabel2;

    @FXML
    private Label intentosLabel2;

    @FXML
    private TextField letraTextField2;

    public void initialize() {
        palabraSeleccionada2 = ("MURCIELAGO");
        intentosRestantes2 = 7;
        palabraActual2 = new StringBuilder();

        for (int i = 0; i < palabraSeleccionada2.length(); i++) {
            palabraActual2.append("_");
        }

        actualizarVista();
    }

    @FXML
    private void adivinarButtonAction(ActionEvent event) throws IOException {
        String letra2 = letraTextField2.getText().toUpperCase();
        letraTextField2.clear();
        if (letra2.length() == 1) {
            actualizarPalabra(letra2.charAt(0));
            actualizarVista();
            if (intentosRestantes2 == 0 || palabraActual2.indexOf("_") == -1) {
                mostrarResultado();
            }
        }
    }

   /* private String seleccionarPalabra() {
        int indice = (int) (Math.random() * PALABRAS.length);
        return PALABRAS[indice];
    }*/

    private void actualizarPalabra(char letra) {
        boolean letraAdivinada2 = false;
        for (int i = 0; i < palabraSeleccionada2.length(); i++) {
            if (palabraSeleccionada2.charAt(i) == letra) {
                palabraActual2.setCharAt(i, letra);
                letraAdivinada2 = true;
            }
        }
        if (!letraAdivinada2) {
            intentosRestantes2--;
        }
    }

    private void actualizarVista() {
        palabraLabel2.setText(palabraActual2.toString());
        intentosLabel2.setText("Intentos restantes: " + intentosRestantes2);
    }

    private void mostrarResultado() throws IOException {
        
        if (intentosRestantes2 == 0) {
        	Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Resultado");
            alert.setHeaderText("Perdiste");
            alert.setContentText("La palabra era: " + palabraSeleccionada2);
            alert.showAndWait();
        } else {
            //alert.setHeaderText("Ganaste");
            //alert.setContentText("¡Felicidades, adivinaste la palabra!");
            tercerahorcado();
        }
        
        /*System.exit(0);*/
    }
    
    private void tercerahorcado() throws IOException {
    	        Stage secondStage = new Stage(); 
    	        Parent root = FXMLLoader.load(getClass().getResource("ahorcado3.fxml"));
    	        secondStage.setTitle("Juego de Ahorcado3");
    	        secondStage.setScene(new Scene(root));
    	        secondStage.show();
    	    
    }
}
