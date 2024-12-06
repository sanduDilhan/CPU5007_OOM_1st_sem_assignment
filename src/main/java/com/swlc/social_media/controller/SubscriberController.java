package com.swlc.social_media.controller;

import com.swlc.social_media.dto.ChannelDTO;
import com.swlc.social_media.model.ChannelModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubscriberController {

    public FlowPane postPane;
    public AnchorPane dynamicPane;
    public Button subscribedBtn;
    public Button forSubBtn;
    ChannelModel channelModel = new ChannelModel();

    String subBtnAction = "unsub";
    OtherChannelController otherChannelController = new OtherChannelController();

    @FXML
    public void initialize() {
        if (postPane == null) {
            return;
        }
        subscribedBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;"); // Green color for active button
        forSubBtn.setStyle("-fx-background-color: white; -fx-text-fill: black;"); // Default color for inactive button
        displaySubChannels();
    }

    public void addPost(String channelName, String imageName, Long channelId) {
        VBox postBox = new VBox(5);
        postBox.getStyleClass().add("post-container");
        postBox.setPrefWidth(200);

        // Post Image
        ImageView postImage = new ImageView(new Image(getClass().getResourceAsStream("/upload/" + imageName)));
        postImage.setFitWidth(180);
        postImage.setFitHeight(100);

        // Profile Name Label
        Label profileNameLabel = new Label(channelName);
        profileNameLabel.getStyleClass().add("post-profile-name");

        // Subscribe Button
        Button subscribeButton = new Button(subBtnAction.equals("sub") ? "subscribe" : "unsubscribe");
        subscribeButton.setId("btnSelectImage");
        subscribeButton.setOnAction(event -> {
            if (subBtnAction.equals("sub")) {
                channelModel.subscribeChannel(
                        LoginController.getLoggedChannel(),
                        new ChannelDTO(channelId, channelName)
                );
                displayUnSubChannels();
            } else {
                channelModel.unSubscribeChannel(
                        LoginController.getLoggedChannel(),
                        new ChannelDTO(channelId, channelName)
                );
                displaySubChannels();
            }
        });

        Label idLabel = new Label("" + channelId);
        idLabel.setVisible(false);
        postBox.getChildren().addAll(postImage, profileNameLabel, subscribeButton, idLabel);

        postBox.setOnMouseClicked(event -> {
            try {
                otherChannelController.setOtherChannelId(channelId);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/other_channel.fxml"));
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
        });
        postPane.getChildren().add(postBox);
    }

    public void onClickSubscribedBtn(ActionEvent actionEvent) {
        subBtnAction = "unsub";
        subscribedBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;"); // Green color for active button
        forSubBtn.setStyle("-fx-background-color: white; -fx-text-fill: black;"); // Default color for inactive button
        postPane.getChildren().clear();
        ChannelDTO channelDTOArray = channelModel.getSubscribedChannelsByChannelId(LoginController.loggedChannel.getChannelId());
        for (ChannelDTO channel : channelDTOArray.getSubscribedChannels()) {
            addPost(channel.getChannelName(), channel.getLogo(), channel.getChannelId());
        }
    }

    public void onClickForSubscribeBtn(ActionEvent actionEvent) {
        subBtnAction = "sub";
        subscribedBtn.setStyle("-fx-background-color: white; -fx-text-fill: black;"); // Green color for active button
        forSubBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;"); //
        postPane.getChildren().clear();
        displayUnSubChannels();
    }

    public void displaySubChannels() {
        postPane.getChildren().clear();
        ChannelDTO channelDTOArray = channelModel.getSubscribedChannelsByChannelId(LoginController.loggedChannel.getChannelId());
        for (ChannelDTO channel : channelDTOArray.getSubscribedChannels()) {
            addPost(channel.getChannelName(), channel.getLogo(), channel.getChannelId());
        }
    }

    public void displayUnSubChannels() {
        List<ChannelDTO> allChannels = channelModel.getAllChannel();
        ChannelDTO channelDTOArray = channelModel.getSubscribedChannelsByChannelId(LoginController.loggedChannel.getChannelId());

        Set<Long> subscribedChannelIds = new HashSet<>();

        for (ChannelDTO subscribedChannel : channelDTOArray.getSubscribedChannels()) {
            subscribedChannelIds.add(subscribedChannel.getChannelId());
        }
        postPane.getChildren().clear();
        for (ChannelDTO channel : allChannels) {
            if (channel.getChannelId() != LoginController.getLoggedChannel().getChannelId() && !subscribedChannelIds.contains(channel.getChannelId())) {
                addPost(channel.getChannelName(), channel.getLogo(), channel.getChannelId());
            }
        }
    }
}

   