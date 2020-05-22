package tinyurl.repository;


import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import tinyurl.entity.TinyUrl;

/**
 * TinyUrl repository for read and insert operation with DynamoDB
 *
 * @author Qian Bing
 */
public class TinyUrlRepository extends AbstractRepository<TinyUrl, Long> {
    public TinyUrlRepository(IDynamoDBMapper dynamoDBMapper) {
        super(dynamoDBMapper);
    }
}