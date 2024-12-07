package com.swlc.social_media.controller;

import com.swlc.social_media.dto.ChannelDTO;
import com.swlc.social_media.model.ChannelModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class RegisterController {
    public ImageView profilePicture;
    public ImageView cameraImgView;
    public TextField txtChannelName;
    public TextField txtPassword;
    public AnchorPane registerPnl;
    ChannelModel channelModel = new ChannelModel();
    public static Stage primaryStage;

    public File selectedFile = null;

    public void handleImageClick(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        fileChooser.setTitle("Select Profile Image");

        selectedFile = fileChooser.showOpenDialog(cameraImgView.getScene().getWindow());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            profilePicture.setImage(image);
        }
    }

    public void btnCreateAccountOnAction(ActionEvent actionEvent) {
        if(txtChannelName.getText() == null || txtPassword.getText() == null || selectedFile == null) {
            new Alert(Alert.AlertType.ERROR, "please fill all field and select channel picture.").show();
            return;
        }
        ChannelDTO new_channel = channelModel.registerChannel(new ChannelDTO(txtChannelName.getText(), txtPassword.getText(), selectedFile.getName()), selectedFile);
        if (new_channel != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
                Pane loginPane = loader.load();
                registerPnl.getChildren().clear();
                registerPnl.getChildren().add(loginPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "registration failed!").show();
        }
    }

    public void btnGoLoginOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Pane loginPane = loader.load();

            LoginController loginController = loader.getController();
            loginController.setPrimaryStage(primaryStage); // Pass the primary stage

            registerPnl.getChildren().clear();
            registerPnl.getChildren().add(loginPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
