package com.swlc.social_media.model;

import com.swlc.social_media.dto.PostDTO;
import com.swlc.social_media.dto.ResponseDTO;
import com.swlc.social_media.entity.ChannelEntity;
import com.swlc.social_media.entity.PostEntity;
import com.swlc.social_media.utill.FactoryConfiguration;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

public class PostModel {
    ModelMapper modelMapper = new ModelMapper();

    public ResponseDTO savePost(PostDTO postDTO, File selectedImage) {
        Session session = FactoryConfiguration.getFactoryConfiguration().getSession();
        String targetPath = "src/main/resources/upload/" + postDTO.getImageName();
        File targetFile = new File(targetPath);

        try {
            PostEntity postEntity = new PostEntity();
            postEntity.setChannel(modelMapper.map(postDTO.getChannel(), ChannelEntity.class));
            postEntity.setDescription(postDTO.getDescription());
            postEntity.setImageName(postDTO.getImageName());
            postEntity.setCreatedDate(LocalDateTime.now());

            // Save the post to the database
            Transaction transaction = session.beginTransaction();
            session.persist(postEntity);
            transaction.commit();
            // Copy the image to the upload directory
            Files.copy(selectedImage.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return new ResponseDTO("200", "Post saved successfully", postEntity);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseDTO("400", "Failed to save post: " + e.getMessage(), null);
        } finally {
            session.close();
        }
    }

    public List<PostDTO> getAllPost() {
        Session session = FactoryConfiguration.getFactoryConfiguration().getSession();
        List<PostDTO> postDTOS = null;
        try {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("from PostEntity ", PostEntity.class);
            List<PostEntity> posts = query.getResultList();
            transaction.commit();
            postDTOS = modelMapper.map(posts, new TypeToken<List<PostDTO>>() {
            }.getType());
            System.out.println(postDTOS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return postDTOS;
    }

    public List<PostDTO> getPostsByChannelId(Long channelId) {
        Session session = FactoryConfiguration.getFactoryConfiguration().getSession();
        try {
            Transaction transaction = session.beginTransaction();
            String hql = "FROM PostEntity p WHERE p.channel.channelId = :channelId";
            Query query = session.createQuery(hql, PostEntity.class);
            query.setParameter("channelId", channelId);
            List<PostEntity> postList = query.getResultList();
            List<PostDTO> postDtoList = modelMapper.map(postList, new TypeToken<List<PostDTO>>() {
            }.getType());
            transaction.commit();
            return postDtoList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            session.close();
        }
    }

    public String getNameByChannelId(Long channelId) {
        Session session = FactoryConfiguration.getFactoryConfiguration().getSession();
        try {
            Transaction transaction = session.beginTransaction();
            ChannelEntity channelEntity = session.get(ChannelEntity.class, channelId);;
            transaction.commit();
            return channelEntity.getChannelName();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            session.close();
        }
    }

    public String getProPicByChannelId(Long channelId) {
        Session session = FactoryConfiguration.getFactoryConfiguration().getSession();
        try {
            Transaction transaction = session.beginTransaction();
            ChannelEntity channelEntity = session.get(ChannelEntity.class, channelId);
            transaction.commit();
            return channelEntity.getLogo();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            session.close();
        }
    }
}
