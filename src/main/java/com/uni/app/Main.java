package com.uni.app;

import com.uni.app.database.DatabaseManager;
import com.uni.app.exceptions.DatabaseException;
import com.uni.app.exceptions.ResourceMissingException;
import com.uni.app.io.CredentialStore;
import com.uni.app.io.ChangeLogStore;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application entry point.
 *
 * <p>A minimal JavaFX window that confirms the JDK&nbsp;25 + JavaFX&nbsp;25
 * toolchain is wired up correctly. Replace the contents of
 * {@link #start(Stage)} with your own UI.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public class Main extends Application {

    private static final String DB_URL = "jdbc:h2:./data/database;INIT=RUNSCRIPT FROM 'classpath:schema.sql'";

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private AppContext context;

    /** A simple click counter, shown to prove the event loop runs. */
    private int clicks;

    /**
     * Opens the database connection before the UI starts; exits the application
     * if the connection cannot be established.
     */
    @Override
    public void init() {
        DatabaseManager db = new DatabaseManager(DB_URL, "sa", "");
        try {
            db.connect();
        } catch (DatabaseException e) {
            log.error("Failed to initialize database connection", e);
            Platform.exit();
        }

        CredentialStore credentialStore = null;
        try {
            credentialStore = new CredentialStore("users.txt");
            credentialStore.read();
        } catch (ResourceMissingException e) {
            log.error("Failed to load credential store", e);
            Platform.exit();
        }

        ChangeLogStore changeLogStore = null;
        try {
            changeLogStore = new ChangeLogStore("users.txt");
            changeLogStore.read();
        } catch (ResourceMissingException e) {
            log.error("Failed to load change log store", e);
            Platform.exit();
        }

        this.context = new AppContext(db, credentialStore, changeLogStore);
    }

    /**
     * Builds and shows the primary window.
     *
     * @param stage the primary stage supplied by the JavaFX runtime
     */
    @Override
    public void start(Stage stage) throws Exception {
        log.info("Starting uni-app UI");
        Navigator navigator = new Navigator(stage, context);
        navigator.showLogin();
    }

    /**
     * Closes the database connection when the application shuts down.
     */
    @Override
    public void stop() {
        try {
            context.dbManager().close();
        } catch (DatabaseException e) {
            log.error("Failed to close database connection", e);
        }
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments, forwarded to JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }
}
