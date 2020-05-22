package db;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tinyurl.Constant;
import tinyurl.entity.TinyUrl;
import tinyurl.repository.TinyUrlRepository;

public class SavePersonHandler
        implements RequestHandler<PersonRequest, PersonResponse> {
    private static final Logger logger = LoggerFactory.getLogger(SavePersonHandler.class);
    private DynamoDB dynamoDb;
    private String DYNAMODB_TABLE_NAME = "Person";
    private Regions REGION = Regions.AP_NORTHEAST_2;

    @Override
    public PersonResponse handleRequest(
            PersonRequest personRequest, Context context) {

        this.initDynamoDbClient();

//        persistData(personRequest);
        TinyUrl url= null;
        try {
            url = persistDataWithMapper(personRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        PersonResponse personResponse = new PersonResponse();
        personResponse.setMessage("Saved Successfully!!!" + url.getLongUrl() );
        return personResponse;
    }

    private TinyUrl persistDataWithMapper(PersonRequest personRequest)
            throws ConditionalCheckFailedException, JsonProcessingException {
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion(Constant.REGION).build();
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        PersonRepository repository = new PersonRepository(mapper);
        repository.save(personRequest);

        Long id=personRequest.getId();

        logger.info("start =================================================");

        logger.info("id is " + id);


        DynamoDBMapper mapper2 = new DynamoDBMapper(dynamoDB);
        TinyUrlRepository tinyUrlRepository = new TinyUrlRepository(mapper2);
        TinyUrl one = tinyUrlRepository.findOne(id);
        ObjectMapper jsonMapper = new ObjectMapper();
        logger.info(jsonMapper.writeValueAsString(one));
        logger.info("end =================================================");
        return one;


    }

    private PutItemOutcome persistData(PersonRequest personRequest)
            throws ConditionalCheckFailedException {
        return this.dynamoDb.getTable(DYNAMODB_TABLE_NAME)
                .putItem(
                        new PutItemSpec().withItem(new Item()
                                .withLong("id",personRequest.getId())
                                .withString("firstName", personRequest.getFirstName())
                                .withString("lastName", personRequest.getLastName())));
    }

    private void initDynamoDbClient() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(REGION));
        this.dynamoDb = new DynamoDB(client);
    }
}