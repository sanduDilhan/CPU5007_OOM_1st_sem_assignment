package com.swlc.social_media.controller;

import com.swlc.social_media.dto.PostDTO;
import com.swlc.social_media.dto.ResponseDTO;
import com.swlc.social_media.model.PostModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class CreatePostController {

    @FXML
    private ImageView imgView;
    @FXML
    private TextArea txtAreaDescription;
    private File selectedPostImage;
    PostModel postModel = new PostModel();
    private boolean isPostCreated = false;
    public static Stage postCreterStage;

    @FXML
    private void selectImgOnAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(imgView.getScene().getWindow());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imgView.setImage(image);
            selectedPostImage = selectedFile;
        }
    }

    @FXML
    public void makePostOnAction() {
        if (selectedPostImage == null) {
            showAlert(new ResponseDTO("400", "Please select an image before posting.",null));
            return;
        }

        String description = txtAreaDescription.getText();
        PostDTO postDTO = new PostDTO(LoginController.getCurrentLoggedChannel(), description, selectedPostImage.getName());
        ResponseDTO response = postModel.savePost(postDTO, selectedPostImage);
        showAlert(response);

        if ("200".equals(response.getStatus())) {
            isPostCreated = true;
            postCreterStage.close();
        }
    }

    public void showAlert(ResponseDTO response) {
        Alert alert;
        if ("200".equals(response.getStatus())) {
            alert = createAlert(Alert.AlertType.CONFIRMATION, response.getMessage());
        } else {
            alert = createAlert(Alert.AlertType.ERROR, response.getMessage());
        }
        alert.show();
    }

    public Alert createAlert(Alert.AlertType alertType, String message) {
        return new Alert(alertType, message);
    }

    public boolean isPostCreated() {
        return isPostCreated;
    }
}
