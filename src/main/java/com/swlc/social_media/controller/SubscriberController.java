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

    public FlowPane postPnl;
    public AnchorPane dynamicPnl;
    public Button subscribedChannelBtn;
    public Button forSubChannelBtn;
    ChannelModel channelModel = new ChannelModel();

    public String subBtnAction = "unsub";
    OtherChannelController othersChannelController = new OtherChannelController();

    @FXML
    public void initialize() {
        if (postPnl == null) {
            return;
        }
        subscribedChannelBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;"); // Green color for active button
        forSubChannelBtn.setStyle("-fx-background-color: white; -fx-text-fill: black;"); // Default color for inactive button
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
                        LoginController.getCurrentLoggedChannel(),
                        new ChannelDTO(channelId, channelName)
                );
                displayUnSubChannels();
            } else {
                channelModel.unSubscribeChannel(
                        LoginController.getCurrentLoggedChannel(),
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
                othersChannelController.setOtherChannelId(channelId);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/other_channel.fxml"));
                Pane channelPane = loader.load();

                dynamicPnl.getChildren().clear();
                dynamicPnl.getChildren().add(channelPane);

                AnchorPane.setTopAnchor(channelPane, 0.0);
                AnchorPane.setBottomAnchor(channelPane, 0.0);
                AnchorPane.setLeftAnchor(channelPane, 0.0);
                AnchorPane.setRightAnchor(channelPane, 0.0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        postPnl.getChildren().add(postBox);
    }

    public void onClickSubscribedChannelBtn(ActionEvent actionEvent) {
        subBtnAction = "unsub";
        subscribedChannelBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;"); // Green color for active button
        forSubChannelBtn.setStyle("-fx-background-color: white; -fx-text-fill: black;"); // Default color for inactive button
        postPnl.getChildren().clear();
        ChannelDTO channelDTOArray = channelModel.getSubscribedChannelsByChannelId(LoginController.currentLoggedChannel.getChannelId());
        for (ChannelDTO channel : channelDTOArray.getSubscribedChannels()) {
            addPost(channel.getChannelName(), channel.getLogo(), channel.getChannelId());
        }
    }

    public void onClickForSubscribeChannelBtn(ActionEvent actionEvent) {
        subBtnAction = "sub";
        subscribedChannelBtn.setStyle("-fx-background-color: white; -fx-text-fill: black;"); // Green color for active button
        forSubChannelBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;"); //
        postPnl.getChildren().clear();
        displayUnSubChannels();
    }

    public void displaySubChannels() {
        postPnl.getChildren().clear();
        ChannelDTO channelDTOArray = channelModel.getSubscribedChannelsByChannelId(LoginController.currentLoggedChannel.getChannelId());
        for (ChannelDTO channel : channelDTOArray.getSubscribedChannels()) {
            addPost(channel.getChannelName(), channel.getLogo(), channel.getChannelId());
        }
    }

    public void displayUnSubChannels() {
        List<ChannelDTO> allChannels = channelModel.getAllChannel();
        ChannelDTO channelDTOArray = channelModel.getSubscribedChannelsByChannelId(LoginController.currentLoggedChannel.getChannelId());

        Set<Long> subscribedChannelIds = new HashSet<>();

        for (ChannelDTO subscribedChannel : channelDTOArray.getSubscribedChannels()) {
            subscribedChannelIds.add(subscribedChannel.getChannelId());
        }
        postPnl.getChildren().clear();
        for (ChannelDTO channel : allChannels) {
            if (channel.getChannelId() != LoginController.getCurrentLoggedChannel().getChannelId() && !subscribedChannelIds.contains(channel.getChannelId())) {
                addPost(channel.getChannelName(), channel.getLogo(), channel.getChannelId());
            }
        }
    }
}

   