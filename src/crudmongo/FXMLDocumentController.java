package crudmongo;

import crudmongo.controllers.MongoController;
import crudmongo.controllers.PetController;
import crudmongo.model.Pet;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class FXMLDocumentController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TableView<Pet> tbPet;

	@FXML
	private TableColumn<Pet, String> clName;

	@FXML
	private TableColumn<Pet, String> clType;

	@FXML
	private TableColumn<Pet, String> clBreed;

	@FXML
	private TableColumn<Pet, Integer> clAge;

	@FXML
	private TableColumn<Pet, HBox> clActions;

	@FXML
	private TextField txtName;

	@FXML
	private ComboBox<String> txtType;

	@FXML
	private TextField txtBreed;

	@FXML
	private TextField txtAge;

	@FXML
	private Button btnAdd;

	private Pet oldPet;

	@FXML
	void initialize() {
		initTable();
		initCombos();
		MongoController.getConnection();
		setPetsInTable();
		btnAdd.setOnAction(e -> {
			add();
		});
	}

	private void add() {
		if (inputsFilled()) {
			if (oldPet == null) {
				PetController.insert(getPet());
			} else {
				PetController.update(oldPet, getPet());
				oldPet = null;
			}
			setPetsInTable();
			clearInputs();
		}
	}

	private Pet getPet() {
		Pet pet = new Pet();
		String name = txtName.getText();
		String type = txtType.getValue();
		String breed = txtBreed.getText();
		int age = Integer.parseInt(txtAge.getText());

		pet.setName(name);
		pet.setType(type);
		pet.setBreed(breed);
		pet.setAge(age);

		return pet;
	}

	private void putActions() {
		tbPet.getItems().forEach(pet -> {
			// Modificar
			((Button) pet.getActions().getChildren().get(0)).setOnAction(event -> {
				modify(pet, event);
			});

			// Eliminar
			((Button) pet.getActions().getChildren().get(1)).setOnAction(event -> {
				delete(pet, event);
			});
		});
	}

	private void modify(Pet pet, ActionEvent event) {
		oldPet = pet;
		setPetInForm();
	}

	private void delete(Pet pet, ActionEvent event) {
		PetController.delete(pet);
		setPetsInTable();
	}

	private void setPetInForm() {
		txtName.setText(oldPet.getName());
		txtBreed.setText(oldPet.getBreed());
		txtType.setValue(oldPet.getType());
		txtAge.setText(String.valueOf(oldPet.getAge()));
	}

	private void clearInputs(){
		txtName.setText("");
		txtBreed.setText("");
		txtType.setValue("");
		txtAge.setText("");
}

	private void initTable() {
		clName.setCellValueFactory(new PropertyValueFactory<>("name"));
		clType.setCellValueFactory(new PropertyValueFactory<>("type"));
		clBreed.setCellValueFactory(new PropertyValueFactory<>("breed"));
		clAge.setCellValueFactory(new PropertyValueFactory<>("age"));
		clActions.setCellValueFactory(new PropertyValueFactory<>("actions"));
	}

	private void setPetsInTable() {
		tbPet.getItems().setAll(PetController.getAll());
		putActions();
	}

	private void initCombos() {
		final String[] types = {"Perro", "Gato"};
		txtType.getItems().setAll(types);
	}

	private boolean inputsFilled() {
		return txtName.getText().length() > 0
				&& txtAge.getText().length() > 0
				&& txtBreed.getText().length() > 0
				&& txtType.getValue().length() > 0;
	}
}
