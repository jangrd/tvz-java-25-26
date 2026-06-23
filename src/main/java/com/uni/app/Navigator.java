package com.uni.app;

import com.uni.app.controller.LoginController;
import com.uni.app.entities.User;
import com.uni.app.exceptions.NavigatorException;
import com.uni.app.exceptions.ResourceMissingException;
import com.uni.app.exceptions.ValidationException;
import com.uni.app.io.CredentialStore;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;

public final class Navigator {
    private final Stage stage;
    private final AppContext context;

    public Navigator(Stage stage, AppContext context) {
        if (stage == null) {
            throw new ValidationException("Navigator 'stage' must not be null");
        }
        if (context == null) {
            throw new ValidationException("Navigator 'context' must not be null");
        }
        this.stage = stage;
        this.context = context;
    }

    private void switchTo(String fxmlPath, Callback<Class<?>, Object> controllerFactory) throws ResourceMissingException, NavigatorException {
        URL location = getClass().getResource(fxmlPath);
        if (location == null) {
            throw new NavigatorException("Navigator failed to find: " + fxmlPath);
        }
        FXMLLoader loader = new FXMLLoader(location);
        loader.setControllerFactory(controllerFactory);
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new ResourceMissingException("Navigator failed to load " + fxmlPath, e);
        }
    }

    public void showLogin() throws ResourceMissingException, NavigatorException {
        switchTo("login.fxml", type -> new LoginController(this, context.credentialStore()));
    }

    public void showAdminHome(User user) {
        switchTo("adminHome.fxml", type -> new AdminHomeController(this, user);
    }

    public void showProfessorHome(User user) {
        switchTo("professorHome.fxml", type -> new ProfessorHomeController(this, user);
    }

    public void showStudentHome(User user) {
        switchTo("studentHome.fxml", type -> new StudentHomeController(this, user));
    }
}
