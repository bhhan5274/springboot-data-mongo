package com.bhhan.mongo.file.service;

import com.bhhan.mongo.file.model.Video;
import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by hbh5274@gmail.com on 2020-09-27
 * Github : http://github.com/bhhan5274
 */

@Service
@RequiredArgsConstructor
public class VideoService {
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations gridFsOperations;

    public Video getVideo(String id) throws IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        Video video = new Video();
        video.setTitle(file.getMetadata().get("title").toString());
        video.setStream(gridFsOperations.getResource(file).getInputStream());
        return video;
    }

    public String addVideo(String title, MultipartFile file) throws IOException {
        BasicDBObject metaData = new BasicDBObject();
        metaData.put("type", "video");
        metaData.put("title", title);
        ObjectId id = gridFsTemplate.store(file.getInputStream(), file.getName(), file.getContentType(), metaData);
        return id.toString();
    }
}
