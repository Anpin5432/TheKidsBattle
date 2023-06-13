package application;



import java.net.URL;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;


public class SignInFormController implements Initializable{
	
	@FXML
	private TextField textUserSignIn, txtPasswordSignInMask;
	
	@FXML
	private PasswordField txtPasswordSignIn;
	
	@FXML
	private CheckBox checkViewPassSignIn;
	
	@FXML
	private Button btnSignIn, btnClear;
	
	/*@FXML
	public void eventKey(KeyEvent e) {
		String c = e.getCharacter();
		
		if(c.equalsIgnoreCase(" ")) {
		e.consume();
		}
	}*/
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		   maskPassword (txtPasswordSignIn, txtPasswordSignInMask, checkViewPassSignIn);   
		   
	    }

	public void maskPassword(PasswordField pass, TextField text, CheckBox check) {
		
		text.setVisible(false);
		text.setManaged(false);
		
		text.managedProperty().bind(check.selectedProperty());
		text.visibleProperty().bind(check.selectedProperty());
		
		text.textProperty().bindBidirectional(pass.textProperty());
	}
	
}