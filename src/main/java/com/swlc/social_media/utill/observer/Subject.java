package com.swlc.social_media.utill.observer;

import com.swlc.social_media.controller.LoginController;
import com.swlc.social_media.dto.PostDTO;
import com.swlc.social_media.model.PostModel;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private List<Observer> observers = new ArrayList<>();
    private List<PostDTO> data = new ArrayList<>(); // Example database data
    private PostModel postModel = new PostModel();
    public Subject() {
        this.data =  postModel.getPostsByChannelId(LoginController.getCurrentLoggedChannel().getChannelId());
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.updatePosts(data);
        }
    }
    public void setData(List<PostDTO> newData) {
        this.data = newData;
        notifyObservers();
    }

    public void addData(PostDTO newItem) {
        this.data.add(newItem);
        notifyObservers();
    }
}