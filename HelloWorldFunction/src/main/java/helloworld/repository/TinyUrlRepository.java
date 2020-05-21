package helloworld.repository;


import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import helloworld.entity.TinyUrl;

/**
 * TinyUrl repository for crud operation with DynamoDB
 */
public class TinyUrlRepository extends AbstractRepository<TinyUrl, Long> {
    public TinyUrlRepository(IDynamoDBMapper dynamoDBMapper) {
        super(dynamoDBMapper);
    }
}