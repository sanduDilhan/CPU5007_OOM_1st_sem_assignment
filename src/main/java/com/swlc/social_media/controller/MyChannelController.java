package com.swlc.social_media.controller;

import com.swlc.social_media.dto.PostDTO;
import com.swlc.social_media.model.PostModel;
import com.swlc.social_media.utill.DateFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MyChannelController {

    public ImageView userImageView;
    public Label userNameLabel;
    public Button createPostButton;
    @FXML
    private VBox postContainer;
    PostModel postModel = new PostModel();
    @FXML
    public void initialize() {
        if (postContainer == null) {
            return;
        }
        setImageToImageView(postModel.getProPicByChannelId(LoginController.loggedChannel.getChannelId()));
        showAllPosts();
    }

    private void setImageToImageView(String imagePath) {
        try {
            Image image = new Image("upload/" + imagePath);
            userImageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAllPosts() {
        postContainer.getChildren().clear();
        List<PostDTO> posts = postModel.getPostsByChannelId(LoginController.getLoggedChannel().getChannelId());
        userNameLabel.setText(postModel.getNameByChannelId(LoginController.getLoggedChannel().getChannelId()));

        for (PostDTO post : posts) {
            addPost(post.getChannel().getLogo(), post.getChannel().getChannelName(),
                    post.getDescription(), post.getImageName(),
                    DateFormatter.dateFormatter(post.getCreatedDate()));
        }
    }

    // Method to dynamically add a post to the VBox
    public void addPost(String propic_name, String userName, String description, String image_name, String postDate) {
        // Main AnchorPane for each post
        AnchorPane postPane = new AnchorPane();
        postPane.getStyleClass().add("post-container");
        postPane.setPrefWidth(350);

        // Header Section (Profile Image, Username, and Date)
        ImageView profileImage = new ImageView(new Image(getClass().getResourceAsStream("/upload/" + propic_name)));
        profileImage.setFitWidth(40.0);
        profileImage.setFitHeight(40.0);
        AnchorPane.setTopAnchor(profileImage, 10.0);
        AnchorPane.setLeftAnchor(profileImage, 10.0);

        Label profileNameLabel = new Label(userName);
        profileNameLabel.getStyleClass().add("post-profile-name");
        AnchorPane.setTopAnchor(profileNameLabel, 10.0);
        AnchorPane.setLeftAnchor(profileNameLabel, 60.0);

        Label dateLabel = new Label(postDate);
        dateLabel.getStyleClass().add("post-date");
        AnchorPane.setTopAnchor(dateLabel, 30.0);
        AnchorPane.setLeftAnchor(dateLabel, 60.0);

        Label descriptionLabel = new Label();
        Label seeMoreLabel = new Label(" see more...");
        seeMoreLabel.getStyleClass().add("see-more");
        Label seeLessLabel = new Label(" see less...");
        seeLessLabel.getStyleClass().add("see-more");

        VBox descriptionAndImageContainer = new VBox(10); // Space between elements
        descriptionAndImageContainer.setAlignment(Pos.CENTER_LEFT);
        descriptionAndImageContainer.setPadding(new Insets(10, 0, 0, 20));

        ImageView postImage = new ImageView(new Image(getClass().getResourceAsStream("/upload/" + image_name)));
        postImage.setFitWidth(300.0);
        postImage.setPreserveRatio(true);

        if (description.length() > 30) {
            descriptionLabel.setText(description.substring(0, 30) + "...");
            seeMoreLabel.setOnMouseClicked(event -> {
                descriptionLabel.setText(description);
                descriptionAndImageContainer.getChildren().removeAll(seeMoreLabel, postImage);
                descriptionAndImageContainer.getChildren().addAll(seeLessLabel, postImage);
            });
            seeLessLabel.setOnMouseClicked(event -> {
                descriptionLabel.setText(description.substring(0, 30) + "...");
                descriptionAndImageContainer.getChildren().removeAll(seeLessLabel, postImage);
                descriptionAndImageContainer.getChildren().addAll(seeMoreLabel, postImage);
            });
        } else {
            descriptionLabel.setText(description);
        }

        descriptionLabel.setWrapText(true);
        descriptionLabel.setPrefWidth(300.0);
        descriptionLabel.getStyleClass().add("post-description");

        if (description.length() > 30) {
            descriptionAndImageContainer.getChildren().addAll(descriptionLabel, seeMoreLabel, postImage);
        } else {
            descriptionAndImageContainer.getChildren().addAll(descriptionLabel, postImage);
        }

        AnchorPane.setTopAnchor(descriptionAndImageContainer, 60.0);

        postPane.getChildren().addAll(profileImage, profileNameLabel, dateLabel, descriptionAndImageContainer);

        HBox postContainerWrapper = new HBox(postPane);
        postContainerWrapper.setAlignment(Pos.CENTER);
        postContainerWrapper.setPadding(new Insets(10));

        // Add to main container VBox
        postContainer.getChildren().add(postContainerWrapper);
        postContainer.setAlignment(Pos.CENTER);
    }

    public void CreatePostOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/createPost.fxml"));
            Parent popupContent = fxmlLoader.load();

            CreatePostController createPostController = fxmlLoader.getController();

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(popupContent));
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Create Post");
            popupStage.setWidth(600);
            popupStage.setHeight(450);

            CreatePostController.createPostStage = popupStage;

            popupStage.showAndWait();

            if (createPostController.isPostCreated()) {
                showAllPosts();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
