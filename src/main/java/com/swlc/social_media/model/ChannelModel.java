package com.swlc.social_media.model;

import com.swlc.social_media.dto.ChannelDTO;
import com.swlc.social_media.dto.ResponseDTO;
import com.swlc.social_media.entity.ChannelEntity;
import com.swlc.social_media.utill.FactoryConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class ChannelModel {

    ModelMapper mapper = new ModelMapper();

    public ChannelEntity login(String channel_name, String password) {
        Session session = FactoryConfiguration.getFactoryConfiguration().getSession();
        Transaction transaction = null;
        transaction = session.beginTransaction();
        // Query to find the user by channel name and password
        Query<ChannelEntity> query = session.createQuery("FROM ChannelEntity e WHERE e.channelName = :channel_name AND e.password = :password", ChannelEntity.class);
        query.setParameter("channel_name", channel_name);
        query.setParameter("password", password);

        ChannelEntity channelEntity = query.uniqueResult();

        transaction.commit();
        session.close();

        return channelEntity;
    }

    public ResponseDTO updateProfilePicture(Long channelId, File propic) {
        String targetPath = "src/main/resources/upload/" + propic.getName();
        File targetFile = new File(targetPath);

        Session session = FactoryConfiguration.getFactoryConfiguration().getSession();
        Transaction transaction = null;

        try {
            ChannelEntity channel = session.get(ChannelEntity.class, channelId);
            channel.setLogo(propic.getName());

            transaction = session.beginTransaction();
            transaction.commit();
            // Copy the image to the upload directory
            Files.copy(propic.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return new ResponseDTO("200", "profile picture updated", channel);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseDTO("400", "update failed!: " + e.getMessage(), null);
        } finally {
            session.close();
        }
    }

    public void subscribeChannel(ChannelDTO subscriberChannel, ChannelDTO channelToSubscribe) {
        Session session = FactoryConfiguration.getFactoryConfiguration().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            ChannelEntity parentChannel = session.get(ChannelEntity.class, subscriberChannel.getChannelId());

            parentChannel.getSubscribedChannels().add(mapper.map(channelToSubscribe, ChannelEntity.class));
            session.persist(parentChannel);
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void unSubscribeChannel(ChannelDTO subscriberChannel, ChannelDTO channelToUnsubscribe) {
        Session session = FactoryConfiguration.getFactoryConfiguration().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            // Fetch the parent channel and the target subscribed channel using their IDs
            ChannelEntity parentChannel = session.get(ChannelEntity.class, subscriberChannel.getChannelId());
            ChannelEntity channelEntityToRemove = session.get(ChannelEntity.class, channelToUnsubscribe.getChannelId());

            // Check if the channelEntityToRemove exists in the subscribed channels set
            if (parentChannel.getSubscribedChannels().contains(channelEntityToRemove)) {
                // Remove the exact entity instance from the set
                parentChannel.getSubscribedChannels().remove(channelEntityToRemove);
                session.update(parentChannel); // Update the parent channel with the modified set
                transaction.commit();
            } else {
                System.out.println("Channel not found in subscribed channels.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    public List<ChannelDTO> getAllChannel() {
        List<ChannelDTO> channelDTOS = null;

        Session session = FactoryConfiguration.getFactoryConfiguration().getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            jakarta.persistence.Query query = session.createQuery("from ChannelEntity ", ChannelEntity.class);
            List<ChannelEntity> channels = query.getResultList();
            transaction.commit();
            channelDTOS = mapper.map(channels, new TypeToken<List<ChannelDTO>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return channelDTOS;
    }

    public ChannelDTO getSubscribedChannelsByChannelId(Long channelId) {
        ChannelEntity channelEntity = null;
        Session session = FactoryConfiguration.getFactoryConfiguration().getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            channelEntity = session.get(ChannelEntity.class, channelId);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return mapper.map(channelEntity, ChannelDTO.class);
    }

    public ChannelDTO registerChannel(ChannelDTO channel, File selected_file) {
        System.out.println(channel);
        System.out.println(selected_file);
        String targetPath = "src/main/resources/upload/" + channel.getLogo();
        File targetFile = new File(targetPath);

        Session session = null;

        try {
            session = FactoryConfiguration.getFactoryConfiguration().getSession();
            Transaction transaction = session.beginTransaction();
            ChannelEntity new_channel = new ChannelEntity(channel.getChannelName(), channel.getPassword(), channel.getLogo());
            session.persist(new_channel);
            transaction.commit();
            Files.copy(selected_file.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            ChannelDTO channelDTO = mapper.map(new_channel, ChannelDTO.class);
            return channelDTO;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }
}
