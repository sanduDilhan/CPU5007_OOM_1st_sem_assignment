package com.swlc.social_media.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDTO {
    private String postId;
    private ChannelDTO channel;
    private String description;
    private String imageName;
    private LocalDateTime createdDate;

    public PostDTO(ChannelDTO channelDTO, String description, String imageName) {
        this.channel = channelDTO;
        this.description = description;
        this.imageName = imageName;
    }
}
