package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.event.ActionEvent;

public class menuController {

	// Event Listener on Button.onAction
	@FXML
	public void btnIniciarJuego(ActionEvent event) throws IOException {
		// TODO Autogenerated
		 Stage secondStage = new Stage(); 
	        Parent root = FXMLLoader.load(getClass().getResource("ahorcado1.fxml"));
	        secondStage.setTitle("Juego de Ahorcado1");
	        secondStage.setScene(new Scene(root));
	        secondStage.show();
	}
}
