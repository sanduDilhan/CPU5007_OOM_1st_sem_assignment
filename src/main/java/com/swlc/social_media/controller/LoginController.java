package com.swlc.social_media.controller;
import com.jfoenix.controls.JFXButton;
import com.swlc.social_media.dto.ChannelDTO;
import com.swlc.social_media.entity.ChannelEntity;
import com.swlc.social_media.model.ChannelModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.modelmapper.ModelMapper;

import java.io.IOException;

public class LoginController {

    public TextField txtChannelName;
    public AnchorPane loginPnl;
    public TextField txtPasswordField;
    public static Stage primaryStage;
    public static ChannelEntity currentLoggedChannel;
    static ModelMapper mapper = new ModelMapper();
    public Button btnLogin;
    public JFXButton btnGoRegisterOnAction;
    public static ChannelDTO getCurrentLoggedChannel() {
        return mapper.map(currentLoggedChannel,ChannelDTO.class);
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public ChannelModel channelModel = new ChannelModel();

    public void onClickBtnLogin(ActionEvent actionEvent) {
        String channelName = txtChannelName.getText();
        String password = txtPasswordField.getText();

        currentLoggedChannel = channelModel.login(channelName, password);

        if (currentLoggedChannel != null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
                primaryStage.setScene(new Scene(root, 850, 550));
                primaryStage.show();
                primaryStage.centerOnScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "incorrect username or password").show();
        }
    }

    public void btnGoRegisterOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            Pane registerPane = loader.load();

            loginPnl.getChildren().clear();
            loginPnl.getChildren().add(registerPane);

            AnchorPane.setTopAnchor(registerPane, 0.0);
            AnchorPane.setBottomAnchor(registerPane, 0.0);
            AnchorPane.setLeftAnchor(registerPane, 0.0);
            AnchorPane.setRightAnchor(registerPane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
