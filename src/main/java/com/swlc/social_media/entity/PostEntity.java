package com.swlc.social_media.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channelId", referencedColumnName = "channelId")
    private ChannelEntity channel;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String imageName;
    private LocalDateTime createdDate;
}
