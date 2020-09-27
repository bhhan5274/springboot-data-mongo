package com.bhhan.mongo.file.service;

import com.bhhan.mongo.file.model.Photo;
import com.bhhan.mongo.file.model.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by hbh5274@gmail.com on 2020-09-27
 * Github : http://github.com/bhhan5274
 */

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;

    public Photo getPhoto(String id){
        return photoRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public String addPhoto(String title, MultipartFile file) throws IOException {
        Photo photo = new Photo(title);
        photo.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        photo = photoRepository.insert(photo);
        return photo.getId();
    }
}
