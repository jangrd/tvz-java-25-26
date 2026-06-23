package com.uni.app.controller;

import com.uni.app.Navigator;
import com.uni.app.entities.User;
import com.uni.app.exceptions.ValidationException;
import com.uni.app.io.CredentialStore;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button loginButton;

    private final Navigator navigator;
    private final CredentialStore store;

    public LoginController(Navigator navigator, CredentialStore store) {
        if (navigator == null) {
            throw new ValidationException("LoginController 'navigator' must not be null");
        }
        if (store == null) {
            throw new ValidationException("LoginController 'store' must not be null");
        }
        this.navigator = navigator;
        this.store = store;
    }

    @FXML private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            Optional<User> user = store.authenticate(username, password);
            user.ifPresent(this::onLoginSuccess);
        } catch (RuntimeException e) {
            log.error("Login failed", e);
        }
    }

    private void onLoginSuccess(User user) {
        switch (user.role()) {
            case ADMIN      -> navigator.showAdminHome(user);
            case PROFESSOR  -> navigator.showProfessorHome(user);
            case STUDENT    -> navigator.showStudentHome(user);
        }
    }
}
