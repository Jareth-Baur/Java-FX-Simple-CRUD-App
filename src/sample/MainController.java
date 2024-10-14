package sample;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class MainController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private ListView<User> userList;

    private UserDAO userDAO = new UserDAO();
    private User selectedUser;

    @FXML
    private void initialize() {
        loadUsers();
        userList.setOnMouseClicked(this::handleUserSelection);
    }

    @FXML
    private void saveUser() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        try {
            if (selectedUser == null) {
                // Create new user
                userDAO.addUser(firstName, lastName);
                showAlert("Success", "User added successfully!");
            } else {
                // Update existing user
                userDAO.updateUser(selectedUser.getId(), firstName, lastName);
                showAlert("Success", "User updated successfully!");
            }
            clearFields();
            loadUsers();
        } catch (SQLException e) {
            showAlert("Error", "Failed to save user.");
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteUser() {
        if (selectedUser != null) {
            try {
                userDAO.deleteUser(selectedUser.getId());
                showAlert("Success", "User deleted successfully!");
                clearFields();
                loadUsers();
            } catch (SQLException e) {
                showAlert("Error", "Failed to delete user.");
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "No user selected.");
        }
    }

    private void loadUsers() {
        try {
            List<User> users = userDAO.getAllUsers();
            userList.getItems().setAll(users);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleUserSelection(MouseEvent event) {
        selectedUser = userList.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            firstNameField.setText(selectedUser.getFirstName());
            lastNameField.setText(selectedUser.getLastName());
        }
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        selectedUser = null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
