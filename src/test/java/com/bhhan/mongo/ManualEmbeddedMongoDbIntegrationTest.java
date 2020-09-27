package com.bhhan.mongo;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.SocketUtils;

import java.io.IOException;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * Created by hbh5274@gmail.com on 2020-09-27
 * Github : http://github.com/bhhan5274
 */
public class ManualEmbeddedMongoDbIntegrationTest {
    private static final String CONNECTION_STRING = "mongodb://%s:%d";
    private MongodExecutable mongodExecutable;
    private MongoTemplate mongoTemplate;

    @AfterEach
    void clean(){
        mongodExecutable.stop();
    }

    @BeforeEach
    void setUp() throws IOException {
        String ip = "localhost";
        int randomPort = SocketUtils.findAvailableTcpPort();

        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
                .net(new Net(ip, randomPort, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
        mongoTemplate = new MongoTemplate(MongoClients.create(String.format(CONNECTION_STRING, ip, randomPort)), "test");
    }

    @DisplayName("Given object When save object using MongoDB template Then object can be found")
    @Test
    void test(){
        DBObject objectToSave1 = BasicDBObjectBuilder.start()
                .add("key", "value1")
                .get();

        DBObject objectToSave2 = BasicDBObjectBuilder.start()
                .add("key", "value2")
                .get();

        mongoTemplate.save(objectToSave1, "collection");
        mongoTemplate.save(objectToSave2, "collection");

        then(mongoTemplate.findAll(DBObject.class, "collection"))
                .hasSize(2)
                .extracting("key")
                .contains("value1", "value2");
    }
}
