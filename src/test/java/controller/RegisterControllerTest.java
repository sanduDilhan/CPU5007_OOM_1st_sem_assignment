package controller;

import com.swlc.social_media.controller.RegisterController;
import com.swlc.social_media.dto.ChannelDTO;
import com.swlc.social_media.model.ChannelModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterControllerTest extends ApplicationTest {

    @Mock
    private ChannelModel mockChannelModel;

    @InjectMocks
    private RegisterController registerController;

    @Mock
    private TextField txtChannelName;

    @Mock
    private TextField password;

    @Mock
    private Pane registerPane;

    private File mockFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockFile = mock(File.class);
    }

    @Test
    void testValidationFailure() {
        Platform.runLater(() -> {
            when(txtChannelName.getText()).thenReturn(null);
            when(password.getText()).thenReturn(null);

            registerController.btnCreateAccountOnAction(mock(ActionEvent.class));

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("please fill all field and select channel picture.");
            assertEquals("please fill all field and select channel picture.", alert.getContentText());
            verifyNoInteractions(mockChannelModel);
        });
    }

    @Test
    void testSuccessfulRegistration() throws IOException {
            when(txtChannelName.getText()).thenReturn("kushan");
        when(password.getText()).thenReturn("1234");
        when(mockFile.getName()).thenReturn("user.jpg");
        registerController.selectedFile = mockFile;

        when(mockChannelModel.registerChannel(any(ChannelDTO.class), eq(mockFile)))
                .thenReturn(new ChannelDTO("kushan", "1234", "user.jpg"));

        FXMLLoader loader = mock(FXMLLoader.class);
        Pane mockLoginPane = mock(Pane.class);
            try {
                when(loader.load()).thenReturn(mockLoginPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            registerController.btnCreateAccountOnAction(mock(ActionEvent.class));

        verify(mockChannelModel, times(1)).registerChannel(any(ChannelDTO.class), eq(mockFile));
        verify(registerPane.getChildren(), times(1)).clear();
        verify(registerPane.getChildren(), times(1)).add(mockLoginPane);
    }

    @Test
    void testRegistrationFailure() {
        Platform.runLater(() -> {
            when(txtChannelName.getText()).thenReturn("TestChannel");
            when(password.getText()).thenReturn("TestPassword");
            when(mockFile.getName()).thenReturn("user.jpg");
            registerController.selectedFile = mockFile;

            when(mockChannelModel.registerChannel(any(ChannelDTO.class), eq(mockFile))).thenReturn(null);

            registerController.btnCreateAccountOnAction(mock(ActionEvent.class));

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("registration failed!");
            assertEquals("registration failed!", alert.getContentText());

            verifyNoInteractions(registerPane.getChildren());
        });
    }
}
