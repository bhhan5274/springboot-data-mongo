package com.bhhan.mongo.aggregation;

import com.bhhan.mongo.aggregation.model.StatePopulation;
import com.bhhan.mongo.config.MongoConfig;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Created by hbh5274@gmail.com on 2020-09-24
 * Github : http://github.com/bhhan5274
 */

@SpringBootTest(classes = MongoConfig.class)
class ZipsAggregationTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static MongoClient client;

    @BeforeAll
    static void setupTests() throws IOException {
        client = new MongoClient("localhost", 27017);
        MongoDatabase testDB = client.getDatabase("test");
        MongoCollection<Document> zipsCollection = testDB.getCollection("zips");
        zipsCollection.drop();

        InputStream zipsJsonStream = ZipsAggregationTest.class.getResourceAsStream("/zips.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(zipsJsonStream));
        reader.lines()
                .forEach(line -> zipsCollection.insertOne(Document.parse(line)));
        reader.close();
    }

    @AfterAll
    static void tearDown(){
        client = new MongoClient("localhost", 27017);
        MongoDatabase testDB = client.getDatabase("test");
        MongoCollection<Document> zipsCollection = testDB.getCollection("zips");
        zipsCollection.drop();
        client.close();
    }

    @Test
    void whenStatesHavePopGrtrThan10MillionAndSorted_thenSuccess() {
        GroupOperation groupByStateAndSumPop = group("state").sum("pop").as("statePop");
        MatchOperation filterStates = match(new Criteria("statePop").gt(10000000));
        SortOperation sortByPopDesc = sort(Sort.by(Sort.Direction.DESC, "statePop"));

        Aggregation aggregation = newAggregation(groupByStateAndSumPop, filterStates, sortByPopDesc);
        AggregationResults<StatePopulation> result = mongoTemplate.aggregate(aggregation, "zips", StatePopulation.class);

        result.forEach(statePopulation -> {
            assertTrue(statePopulation.getStatePop() > 10000000);
        });

        List<StatePopulation> actualList = StreamSupport.stream(result.spliterator(), false)
                .collect(Collectors.toList());

        List<StatePopulation> expectedList = new ArrayList<>(actualList);
        expectedList.sort((s1, s2) -> s2.getStatePop() - s1.getStatePop());

        assertEquals(expectedList, actualList);
    }

    @Test
    void whenStateWithLowestAvgCityPopIsND_theSuccess() {
        GroupOperation sumTotalCityPop = group("state", "city").sum("pop").as("cityPop");
        GroupOperation averageStatePop = group("_id.state").avg("cityPop").as("avgCityPop");
        SortOperation sortByAvgPopAsc = sort(Sort.by(Sort.Direction.ASC, "avgCityPop"));
        ProjectionOperation projectToMatchModel = project().andExpression("_id").as("state")
                .andExpression("avgCityPop").as("statePop");

        LimitOperation limitToOnlyFirstDoc = limit(1);

        Aggregation aggregation = newAggregation(sumTotalCityPop, averageStatePop, sortByAvgPopAsc, limitToOnlyFirstDoc, projectToMatchModel);

        AggregationResults<StatePopulation> result = mongoTemplate.aggregate(aggregation, "zips", StatePopulation.class);
        StatePopulation smallestState = result.getUniqueMappedResult();

        assertEquals("ND", smallestState.getState());
        assertEquals(1645, smallestState.getStatePop());
    }

    @Test
    void whenMaxTXAndMinDC_theSuccess() {
        GroupOperation sumZips = group("state").count().as("zipCount");
        SortOperation sortByCount = sort(Sort.Direction.ASC, "zipCount");
        GroupOperation groupFirstAndLast = group().first("_id").as("minZipState")
                .first("zipCount").as("minZipCount")
                .last("_id").as("maxZipState")
                .last("zipCount").as("maxZipCount");

        Aggregation aggregation = newAggregation(sumZips, sortByCount, groupFirstAndLast);
        AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, "zips", Document.class);
        Document document = result.getUniqueMappedResult();

        assertEquals("DC", document.get("minZipState"));
        assertEquals(24, document.get("minZipCount"));
        assertEquals("TX", document.get("maxZipState"));
        assertEquals(1671, document.get("maxZipCount"));
    }
}
