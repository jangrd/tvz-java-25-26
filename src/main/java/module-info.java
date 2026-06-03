/**
 * Application module for uni-app.
 */
module uni.app {
    requires javafx.controls;
    requires javafx.fxml;

    // SLF4J facade + Logback. Requiring logback.classic pulls the
    // SLF4J service provider into the module graph so it's discovered.
    requires org.slf4j;
    requires ch.qos.logback.classic;

    // Allow JavaFX's FXML loader to reflectively access this package.
    opens com.uni.app to javafx.fxml;
    exports com.uni.app;
}
