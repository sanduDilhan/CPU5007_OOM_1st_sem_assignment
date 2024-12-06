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
    private ImageView imageView;
    @FXML
    private TextArea textAreaDescription; //
    private File selectedImage;
    PostModel postModel = new PostModel();
    private boolean postCreated = false;
    public static Stage createPostStage;

    @FXML
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
            selectedImage = selectedFile;
        }
    }

    @FXML
    private void createPostOnAction() {
        String description = textAreaDescription.getText();
        ResponseDTO response = postModel.savePost(new PostDTO(LoginController.getLoggedChannel(), description, selectedImage.getName()), selectedImage);
        if (response.getStatus().equals("200")) {
            new Alert(Alert.AlertType.CONFIRMATION, response.getMessage()).show();
        } else {
            new Alert(Alert.AlertType.ERROR, response.getMessage()).show();
        }
        postCreated = true;
        createPostStage.close();
    }

    public boolean isPostCreated() {
        return postCreated;
    }
}
