package com.swlc.social_media.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "channel")
@AllArgsConstructor
@NoArgsConstructor

public class ChannelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long channelId;
    private String channelName;
    private String password;
    private  String logo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "channel_subscriptions",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "subscribed_channel_id")
    )
    private Set<ChannelEntity> subscribedChannels = new HashSet<>();

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

    public Set<ChannelEntity> getSubscribedChannels() {
        return subscribedChannels;
    }

    public void setSubscribedChannels(Set<ChannelEntity> subscribedChannels) {
        this.subscribedChannels = subscribedChannels;
    }

    @Override
    public String toString() {
        return "ChannelEntity{" +
                "channelId=" + channelId +
                ", channelName='" + channelName + '\'' +
                ", password='" + password + '\'' +
                ", logo='" + logo + '\'' +
                ", subscribedChannels=" + subscribedChannels +
                '}';
    }

    public ChannelEntity(String channelName, String password, String logo) {
        this.channelName = channelName;
        this.password = password;
        this.logo = logo;
    }
}
