package com.swlc.social_media.controller;

import com.swlc.social_media.dto.PostDTO;
import com.swlc.social_media.dto.ResponseDTO;
import com.swlc.social_media.model.ChannelModel;
import com.swlc.social_media.model.PostModel;
import com.swlc.social_media.utill.DateFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HomeController {

    public AnchorPane dynamicPane;
    public AnchorPane mainPanel;
    public Label channelName;
    public ImageView cameraView;
    public ImageView propic;
    public AnchorPane sidePanel;
    public Button btnNewsFeed;
    public Button btnLogout;
    public Button btnChannel;
    @FXML
    private VBox postContainer;

    PostModel postModel = new PostModel();
    ChannelModel channelModel = new ChannelModel();
    OtherChannelController otherChannelController = new OtherChannelController();

    @FXML
    public void initialize() {

        if (postContainer == null) {
            return;
        }

        if (!LoginController.getLoggedChannel().getLogo().isEmpty()) {
            propic.setImage(new Image(getClass().getResourceAsStream("/upload/" + LoginController.getLoggedChannel().getLogo())));
        }

        // Set the channel name
        channelName.setText(LoginController.getLoggedChannel().getChannelName());

        // Retrieve and display posts
        List<PostDTO> posts = postModel.getAllPost();
        for (PostDTO post : posts) {
            addPost(post.getChannel().getLogo(), post.getChannel().getChannelName(),
                    post.getDescription(), post.getImageName(),
                    DateFormatter.dateFormatter(post.getCreatedDate()),
                    post.getChannel().getChannelId());
        }

        cameraView.setOnMouseClicked(event -> openFileChooser());
        propic.setOnMouseClicked(this::handleImageClick);
        setButtonSelected(btnNewsFeed);
    }

    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File file = fileChooser.showOpenDialog(cameraView.getScene().getWindow());
        if (file == null) {
            return;
        }

        ResponseDTO responseDTO = channelModel.updateProfilePicture(LoginController.loggedChannel.getChannelId(), file);

        if (responseDTO.getObj() != null) {
            propic.setImage(new Image(file.toURI().toString()));
        }
    }

    // Method to dynamically add a post to the VBox
    public void addPost(String propic_name, String userName, String description, String image_name, String postDate, Long channelId) {
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

        // Description and "see more..." section
        Label descriptionLabel = new Label();
        Label seeMoreLabel = new Label(" see more...");
        seeMoreLabel.getStyleClass().add("see-more");
        Label seeLessLabel = new Label(" see less...");
        seeLessLabel.getStyleClass().add("see-more");

        // VBox for description and image, with top padding for alignment
        VBox descriptionAndImageContainer = new VBox(10); // Space between elements
        descriptionAndImageContainer.setAlignment(Pos.CENTER_LEFT); // Aligns items to left side
        descriptionAndImageContainer.setPadding(new Insets(10, 0, 0, 20)); // Adds left padding

        // Post Image
        ImageView postImage = new ImageView(new Image(getClass().getResourceAsStream("/upload/" + image_name)));
        postImage.setFitWidth(300.0);
        postImage.setPreserveRatio(true);

        postPane.setOnMouseClicked(event -> {
            try {
                Pane pane;
                if(channelId == LoginController.loggedChannel.getChannelId()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/my_channel.fxml"));
                    pane = loader.load();
                } else {
                    otherChannelController.setOtherChannelId(channelId);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/other_channel.fxml"));
                    pane = loader.load();
                }
                dynamicPane.getChildren().clear();
                dynamicPane.getChildren().add(pane);

                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setBottomAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Truncate description
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

    public void btnHomeOnAction(ActionEvent actionEvent) {
        try {
            setButtonSelected(btnNewsFeed);
            // Load the FXML for the channel page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home_duplicate.fxml"));
            Pane channelPane = loader.load();

            dynamicPane.getChildren().clear();
            dynamicPane.getChildren().add(channelPane);

            AnchorPane.setTopAnchor(channelPane, 0.0);
            AnchorPane.setBottomAnchor(channelPane, 0.0);
            AnchorPane.setLeftAnchor(channelPane, 0.0);
            AnchorPane.setRightAnchor(channelPane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnChannelOnAction(ActionEvent actionEvent) {
        try {
            setButtonSelected(btnChannel);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/subscriber.fxml"));
            Pane channelPane = loader.load();

            dynamicPane.getChildren().clear();
            dynamicPane.getChildren().add(channelPane);

            AnchorPane.setTopAnchor(channelPane, 0.0);
            AnchorPane.setBottomAnchor(channelPane, 0.0);
            AnchorPane.setLeftAnchor(channelPane, 0.0);
            AnchorPane.setRightAnchor(channelPane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleImageClick(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/my_channel.fxml"));
            Pane channelPane = loader.load();

            dynamicPane.getChildren().clear();
            dynamicPane.getChildren().add(channelPane);

            AnchorPane.setTopAnchor(channelPane, 0.0);
            AnchorPane.setBottomAnchor(channelPane, 0.0);
            AnchorPane.setLeftAnchor(channelPane, 0.0);
            AnchorPane.setRightAnchor(channelPane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) {
        try {
            setButtonSelected(btnLogout);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            AnchorPane loginPane = loader.load();

            Stage currentStage = (Stage) mainPanel.getScene().getWindow();

            currentStage.setScene(new Scene(loginPane));
            currentStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setButtonSelected(Button selectedButton) {
        btnNewsFeed.getStyleClass().remove("custom-button-selected");
        btnChannel.getStyleClass().remove("custom-button-selected");
        btnLogout.getStyleClass().remove("custom-button-selected");
        selectedButton.getStyleClass().add("custom-button-selected");
    }
}
