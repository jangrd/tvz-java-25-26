package com.uni.app;

import com.uni.app.database.DatabaseManager;
import com.uni.app.exceptions.DatabaseException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
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

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final String DB_URL = "jdbc:h2:./data/database;INIT=RUNSCRIPT FROM 'classpath:schema.sql'";

    private DatabaseManager db;

    /** A simple click counter, shown to prove the event loop runs. */
    private int clicks;

    /** Creates the application; instantiated by the JavaFX runtime. */
    public Main() {
    }

    @Override
    public void init() {
        db = new DatabaseManager(DB_URL, "sa", "");
        try {
            db.connect();
        } catch (DatabaseException e) {
            log.error("Failed to initialize database connection", e);
            Platform.exit();
        }
    }

    /**
     * Builds and shows the primary window.
     *
     * @param stage the primary stage supplied by the JavaFX runtime
     */
    @Override
    public void start(Stage stage) {
        log.info("Starting uni-app UI");
        Label label = new Label("It just works ✔  JavaFX on JDK 25");
        Button button = new Button("Click me");
        button.setOnAction(e -> {
            log.info("Button clicked, count={}", ++clicks);
            label.setText("Clicked " + clicks + " time(s)");
        });

        VBox root = new VBox(16, label, button);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(24));

        stage.setTitle("uni-app");
        stage.setScene(new Scene(root, 480, 220));
        stage.show();
    }

    @Override
    public void stop() {
        try {
            db.close();
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
