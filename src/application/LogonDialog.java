package application;

import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class LogonDialog {
	private Dialog<Pair<Environment, String>> dialog;
	private ChoiceBox<Environment> cbxEnv;
	private ComboBox<String> cbxUsers;
	private PasswordField passField;
	private ButtonType loginBtnType;
	private ButtonType cancelBtnType;
	private GridPane grid = new GridPane();
	private Label titleL = new Label("Logowanie do systemu STYLEman");
	private Label imageL = new Label("");
	private Label labelS = new Label("Środowisko:");
	private Label labelU = new Label("Użytkownik:");
	private Label labelH = new Label("Hasło:");
	private HBox buttRow = new HBox();

	public LogonDialog() {

		dialog = new Dialog();
		dialog.setTitle("Logowanie");
		dialog.getDialogPane().setMinSize(300, 210);
		dialog.getDialogPane().setMaxSize(300, 210);
		cbxEnv = new ChoiceBox();
		cbxEnv.getItems().addAll(Environment.values());
		cbxEnv.valueProperty().addListener((observable, oldVal, newVal) -> cbxEnv_Changed(newVal));
		cbxUsers = new ComboBox();
		cbxUsers.setEditable(true);
		cbxUsers.valueProperty().addListener((observable, oldVal, newVal) -> cbxUsers(newVal));
		cbxUsers.setDisable(true);

		passField = new PasswordField();
		passField.setDisable(true);
		loginBtnType = new ButtonType("Logon");
		cancelBtnType = new ButtonType("Anuluj");
		dialog.getDialogPane().getButtonTypes().add(loginBtnType);
		dialog.getDialogPane().getButtonTypes().add(cancelBtnType);

		// alert.getButtonTypes().add(loginBtnType);

		this.addLabelsAndFields(grid);
		dialog.getDialogPane().setContent(grid);
		Optional<Pair<Environment, String>> result = dialog.showAndWait();

		if (result.toString().contains("Logon")) {
			
			Alert alert = new Alert(AlertType.INFORMATION);
			
			if ( Users.passCheck(cbxUsers.getValue(), passField.getText()) ) {
				alert.setHeaderText("Login accepted");
				alert.setContentText("Hello " + cbxUsers.getValue() + "!");
			} else {
				alert.setHeaderText("Incorrect login data");
			}
			
			alert.showAndWait();
			
		}

	}

	private void cbxUsers(String newVal) {
		if (newVal.equals("")) {
			passField.setDisable(true);
		} else {
			passField.setDisable(false);
		}
	}

	private void cbxEnv_Changed(Environment newVal) {
		cbxUsers.setValue("");
		cbxUsers.getItems().clear();

		if (newVal != null) {
			cbxUsers.setDisable(false);
		}

		Users.init(cbxEnv.getValue());

		for (String s : Users.users.keySet()) {
			cbxUsers.getItems().add(s);
		}

	}

	public void addLabelsAndFields(GridPane grid) {

		Image image = new Image(getClass().getResourceAsStream("Login_64x.png"));
		ImageView imageView = new ImageView(image);
		imageL.setGraphic(imageView);

		grid.setMinWidth(300);
		grid.setMaxWidth(300);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(30);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(50);
		ColumnConstraints column3 = new ColumnConstraints();
		column3.setPercentWidth(20);
		grid.getColumnConstraints().addAll(column1, column2, column3);

		GridPane.setConstraints(titleL, 0, 0);
		GridPane.setColumnSpan(titleL, 2);
		GridPane.setConstraints(imageL, 2, 0);
		GridPane.setHalignment(imageL, HPos.RIGHT);

		GridPane.setConstraints(labelS, 0, 1);
		GridPane.setConstraints(cbxEnv, 1, 1);
		GridPane.setColumnSpan(cbxEnv, 2);
		cbxEnv.setMinWidth(180);
		GridPane.setConstraints(labelU, 0, 2);
		GridPane.setConstraints(cbxUsers, 1, 2);
		cbxUsers.setMinWidth(180);
		GridPane.setConstraints(labelH, 0, 3);
		GridPane.setConstraints(passField, 1, 3);
		passField.setMinWidth(180);
		// grid.setGridLinesVisible(true);

		grid.getChildren().addAll(titleL, imageL, labelS, labelU, labelH, cbxEnv, cbxUsers, passField);

	}

}
