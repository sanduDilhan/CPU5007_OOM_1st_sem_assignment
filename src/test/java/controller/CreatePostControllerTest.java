package controller;

import com.swlc.social_media.controller.CreatePostController;
import com.swlc.social_media.dto.ResponseDTO;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CreatePostControllerTest extends ApplicationTest {
    private final CreatePostController alertService =Mockito.spy(new CreatePostController());

    @Test
    void testPostSavedConfirmationAlert() {
        // Arrange
        ResponseDTO mockResponse = new ResponseDTO();
        mockResponse.setStatus("200");
        mockResponse.setMessage("Success");

        // Mock the Alert class
        Alert mockAlert = Mockito.mock(Alert.class);
        doReturn(mockAlert).when(alertService).createAlert(any(Alert.AlertType.class), anyString());

        // Act
        alertService.showAlert(mockResponse);

        // Assert
        verify(alertService).createAlert(Alert.AlertType.CONFIRMATION, "Success");
        verify(mockAlert).show();
    }

    @Test
    void testPostSavedErrorAlert() {
        // Arrange
        ResponseDTO mockResponse = new ResponseDTO();
        mockResponse.setStatus("400");
        mockResponse.setMessage("Error");

        CreatePostController alertService = Mockito.spy(new CreatePostController());

        // Mock the Alert class
        Alert mockAlert = Mockito.mock(Alert.class);
        doReturn(mockAlert).when(alertService).createAlert(any(Alert.AlertType.class), anyString());

        // Act
        alertService.showAlert(mockResponse);

        // Assert
        verify(alertService).createAlert(Alert.AlertType.ERROR, "Error");
        verify(mockAlert).show();
    }
}
