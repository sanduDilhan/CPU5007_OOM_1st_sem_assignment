package controller;

import com.swlc.social_media.controller.DuplicateHomeController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class HomeControllerTest extends ApplicationTest {

    private DuplicateHomeController controller;

    @Override
    public void start(Stage stage) throws Exception {
        // Load your FXML and initialize the controller (replace with your actual logic)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home_duplicate.fxml")); // Replace with your FXML path
        stage.setScene(new Scene(loader.load()));
        controller = loader.getController();
        stage.show();
    }

    @Test
    public void testAddPost_ShortDescription() {
        Platform.runLater(() -> {
            String shortDescription = "testing post";
            controller.addPost("user.jpg", "John Doe", shortDescription, "user.jpg", "2024-12-03", 1L);

            // Get the description and seeMore labels from the latest post
            Label descriptionLabel = (Label) lookup("#postContainer").query()
                    .lookup(".post-container .post-description");
            Label seeMoreLabel = (Label) lookup("#postContainer").query()
                    .lookup(".post-container .see-more");

            // Assert that the description label shows the full text
            assertEquals(shortDescription, descriptionLabel.getText());
        });
    }

    @Test
    public void testAddPost_LongDescription_SeeMoreLabelVisible() {
            Platform.runLater(() -> {
                String longDescription = "testing post";
                controller.addPost("user.jpg", "John Doe", longDescription, "user.jpg", "2024-12-03", 1L);

                // Get the description and seeMore labels from the latest post (similar to previous test)
                Label descriptionLabel = (Label) lookup("#postContainer").query()
                        .lookup(".post-container .post-description");
                Label seeMoreLabel = (Label) lookup("#postContainer").query()
                        .lookup(".post-container .see-more");

                assertEquals(longDescription, descriptionLabel.getText());
                // Assert that the description label shows the truncated text
                assertTrue(descriptionLabel.getText().endsWith("..."));
                // Assert that the "see more" label is visible
                assertTrue(seeMoreLabel.isVisible());
            });
        }
}