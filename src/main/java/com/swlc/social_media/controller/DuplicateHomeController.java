package com.swlc.social_media.controller;

import com.swlc.social_media.dto.ChannelDTO;
import com.swlc.social_media.dto.PostDTO;
import com.swlc.social_media.model.ChannelModel;
import com.swlc.social_media.model.PostModel;
import com.swlc.social_media.utill.DateFormatter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class DuplicateHomeController {
    public VBox postVbox;
    public AnchorPane dynamicPnl;
    PostModel postModel = new PostModel();
    OtherChannelController othersChannelController = new OtherChannelController();

    ChannelModel channelModel = new ChannelModel();

    @FXML
    public void initialize() {
        if (postVbox == null) {
            return;
        }

        // Retrieve and display subscribed channels' posts
        ChannelDTO subscribedChannelsArray = channelModel.getSubscribedChannelsByChannelId(LoginController.currentLoggedChannel.getChannelId());
        for (ChannelDTO subscribedChannel : subscribedChannelsArray.getSubscribedChannels()) {
            for (PostDTO post : postModel.getPostsByChannelId(subscribedChannel.getChannelId())) {
                addPost(post.getChannel().getLogo(), post.getChannel().getChannelName(),
                        post.getDescription(), post.getImageName(),
                        DateFormatter.dateFormatter(post.getCreatedDate()),
                        post.getChannel().getChannelId());
            }
        }

        // Retrieve and display subscribed my posts
        List<PostDTO> posts = postModel.getPostsByChannelId(LoginController.getCurrentLoggedChannel().getChannelId());
        for (PostDTO post : posts) {
            addPost(post.getChannel().getLogo(), post.getChannel().getChannelName(),
                    post.getDescription(), post.getImageName(),
                    DateFormatter.dateFormatter(post.getCreatedDate()),
                    post.getChannel().getChannelId());
        }
    }

    // Method to dynamically add a post to the VBox
    public void addPost(String propic_name, String userName, String description, String image_name, String postDate, Long channelId) {
        AnchorPane postPane = new AnchorPane();
        postPane.setId("postContainer");
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

        VBox descriptionAndImageContainer = new VBox(10); // Space between elements
        descriptionAndImageContainer.setAlignment(Pos.CENTER_LEFT);
        descriptionAndImageContainer.setPadding(new Insets(10, 0, 0, 20));

        // Post Image
        ImageView postImage = new ImageView(new Image(getClass().getResourceAsStream("/upload/" + image_name)));
        postImage.setFitWidth(300.0);
        postImage.setPreserveRatio(true);

        postPane.setOnMouseClicked(event -> {
            try {
                Pane pane;
                if (channelId == LoginController.currentLoggedChannel.getChannelId()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/my_channel.fxml"));
                    pane = loader.load();
                } else {
                    othersChannelController.setOtherChannelId(channelId);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/other_channel.fxml"));
                    pane = loader.load();
                }
                dynamicPnl.getChildren().clear();
                dynamicPnl.getChildren().add(pane);

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

        // Add to VBox based on description length
        if (description.length() > 30) {
            descriptionAndImageContainer.getChildren().addAll(descriptionLabel, seeMoreLabel, postImage);
        } else {
            descriptionAndImageContainer.getChildren().addAll(descriptionLabel, postImage);
        }

        AnchorPane.setTopAnchor(descriptionAndImageContainer, 60.0);

        postPane.getChildren().addAll(profileImage, profileNameLabel, dateLabel, descriptionAndImageContainer);

        // Wrap postPane in HBox to center horizontally
        HBox postContainerWrapper = new HBox(postPane);
        postContainerWrapper.setAlignment(Pos.CENTER);
        postContainerWrapper.setPadding(new Insets(10));

        // Add to main container VBox
        postVbox.getChildren().add(postContainerWrapper);
        postVbox.setAlignment(Pos.CENTER);
    }
}
