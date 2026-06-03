package com.uni.app;

import javafx.application.Application;
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
 */
public class Main extends Application {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    /** A simple click counter, shown to prove the event loop runs. */
    private int clicks;

    /** Creates the application; instantiated by the JavaFX runtime. */
    public Main() {
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

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments, forwarded to JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }
}
