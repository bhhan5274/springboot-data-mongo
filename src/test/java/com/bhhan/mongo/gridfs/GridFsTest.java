package com.bhhan.mongo.gridfs;

import com.bhhan.mongo.config.MongoConfig;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by hbh5274@gmail.com on 2020-09-25
 * Github : http://github.com/bhhan5274
 */

@SpringBootTest(classes = MongoConfig.class)
@Slf4j
class GridFsTest {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @AfterEach
    void tearDown(){
        List<GridFSFile> fileList = new ArrayList<>();
        gridFsTemplate.find(new Query()).into(fileList);
        for (GridFSFile file : fileList) {
            gridFsTemplate.delete(new Query(Criteria.where("filename").is(file.getFilename())));
        }
    }

    @Test
    void whenStoringFileWithMetadata_thenFileAndMetadataAreStored() {
        BasicDBObject metaData = new BasicDBObject();
        metaData.put("user", "alex");
        InputStream inputStream = null;
        String id = "";

        try {
            inputStream = GridFsTest.class.getResourceAsStream("/test.png");
            id = gridFsTemplate.store(inputStream, "test.png", "image/png", metaData).toString();
        }catch(Exception e){
            log.info("File not found {}", e.toString());
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                }catch(IOException e){
                    log.info("Failed to close {}", e.toString());
                }
            }
        }

        assertNotNull(id);
        log.info(id);
    }

    @Test
    void givenFileWithMetadataExist_whenFindingFileById_thenFileWithMetadataIsFound() {
        BasicDBObject metaData = new BasicDBObject();
        metaData.put("user", "alex");
        InputStream inputStream = null;
        ObjectId id = null;

        try {
            inputStream = GridFsTest.class.getResourceAsStream("/test.png");
            id = gridFsTemplate.store(inputStream, "test.png", "image/png", metaData);
        }catch(Exception e){
            log.info("File not found {}", e.toString());
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                }catch(IOException e){
                    log.info("Failed to close {}", e.toString());
                }
            }
        }

        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));

        assertNotNull(gridFSFile);
        assertEquals("test.png", gridFSFile.getFilename());
        assertEquals(id, gridFSFile.getObjectId());
        assertNotNull(gridFSFile.getUploadDate());
        assertNotNull(gridFSFile.getChunkSize());
        assertEquals("image/png", gridFSFile.getMetadata().get("_contentType"));
        assertEquals("alex", gridFSFile.getMetadata().get("user"));
    }

    @Test
    void givenMetadataAndFilesExist_whenFindingAllFiles_thenFilesWithMetadataAreFound() {
        DBObject metaDataUser1 = new BasicDBObject();
        metaDataUser1.put("user", "alex");
        DBObject metaDataUser2 = new BasicDBObject();
        metaDataUser2.put("user", "david");
        InputStream inputStream = null;

        try {
            inputStream = GridFsTest.class.getResourceAsStream("/test.png");
            gridFsTemplate.store(inputStream, "test.png", "image/png", metaDataUser1);
            gridFsTemplate.store(inputStream, "test.png", "image/png", metaDataUser2);
        }catch(Exception e){
            log.info("File not found {}", e.toString());
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                }catch(IOException e){
                    log.info("Failed to close {}", e.toString());
                }
            }
        }

        List<GridFSFile> gridFSFiles = new ArrayList<>();
        gridFsTemplate.find(new Query()).into(gridFSFiles);

        assertNotNull(gridFSFiles);
        assertEquals(2, gridFSFiles.size());
    }

    @Test
    void givenMetadataAndFilesExist_whenFindingAllFilesOnQuery_thenFilesWithMetadataAreFoundOnQuery() {
        DBObject metaDataUser1 = new BasicDBObject();
        metaDataUser1.put("user", "alex");
        DBObject metaDataUser2 = new BasicDBObject();
        metaDataUser2.put("user", "david");
        InputStream inputStream = null;

        try {
            inputStream = GridFsTest.class.getResourceAsStream("/test.png");
            gridFsTemplate.store(inputStream, "test.png", "image/png", metaDataUser1);
            gridFsTemplate.store(inputStream, "test.png", "image/png", metaDataUser2);
        }catch(Exception e){
            log.info("File not found {}", e.toString());
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                }catch(IOException e){
                    log.info("Failed to close {}", e.toString());
                }
            }
        }

        List<GridFSFile> gridFSFiles = new ArrayList<>();
        gridFsTemplate.find(new Query(Criteria.where("metadata.user").is("alex"))).into(gridFSFiles);

        assertNotNull(gridFSFiles);
        assertEquals(1, gridFSFiles.size());
    }

    @Test
    void givenFileWithMetadataExist_whenDeletingFileById_thenFileWithMetadataIsDeleted() {
        DBObject metaData = new BasicDBObject();
        metaData.put("user", "alex");
        InputStream inputStream = null;
        String id = "";
        try {
            inputStream = GridFsTest.class.getResourceAsStream("/test.png");
            gridFsTemplate.store(inputStream, "test.png", "image/png", metaData);
        }catch(Exception e){
            log.info("File not found {}", e.toString());
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                }catch(IOException e){
                    log.info("Failed to close {}", e.toString());
                }
            }
        }

        gridFsTemplate.delete(new Query(Criteria.where("_id").is(id)));

        assertNull(gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id))));
    }

    @Test
    void givenFileWithMetadataExist_whenGettingFileByResource_thenFileWithMetadataIsGotten() {
        DBObject metaData = new BasicDBObject();
        metaData.put("user", "alex");
        InputStream inputStream = null;
        try {
            inputStream = GridFsTest.class.getResourceAsStream("/test.png");
            gridFsTemplate.store(inputStream, "test.png", "image/png", metaData);
        }catch(Exception e){
            log.info("File not found {}", e.toString());
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                }catch(IOException e){
                    log.info("Failed to close {}", e.toString());
                }
            }
        }

        GridFsResource[] gridFsResource = gridFsTemplate.getResources("test*");

        assertNotNull(gridFsResource);
        assertEquals(gridFsResource.length, 1);
        assertEquals("test.png", gridFsResource[0].getFilename());
    }
}
