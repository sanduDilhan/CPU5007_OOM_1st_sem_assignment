package com.swlc.social_media.utill.observer;

import com.swlc.social_media.dto.PostDTO;

import java.util.List;

public interface Observer {
    void updatePosts(List<PostDTO> data);
}
