package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class SignUpFormController {
	
	@FXML
	private TextField txtNameSignUp, txtEmailSignUp, txtUserSignUp, txtPasswordSignUp, txtConfirmPasswordSignUp;
	
	@FXML
	private Button btnAccountSignUp, btnClearAccSignUp;
	
	@FXML
	public void keyEvent(KeyEvent e) {
		
		String c = e.getCharacter();
		if(c.equalsIgnoreCase(" ")) {
			e.consume();
		}
	}
	
}
