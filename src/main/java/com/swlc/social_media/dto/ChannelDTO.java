package com.swlc.social_media.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
public class ChannelDTO {
    private Long channelId;
    private String channelName;
    private String password;
    private  String logo;
    private Set<ChannelDTO> subscribedChannels = new HashSet<>();

    public ChannelDTO(Long channelId, String channelName, String logo) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.logo = logo;
    }

    public ChannelDTO(Long channelId, String channelName) {
        this.channelId = channelId;
        this.channelName = channelName;
    }

    public ChannelDTO(String channelName, String password, String logo) {
        this.channelName = channelName;
        this.password = password;
        this.logo = logo;
    }

    public ChannelDTO(Long otherChannelId) {
        this.channelId = otherChannelId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<ChannelDTO> getSubscribedChannels() {
        return subscribedChannels;
    }

    public void setSubscribedChannels(Set<ChannelDTO> subscribedChannels) {
        this.subscribedChannels = subscribedChannels;
    }
}
